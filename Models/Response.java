package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Response
 *
 * Output model for Feature 1.
 * Items are stored as plain Strings (e.g., raw lines or IDs from
 * Input/LineItem.txt).
 *
 * What to record:
 * - Per-box detail (container type, box weight, item count, item refs)
 * - Packaging/tare added per container (pallet/crate)
 * - Rollups: totals for artwork, packaging, shipment; counts for
 * boxes/pallets/crates/items
 */

/*
 * Workflow:
 * 1) The app reads rows from the line-item text/Excel and packs them into
 * boxes, then places those boxes onto containers
 * (pallets/crates) using the business rules.
 * 2) For each box it creates, the packing code calls
 * addBoxDetail(containerType, boxWeight, itemCount, itemRefs),
 * which records the box and adds its artwork weight to the running total.
 * 3) Once per container, the packing code also calls
 * addPackagingTare(containerType, tareLb) to add the pallet/crate packaging
 * weight and bump the pallet/crate counts.
 * 4) Response keeps all per-box details plus rollups: total boxes, items,
 * pallets, crates, artwork weight, packaging weight,
 * and the computed final shipment weight = artwork + packaging.
 * 5) Finally, reading Response via its getters or toString() to print the
 * work-order style summary ops expects.
 */

public class Response {

  // ---------- Per-box record ----------
  public static class BoxDetail {
    private final String containerType; // "PALLET" | "CRATE"
    private final double boxWeightLb; // total weight of items in this box (artwork only)
    private final int itemCount; // number of items in this box
    private final List<String> itemRefs; // IDs describing the items

    public BoxDetail(String containerType, double boxWeightLb, int itemCount, List<String> itemRefs) {
      this.containerType = containerType;
      this.boxWeightLb = boxWeightLb;
      this.itemCount = itemCount;
      this.itemRefs = Collections.unmodifiableList(new ArrayList<>(itemRefs));
    }

    public String getContainerType() {
      return containerType;
    }

    public double getBoxWeightLb() {
      return boxWeightLb;
    }

    public int getItemCount() {
      return itemCount;
    }

    public List<String> getItemRefs() {
      return itemRefs;
    }
  }

  private final List<BoxDetail> boxDetails = new ArrayList<>();

  // Weight rollups
  private double totalArtworkWeightLb = 0.0; // sum of all box weights
  private double totalPackagingWeightLb = 0.0; // pallets (60/75) + crates (125) etc.

  // Counts
  private int totalBoxes = 0;
  private int totalItems = 0;
  private int palletCount = 0;
  private int crateCount = 0;

  /* Record one packed box of artwork (does not include pallet/crate tare) */
  public void addBoxDetail(String containerType, double boxWeightLb, int itemCount, List<String> itemRefs) {
    boxDetails.add(new BoxDetail(containerType, boxWeightLb, itemCount, itemRefs));
    totalArtworkWeightLb += boxWeightLb;
    totalBoxes += 1;
    totalItems += itemCount;
  }

  /**
   * Add packaging tare once per container (e.g., pallet=60/75 lb, crate=125 lb)
   */
  public void addPackagingTare(String containerType, double tareLb) {
    totalPackagingWeightLb += Math.max(0.0, tareLb);
    if ("PALLET".equalsIgnoreCase(containerType))
      palletCount += 1;
    if ("CRATE".equalsIgnoreCase(containerType))
      crateCount += 1;
  }

  // ---------- Getters ----------
  public List<BoxDetail> getBoxDetails() {
    return Collections.unmodifiableList(boxDetails);
  }

  public double getTotalArtworkWeightLb() {
    return totalArtworkWeightLb;
  }

  public double getTotalPackagingWeightLb() {
    return totalPackagingWeightLb;
  }

  public double getFinalShipmentWeightLb() {
    return totalArtworkWeightLb + totalPackagingWeightLb;
  }

  public int getTotalBoxes() {
    return totalBoxes;
  }

  public int getTotalItems() {
    return totalItems;
  }

  public int getPalletCount() {
    return palletCount;
  }

  public int getCrateCount() {
    return crateCount;
  }

  // Debug / printable summary
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("=== Weight & Packing Summary ===\n");
    sb.append(String.format("Boxes: %d | Pallets: %d | Crates: %d | Items: %d\n",
        totalBoxes, palletCount, crateCount, totalItems));
    sb.append(String.format("Artwork Weight: %.1f lb | Packaging: %.1f lb | Final Shipment: %.1f lb\n",
        getTotalArtworkWeightLb(), getTotalPackagingWeightLb(), getFinalShipmentWeightLb()));
    for (BoxDetail b : boxDetails) {
      sb.append(String.format("- [%s] box: %.1f lb, items: %d\n",
          b.getContainerType(), b.getBoxWeightLb(), b.getItemCount()));
    }
    return sb.toString();
  }
}
