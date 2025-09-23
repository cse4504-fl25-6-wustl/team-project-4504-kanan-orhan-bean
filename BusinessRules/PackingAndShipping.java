package BusinessRules;

import Models.LineItem;
import Models.Response;

import java.util.*;

/**
 * PackingAndShipping
 *
 * Automates packing decisions and weight calculations for ARCH Design.
 * 
 * Input: List<LineItem> with weight, width, height, product type, glazing, etc.
 * Output: Response object containing:
 *   - Weight of each item
 *   - Items packed in boxes (grouped)
 *   - Weight of each box
 *   - Boxes packed in pallets or crates
 *   - Weight and height of each pallet/crate
 *   - Total shipment weight
 */
public class PackingAndShipping {

    // === Public Method ===

    /**
     * Process packing for a list of LineItems.
     * @param items List of LineItems
     * @param acceptsPallets boolean, whether client accepts pallets
     * @param acceptsCrates boolean, whether client accepts crates
     * @return Response object containing packing and weight details
     */
    public Response processPacking(List<LineItem> items, boolean acceptsPallets, boolean acceptsCrates) {

        // Step 1: Apply size, weight, and glass rules
        List<LineItem> processedItems = applySizeAndGlassRules(items);

        // Step 2: Group by product type and similar size
        Map<String, List<LineItem>> grouped = groupItems(processedItems);

        // Step 3: Assign items to boxes
        Map<String, List<List<LineItem>>> boxes = assignItemsToBoxes(grouped);

        // Step 4: Assign boxes to pallets or crates
        Map<String, List<List<List<LineItem>>>> palletsOrCrates = assignBoxesToPalletsOrCrates(boxes, acceptsPallets, acceptsCrates);

        // Step 5: Generate final response with totals and packing details
        return generateResponse(palletsOrCrates);
    }

    // === Step Methods ===

    /**
     * Apply size, weight, and glass rules.
     * Input: List<LineItem>
     * Output: List<LineItem> with flags set for oversized, custom packaging, cannot ship UPS
     */
    private List<LineItem> applySizeAndGlassRules(List<LineItem> items) {
        for (LineItem item : items) {
            double w = Math.ceil(item.getWidth());
            double h = Math.ceil(item.getHeight());
            double wt = Math.ceil(item.getWeight());

            item.setWidth(w);
            item.setHeight(h);
            item.setWeight(wt);

            // Determine if oversized or requires custom packaging
            if (w > PackingRules.OversizeRules.CRATE_THRESHOLD || h > PackingRules.OversizeRules.CRATE_THRESHOLD) {
                item.setCustomPackaging(true);
            } else if (w > PackingRules.OversizeRules.STANDARD_BOX_LIMIT || h > PackingRules.OversizeRules.STANDARD_BOX_LIMIT) {
                item.setOversized(true);
            }

            // Glass cannot ship UPS
            if ("Glass".equalsIgnoreCase(item.getGlazing()) && wt > 0) {
                item.setCannotShipUPS(true);
            }
        }
        return items;
    }

    /**
     * Group items by product type.
     * Input: List<LineItem>
     * Output: Map<ProductType, List<LineItem>>
     */
    private Map<String, List<LineItem>> groupItems(List<LineItem> items) {
        Map<String, List<LineItem>> grouped = new HashMap<>();
        for (LineItem item : items) {
            grouped.computeIfAbsent(item.getProductType(), k -> new ArrayList<>()).add(item);
        }
        return grouped;
    }

    /**
     * Assign items to boxes by product type, similar size, and box capacity.
     * Packs items "face-to-face" (even numbers per box).
     * Input: Map<ProductType, List<LineItem>>
     * Output: Map<ProductType, List<List<LineItem>>> (boxes)
     */
    private Map<String, List<List<LineItem>>> assignItemsToBoxes(Map<String, List<LineItem>> grouped) {
        Map<String, List<List<LineItem>>> boxAssignments = new HashMap<>();

        for (Map.Entry<String, List<LineItem>> entry : grouped.entrySet()) {
            String productType = entry.getKey();
            List<LineItem> items = new ArrayList<>(entry.getValue());

            // Sort by height descending to group similar sizes
            items.sort((a, b) -> Double.compare(b.getHeight(), a.getHeight()));

            int capacity = determineBoxCapacity(productType, items);
            List<List<LineItem>> boxes = new ArrayList<>();

            for (int i = 0; i < items.size(); i += capacity) {
                boxes.add(items.subList(i, Math.min(i + capacity, items.size())));
            }

            boxAssignments.put(productType, boxes);
        }

        return boxAssignments;
    }

    /**
     * Determine max items per box based on product type and size.
     * Input: product type, list of LineItems
     * Output: int (max items per box)
     */
    private int determineBoxCapacity(String productType, List<LineItem> items) {
        if (items.stream().anyMatch(LineItem::isCustomPackaging)) return 1;
        if (items.stream().anyMatch(LineItem::isOversized)) return PackingRules.BoxSpecs.LARGE.piecesPerBox();

        String type = productType.toLowerCase();
        if (type.contains("framed")) return PackingRules.BoxSpecs.STANDARD.piecesPerBox();
        if (type.contains("canvas") || type.contains("acoustic")) return PackingRules.BoxSpecs.CANVAS_ACOUSTIC.piecesPerBox();

        return PackingRules.BoxSpecs.STANDARD.piecesPerBox();
    }

    /**
     * Assign boxes to pallets or crates.
     * Input: Map<ProductType, List<List<LineItem>>> (boxes), booleans acceptsPallets, acceptsCrates
     * Output: Map<ProductType, List<List<List<LineItem>>>> (pallets or crates)
     */
    private Map<String, List<List<List<LineItem>>>> assignBoxesToPalletsOrCrates(
            Map<String, List<List<LineItem>>> boxes, boolean acceptsPallets, boolean acceptsCrates) {

        Map<String, List<List<List<LineItem>>>> containerAssignments = new HashMap<>();

        for (String productType : boxes.keySet()) {
            List<List<LineItem>> boxList = new ArrayList<>(boxes.get(productType));
            List<List<List<LineItem>>> containers = new ArrayList<>();
            List<List<LineItem>> currentContainer = new ArrayList<>();

            for (List<LineItem> box : boxList) {
                // Decide container type
                boolean isOversized = box.stream().anyMatch(LineItem::isOversized);
                int containerCapacity = isOversized ? PackingRules.BoxSpecs.LARGE.palletCapacity()
                                                   : PackingRules.BoxSpecs.STANDARD.palletCapacity();

                if (currentContainer.size() >= containerCapacity) {
                    containers.add(new ArrayList<>(currentContainer));
                    currentContainer.clear();
                }

                currentContainer.add(box);
            }

            if (!currentContainer.isEmpty()) {
                containers.add(new ArrayList<>(currentContainer));
            }

            containerAssignments.put(productType, containers);
        }

        return containerAssignments;
    }

    /**
     * Generate a Response with totals, weights, and packing details.
     * Input: Map<ProductType, List<List<List<LineItem>>>> (containers)
     * Output: Response object
     */
    private Response generateResponse(Map<String, List<List<List<LineItem>>>> containers) {
        Response response = new Response();

        double totalWeight = 0.0;
        int palletCount = 0;

        for (String type : containers.keySet()) {
            List<List<List<LineItem>>> containerList = containers.get(type);
            palletCount += containerList.size();

            for (List<List<LineItem>> container : containerList) {
                for (List<LineItem> box : container) {
                    double boxWeight = box.stream().mapToDouble(LineItem::getWeight).sum();
                    response.addBoxDetail(type, boxWeight, box.size(), box);
                    totalWeight += boxWeight;
                }
            }
        }

        // Add pallet tare weights
        int tareWeight = palletCount * PackingRules.PalletSpecs.STANDARD.tareWeight();
        response.setTotalWeight(Math.ceil(totalWeight + tareWeight));
        response.setPalletCount(palletCount);
        response.setDoNotDoubleStack(true);

        return response;
    }
}
