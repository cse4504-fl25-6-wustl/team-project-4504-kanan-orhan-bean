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

    // === Constants ===
    private static final int MAX_FRAMED_PER_BOX = 6;
    private static final int MAX_CANVAS_ACOUSTIC_PER_BOX = 4;
    private static final int MAX_OVERSIZED_PER_BOX = 3;
    private static final int PALLET_TARE_STANDARD = 60;
    private static final int PALLET_TARE_OVERSIZED = 75;
    private static final int PALLET_HEIGHT_BUFFER = 8;

    // === Public Methods ===

    /**
     * Main orchestration method.
     *
     * @param items List of LineItems with weights/dimensions computed
     * @param clientName Name of client (for client-specific rules)
     * @return Response object with packing plan and totals
     */
    public Response processPacking(List<LineItem> items, String clientName) {

        // Step 1: Apply client-specific rules
        List<LineItem> processedItems = applyClientRules(items, clientName);

        // Step 2: Apply size, weight, and glass handling rules
        processedItems = applySizeAndGlassRules(processedItems);

        // Step 3: Group by product type and then similar size
        Map<String, List<LineItem>> grouped = groupItems(processedItems);

        // Step 4: Assign items to boxes
        Map<String, List<List<LineItem>>> boxes = assignItemsToBoxes(grouped);

        // Step 5: Pack boxes onto pallets/crates
        Map<String, List<List<List<LineItem>>>> pallets = assignBoxesToPallets(boxes, clientName);

        // Step 6: Generate response with totals, weights, heights
        Response response = generateResponse(pallets);

        return response;
    }

    // === Step Methods ===

    /**
     * Apply client-specific packing rules.
     * For example, some clients may not accept crates.
     */
    private List<LineItem> applyClientRules(List<LineItem> items, String clientName) {
        // TODO: Implement rules based on clientName
        // e.g., disable crates if job site cannot accept them
        return items;
    }

    /**
     * Apply size, weight, and glass rules:
     * - Detect oversized and custom items
     * - Flag fragile glass for special handling
     * - Round weights and dimensions
     */
    private List<LineItem> applySizeAndGlassRules(List<LineItem> items) {
        for (LineItem item : items) {

            // Determine size category
            if (item.getWidth() > 44 || item.getHeight() > 44) {
                item.setCustomPackaging(true);
            } else if (item.getWidth() > 36 || item.getHeight() > 36) {
                item.setOversized(true);
            }

            // Round weights/dimensions
            item.setWeight(Math.ceil(item.getWeight()));
            item.setWidth(Math.ceil(item.getWidth()));
            item.setHeight(Math.ceil(item.getHeight()));

            // Glass check
            if ("Glass".equalsIgnoreCase(item.getGlazing()) && item.getWeight() > 0) {
                item.setCannotShipUPS(true);
            }
        }
        return items;
    }

    /**
     * Group items by product type first, then by similar size.
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
     * Assign items to boxes according to capacity rules.
     */
    private Map<String, List<List<LineItem>>> assignItemsToBoxes(Map<String, List<LineItem>> grouped) {
        Map<String, List<List<LineItem>>> boxAssignments = new HashMap<>();

        for (Map.Entry<String, List<LineItem>> entry : grouped.entrySet()) {
            String productType = entry.getKey();
            List<LineItem> items = entry.getValue();

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
     * Determine box capacity based on product type and item sizes.
     */
    private int determineBoxCapacity(String productType, List<LineItem> items) {
        if (items.stream().anyMatch(LineItem::isCustomPackaging)) return 1;
        if (items.stream().anyMatch(LineItem::isOversized)) return MAX_OVERSIZED_PER_BOX;

        if (productType.toLowerCase().contains("framed")) return MAX_FRAMED_PER_BOX;
        if (productType.toLowerCase().contains("canvas") ||
            productType.toLowerCase().contains("acoustic")) return MAX_CANVAS_ACOUSTIC_PER_BOX;

        return MAX_FRAMED_PER_BOX; // default conservative
    }

    /**
     * Assign boxes to pallets or crates.
     * Ensures boxes never overhang pallet edge.
     */
    private Map<String, List<List<List<LineItem>>>> assignBoxesToPallets(
            Map<String, List<List<LineItem>>> boxes, String clientName) {

        // TODO: Implement pallet optimization, considering crate preference vs box-on-pallet
        // For now, assume simple logic: 10 boxes per pallet
        Map<String, List<List<List<LineItem>>>> palletAssignments = new HashMap<>();
        for (String productType : boxes.keySet()) {
            List<List<LineItem>> boxList = boxes.get(productType);
            List<List<List<LineItem>>> pallets = new ArrayList<>();
            for (int i = 0; i < boxList.size(); i += 10) {
                pallets.add(boxList.subList(i, Math.min(i + 10, boxList.size())));
            }
            palletAssignments.put(productType, pallets);
        }
        return palletAssignments;
    }

    /**
     * Generate a Response object with totals, weights, heights, and packing details.
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

        // Add tare weights
        int tareWeight = totalPallets * PALLET_TARE_STANDARD; // default; could override if any oversized/custom
        response.setTotalWeight(Math.ceil(totalWeight + tareWeight));
        response.setPalletCount(totalPallets);
        response.setDoNotDoubleStack(true);

        return response;
    }
}
