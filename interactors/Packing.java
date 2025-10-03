package interactors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import entities.Art;
import entities.Box;
import entities.Container;
import entities.Client;
import requests.Request;
import responses.Response;

public class Packing {

    private Client currentClient;
    private int mirrorCount;

    public Response packEverything(Request request) {
        Objects.requireNonNull(request, "request must not be null");
        this.currentClient = Objects.requireNonNull(request.getClient(), "client must not be null");

        List<Art> arts = Objects.requireNonNull(request.getArts(), "arts must not be null");

        List<Box> boxes = packArtIntoBoxes(arts); // Pack arts into boxes
        List<Container> containers = packBoxIntoContainers(boxes); // Pack boxes into containers
        // Response prints items/boxes/containers summary and totals
        return new Response(arts, boxes, containers);
    }

    /**
     * placeholder Box objects to represent how many boxes we'd need.
     * We do NOT call Box.addArt(...) because Box's internal list isn't initialized
     * yet.
     * Instead, we compute counts and set dimensions via setBoxCustom so the summary
     * looks meaningful.
     */
    private List<Box> packArtIntoBoxes(List<Art> arts) {
        List<Box> result = new ArrayList<>();
        if (arts.isEmpty())
            return result;

        // Partition
        List<Art> mirrors = new ArrayList<>();
        List<Art> customs = new ArrayList<>();
        List<Art> normals = new ArrayList<>();
        for (Art a : arts) {
            if (a.getType() == Art.Type.Mirror) {
                mirrors.add(a);
            } else if (a.isCustom()) {
                customs.add(a);
            } else {
                normals.add(a);
            }
        }
        this.mirrorCount = mirrors.size();

        // --- Custom items: create 1 custom box per custom item (simple baseline) ---
        for (Art a : customs) {
            Box customBox = new Box();
            // plausible custom size (a bit larger than the art)
            double L = roundUp(a.getWidth() + 2); // add ~2" margin
            double W = 13; // similar to oversize width
            double H = roundUp(a.getHeight() + 2); // add ~2" margin
            customBox.setBoxCustom(L, W, H); // public setter available
            result.add(customBox);
        }

        // --- Normals: group by material "family" to get capacity, and split oversize
        // vs standard ---
        int stdCount = 0;
        int overCount = 0;

        for (Art a : normals) {
            if (isOversizeForBox(a))
                overCount++;
            else
                stdCount++;
        }

        int capStdFamily = 0;
        int capOverFamily = 0;

        // We'll approximate capacity by the dominant family among normals.
        // (Glass/Acrylic=6; Canvas=6 (per current code/TODO); Acoustic=4; default=4)
        String dominantFamily = dominantFamily(normals);
        if ("GLASS_OR_ACRYLIC".equals(dominantFamily)) {
            capStdFamily = 6;
            capOverFamily = 6; // same cap; tweak later if needed
        } else if ("CANVAS".equals(dominantFamily)) {
            capStdFamily = 6; // TODO: possible change; keep 6 for now
            capOverFamily = 6;
        } else if ("ACOUSTIC".equals(dominantFamily)) {
            capStdFamily = 4;
            capOverFamily = 4;
        } else {
            capStdFamily = 4;
            capOverFamily = 4;
        }

        int numStdBoxes = ceilDiv(stdCount, capStdFamily);
        int numOverBoxes = ceilDiv(overCount, capOverFamily);

        // --- Materialized placeholder boxes
        // Standard box dims: 37 x 11 x 31
        for (int i = 0; i < numStdBoxes; i++) {
            Box b = new Box();
            b.setBoxCustom(37, 11, 31);
            result.add(b);
        }
        // Oversize box dims: 44 x 13 x 48
        for (int i = 0; i < numOverBoxes; i++) {
            Box b = new Box();
            b.setBoxCustom(44, 13, 48);
            result.add(b);
        }

        return result;
    }

    /**
     * placeholder Container objects to represent how many pallets/crates
     * we’d need.
     * We do NOT call Container.addBox(...) because Container's internal lists
     * aren’t initialized yet.
     */
    private List<Container> packBoxIntoContainers(List<Box> boxes) {
        List<Container> result = new ArrayList<>();
        if (currentClient == null)
            return result;

        // Client delivery capabilities
        var caps = currentClient.getDeliveryCapabilities();
        boolean acceptsPallets = caps.doesAcceptPallets();
        boolean acceptsCrates = caps.doesAcceptCrates();

        // Mirror crates: 24 mirrors per crate (baseline)
        if (mirrorCount > 0) {
            int mirrorCrates = ceilDiv(mirrorCount, 24);
            for (int i = 0; i < mirrorCrates; i++) {
                result.add(new Container(Container.Type.Crate, /* canAcceptCrate= */acceptsCrates));
            }
        }

        if (boxes.isEmpty())
            return result;

        // Count oversize vs standard boxes (infer from our custom-set dimensions)
        int std = 0, over = 0;
        for (Box b : boxes) {
            if (isOversizeBoxDim(b))
                over++;
            else
                std++;
        }

        // Pallet baseline capacity: 4 boxes; treat oversize as more “expensive” (3 per
        // pallet)
        int pallets = 0, crates = 0;
        if (acceptsPallets) {
            pallets += ceilDiv(std, 4);
            pallets += ceilDiv(over, 3); // heuristic: oversize reduces pallet capacity by ~1
            for (int i = 0; i < pallets; i++) {
                result.add(new Container(Container.Type.Pallet, /* canAcceptCrate= */acceptsCrates));
            }
        } else if (acceptsCrates) {
            crates = ceilDiv(std + over, 4);
            for (int i = 0; i < crates; i++) {
                result.add(new Container(Container.Type.Crate, /* canAcceptCrate= */true));
            }
        } else {
            // Neither pallets nor crates accepted means boxes only (no containers)
        }

        return result;
    }

    // ---------------- helpers ----------------

    private int ceilDiv(int a, int b) {
        if (b <= 0)
            return 0;
        return (a + b - 1) / b;
    }

    private double roundUp(double x) {
        return Math.ceil(x);
    }

    /**
     * Oversize threshold in Box()
     */
    private boolean isOversizeForBox(Art a) {
        return a.getWidth() > 36.0 || a.getHeight() > 36.0;
    }

    /**
     * Infer whether a placeholder Box should be considered “oversize” by its
     * dimensions
     * (we set oversize placeholders to 44 x 13 x 48).
     */
    private boolean isOversizeBoxDim(Box b) {
        return b.getLength() >= 44 || b.getWidth() >= 13 || b.getHeight() >= 48;
    }

    /**
     * Determine dominant family among a list for picking a capacity rule.
     */
    private String dominantFamily(List<Art> items) {
        int glassAcr = 0, canvas = 0, acoustic = 0, other = 0;
        for (Art a : items) {
            String fam = familyOf(a);
            switch (fam) {
                case "GLASS_OR_ACRYLIC" -> glassAcr++;
                case "CANVAS" -> canvas++;
                case "ACOUSTIC" -> acoustic++;
                default -> other++;
            }
        }
        if (glassAcr >= canvas && glassAcr >= acoustic && glassAcr >= other)
            return "GLASS_OR_ACRYLIC";
        if (canvas >= glassAcr && canvas >= acoustic && canvas >= other)
            return "CANVAS";
        if (acoustic >= glassAcr && acoustic >= canvas && acoustic >= other)
            return "ACOUSTIC";
        return "OTHER";
    }

    /**
     * Map Art to a capacity family based on its material string.
     */
    private String familyOf(Art a) {
        String mat = a.getMaterial().toString();
        if (mat.contains("Glass") || mat.contains("Acyrlic") || mat.contains("Acrylic"))
            return "GLASS_OR_ACRYLIC";
        if (mat.contains("Canvas"))
            return "CANVAS";
        if (mat.contains("Acoustic"))
            return "ACOUSTIC";
        return "OTHER";
    }
}
