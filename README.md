# SmartShip Package Management System 📦🚛

**Course:** Advanced Programming (CIT3009)\
**Institution:** University of Technology, Jamaica\
**Architecture:** 2-Tier Desktop Application (Java Swing + MySQL)

## 📖 Overview

SmartShip is a Java-based desktop application designed to support
courier company operations. It provides customers with tools to create
and track shipments while giving staff (Managers, Clerks, Drivers) the
ability to manage accounts, fleets, invoices, and delivery assignments.

The system communicates directly with a MySQL database via JDBC and uses
database transaction locking to prevent concurrency conflicts, ensuring
vehicles never get over-assigned.

## ✨ Key Features

### 🛡️ Core Functionality

-   **Concurrency Control:** Uses Pessimistic Locking
    (`SELECT FOR UPDATE`) to guarantee safe assignment of packages to
    vehicles.
-   **Logging:** Full system logging using Log4j2.
-   **Reporting:** Generates PDF reports using OpenPDF (iText).
-   **Validation Rules:** Invoice must be fully paid before assignment;
    capacity validation; zone filtering.

### 👥 User Modules & Capabilities

#### 1. Manager Module

-   Account Management\
-   Fleet Management\
-   PDF Reports: Shipments, Revenue, Performance, Vehicle Utilization

#### 2. Clerk Module

-   Create Assignment (Concurrency-Safe)\
-   Process Invoices\
-   Update Package Status

#### 3. Driver Module

-   View Assigned Deliveries\
-   Update Delivery Status

#### 4. Customer Module

-   Request Shipment (Cost Auto-Calculated)\
-   Track Package (Real-Time)

## 🛠️ Technology Stack

-   **Language:** Java (JDK 8+)\
-   **GUI:** Java Swing\
-   **Database:** MySQL\
-   **Driver:** JDBC\
-   **Libraries:** Log4j2, OpenPDF/iText, mysql-connector-j

## ⚙️ Setup & Installation

### 1. Database Setup

    URL: jdbc:mysql://localhost:3307/smartship
    Username: root
    Password: usbw

Import `smartship.sql`.

### 2. Dependencies

Include: - mysql-connector-j\
- log4j-api, log4j-core\
- openpdf / itext

### 3. Running

Run `gui.MainMenu.java` → select user role.

## 📂 Project Structure

    src/
    ├── gui/
    │   └── MainMenu.java
    ├── managerModule/
    │   ├── ManagerFrame.java
    │   ├── CreateAccView.java
    │   ├── AccDelete.java
    │   ├── ReportPg.java
    │   └── ...
    ├── clerkModule/
    │   ├── CreateAssignment.java
    │   ├── ProcessInvoice.java
    │   ├── PackageStatus.java
    │   └── Login.java
    ├── customerModule/
    │   ├── RequestShipment.java
    │   ├── PackageTracking.java
    │   ├── CreateAccount.java
    │   └── ...
    ├── driverModule/
    │   ├── DriverDashboard.java
    │   └── DriverLogin.java
    └── system/
        ├── User.java
        ├── Shipment.java
        ├── Vehicle.java
        └── ...

## 📝 Authors

-   Sean Groves -- Manager Module\
-   Diwani Walters -- Customer & Driver Modules\
-   Olivia McFarlane -- Clerk Module & Concurrency Logic