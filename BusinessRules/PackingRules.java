package BusinessRules;

import java.util.Map;

/**
 * Brianna's Packing Rules & Box Specifications
 * Updated 4 days ago by Lyla Freshwater
 *
 * Centralized business logic constants.
 */
public class PackingRules {

    // === BOX SPECIFICATIONS ===
    public static class BoxSpecs {
        public static final Box STANDARD = new Box("Standard", 37, 11, 31, "Most common size", 4);
        public static final Box LARGE = new Box("Large", 44, 13, 48, ">36\" in both directions", 3);
        public static final Box UPS_SMALL = new Box("UPS Small", 36, 6, 36, "Small orders/replacements", 0);
        public static final Box UPS_LARGE = new Box("UPS Large", 44, 6, 35, "Adjustable length", 0);
    }

    public record Box(String name, double length, double width, double height,
                      String usage, int palletCapacity) {}

    // === PALLET SPECIFICATIONS ===
    public static class PalletSpecs {
        public static final Pallet STANDARD = new Pallet("Standard", 48, 40, 60, 4);
        public static final Pallet GLASS_SMALL = new Pallet("Glass (small)", 43, 35, 60, 0);
        public static final Pallet OVERSIZE = new Pallet("Oversize", 60, 40, 75, 5);
    }

    public record Pallet(String name, double length, double width, double tareWeight,
                         int boxCapacity) {}

    // === CRATE SPECIFICATIONS ===
    public static class CrateSpecs {
        public static final Crate STANDARD = new Crate("Standard", 50, 38, 125,
                "Most protective option");
    }

    public record Crate(String name, double length, double width, double tareWeight,
                        String usage) {}

    // === CRITICAL OVERSIZED DETECTION RULES ===
    public static class OversizeRules {
        public static final double STANDARD_BOX_LIMIT = 36.0;   // any dimension >36" → requires large box
        public static final double LARGE_BOX_LIMIT = 43.5;      // up to 43.5" in both directions
        public static final double CRATE_THRESHOLD = 46.0;      // >46" → requires custom pallet
        public s
