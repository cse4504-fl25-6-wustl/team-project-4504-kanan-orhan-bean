package BusinessRules;

import Models.LineItem;
import java.util.List;

/**
 * PackingRules
 *
 * Contains all hardcoded packing and shipping business rules for ARCH Design.
 * Includes box, pallet, crate specifications, oversized detection, and client-specific overrides.
 */
public class PackingRules {

    // === Client Names ===
    public static final String CLIENT_SUNRISE = "Sunrise Senior Living";

    // === Oversized Detection Rules ===
    public static class OversizeRules {
        public static final double STANDARD_BOX_LIMIT = 36.0; // any dimension >36" → large box
        public static final double LARGE_BOX_LIMIT = 43.5;    // largest box allowed
        public static final double CRATE_THRESHOLD = 46.0;    // requires custom pallet
        public static final double MAX_HEIGHT_RECOMMENDED = 84.0;
        public static final double MAX_HEIGHT_ABSOLUTE = 102.0;
    }

    // === Box Types ===
    public enum BoxSpecs {
        STANDARD(37, 11, 31, 4),
        LARGE(44, 13, 48, 3),
        UPS_SMALL(36, 6, 36, 0),
        UPS_LARGE(44, 6, 35, 0);

        private final int length;
        private final int width;
        private final int height;
        private final int palletCapacity; // how many of this box fit per pallet

        BoxSpecs(int l, int w, int h, int palletCapacity) {
            this.length = l;
            this.width = w;
            this.height = h;
            this.palletCapacity = palletCapacity;
        }

        public int length() { return length; }
        public int width() { return width; }
        public int height() { return height; }
        public int palletCapacity() { return palletCapacity; }
    }

    // === Pallet Specifications ===
    public enum PalletSpecs {
        STANDARD(48, 40, 60, 4),
        GLASS_SMALL(43, 35, 60, 0),
        OVERSIZE(60, 40, 75, 5);

        private final int length;
        private final int width;
        private final int tareWeight;
        private final int boxCapacity;

        PalletSpecs(int l, int w, int tareWeight, int boxCapacity) {
            this.length = l;
            this.width = w;
            this.tareWeight = tareWeight;
            this.boxCapacity = boxCapacity;
        }

        public int length() { return length; }
        public int width() { return width; }
        public int tareWeight() { return tareWeight; }
        public int boxCapacity() { return boxCapacity; }
    }

    // === Crate Specifications ===
    public enum CrateSpecs {
        STANDARD(50, 38, 125);

        private final int length;
        private final int width;
        private final int tareWeight;

        CrateSpecs(int l, int w, int tare) {
            this.length = l;
            this.width = w;
            this.tareWeight = tare;
        }

        public int length() { return length; }
        public int width() { return width; }
        public int tareWeight() { return tareWeight; }
    }

    // === Pieces per Box / Container ===
    public static int getBoxCapacity(String type, List<LineItem> items) {
        String typeLower = type.toLowerCase();

        // If any item is oversized, only 3 per box
        boolean hasOversized = items.stream().anyMatch(LineItem::isOversized);
        if (hasOversized) return BoxSpecs.LARGE.palletCapacity();

        // Sunrise client override: 8 pieces per box for glass/acrylic
        boolean isSunrise = items.stream()
                .anyMatch(item -> CLIENT_SUNRISE.equalsIgnoreCase(item.getClientName()));
        if (isSunrise && typeLower.contains("glass")) return 8;

        // Standard rules
        if (typeLower.contains("framed") && typeLower.contains("glass")) return 6;
        if (typeLower.contains("canvas")) return 4;
        if (typeLower.contains("acoustic")) return 4;

        // Mirrors: best packed in crate
        if (typeLower.contains("mirror")) return 0;

        // Default fallback
        return 6;
    }

    // === Crate Pieces Rules ===
    public static int getCrateCapacity(String type, LineItem item) {
        String typeLower = type.toLowerCase();
        double maxDim = Math.max(item.getWidth(), item.getHeight());

        if (typeLower.contains("glass") || typeLower.contains("acrylic")) {
            return maxDim < 33 ? 25 : 18;
        } else if (typeLower.contains("canvas")) {
            return maxDim < 33 ? 18 : 12;
        } else if (typeLower.contains("mirror")) {
            return 24; // direct in crate
        }

        return 12; // default conservative
    }

    // === Utility ===
    public static boolean requiresCustomPallet(LineItem item) {
        double w = item.getWidth();
        double h = item.getHeight();
        return w > OversizeRules.CRATE_THRESHOLD || h > OversizeRules.CRATE_THRESHOLD;
    }

    public static boolean isOversized(LineItem item) {
        double w = item.getWidth();
        double h = item.getHeight();
        return (w > OversizeRules.STANDARD_BOX_LIMIT || h > OversizeRules.STANDARD_BOX_LIMIT)
                && !requiresCustomPallet(item);
    }
}
