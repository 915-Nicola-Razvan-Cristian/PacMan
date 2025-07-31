# PacMan Java

A simple PacMan clone written in Java using Swing.

## Features

- Classic PacMan gameplay
- Customizable map loaded from a text file
- Basic ghost AI

## Getting Started

### Prerequisites

- Java 8 or higher
- A Java IDE (e.g., IntelliJ IDEA, Eclipse, VS Code) or command line tools

### Running the Game

1. **Clone the repository**  
   Download or clone this repository to your local machine.

2. **Add a map file**  
   Place your map text file in `src/assets/map.txt`.  
   The map should use characters to represent walls, pellets, PacMan, ghosts, etc.

3. **Compile and run**  
   From the root directory, run:

   ```sh
   javac -d bin src/*.java
   java -cp bin App
   ```
