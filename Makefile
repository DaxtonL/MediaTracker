JAVAC = javac
JAVA = java

SRC_DIR = src/main
BIN_DIR = bin
LIB_DIR = lib

JSON_JAR = $(LIB_DIR)/json-20240303.jar

MAIN_CLASS = ui.Main

SOURCES = $(shell find $(SRC_DIR) -name "*.java")


all: compile

compile:
	$(JAVAC) -cp "$(JSON_JAR)" -d $(BIN_DIR) $(SOURCES)

run: compile
	$(JAVA) -cp "$(BIN_DIR):$(JSON_JAR)" $(MAIN_CLASS)

clean:
	rm -rf $(BIN_DIR)/*
