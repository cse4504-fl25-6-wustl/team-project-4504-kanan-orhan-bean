package BusinessRules;

import Models.LineItem;
import Models.Response;
import java.util.*;

/**
 * PackingAndShipping
 *
 * Automates packing and weight calculation.
 */
public class PackingAndShipping {

    private static final double HEIGHT_BUFFER = 8.0;

    /**
     * Input: List<LineItem>, client preferences for pallets/crates
     * Output: Response with packing info
     */
    public Response processPacking(List<LineItem> items, boolean acceptsPallets, boolean acceptsCrates) {
        // Step 1: Apply size and glass rules
        applySizeAndGlassRules(items);

        // Step 2: Group items by product type
        Map<String, List<LineItem>> grouped = groupItemsByType(items);

        // Step 3: Pack items into boxes
        Map<String, List<Box>> boxes = assignItemsToBoxes(grouped);

        // Step 4: Pack boxes into containers
        Map<String, List<Container>> containers = assignBoxesToContainers(boxes, acceptsPallets, acceptsCrates);

        // Step 5: Generate response
        return generateResponse(containers);
    }

    // === Helper Functions ===

    private void applySizeAndGlassRules(List<LineItem> items) {
        for (LineItem item : items) {
            double w = Math.ceil(item.getWidth());
            double h = Math.ceil(item.getHeight());
            item.setWidth(w);
            item.setHeight(h);
            item.setWeight(Math.ceil(item.getWeight()));

            if (w > PackingRules.OversizeRules.STANDARD_BOX_LIMIT || h > PackingRules.OversizeRules.STANDARD_BOX_LIMIT)
                item.setOversized(true);

            if ("Glass".equalsIgnoreCase(item.getGlazing()))
                item.setCannotShipUPS(true);
        }
    }

    private Map<String, List<LineItem>> groupItemsByType(List<LineItem> items) {
        Map<String, List<LineItem>> grouped = new HashMap<>();
        for (LineItem item : items) {
            grouped.computeIfAbsent(item.getProductType(), k -> new ArrayList<>()).add(item);
        }
        return grouped;
    }

    private Map<String, List<Box>> assignItemsToBoxes(Map<String, List<LineItem>> grouped) {
        Map<String, List<Box>> boxMap = new HashMap<>();
        for (String type : grouped.keySet()) {
            List<LineItem> items = new ArrayList<>(grouped.get(type));
            items.sort((a,b) -> Double.compare(b.getHeight(), a.getHeight()));

            int capacity = PackingRules.getBoxCapacity(type, items);

            List<Box> boxes = new ArrayList<>();
            for (int i = 0; i < items.size(); i += capacity) {
                boxes.add(new Box(items.subList(i, Math.min(i + capacity, items.size()))));
            }
            boxMap.put(type, boxes);
        }
        return boxMap;
    }

    private Map<String, List<Container>> assignBoxesToContainers(
            Map<String, List<Box>> boxesMap, boolean acceptsPallets, boolean acceptsCrates) {
        Map<String, List<Container>> containerMap = new HashMap<>();

        for (String type : boxesMap.keySet()) {
            List<Box> boxes = new ArrayList<>(boxesMap.get(type));
            boxes.sort((b1, b2) -> Double.compare(
                    b2.getBoxesHeight(), b1.getBoxesHeight()
            ));

            List<Container> containers = new ArrayList<>();
            List<Box> current = new ArrayList<>();
            int capacity;

            for (Box box : boxes) {
                capacity = box.isOversized() ? 3 : 4;

                if (current.size() >= capacity) {
                    containers.add(new Container(acceptsCrates ? Container.Type.CRATE : Container.Type.PALLET, current, HEIGHT_BUFFER));
                    current = new ArrayList<>();
                }
                current.add(box);
            }

            if (!current.isEmpty()) {
                containers.add(new Container(acceptsCrates ? Container.Type.CRATE : Container.Type.PALLET, current, HEIGHT_BUFFER));
            }

            containerMap.put(type, containers);
        }

        return containerMap;
    }

    private Response generateResponse(Map<String, List<Container>> containerMap) {
        Response response = new Response();

        for (String type : containerMap.keySet()) {
            for (Container c : containerMap.get(type)) {
                for (Box b : c.getBoxes()) {
                    response.addBoxDetail(type, b.getWeight(), b.getItems().size(), b.getItems());
                }
            }
        }

        return response;
    }
}
