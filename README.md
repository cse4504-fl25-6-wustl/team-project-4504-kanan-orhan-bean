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

To build the file run `javac -d bin Main.java` in the terminal. After building the file to run the code run `java -cp bin Main [inputFileName] [inputClientFileName]`, replacing `[inputFileName]` and `[inputClientFileName]` with the actual names of the input files. The following is how it would look like

```
(base) > team-project-4504-kanan-orhan-bean % javac Main.java      
(base) > team-project-4504-kanan-orhan-bean % java Main hello world
Input File Name: hello
Client File Name: world
```

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