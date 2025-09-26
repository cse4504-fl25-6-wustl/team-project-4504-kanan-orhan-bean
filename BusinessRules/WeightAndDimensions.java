package BusinessRules;

import java.util.Locale;
import java.util.Map;

/**
 * WeightAndDimensions
 *
 * WORKFLOW:
 * 1) The caller provides outside width/height (inches), the Final Medium, the
 * Glazing, and a quantity.
 * 2) We normalize Final Medium + Glazing to a known material key (e.g.,
 * CANVAS-FRAMED, GLASS) that exists in a
 * lbs-per-square-inch map. This keeps calculations consistent across inputs.
 * 3) Per the business rule, we ROUND UP the width and height to whole inches,
 * then compute
 * per-piece weight = (rounded width * rounded height * lbs/sq-in), and ROUND UP
 * that weight to the next whole lb.
 * 4) For line items with quantity, we multiply the (already-rounded) per-piece
 * weight by quantity and ROUND UP again
 * to stay conservative for quoting.
 * 5) We also expose helpers for packing decisions: oversize (>36"),
 * square-oversize (36.5"–43.5"), and crate/custom
 * threshold (>46"). A coarse box classifier returns STANDARD, LARGE, or
 * CUSTOM_EXCEEDS to steer packing.
 * 6) All methods are pure (no I/O); the packing pipeline calls these functions
 * and then records results into Response.
 *
 */
public final class WeightAndDimensions {

  // Materials mapped to lbs/sq-in
  private static final Map<String, Double> LBS_PER_SQIN = Map.ofEntries(
      Map.entry("GLASS", 0.0098),
      Map.entry("ACRYLIC", 0.0094),
      Map.entry("CANVAS-FRAMED", 0.0085),
      Map.entry("CANVAS-GALLERY", 0.0061),
      Map.entry("MIRROR", 0.0191),
      Map.entry("ACOUSTIC PANEL", 0.0038),
      Map.entry("ACOUSTIC PANEL-FRAMED", 0.0037),
      Map.entry("PATIENT BOARD", 0.0347));

  public enum BoxType {
    STANDARD, LARGE, CUSTOM_EXCEEDS
  }

  private WeightAndDimensions() {
    /* no instances */ }

  // ----------------- Helpers
  // -------------------------------------------------------------------------------------------

  /** business rounding: ceil to next whole number; not below zero. */
  public static double roundUp(double v) {
    return Math.ceil(Math.max(0.0, v));
  }

  /** normalize to uppercase for robust matching. */
  private static String normUp(String s) {
    return s == null ? "" : s.trim().toUpperCase(Locale.ROOT);
  }

  /** look up lbs/sq-in or throw to surface data issues early. */
  public static double lbsPerSqIn(String materialKey) {
    Double v = LBS_PER_SQIN.get(normUp(materialKey));
    if (v == null)
      throw new IllegalArgumentException("Unknown material key: " + materialKey);
    return v;
  }

  /**
   * resolve a material key using Final Medium + Glazing text from Excel.
   */
  public static String resolveMaterialKey(String finalMedium, String glazing) {
    String m = normUp(finalMedium);
    String g = normUp(glazing);

    if (m.contains("MIRROR"))
      return "MIRROR";
    if (g.contains("GLASS"))
      return "GLASS";
    if (g.contains("ACRYLIC"))
      return "ACRYLIC";

    if (m.contains("ACOUSTIC") && m.contains("FRAM"))
      return "ACOUSTIC PANEL-FRAMED";
    if (m.contains("ACOUSTIC"))
      return "ACOUSTIC PANEL";
    if (m.contains("PATIENT"))
      return "PATIENT BOARD";

    if (m.contains("CANVAS") && (m.contains("FLOAT") || m.contains("FRAME") || m.contains("FRAMED")))
      return "CANVAS-FRAMED";
    if (m.contains("CANVAS"))
      return "CANVAS-GALLERY";

    return "ACRYLIC";
  }

  // ----------------- Weights
  // --------------------------------------------------------------------------------------------

  /**
   * Compute per-piece weight (lb): round up dims, multiply by lbs/sq-in, round up
   * the result.
   * Example: 48×36 Canvas-Framed will be mapped to (48×36×0.0085)=14.688 so it
   * will be 15.0 lb per piece.
   */
  public static double perPieceWeightLb(double widthIn, double heightIn, String materialKey) {
    double w = roundUp(widthIn);
    double h = roundUp(heightIn);
    double unit = w * h * lbsPerSqIn(materialKey);
    return roundUp(unit);
  }

  /**
   * Compute line weight (lb) for quantity pieces.
   * We multiply the rounded per-piece by quantity and round again
   */
  public static double lineWeightLb(double widthIn, double heightIn, String materialKey, int quantity) {
    double per = perPieceWeightLb(widthIn, heightIn, materialKey);
    return roundUp(per * Math.max(0, quantity));
  }

  /**
   * Convenience when we only have raw Final Medium + Glazing strings (from
   * text/CSV).
   */
  public static double lineWeightFromRaw(double widthIn, double heightIn,
      String finalMedium, String glazing, int quantity) {
    String key = resolveMaterialKey(finalMedium, glazing);
    return lineWeightLb(widthIn, heightIn, key, quantity);
  }

  // ----------------- Dimension rules
  // ------------------------------------------------------------------------------------

  /** Oversize if ANY dimension > 36". */
  public static boolean isOversize(double widthIn, double heightIn) {
    return widthIn > 36.0 || heightIn > 36.0;
  }

  /** Special square-oversize: squares from 36.5" to 43.5" inclusive. */
  public static boolean isSquareOversize(double widthIn, double heightIn) {
    return Math.abs(widthIn - heightIn) < 1e-6 && widthIn >= 36.5 && widthIn <= 43.5;
  }

  /** Crate/custom threshold: any dimension > 46" requires custom pallet. */
  public static boolean exceedsCrateThreshold(double widthIn, double heightIn) {
    return widthIn > 46.0 || heightIn > 46.0;
  }

  /**
   * Box classifier:
   * - STANDARD: both dims ≤ 36"
   * - LARGE: any dim > 36" but both ≤ 43.5"
   * - CUSTOM_EXCEEDS: any dim > 46" (crate/custom pallet)
   * (The 43.5–46 range may still require special handling)
   */

  public static BoxType classifyBox(double widthIn, double heightIn) {
    if (exceedsCrateThreshold(widthIn, heightIn))
      return BoxType.CUSTOM_EXCEEDS;
    boolean anyOver36 = isOversize(widthIn, heightIn);
    boolean bothWithinLarge = widthIn <= 43.5 && heightIn <= 43.5;
    if (bothWithinLarge && anyOver36)
      return BoxType.LARGE;
    return BoxType.STANDARD;
  }

  // ----------------- Example usage
  // ---------------------------------------------------------------------------------
  /*
   * // Example:
   * double w = 48, h = 36;
   * String finalMedium = "Canvas - Float Frame";
   * String glazing = "Regular Glass"; // or "" if none
   * int qty = 2;
   * 
   * String key = WeightAndDimensions.resolveMaterialKey(finalMedium, glazing); //
   * "CANVAS-FRAMED"
   * double perLb = WeightAndDimensions.perPieceWeightLb(w, h, key); // -> 15.0
   * double lineLb = WeightAndDimensions.lineWeightLb(w, h, key, qty); // -> 30.0
   * boolean os = WeightAndDimensions.isOversize(w, h) ||
   * WeightAndDimensions.isSquareOversize(w, h);
   * BoxType choice = WeightAndDimensions.classifyBox(w, h); // LARGE
   */
}
