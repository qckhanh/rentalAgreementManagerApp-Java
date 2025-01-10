# COSC2440 | Further Programming
# Assignment 2: Build a Backend - Rental Agreement Application

## Group: 4Knights
| Student Name     | Student ID  |
|------------------|-------------|
| Khong Quoc Khanh | S4021494    |
| Dao Duc Lam      | S4019052    |
| Luong Chi Bach   | S4029308    |
| Duong Bao Ngoc   | S3425449    |

## Project Structure
```
├── src/
│   ├── controller/
│   │   ├── Admin/         // Controllers for admin functionality
│   │   ├── Guest/         // Controllers for guest functionality  
│   │   ├── Host/          // Controllers for host functionality
│   │   ├── Owner/         // Controllers for owner functionality
│   │   ├── Renter/        // Controllers for renter functionality
│   │   └── Start/         // Controllers for initial app setup
│   ├── database/          // Database access and management
│   ├── Helper/            // Utility classes
│   ├── model/
│   │   ├── Agreement/     // Rental agreement related models
│   │   ├── Persons/       // User type models
│   │   └── Property/      // Property related models
│   ├── Notification/      // Notification system
│   ├── view/              // JavaFX view factories and enums
│   └── RentalAgreementApplication.java
```

### Package Descriptions

- **controller/**: Contains controllers for different user types and functionalities
  - `Admin/`: Administrative control and management
  - `Guest/`: Guest user interface control
  - `Host/`: Property host management
  - `Owner/`: Property owner management
  - `Renter/`: Renter functionality
  - `Start/`: Application initialization

- **database/**: Database interaction layer
  - Includes DAO implementations for all entities
  - Handles database connections and transactions
  - Manages data persistence

- **Helper/**: Utility classes for common functions
  - `DateUtils.java`: Date formatting and manipulation
  - `ImageUtils.java`: Image handling utilities
  - `InputValidator.java`: Input validation
  - `UIDecorator.java`: UI styling utilities

- **model/**: Core business logic and data models
  - `Agreement/`: Rental agreement models and enums
  - `Persons/`: User type models (Admin, Host, Owner, Renter)
  - `Property/`: Property models and related enums

- **Notification/**: Notification system implementation
  - Handles user notifications and requests
  - Manages notification persistence

- **view/**: JavaFX view management
  - View factories for different user types
  - Menu option enums
  - Scene management

## Development Environment

### Requirements
- Java SDK (version used in development)
- JavaFX
- Hibernate for database management
- Maven/Gradle for dependency management

### Platform:
- Windows:
Orcale OpenJDK 21+

- MacOS:
Oracle Open JDK 21

### Dependencies
- Jakarta Persistence API
- Hibernate Core
- JavaFX
- AtlantaFX for UI components
- Ikonli for icons

### Database
- Uses Hibernate for ORM
- Supports various database backends through Hibernate

### Running the Project
1. Ensure all dependencies are installed
2. Configure database connection in `hibernate.cfg.xml`
3. Run `RentalAgreementApplication.java`

### Key Features
1. Multi-user type support (Admin, Host, Owner, Renter, Guest)
2. Property management system
3. Rental agreement handling
4. Payment processing
5. Notification system
6. User profile management
7. Real-time updates through JavaFX properties

## Design Patterns Used
1. MVC Architecture
2. Factory Pattern (View Factories)
3. Singleton Pattern (ModelCentral)
4. Observer Pattern (JavaFX Properties)
5. DAO Pattern (Database Access)


