# team-project-4504-kanan-orhan-bean
team-project-4504-kanan-orhan-bean created by GitHub Classroom


# Feature 1 Work Assignment

1. `Main.java` is implemented and will not change.
2. `public` functions. Any public functions parameters or return values will not be changed, but the internal implementation in the function is up to the designated Coder.
3. `private` functions. Can be changed anyway. Will not be accessed by other other modules outside its scope.
4. If you need to have a public function implemented in another module (that is not yours), instead of implementing it, just code as if it exists and let the teammate responsible know.

## Coders:

1. Coder #1: Orhan
2. Coder #2: Bean
3. Coder #3: Kanan

## Entities

1. `Art.java`: Coder #1
2. `Box.java`: Coder #1
3. `Client.java`: Coder #2
4. `Container.java`: Coder #1

## Interactors

1. `Packing.java`: Coder #3

## Parser

1. `ClientParser.java`: Coder #2
2. `CSVParser.java`: Coder #2

## Requests

1. `Request.java`: Coder #3

## Responses

1. `Responses.java`: Coder #3

# Developer Guide

1. This code uses gradle to build and run both the source and the test codes. Make sure you have gradle installed 

2. To run the code input 2 command line arguments. First one being the LineItemFileName, and the second one being the ClientFileName. 

To build and run the program you can run `gradle run --args="[inputFileName] [inputClientFileName]"`, replacing `[inputFileName]` and `[inputClientFileName]` with the actual names of the input files. The following is how it would look like:

```
(base) > team-project-4504-kanan-orhan-bean % gradle run --args="input/Input1.csv input/Site_requirements.csv"
Input File Name: input/LineItemInput.csv
Client File Name: input/ClientInput.csv
=== PACKING SUMMARY ===
Items: 7 total
  - Art #1 (line 1): 31.38" x 45.38" PaperPrintFramed | weight=13.95 lb | CUSTOM
  - Art #2 (line 2): 27.00" x 27.00" CanvasFloatFrame | weight=6.20 lb
  - Art #3 (line 3): 25.00" x 49.00" PaperPrintFramedWithTitlePlate | weight=12.00 lb | CUSTOM
  - Art #4 (line 4): 32.00" x 32.00" WallDecor | weight=6.25 lb
  - Art #5 (line 5): 24.00" x 36.00" MetalPrint | weight=29.98 lb
  - Art #6 (line 7): 42.50" x 30.50" PaperPrintFramed | weight=12.70 lb
  - Art #7 (line 8): 36.13" x 48.13" Mirror | weight=33.21 lb | CUSTOM
Boxes: 4
  - Box #1: STANDARD | 34" L x 13" W x 48" H | weight=0.00 lb
  - Box #2: STANDARD | 27" L x 13" W x 51" H | weight=0.00 lb
  - Box #3: STANDARD | 37" L x 11" W x 31" H | weight=0.00 lb
  - Box #4: STANDARD | 44" L x 13" W x 48" H | weight=0.00 lb
Containers: 3
  - Container #1: Crate | 0" L x 0" W x 0" H | weight=125.00 lb
  - Container #2: Pallet | 0" L x 0" W x 0" H | weight=60.00 lb
  - Container #3: Pallet | 0" L x 0" W x 0" H | weight=60.00 lb
TOTAL SHIPMENT WEIGHT: 245.00 lb
```

To build and run the test cases  you can run `gradle test`, and this will run all test cases under the test folder. The following is how it would look like:

```
(base) orhanerdogan@mac team-project-4504-kanan-orhan-bean % gradle test
Calculating task graph as no cached configuration is available for tasks: test

BUILD SUCCESSFUL in 532ms
3 actionable tasks: 3 up-to-date
Configuration cache entry stored.
```

Then once it is built successfully you can go to the following directory `app/build/reports/index.html` and open it with default application (option+command+O `⌥+⌘+O` on macOS) in VSCode. This `index.html` file will show you a report of your Test Cases

# Feature 1 Testing Assignment

## Coders:

1. Coder #1: Orhan
2. Coder #2: Bean
3. Coder #3: Kanan

## Entities

1. `Art.java`: Coder #3
  - Art()
  - getWeight()
  - isCustom()
2. `Box.java`: Coder #2
  - createBoxForArt()
  - addArt()
  - canArtFit()
  - getWeight()
  - getCapacity()
3. `Client.java`: Coder #2
  - Client()
4. `Container.java`: Coder #1
  - constructContainerForArt()
  - constructContainerForBox()
  - addBox()
  - addArt()
  - canBoxFit()
  - canArtFit()
  - getWeight()
  - getCapacity()

## Interactors

1. `Packing.java`: Coder #3
  - packEverything()

## Parser

1. `Parser.java`: Coder #2
  - parseArt()
  - parseClient()

## Requests

1. `Request.java`: Coder #3
  - Request()

## Responses

1. `Responses.java`: Coder #3
  - Response()

## Main

1. `Main.java`: Coder #3
  - main()

# Alpha Release Assignment

To create a one-click executable for Mac simply run `gradle createMacApp` on your terminal, and to create a one-click executable for Windows run `gradle createWindowsApp` on your terminal. For the Windows executable make sure you have WiX toolset installed. To download WiX toolset if you don't have it, follow this [link](https://github.com/wixtoolset).

# Beta Release Assignment Testing

For all testing we are using a set depth with pack_by_depth strategy

## test_cases main branch

### Passing

- /all/test1 passes
- /pack_by_depth/test2 passes

### Failing

- /pack_by_depth/test1 fails, this is because our packing strategies are different. We use a set depth of 1.8333" for Paper Prints, while they use 1/6th of the box (whether it is standard or large), this causes them to have variable depth (1.8333" for standard box, and 2.1667" for large box), you can see this assumption in their read me `Assumption: these numbers are the same, regardless of whether box is standard or large`

## test_cases stress_tests_redo branch

### Passing

- /all/test1 passes
- /pack_by_depth/test2 passes
- /pack_by_depth/test5 passes

### failing

- For All of the ones where I tell they have wrong pack by depth it is because of this scenario:

An oversized 4perBox needs a Large Box, so you put 4 into a Large Box, but then there is enough space to fit a standard 6perBox into that same box. All of those tests fail to consider that. Then some of them also pack the boxes into pallets wrong (not optimally as well), but the main problem is they don't pack by depth correctly.

#### /all/test2 fails, 
because they put the wrong numbers to their expected_output. Their expected_output.json does not match their ReadMe explanation. In their readme they get 11 large pallets and 0 large boxes. and they don't have any custom art. But on their expected_output.json they got that mixed and for some reason split the arts into standard and custom_piece_count, but I think they meant to put it into oversized art instead. 
#### /pack_by_depth/test1 fails, 
this is because our packing strategies are different. We use a set depth of 1.8333" for Paper Prints, while they use 1/6th of the box (whether it is standard or large), this causes them to have variable depth (1.8333" for standard box, and 2.1667" for large box), you can see this assumption in their read me `Assumption: these numbers are the same, regardless of whether box is standard or large`
#### /pack_by_depth/test3 fails, 
because it is packing wrong by depth. If it actually packed by depth it would be able to fit it into 28 large boxes and 68 standard boxes, and not 30 large boxes. And With those boxes you can then put them into 10 oversized pallets and 14 standard pallets which result int total shipment weight of 7100 instead of 7130. Here is the packing for the large boxes.

- Is Large? true 11: 44.0"x13.0"x48.0"H @ 71 lbs 
- Arts 
- Art LineNumber 10
- Art LineNumber 10
- Art LineNumber 10
- Art LineNumber 10
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 71 lbs 
- Arts 
- Art LineNumber 10
- Art LineNumber 10
- Art LineNumber 10
- Art LineNumber 10
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 71 lbs 
- Arts 
- Art LineNumber 10
- Art LineNumber 10
- Art LineNumber 10
- Art LineNumber 10
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 71 lbs 
- Arts 
- Art LineNumber 10
- Art LineNumber 10
- Art LineNumber 10
- Art LineNumber 10
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 71 lbs 
- Arts 
- Art LineNumber 10
- Art LineNumber 10
- Art LineNumber 10
- Art LineNumber 10
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 83 lbs 
- Arts 
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 83 lbs 
- Arts 
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 83 lbs 
- Arts 
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 83 lbs 
- Arts 
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 83 lbs 
- Arts 
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 83 lbs 
- Arts 
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 83 lbs 
- Arts 
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 83 lbs 
- Arts 
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 83 lbs 
- Arts 
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 83 lbs 
- Arts 
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 7
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 124 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 112 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 112 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 112 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 112 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 112 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 112 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 112 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 112 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Is Large? true 11: 44.0"x13.0"x48.0"H @ 60 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 6
- Art LineNumber 6
- Art LineNumber 6
- Art LineNumber 6

#### /pack_by_depth/test4 fails, 
because it is packing wrong by depth. If it actually packed by depth it would be able to fit it into 76 large boxes and 13 standard boxes, and not 84 large boxes and 17 standard boxes. And With those boxes you can then put them into 0 oversized pallets and 29 standard pallets which result int total shipment weight of 7100 instead of 7130. Here is the packing for the large boxes. 

Box Number 1 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 2 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 3 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 4 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 5 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 6 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 7 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 8 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 9 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 10 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 11 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 12 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 13 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 14 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 15 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 16 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 17 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 18 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 19 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 20 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 21 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 22 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 23 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 24 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 25 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 26 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 27 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 28 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 29 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 30 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 31 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 32 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 33 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 34 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 35 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 36 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 37 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
Box Number 38 - Is Large? true: 44.0"x13.0"x48.0"H @ 74 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 2
- Art LineNumber 2
Box Number 39 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
Box Number 40 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
Box Number 41 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
Box Number 42 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
Box Number 43 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
Box Number 44 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
Box Number 45 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
Box Number 46 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
Box Number 47 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
Box Number 48 - Is Large? true: 44.0"x13.0"x48.0"H @ 102 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 3
Box Number 49 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 50 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 51 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 52 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 53 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 54 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 55 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 56 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 57 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 58 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 59 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 60 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 61 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 62 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 63 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 64 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 65 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 66 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 67 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 68 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 69 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 70 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 71 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 72 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 73 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 74 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 75 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1
Box Number 76 - Is Large? true: 44.0"x13.0"x48.0"H @ 42 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 1

#### /pack_by_depth/test6 fails, 
because of the extra line in the input that has all nulls. It is formatted correctly, but gives null for every field. Once you remove those it doesn't fail the test. We get different oversized and standard pallet counts, but all other numbers are the same and those weight the exact same and have the same quantity so we should be indifferent to them. So even though technically we have different pallets, in reality we don't.
#### /pack_by_depth/test7 fails, 
because they put floats instead of ints in the expected_output. if they get switched to int they pass
#### /pack_by_depth/test8 fails, 
they have an invalid input.csv. They added and extra column, and didn't populate necessary columns (hardware). If they were to populate them (with non nulls like N/A), and remove the extra column they added then their result would still be wrong, because their ReadMe does not match their expected_output (see large_box_count), then their packing by depth is wrong, because they can put 6perBox items in Large boxes to fill up the box. Here is the packing for the large boxes. 

Box Number 1 - Is Large? true: 44.0"x13.0"x48.0"H @ 59 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 5
- Art LineNumber 1
Box Number 2 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 3 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 4 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 5 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 6 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 7 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 8 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 9 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 10 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 11 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 12 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 13 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 14 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 15 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 16 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 17 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 18 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 19 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 20 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 21 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 22 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 23 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 24 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 25 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 26 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 27 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 28 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 29 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 30 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 31 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 32 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 33 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 34 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 1
Box Number 35 - Is Large? true: 44.0"x13.0"x48.0"H @ 72 lbs 
- Arts 
- Art LineNumber 5
- Art LineNumber 5
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 1
Box Number 36 - Is Large? true: 44.0"x13.0"x48.0"H @ 82 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 1
Box Number 37 - Is Large? true: 44.0"x13.0"x48.0"H @ 82 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 1
Box Number 38 - Is Large? true: 44.0"x13.0"x48.0"H @ 82 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 1
Box Number 39 - Is Large? true: 44.0"x13.0"x48.0"H @ 82 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 1
Box Number 40 - Is Large? true: 44.0"x13.0"x48.0"H @ 82 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 1
Box Number 41 - Is Large? true: 44.0"x13.0"x48.0"H @ 82 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 1
Box Number 42 - Is Large? true: 44.0"x13.0"x48.0"H @ 82 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 1
Box Number 43 - Is Large? true: 44.0"x13.0"x48.0"H @ 82 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 1
Box Number 44 - Is Large? true: 44.0"x13.0"x48.0"H @ 82 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 1
Box Number 45 - Is Large? true: 44.0"x13.0"x48.0"H @ 82 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 1
Box Number 46 - Is Large? true: 44.0"x13.0"x48.0"H @ 79 lbs 
- Arts 
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 2
- Art LineNumber 1
- Art LineNumber 1

#### /pack_by_depth/test9 fails, 
because it is packing wrong by depth. Here is the packing for the large boxes.

Box Number 1 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
Box Number 2 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
Box Number 3 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
Box Number 4 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
Box Number 5 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
Box Number 6 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
Box Number 7 - Is Large? true: 44.0"x13.0"x48.0"H @ 133 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
- Art LineNumber 3
Box Number 8 - Is Large? true: 44.0"x13.0"x48.0"H @ 67 lbs 
- Arts 
- Art LineNumber 3
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
Box Number 9 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 10 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 11 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 12 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 13 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 14 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 15 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 16 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 17 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 18 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 19 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 20 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 21 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 22 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 23 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 24 - Is Large? true: 44.0"x13.0"x48.0"H @ 62 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
Box Number 25 - Is Large? true: 44.0"x13.0"x48.0"H @ 80 lbs 
- Arts 
- Art LineNumber 4
- Art LineNumber 4
- Art LineNumber 1
- Art LineNumber 1
- Art LineNumber 1
- Art LineNumber 1