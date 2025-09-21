package BusinessRules;

import Models.Request;
import Models.Response;
import Models.LineItem;

import java.util.*;

/**
 * ParsingAndShipping
 *
 * Implements business rules for parsing, packing, and shipping logic.
 * Delegates weight & dimension calculations to WeightAndDimensions.java.
 */
public class ParsingAndShipping {

    /**
     * Parses the raw input file (CSV/Excel) into structured LineItem objects.
     * 
     * @param fileName Path to the input file containing line item data.
     * @return List of LineItem objects with raw attributes.
     * @throws Exception if file parsing fails.
     */
    public List<LineItem> parseLineItems(String fileName) throws Exception {
        // NOTE: In a real implementation, delegate to ArtParser
        // For now, assume ArtParser is responsible and this function calls it.
        // Example:
        // return ArtParser.parse(fileName);

        throw new UnsupportedOperationException("parseLineItems() not implemented yet.");
    }

    /**
     * Applies client-specific rules (stub for now).
     * 
     * @param clientName Name of client.
     * @param items Parsed line items.
     * @return Updated line items after applying rules.
     */
    public List<LineItem> applyClientRules(String clientName, List<LineItem> items) {
        // Example: If client cannot accept crates, mark restriction flag
        // item.setAllowCrates(false);
        return items;
    }

    /**
     * Delegates to WeightAndDimensions to calculate item-level weights/dimensions.
     * Also applies oversized/custom packaging flags.
     *
     * @param items Line items.
     * @return Updated line items with computed weights/dimensions.
     */
    public List<LineItem> calculateWeightsAndDimensions(List<LineItem> items) {
        for (LineItem item : items) {
            // Assume WeightAndDimensions.applyRules updates:
            // - item.weight
            // - item.oversizedFlag
            // - item.customPackagingFlag
            // - item.rounded dimensions
            WeightAndDimensions.applyRules(item);
        }
        return items;
    }

    /**
     * Groups items into boxes and pallets following packing rules.
     * 
     * @param items Items with computed weights and dimensions.
     * @return Response object containing packing plan and totals.
     */
    public Response applyPackingLogic(List<LineItem> items) {
        Response response = new Response();

        // === STEP 1: Group by product type ===
        Map<String, List<LineItem>> groupedByType = new HashMap<>();
        for (LineItem item : items) {
            String type = item.getProductType();
            groupedByType.computeIfAbsent(type, k -> new ArrayList<>()).add(item);
        }

        // === STEP 2: Apply box capacity limits ===
        for (Map.Entry<String, List<LineItem>> entry : groupedByType.entrySet()) {
            String productType = entry.getKey();
            List<LineItem> typeItems = entry.getValue();

            int capacity = getBoxCapacity(productType, typeItems);
            int boxCount = (int) Math.ceil((double) typeItems.size() / capacity);

            response.addPackingDetail(productType, boxCount, capacity);
        }

        // === STEP 3: Pallet logic ===
        // Pack boxes onto pallets without overhang
        int totalPallets = calculatePallets(response);
        response.setPalletCount(totalPallets);

        // === STEP 4: Add tare weights ===
        double totalWeight = 0.0;
        for (LineItem item : items) {
            totalWeight += item.getWeight();
        }

        // Add pallet tare weight
        double tarePerPallet = 60; // default
        if (response.hasOversizedOrCustom()) {
            tarePerPallet = 75;
        }
        totalWeight += totalPallets * tarePerPallet;

        // Round up per rule #11
        totalWeight = Math.ceil(totalWeight);

        response.setTotalWeight(totalWeight);
        response.setDoNotDoubleStack(true);

        return response;
    }

    /**
     * Determines box capacity based on product type.
     */
    private int getBoxCapacity(String productType, List<LineItem> items) {
        if (containsCustom(items)) {
            return 1; // custom always requires special handling
        }
        if (productType.toLowerCase().contains("framed")) {
            return 6;
        }
        if (productType.toLowerCase().contains("canvas") ||
            productType.toLowerCase().contains("acoustic")) {
            return 4;
        }
        if (containsOversized(items)) {
            return 3;
        }
        return 6; // default conservative value
    }

    /**
     * Helper to check if list has oversized items.
     */
    private boolean containsOversized(List<LineItem> items) {
        return items.stream().anyMatch(LineItem::isOversized);
    }

    /**
     * Helper to check if list has custom items.
     */
    private boolean containsCustom(List<LineItem> items) {
        return items.stream().anyMatch(LineItem::isCustomPackaging);
    }

    /**
     * Estimate pallet count based on packing details.
     */
    private int calculatePallets(Response response) {
        // TODO: Use dimensions to check pallet overhang
        // For now assume 1 pallet per 10 boxes
        int boxes = response.getTotalBoxes();
        return (int) Math.ceil(boxes / 10.0);
    }

    /**
     * Ensures profit margin rules are respected.
     */
    public boolean checkProfitMargin(double estimatedCost, double quotedPrice) {
        if (quotedPrice <= 0) return false;
        double margin = (quotedPrice - estimatedCost) / quotedPrice;
        return margin >= 0.25;
    }

    /**
     * Orchestrates full pipeline:
     */
    public Response processRequest(Request request) throws Exception {
        List<LineItem> items = parseLineItems(request.getLineItemFileName());
        items = applyClientRules(request.getClientName(), items);
        items = calculateWeightsAndDimensions(items);
        return applyPackingLogic(items);
    }
}

