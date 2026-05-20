# 🗡️ Dungeon Crawler RPG

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)

> **Note:** This repository contains a standalone academic project developed for the *Principles of Object-Oriented Programming* course as part of the Software Engineering B.Sc. program at Ben-Gurion University. It serves to showcase practical experience with advanced OOP concepts, event-driven design, and unit testing in Java.

## 📖 Overview

This project is a fully functional, terminal-based Role-Playing Game (RPG) written in **Java**. The player navigates through 2D grid-based dungeon levels, battling various monsters and avoiding traps to progress. The core focus of this project is the robust underlying architecture, designed from scratch utilizing strict Object-Oriented Programming principles and design patterns.

## ✨ Key Features

* **Diverse Playable Classes:** Choose between distinct classes—`Warrior`, `Mage`, `Rogue`, and `Hunter`—each with unique abilities, resource management systems (e.g., Mana, Energy), and leveling mechanics.
* **Dynamic Enemy AI:** Face off against different enemy types, including roaming `Monsters` and hidden `Traps`, each implementing unique movement and engagement behaviors.
* **Level Progression:** Dynamic level loading from text files (`level1.txt`, `level2.txt`, etc.), allowing for easy expansion and customized dungeon layouts.
* **Event-Driven Combat & UI:** The game relies on a custom callback system (e.g., `MessageCallback`, `PlayerDeathCallback`) to broadcast game events to the user interface, keeping the backend logic completely decoupled from the presentation layer.

## 🏗️ Architecture & OOP Principles

The codebase is structured to enforce strong separation of concerns, utilizing several core design principles:

* **Extensive Polymorphism & Inheritance:** The game uses a deep class hierarchy. For example, `Mage` and `Warrior` inherit from `Player`, which inherits from `Unit`, which is a `Tile`. This allows the `Board` to interact with all entities uniformly.
* **Callback Mechanisms:** Instead of the game logic directly printing to the console, entities emit events via interfaces (`MessageCallback`). This decouples the view (`GameMain`, `GameInitializer`) from the domain models.
* **Factory Pattern:** A `TileFactory` is used during the parsing phase (`LevelLoader`) to dynamically instantiate the correct classes (Walls, Enemies, Players) based on the input text files.
* **Robust Testing:** A comprehensive test suite (`tests/` directory) ensures the reliability of core game mechanics, unit behaviors, and utility classes using unit tests.

## 🛠️ Getting Started

### Prerequisites
* [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) 8 or higher.
* A terminal or command-line interface.

### Compilation & Execution
1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/oop-game.git](https://github.com/your-username/oop-game.git)
    cd oop-game
    ```
2.  **Compile the Java source files:**
    Ensure you compile the files from the `src` directory.
    ```bash
    javac -d out -sourcepath src src/View/GameMain.java
    ```
3.  **Run the Game:**
    You will need to pass the path to the directory containing the level files as a command-line argument.
    ```bash
    java -cp out View.GameMain instructions/levels_dir/
    ```

## 📂 Project Structure

```text
oop-game/
├── instructions/            
│   └── levels_dir/          # Text files defining the layout of each dungeon level
├── src/
│   ├── Game/                # Core business logic and domain models
│   │   ├── Board/           # Game board and grid management
│   │   ├── Callbacks/       # Interfaces for event-driven communication
│   │   ├── Tiles/           # Hierarchy of all objects on the board (Units, Empty, Wall)
│   │   └── Utils/           # Helper classes (Position, Resource)
│   └── View/                # Presentation layer and input parsing
│       ├── Input/           # User command handlers
│       ├── Parser/          # LevelLoader and TileFactory
│       └── GameMain.java    # Application entry point
└── tests/                   # Unit testing suite for Game logic and Units