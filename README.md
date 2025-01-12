# COSC2440 | Further Programming - RMIT UNIVERSITY VIETNAM
# Assignment 2: Build a Backend - Rental Agreement Application

### Application Review - Presentation
[Youtube Video - Click me](https://youtu.be/x3Z8aLwmN5k?si=2WFltxoA40n7WPwr)  
### Github repository
[Github repository - Cick me](https://github.com/RMIT-Vietnam-Teaching/further-programming-assignment-2-build-a-backend-4knights)  

## Group: 4Knights
| Student Name     | Student ID  |
|------------------|-------------|
| Khong Quoc Khanh | S4021494    |
| Dao Duc Lam      | S4019052    |
| Luong Chi Bach   | S4029308    |
| Duong Bao Ngoc   | S3425449    |

## Contribution
| Student Name     | Student ID  | Task             | Contribution score | 
|------------------|-------------|------------------|------------|  
| Khong Quoc Khanh | S4021494    | Report, Video, Project management, backend development, database integration, implementing the notification system, Frontend development (JavaFX)     | 25% ( 3 ) |  
| Dao Duc Lam      | S4019052    | Report, Video,  Property management module, rental agreement logic, Hibernate ORM configuration, code optimization     | 25% ( 3)  | 
| Luong Chi Bach   | S4029308    | Report, Video,  Writing documentation, UI design, integrating user interfaces with backend, managing user authentication     | 25% ( 3 )  |  
| Duong Bao Ngoc   | S3425449    | Report, Video, Database schema design, DAO pattern implementation, handling database transactions, testing and debugging     | 25% ( 3 )|  



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
Oracle Open JDK 21.0.5: [Download here](https://www.oracle.com/java/technologies/downloads/#java21)  
JavaFX JDK 21.0.5 : [Download here](https://gluonhq.com/products/javafx/)  

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

## Program Running Instructions  

#### For Windows  
1. **Unzip the program files**:  
   - Locate the `.zip` file containing the program. After unzipping, you should see a folder containing the program’s main `.jar` file and its dependencies.  

2. **Run the main executable file**:  
   - Locate the `RENTAL_AGREEMENT_MANAGER_APPLICATION.jar` file.  
   - Double-click on the file to launch the application.  

3. **If the application does not start**:  
   - Open the terminal and navigate to the folder containing the `.jar` file using the `cd` command.  
   - Verify the `.jar` file is present using:  
     ```bash
     dir
     ```  
   - Run the application with the following command:  
     ```bash
     java -jar RENTAL_AGREEMENT_MANAGER_APPLICATION.jar
     ```  

#### For macOS  
1. **Unzip the program files**:  
   - After unzipping, ensure the `.jar` file and its dependencies are in the same directory.  

2. **Run the application**:  
   - Open the terminal and navigate to the folder using the `cd` command.  
   - Verify the files using:  
     ```bash
     ls
     ```  
   - Ensure that you have JavaFX SDK installed and placed in `/Library/Java/JavaVirtualMachines/`.  
   - Run the application using the following command:  
     ```bash
     java --module-path /Library/Java/JavaVirtualMachines/javafx-sdk-21.0.5/lib --add-modules javafx.controls,javafx.fxml -jar RENTAL_AGREEMENT_MANAGER_APPLICATION.jar
     ```  
   - Wait for 15 seconds, and the application should launch successfully.  

#### Notes  
- The main program executable `.jar` file **must stay in the same folder** as its dependencies.  
- A stable internet connection is required to run the application. If the application cannot connect to the internet, it may fail to launch.  
