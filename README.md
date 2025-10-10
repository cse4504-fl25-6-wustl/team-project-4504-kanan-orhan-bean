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