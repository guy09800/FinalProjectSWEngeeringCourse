# Tournament Betting System – Java OOP Project

## 1. Project Overview

This repository contains an academic Java project developed as part of an Software Engineering course. The system simulates a tournament betting platform, allowing users to place bets on matches and tournaments, track results, and accumulate points. The project emphasizes clean architecture, object-oriented design, and manual QA through console-based test flows.

## 2. Project Goals and Purpose

The primary goals of this project are:

* To design and implement a non-trivial object-oriented system in Java
* To demonstrate correct use of OOP principles such as encapsulation, abstraction, and responsibility separation
* To model real-world entities (users, teams, matches, tournaments, bets)
* To validate system correctness using structured manual QA (console output–based testing)

## 3. System Architecture Overview

The system is structured around a core domain model representing a sports tournament ecosystem. A central system class manages users, tournaments, teams, and matches. Business logic is separated from UI concerns (GUI / console), and persistence is handled via a dedicated file manager component.

High-level layers:

* **Domain Layer** – Core entities and business logic
* **System Management Layer** – Coordination and orchestration of flows
* **UI Layer** – Console and GUI interaction
* **Infrastructure Layer** – File handling and threading utilities

## 4. Main Entities and Their Roles

### Core Domain Entities

* **User** – Represents a system user, including identification and accumulated points
* **Team** – Represents a team participating in tournaments
* **Match** – Represents a single match between two teams, including timing and results
* **Tournament** – Manages teams, matches, and tournament-level logic

### Betting Entities

* **UserMatchBet** – Represents a user’s bet on a specific match
* **UserTournamentBet** – Represents a user’s bet on an entire tournament
* **TournamentTeamKey** – Composite key used for mapping tournament-team relationships
* **TeamTournamentResult** – Stores final tournament results for a team

### System & Utility Components

* **MySystem** – Central system controller managing users, tournaments, and flows
* **Main** – Application entry point
* **FileManager** – Handles file-based persistence and loading
* **TournamentStatsRevealThread** – Manages delayed or concurrent reveal of tournament results
* **MatchCountdownTimer** – Handles match timing logic

### UI & QA Components

* **GUI** – Graphical user interface layer
* **RightClickMenu** – UI utility component
* **TestConsole** – Manual QA runner using console outputs

## 5. Core Workflows and Processes

* System initialization (loading users, teams, tournaments)
* User registration and participation
* Match scheduling and execution
* Bet placement (match-level and tournament-level)
* Match completion and points calculation
* Tournament result reveal (including multi-threaded execution)
* Console-based validation of flows

## 6. Software Engineering Principles Applied

### Software Engineering

* **Encapsulation** – Internal state is protected and accessed via methods
* **Abstraction** – Clear separation between domain concepts and system orchestration
* **Single Responsibility** – Each class has a well-defined role

### Separation of Concerns

* UI logic is separated from business logic
* Persistence logic is isolated in a dedicated component

### Modularity and Maintainability

* Classes are logically grouped by responsibility
* Clear naming and structure support extensibility

### Design Patterns

* **Controller-style central system manager** (MySystem)
* **Thread-based concurrency** for result reveal and timing logic

## 7. Project Structure

```
/src
 ├── Main.java                # Application entry point
 ├── MySystem.java            # Core system controller
 ├── User.java                # User entity
 ├── Team.java                # Team entity
 ├── Match.java               # Match entity
 ├── Tournament.java          # Tournament logic
 ├── UserMatchBet.java        # Match betting logic
 ├── UserTournamentBet.java   # Tournament betting logic
 ├── TeamTournamentResult.java
 ├── TournamentTeamKey.java
 ├── FileManager.java         # Persistence handling
 ├── GUI.java                 # Graphical UI
 ├── RightClickMenu.java      # UI utility
 ├── MatchCountdownTimer.java # Timing logic
 ├── TournamentStatsRevealThread.java
 └── TestConsole.java         # Manual QA tests
```

## 8. Compilation and Execution

### Compile

```bash
javac *.java
```

### Run

```bash
java Main
```

All execution is performed locally using the Java compiler and runtime.

## 9. Quality Assurance (QA) Methodology

QA is performed manually using structured console output tests:

* Each test prints a clear **expected state** and **actual state**
* Tests are grouped by system functionality (users, matches, tournaments, bets)
* Concurrency behavior is validated through visible interleaving of outputs
* No external testing frameworks (e.g., JUnit) are used, by design

The **TestConsole** class serves as the primary QA driver.

## 10. Technologies and Tools Used

* **Java (JDK)**
* **Software Engineering (OOP)**
* **Multi-threading (Thread / Runnable)**
* **AWT / GUI components**
* **Console-based QA testing**
* **Git & GitHub**

## 11. Academic Context

This project was developed as part of an academic course in Software Engineering. The emphasis is on software engineering quality, correct modeling, and validation through controlled test flows rather than production deployment.

---
