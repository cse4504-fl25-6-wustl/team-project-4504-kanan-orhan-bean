package BusinessRules;

import Models.LineItem;
import Models.Response;

import java.util.*;

/**
 * PackingAndShipping
 *
 * Implements core business rules for packing and shipping.
 * Rules are based on product type, size, and client constraints.
 * 
 * This class is independent of CLI, file parsing, or external frameworks.
 */
public class PackingAndShipping {

    // === Public Methods ===

    /**
     * Orchestrates the full packing and shipping process.
     *
     * @param items List<LineItem> → product items with weight and dimensions already computed
     * @param clientName String → name of client (e.g., "Sunrise Senior Living") to apply client-specific rules
     * @return Response → fully built packing plan including pallets, boxes, weights, and summary
     */
    public Response processPacking(List<LineItem> items, String clientName) {

        // Step 1: Apply size, weight, and glass handling rules
        List<LineItem> processedItems = applySizeAndGlassRules(items);

        // Step 2: Group by product type and then similar size
        Map<String, List<LineItem>> grouped = groupItems(processedItems);

        // Step 3: Assign items to boxes
        Map<String, List<List<LineItem>>> boxes = assignItemsToBoxes(grouped, clientName);

        // Step 4: Pack boxes onto pallets/crates
        Map<String, List<List<List<LineItem>>>> pallets = assignBoxesToPallets(boxes, clientName);

        // Step 5: Generate response with totals, weights, heights
        return generateResponse(pallets);
    }

    // === Step Methods ===

    /**
     * Applies dimensional and material rules to each LineItem.
     * - Rounds weights/dimensions upward
     * - Flags oversized vs. custom packaging based on thresholds
     * - Flags glass items that cannot ship via UPS
     *
     * @param items List<LineItem> → product items with raw dimensions/weight
     * @return List<LineItem> → same items with flags (oversized/custom/UPS restrictions) applied
     */
    private List<LineItem> applySizeAndGlassRules(List<LineItem> items) {
        for (LineItem item : items) {
            double w = item.getWidth();
            double h = item.getHeight();

            // Apply Brianna’s oversized detection rules
            if (w > PackingRules.OversizeRules.CRATE_THRESHOLD || h > PackingRules.OversizeRules.CRATE_THRESHOLD) {
                item.setCustomPackaging(true); // requires custom pallet
            } else if (w > PackingRules.OversizeRules.STANDARD_BOX_LIMIT || h > PackingRules.OversizeRules.STANDARD_BOX_LIMIT) {
                item.setOversized(true); // requires large box
            }

            // Round weights/dimensions
            item.setWeight(Math.ceil(item.getWeight()));
            item.setWidth(Math.ceil(w));
            item.setHeight(Math.ceil(h));

            // Glass check
            if ("Glass".equalsIgnoreCase(item.getGlazing()) && item.getWeight() > 0) {
                item.setCannotShipUPS(true);
            }
        }
        return items;
    }

    /**
     * Groups items by product type.
     *
     * @param items List<LineItem> → collection of items with productType already set
     * @return Map<String, List<LineItem>> → key = product type, value = list of items of that type
     */
    private Map<String, List<LineItem>> groupItems(List<LineItem> items) {
        Map<String, List<LineItem>> grouped = new HashMap<>();
        for (LineItem item : items) {
            String key = item.getProductType();
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }
        return grouped;
    }

    /**
     * Assigns items to boxes based on capacity rules.
     * Groups items of similar height to reduce wasted space.
     *
     * @param grouped Map<String, List<LineItem>> → grouped items by product type
     * @param clientName String → client name for applying overrides (e.g., Sunrise glass rule)
     * @return Map<String, List<List<LineItem>>> → key = product type,
     *         value = list of boxes, each box containing a list of items
     */
    private Map<String, List<List<LineItem>>> assignItemsToBoxes(Map<String, List<LineItem>> grouped, String clientName) {
        Map<String, List<List<LineItem>>> boxAssignments = new HashMap<>();
    
        for (Map.Entry<String, List<LineItem>> entry : grouped.entrySet()) {
            String productType = entry.getKey();
            List<LineItem> items = new ArrayList<>(entry.getValue()); // copy
    
            // Sort by height (tallest first)
            items.sort((a, b) -> Double.compare(b.getHeight(), a.getHeight()));
    
            int capacity = determineBoxCapacity(productType, items, clientName);
            List<List<LineItem>> boxes = new ArrayList<>();
    
            for (int i = 0; i < items.size(); i += capacity) {
                boxes.add(items.subList(i, Math.min(i + capacity, items.size())));
            }
    
            boxAssignments.put(productType, boxes);
        }
        return boxAssignments;
    }

    /**
     * Determines how many items can fit into a box, based on:
     * - Product type
     * - Oversized/custom flags
     * - Client-specific overrides
     *
     * @param productType String → type of product ("canvas", "framed", "acoustic", etc.)
     * @param items List<LineItem> → items being evaluated
     * @param clientName String → client name for applying overrides
     * @return int → number of pieces allowed per box
     */
    private int determineBoxCapacity(String productType, List<LineItem> items, String clientName) {
        if (items.stream().anyMatch(LineItem::isCustomPackaging)) return 1;
        if (items.stream().anyMatch(LineItem::isOversized)) return PackingRules.BoxSpecs.LARGE.palletCapacity();

        String typeLower = productType.toLowerCase();

        // Sunrise client overrides glass/acrylic rule
        if ("sunrise senior living".equalsIgnoreCase(clientName) && typeLower.contains("glass")) {
            return PackingRules.ClientRules.Sunrise.GLASS_ACRYLIC_PIECES_PER_BOX;
        }

        if (typeLower.contains("framed")) return PackingRules.PiecesPerContainer.GLASS_ACRYLIC_FRAMED.get("piecesPerBox");
        if (typeLower.contains("canvas")) return PackingRules.PiecesPerContainer.CANVAS.get("piecesPerBox");
        if (typeLower.contains("acoustic")) return PackingRules.PiecesPerContainer.ACOUSTIC_PANELS.get("piecesPerBox");

        return PackingRules.PiecesPerContainer.GLASS_ACRYLIC_FRAMED.get("piecesPerBox"); // default conservative
    }

    /**
     * Assigns boxes to pallets or crates based on capacity rules.
     * Uses Brianna’s pallet specifications for standard vs. oversized.
     *
     * @param boxes Map<String, List<List<LineItem>>> → key = product type, value = boxes (list of items per box)
     * @param clientName String → client name (reserved for overrides if needed)
     * @return Map<String, List<List<List<LineItem>>>> → key = product type,
     *         value = list of pallets, each pallet containing a list of boxes
     */
    private Map<String, List<List<List<LineItem>>>> assignBoxesToPallets(
            Map<String, List<List<LineItem>>> boxes, String clientName) {
    
        Map<String, List<List<List<LineItem>>>> palletAssignments = new HashMap<>();
    
        for (String productType : boxes.keySet()) {
            List<List<LineItem>> boxList = new ArrayList<>(boxes.get(productType)); 
    
            // Sort by tallest box first
            boxList.sort((a, b) -> {
                double heightA = a.stream().mapToDouble(LineItem::getHeight).max().orElse(0);
                double heightB = b.stream().mapToDouble(LineItem::getHeight).max().orElse(0);
                return Double.compare(heightB, heightA);
            });
    
            List<List<List<LineItem>>> pallets = new ArrayList<>();
            List<List<LineItem>> currentPallet = new ArrayList<>();
            int palletCapacity;
    
            for (List<LineItem> box : boxList) {
                boolean isOversized = box.stream().anyMatch(LineItem::isOversized);
                palletCapacity = isOversized
                        ? PackingRules.BoxSpecs.LARGE.palletCapacity()
                        : PackingRules.BoxSpecs.STANDARD.palletCapacity();
    
                if (currentPallet.size() >= palletCapacity) {
                    pallets.add(new ArrayList<>(currentPallet));
                    currentPallet.clear();
                }
    
                currentPallet.add(box);
            }
    
            if (!currentPallet.isEmpty()) {
                pallets.add(new ArrayList<>(currentPallet));
            }
    
            palletAssignments.put(productType, pallets);
        }
    
        return palletAssignments;
    }

    /**
     * Builds a Response object containing:
     * - Total weight
     * - Pallet count
     * - Box-level details
     * - Do-not-double-stack flag
     *
     * @param pallets Map<String, List<List<List<LineItem>>>> → palletized items grouped by product type
     * @return Response → structured result with shipment summary and metadata
     */
    private Response generateResponse(Map<String, List<List<List<LineItem>>>> pallets) {
        Response response = new Response();

        double totalWeight = 0.0;
        int totalPallets = 0;

        for (String type : pallets.keySet()) {
            List<List<List<LineItem>>> typePallets = pallets.get(type);
            totalPallets += typePallets.size();

            for (List<List<LineItem>> pallet : typePallets) {
                for (List<LineItem> box : pallet) {
                    double boxWeight = 0;
                    for (LineItem item : box) {
                        boxWeight += item.getWeight();
                    }
                    response.addBoxDetail(type, boxWeight, box.size(), box);
                    totalWeight += boxWeight;
                }
            }
        }

        // Default: use standard pallet tare unless oversize encountered
        int tareWeight = totalPallets * PackingRules.PalletSpecs.STANDARD.tareWeight();
        response.setTotalWeight(Math.ceil(totalWeight + tareWeight));
        response.setPalletCount(totalPallets);
        response.setDoNotDoubleStack(true);

        return response;
    }

    /**
     * Formats the Response object into a structured human-readable summary.
     *
     * @param response Response → fully built packing/shipping response
     * @return String → formatted shipment summary (weights, boxes, pallets, hardware)
     */
    public String toString(Response response) {
        StringBuilder sb = new StringBuilder();

        // === 1. Weight Summary ===
        sb.append("1. Weight Summary\n");
        sb.append("Total Artwork Weight: ").append((int) response.getTotalArtworkWeight()).append(" lbs\n");
        sb.append("- Glass framed prints: ").append((int) response.getGlassWeight()).append(" lbs\n");
        sb.append("- Oversized pieces: ").append((int) response.getOversizedWeight()).append(" lbs\n\n");

        sb.append("Total Packaging Weight: ").append((int) response.getPackagingWeight()).append(" lbs\n");
        sb.append("- Pallets: ").append((int) response.getPalletWeight())
          .append(" lbs (").append(response.getPalletCount())
          .append(" pallets @ 60-75 lbs each)\n");
        sb.append("- Crates: ").append((int) response.getCrateWeight()).append(" lbs\n\n");

        sb.append("Final Shipment Weight: ")
          .append((int) response.getTotalWeight()).append(" lbs\n\n");

        // === 2. Packing Summary ===
        sb.append("2. Packing Summary\n");
        sb.append("Box Requirements:\n");
        sb.append("- Standard boxes (37\"×11\"×31\"): ")
          .append(response.getStandardBoxCount()).append(" boxes\n");
        sb.append("- Large boxes (44\"×13\"×48\"): ")
          .append(response.getLargeBoxCount()).append(" boxes\n");
        sb.append("- Total boxes: ").append(response.getTotalBoxCount()).append("\n\n");

        sb.append("Pallet/Crate Requirements:\n");
        sb.append("- Standard pallets (48\"×40\"): ")
          .append(response.getStandardPalletCount()).append(" pallet\n");
        sb.append("- Oversize pallets (60\"×40\"): ")
          .append(response.getOversizePalletCount()).append(" pallet\n");
        sb.append("- Crates: ").append(response.getCrateCount()).append("\n\n");

        sb.append("Final Dimensions:\n");
        int palletIndex = 1;
        for (Response.PalletDetail pd : response.getPalletDetails()) {
            sb.append("- Pallet ").append(palletIndex++).append(": ")
              .append(pd.getWidth()).append("\"×")
              .append(pd.getLength()).append("\"×")
              .append(pd.getHeight()).append("\"H @ ")
              .append((int) pd.getWeight()).append(" lbs\n");
        }
        sb.append("\n");

        // === Hardware Calculation ===
        sb.append("Hardware Calculation:\n");
        for (Map.Entry<String, Integer> e : response.getHardwareSummary().entrySet()) {
            sb.append("- ").append(e.getKey()).append(": qty ").append(e.getValue()).append("\n");
        }
        sb.append("- Wall hardware needed: ").append(response.getWallHardwareTotal()).append(" pieces\n");
        sb.append("  * Drywall anchors: ").append(response.getDrywallAnchors()).append("\n");
        sb.append("  * Screws: ").append(response.getScrews()).append("\n");
        sb.append("  * T-bolts: ").append(response.getTBolts()).append("\n");

        return sb.toString();
    }
}
