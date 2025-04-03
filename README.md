# Game Management System

A Java-based application for managing games and game categories with a graphical user interface built using JavaFX.

## Features

- Game Management
  - Add new games with details (name, description, release date, image, link)
  - Edit existing games
  - Delete games
  - View game details
  - Play games via embedded web view
  - Search games
  - Sort games by newest/top rated

- Category Management
  - Create game categories
  - Edit category details
  - Delete categories
  - View games by category

## Technical Stack

- Java
- JavaFX for GUI
- MySQL Database
- FXML for layout design
- CSS for styling

## Project Structure

- `src/main/java/`
  - `Controller/` - Contains all JavaFX controllers for different views
  - `Entity/` - Domain model classes (Game, CategorieJeux)
  - `Service/` - Business logic and database operations
  - `Outil/` - Utility classes including database connection
  
- `src/main/resources/`
  - FXML layout files
  - CSS style sheets
  - Images and other assets

## Key Components

### Models
- `Game` - Represents a game with properties like name, description, date, image, etc.
- `CategorieJeux` - Represents a game category

### Services
- `Game_s` - Handles game CRUD operations
- `CategorieJeux_s` - Handles category CRUD operations

### Controllers
- Various controllers for handling user interface interactions and business logic

## Getting Started

1. Ensure you have Java and JavaFX installed
2. Configure database connection in `DataBase.java`
3. Build project using Maven
4. Run `Main.java` to start application

## Database

The application requires a MySQL database with appropriate tables for games and categories. Connection settings can be configured in the `DataBase` class.

## Styling

The application uses two CSS files:
- `style.css` - General styling
- `dashboardDesign.css` - Dashboard-specific styling
