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

1. This code does not need any external dependencies.

2. To run the code input 2 command line arguments. First one being the LineItemFileName, and the second one being the ClientFileName. 

To build the file run `javac entities/*.java Main.java` (or `javac -d bin entities/*.java Main.java` if you need to compile binaries) in the terminal. After building the file to run the code run `java Main [inputFileName] [inputClientFileName]` (or `java -cp bin entities/*.java Main.java` if you need to compile binaries), replacing `[inputFileName]` and `[inputClientFileName]` with the actual names of the input files. The following is how it would look like

```
(base) > team-project-4504-kanan-orhan-bean % javac entities/*.java Main.java     
(base) > team-project-4504-kanan-orhan-bean % java Main input/LineItemInput.csv input/ClientInput.csv
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
========================
```

For example output with premade inputs use `java -cp bin Main input/LineItemInput.csv input/ClientInput.csv`.
Use `rm -r bin/*` to start a clean build, and then recompile and run the code as described above.
For more information about command line arguments in VSCode specifically follow this [link](https://code.visualstudio.com/docs/debugtest/debugging-configuration).

 For Example: You can add a command line arguments in VSCode by creating a folder called `.vscode` and creating a file in it called `launch.json`. Populate the `launch.json` file with the following code, replace the `inputFileName` arguments in `args` with your desired inputFileName, and replace the `inputClientFileName` with your desired inputClientFileName.

```
{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Current File",
            "request": "launch",
            "mainClass": "${file}"
        },
        {
            "type": "java",
            "name": "Main",
            "request": "launch",
            "mainClass": "Main",
            "args": ["inputFileName", "inputClientFileName"],
            "projectName": "team-project-4504-kanan-orhan-bean_f1b55c66"
        }
    ]
}
```