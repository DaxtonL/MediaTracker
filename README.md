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

## User Stories

* As a user, I want to be able to specify the media type, name of a work, length of the work, set its status to "waitlist/viewing/finished/on-hold/dropped", specify its priority, and add it to my list of media.
* As a user, I want to be able to add games, movies, books, TV shows, and manga to my list of media.
* As a user, I want to be able to view a list of all my media.
* As a user, I want to be able to view a filtered list of my media based on status, rating, and/or type.
* As a user, I want to be able to change the status of a work to "waitlist/viewing/finished/on-hold/dropped".
* As a user, I want to be able to log that I watched 2 episodes of a TV show that is marked as "watching".
* As a user, I want to be able to change the status of a work I didn't like to "dropped".
* As a user, I want to be able to give a work I liked a rating of 8/10 after changing its status to "finished".
* As a user, I want to be able to change a work's status to "on hold".
* As a user, I want the status of my media to reflect the type of media it is (e.g. "watching" for TV shows and "playing" for games).
* As a user, I want to be able to save my list of media if I so choose.
* As a user, I want to be prompted and given the option to save my list of media when I quit the application.
* As a user, I want to be prompted and given the option to load my list of media when I open the application.
* As a user, I want to be able to load my list of media from a file if I so choose.
* As a user, I want to be able to save my list of media to a file if I so choose.

## Sample EventLog

```text
Event log cleared.

Created new media tracker

Added media to tracker

Added media to tracker

Added media to tracker

Added media to tracker

Added media to tracker

-- This section is from when a tracker is loaded and media is added to the tracker --

Created new media

Added media to tracker

Logged a viewing for a piece of media

Logged a viewing for a piece of media

Changed rating for a piece of media

Changed status for a piece of media
```

## Reflection

Looking at my UML diagram and even while writing my GUI, I have a lot of classes that extend the `AppPanelUI` class that contain repeated code between them.

This made it harder to debug when I made changes and overall made it harder to quickly code the panels I wanted. To improve my code, I would create and abstract more methods and place them in the `AppPanelUI` interface. Almost all my UI panels had a "back" and "confirm" button that I could probably include in the fields and constructor of the `AppPanelUI` class.

Adding elements such as buttons is also very repetitive, as the only points of variance are often the name and `actionID` of the button, while 3–4 lines of code are repeated frequently. I could likely create a `createButton` method and add that to the abstract class as well.

Another issue in my UI is that currently all classes that extend from `AppPanelUI` return a list of strings as their data type for the `closePanel` method. However, the data they convey needs to be parsed from a list of strings, making it harder to debug and increasing the chance of the code crashing if the wrong data type is parsed.

This is less of an issue since this is a method that will never be user-facing, but it could still be improved for debugging and ease of use. One solution could be creating a new class to store this data and include an identifier that conveys what kind of information should be expected in the list of strings. Additionally, when calling a "get data" method from this class, if the expected data does not match the identified data type, an exception could be thrown and handled, making it easier to avoid invalid data-type crashes.
