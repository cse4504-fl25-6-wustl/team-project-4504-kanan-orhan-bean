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
        // Step 1: Apply size, glass, and rounding rules
        preprocessItems(items);

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

    /** Round weights/dimensions and mark oversized or UPS-restricted items */
    private void preprocessItems(List<LineItem> items) {
        for (LineItem item : items) {
            double w = Math.ceil(item.getWidth());
            double h = Math.ceil(item.getHeight());
            item.setWidth(w);
            item.setHeight(h);
            item.setWeight(Math.ceil(item.getWeight()));

            item.setOversized(PackingRules.isOversized(item));

            if ("Glass".equalsIgnoreCase(item.getGlazing())) {
                item.setCannotShipUPS(true);
            }
        }
    }

    /** Group LineItems by their product type */
    private Map<String, List<LineItem>> groupItemsByType(List<LineItem> items) {
        Map<String, List<LineItem>> grouped = new HashMap<>();
        for (LineItem item : items) {
            grouped.computeIfAbsent(item.getProductType(), k -> new ArrayList<>()).add(item);
        }
        return grouped;
    }

    /** Assign LineItems into boxes based on PackingRules */
    private Map<String, List<Box>> assignItemsToBoxes(Map<String, List<LineItem>> grouped) {
        Map<String, List<Box>> boxMap = new HashMap<>();

        for (Map.Entry<String, List<LineItem>> entry : grouped.entrySet()) {
            String type = entry.getKey();
            List<LineItem> items = entry.getValue();

            // Sort items by height descending (tallest first)
            items.sort(Comparator.comparingDouble(LineItem::getHeight).reversed());

            int capacity = PackingRules.getBoxCapacity(type, items);

            List<Box> boxes = new ArrayList<>();
            for (int i = 0; i < items.size(); i += capacity) {
                boxes.add(new Box(items.subList(i, Math.min(i + capacity, items.size()))));
            }

            boxMap.put(type, boxes);
        }

        return boxMap;
    }

    /** Assign boxes into containers (pallets or crates) */
    private Map<String, List<Container>> assignBoxesToContainers(
            Map<String, List<Box>> boxesMap,
            boolean acceptsPallets,
            boolean acceptsCrates) {

        Map<String, List<Container>> containerMap = new HashMap<>();

        // Determine container type once
        Container.Type containerType = acceptsCrates ? Container.Type.CRATE : Container.Type.PALLET;

        for (Map.Entry<String, List<Box>> entry : boxesMap.entrySet()) {
            String type = entry.getKey();
            List<Box> boxes = entry.getValue();

            // Sort boxes by tallest height first
            boxes.sort(Comparator.comparingDouble(Box::getBoxesHeight).reversed());

            List<Container> containers = new ArrayList<>();
            List<Box> current = new ArrayList<>();
            int capacity;

            for (Box box : boxes) {
                capacity = box.isOversized() ? PackingRules.BoxSpecs.LARGE.palletCapacity() :
                        PackingRules.BoxSpecs.STANDARD.palletCapacity();

                if (current.size() >= capacity) {
                    containers.add(new Container(containerType, current, HEIGHT_BUFFER));
                    current = new ArrayList<>();
                }
                current.add(box);
            }

            if (!current.isEmpty()) {
                containers.add(new Container(containerType, current, HEIGHT_BUFFER));
            }

            containerMap.put(type, containers);
        }

        return containerMap;
    }

    /** Convert containers and boxes into Response object */
    private Response generateResponse(Map<String, List<Container>> containerMap) {
        Response response = new Response();

        for (List<Container> containers : containerMap.values()) {
            for (Container c : containers) {
                for (Box b : c.getBoxes()) {
                    response.addBoxDetail(
                            c.getType().name(), b.getWeight(), b.getItems().size(), b.getItems()
                    );
                }
            }
        }

        return response;
    }
}
