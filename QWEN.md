# IN THE MYST - Project Context

## Project Overview

**IN THE MYST** is a tactical strategy game with RPG elements, developed in Java using LWJGL 3 and OpenGL 3.3+. The project features advanced graphics including custom shaders, particle systems, shadows, and fog of war. It combines procedural world generation (using Perlin Noise) with turn-based/real-time tactical combat system with AI opponents.

### Key Features
- Advanced OpenGL 3.3+ graphics with custom GLSL shaders
- Tactical gameplay with AI opponents
- Procedural world generation using Perlin Noise
- Modular architecture with clear separation between engine core and game logic
- Save/load system for game progress
- Inventory and equipment system
- Multiple scene types (menu, tactical, strategy, cutscenes, logo)

## Tech Stack

- **Language**: Java 17+
- **Graphics**: LWJGL 3.3.3, OpenGL 3.3+, GLSL shaders
- **Math**: JOML 1.10.0 (plus custom math library in `core/math`)
- **Build Tool**: Gradle
- **IDE**: IntelliJ IDEA
- **Dependencies**: LWJGL (core, glfw, opengl, stb, assimp), JOML

## Project Structure

```
D:\CODE\InTheMyst\
├── src/
│   ├── ru/phoenix/
│   │   ├── core/           # Engine core
│   │   │   ├── kernel/     # Core classes (Game.java, CoreEngine.java, Window.java, Render.java, Camera.java, Input.java)
│   │   │   ├── shader/     # Shader management
│   │   │   ├── buffer/     # Buffer management
│   │   │   ├── config/     # Configuration classes
│   │   │   ├── debug/      # Debug utilities
│   │   │   ├── frame/      # Frame management
│   │   │   ├── loader/     # Resource loading
│   │   │   ├── math/       # Custom math library
│   │   │   └── util/       # Utility classes
│   │   └── game/           # Game logic
│   │       ├── scene/      # Scene system (menu, tactical, strategy, cut, logo, loading)
│   │       ├── content/    # Game content (characters, objects, inventory, stages)
│   │       ├── logic/      # Game logic (generator, movement, lighting, battle, effects, grid)
│   │       ├── hud/        # HUD system (inventory, elements, displays)
│   │       ├── property/   # Game properties and controllers
│   │       ├── loop/       # Scene control loop
│   │       └── datafile/   # Save/load system
│   └── META-INF/           # Manifest files
├── data/                   # Game data files
│   ├── config/             # Configuration files
│   ├── content/            # Game content
│   ├── logo/               # Logo assets
│   ├── save/               # Save game files
│   └── shader/             # GLSL shaders (vs/, fs/, gs/ directories)
├── build.gradle            # Gradle build configuration
└── README.md               # Project documentation
```

## Building and Running

### Prerequisites
- Java JDK 17 or higher
- Graphics card with OpenGL 3.3+ support
- 2 GB RAM minimum
- Windows/Linux/macOS

### Commands

```bash
# Run the game (Windows)
gradlew.bat run

# Run the game (Linux/Mac)
./gradlew run

# Build the project
gradlew.bat build

# Clean build artifacts
gradlew.bat clean

# Build JAR
gradlew.bat jar
```

### IntelliJ IDEA
1. Open project in IDEA (`File → Open → select project folder`)
2. Wait for Gradle to import dependencies
3. Run via Run button or Gradle `run` task

## Architecture

### Core Engine (`ru.phoenix.core`)
- **Game.java**: Main entry point (main class configured in build.gradle)
- **CoreEngine.java**: Main game loop and engine management
- **Window.java**: Window creation and management using GLFW
- **Render.java**: Rendering pipeline
- **Camera.java**: Camera system
- **Input.java**: Input handling (keyboard, mouse)

### Game Logic (`ru.phoenix.game`)
- **Scene System**: Different scenes for different game states (MenuScene, TacticalScene, StrategyScene, CutScene, LogoScene, LoadingScene)
- **Character System**: Base Character class with specialized types (humans, factions like Anarchy/Communis)
- **Inventory System**: Equipment, Backpack, Items with different types and slots
- **World Generation**: Procedural generation with components (HeightMap, Trees, Water, Grass, Resources)
- **Battle System**: Turn-based/real-time combat with SimpleAI
- **Grid System**: Cell-based tactical movement and pathfinding
- **HUD System**: Inventory HUD, indicators, buttons, displays
- **Save/Load**: Complete save system with SaveGame, SaveData, SaveElement classes

### Shader System
- Vertex shaders in `data/shader/vs/`
- Fragment shaders in `data/shader/fs/`
- Geometry shaders in `data/shader/gs/`

## Development Conventions

- **Package Structure**: Clear separation between core engine (`ru.phoenix.core`) and game logic (`ru.phoenix.game`)
- **Modular Design**: Components organized by functionality with clear responsibilities
- **Encoding**: UTF-8 for all source files
- **JVM Arguments**: `-Xms512m -Xmx2g -Dfile.encoding=UTF-8`

## Key Entry Points

- **Main Class**: `ru.phoenix.core.kernel.Game`
- **Core Engine**: `ru.phoenix.core.kernel.CoreEngine`
- **Scene Management**: `ru.phoenix.game.loop.SceneControl`
- **Game Controller**: `ru.phoenix.game.property.GameController`

## Configuration

- **Window Settings**: Located in `src/ru/phoenix/core/config/` (check for WindowConfig.java or similar)
- **Game Config**: Located in `data/config/`
- **Gradle Settings**: `build.gradle` contains JVM args and application configuration

## Current Status

- **Development Stage**: Alpha/Beta
- **Total Java Files**: 124+ files in the game package
- **Active Development**: Recent file modifications indicate ongoing development

## Notes

- The project uses a custom math library alongside JOML
- Supports multiple factions/character types (Anarchy, Communis factions)
- Includes complex systems: inventory, equipment, battle AI, pathfinding, procedural generation
- Scene-based architecture allows for clean state management between different game modes
