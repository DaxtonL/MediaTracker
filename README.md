# Media Tracking Application

## Target Audience

As someone who likes various types of media, I use multiple websites to track my progress and ratings of various works.

However, I find it a hassle to always switch between applications and wanted a single application to handle all types of media that I consume.

As a result, the target audience for my application is anyone who consumes various types of media (such as games, movies, shows, books).

## Requirements

This application requires:

* Java (JDK)
* `make`
* The JSON library included in `lib/json-20240303.jar`

## Building and Running

The project includes a `Makefile` to simplify compiling and running the application.

### Compile the Application

From the root directory of the project, run:

```bash
make
```

This compiles all Java source files in `src/main/` and places the resulting `.class` files in the `bin/` directory.

You can also explicitly run:

```bash
make compile
```

### Run the Application

To compile and run the application:

```bash
make run
```

The application will start from the `ui.Main` class.

### Clean Compiled Files

To remove all compiled Java files:

```bash
make clean
```

This removes the contents of the `bin/` directory. The source code in `src/` is not affected.

### Manual Compilation

If the Makefile is unavailable, the application can be compiled manually with:

```bash
javac -cp "lib/json-20240303.jar" -d bin $(find src/main -name "*.java")
```

It can then be run with:

```bash
java -cp "bin:lib/json-20240303.jar" ui.Main
```

## Project Structure

```text
MediaTracker/
├── bin/                # Compiled Java files (not tracked by Git)
├── data/               # Personal tracker data (not tracked by Git)
├── lib/                # External libraries
├── src/
│   ├── main/           # Application source code
│   │   ├── exceptions/
│   │   ├── logging/
│   │   ├── model/
│   │   ├── persistence/
│   │   └── ui/
│   └── test/           # Test source code
├── Makefile
├── README.md
├── checkstyle.xml
└── UML_Design_Diagram.png
```

Personal media tracker data and compiled files are excluded from version control through `.gitignore`.

## Application Features

My application will allow users to easily add and remove works to their list of tracked media.

Users will also be able to update a given work's status, which will include:

* Want to watch/play/read
* Currently watching/playing/reading
* Finished
* On-hold
* Dropped

In addition, depending on the work's status, the user will be able to track other aspects of their media such as:

* Priority in the watch/play/read later list
* Progress through currently watching/playing/reading works
* Rating once a work is finished
