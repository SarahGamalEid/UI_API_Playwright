# Playwright Java Hybrid Automation Framework (UI & API)

A robust, enterprise-grade test automation framework built using **Java**, **Microsoft Playwright for Java**, **TestNG**, and **Jackson**.

This framework provides end-to-end automation for both **Web UI** (OrangeHRM Demo) and **RESTful API** services with support for data-driven testing and parallel execution.

---

## 📋 Table of Contents
- [Project Architecture](#-project-architecture)
- [Design Patterns Used & Rationale](#-design-patterns-used--rationale)
  - [1. Page Object Model (POM)](#1-page-object-model-pom)
  - [2. Factory Pattern](#2-factory-pattern)
  - [3. Builder Pattern](#3-builder-pattern)
  - [4. Fluent Interface / Method Chaining](#4-fluent-interface--method-chaining)
  - [5. Singleton Pattern](#5-singleton-pattern)
- [Framework Features](#-framework-features)
- [Prerequisites](#-prerequisites)
- [How to Run Tests](#-how-to-run-tests)
  - [Running UI Tests](#running-ui-tests)
  - [Running API Tests](#running-api-tests)
  - [Running Full Test Suite via TestNG](#running-full-test-suite-via-testng)
- [Test Scenarios Covered](#-test-scenarios-covered)
  - [UI Automation Flow (OrangeHRM)](#ui-automation-flow-orangehrm)
  - [API Automation Flows](#api-automation-flows)
- [Configuration Reference](#-configuration-reference)

---

## 🏛 Project Architecture

```
playwright-ui-api-framework
├── pom.xml                                   # Maven dependencies and plugins configuration
├── testng.xml                                # TestNG test suite runner
├── README.md                                 # Framework documentation
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── gamil
    │   │           ├── api
    │   │           │   ├── api               # API Service Clients (LoginApi, UserApi)
    │   │           │   ├── base              # Base API Test Context setup/teardown
    │   │           │   └── models            # POJO Data Models (UserRequest, UserResponse, etc.)
    │   │           └── ui
    │   │               ├── base              # Base UI Test with lifecycle hooks
    │   │               ├── config            # ConfigReader (Singleton Pattern)
    │   │               ├── factory           # PlaywrightFactory (Factory Pattern)
    │   │               ├── models            # UserData model (Builder Pattern)
    │   │               └── pages             # Page Objects (POM + Fluent Interface)
    │   └── resources
    │       ├── config.properties             # Environment and browser settings
    │       └── logback.xml                   # Logging configuration
    └── test
        ├── java
        │   └── com
        │       └── gamil
        │           ├── api
        │           │   └── tests             # API Test Classes (E2E & Data-Driven)
        │           └── ui
        │               └── tests             # UI Test Classes (OrangeHRM Scenarios)
        └── resources
            └── testdata
                └── users.json                # External test data for Data-Driven tests
```

---

## 🎨 Design Patterns Used & Rationale

This framework incorporates **5 core Software Engineering & Test Automation Design Patterns**:

### 1. Page Object Model (POM)
* **Classes:** [`BasePage`](src/main/java/com/gamil/ui/pages/BasePage.java), [`LoginPage`](src/main/java/com/gamil/ui/pages/LoginPage.java), [`DashboardPage`](src/main/java/com/gamil/ui/pages/DashboardPage.java), [`AdminPage`](src/main/java/com/gamil/ui/pages/AdminPage.java), [`AddUserPage`](src/main/java/com/gamil/ui/pages/AddUserPage.java)
* **What it does:** Each web page or functional area is represented by its own dedicated Java class containing locators and action methods.
* **Why it was used:**
  - **Separation of Concerns:** Separates test assertion logic from page element identification and UI interaction logic.
  - **High Maintainability:** When the OrangeHRM UI layout or locators change, updates are made only once inside the corresponding Page Object without modifying any test cases.
  - **Code Reusability:** Common page interactions can be called across multiple test scenarios without duplicating locator code.

### 2. Factory Pattern
* **Class:** [`PlaywrightFactory`](src/main/java/com/gamil/ui/factory/PlaywrightFactory.java)
* **What it does:** Centralizes the initialization and teardown of `Playwright`, `Browser`, `BrowserContext`, and `Page` objects.
* **Why it was used:**
  - **Decoupling:** Decouples the tests from concrete browser instantiation logic (`chromium`, `firefox`, `webkit`, `chrome`, `msedge`).
  - **Thread-Safety for Parallel Execution:** Uses `ThreadLocal<Page>`, `ThreadLocal<BrowserContext>`, and `ThreadLocal<Browser>` to isolate browser instances per thread, avoiding race conditions during parallel test runs.
  - **Centralized Browser Management:** Easily configure viewport size, timeout, headless/headed mode, and slowMo in one place.

### 3. Builder Pattern
* **Class:** [`UserData`](src/main/java/com/gamil/ui/models/UserData.java) with `UserData.Builder`
* **What it does:** Provides a step-by-step constructor mechanism for creating test data objects with sensible defaults and fluent attribute setters.
* **Why it was used:**
  - **Avoids Telescoping Constructors:** Eliminates cumbersome constructors with multiple parameters (`userRole`, `employeeName`, `status`, `username`, `password`, `confirmPassword`).
  - **Readability & Immutability:** Makes test data generation clean, self-documenting, and immutable:
    ```java
    UserData newUser = UserData.builder()
            .userRole("Admin")
            .employeeName("a")
            .status("Enabled")
            .username("autouser_12345")
            .password("Test@12345678")
            .build();
    ```

### 4. Fluent Interface / Method Chaining Pattern
* **Classes:** [`LoginPage`](src/main/java/com/gamil/ui/pages/LoginPage.java), [`AdminPage`](src/main/java/com/gamil/ui/pages/AdminPage.java), [`AddUserPage`](src/main/java/com/gamil/ui/pages/AddUserPage.java)
* **What it does:** Page action methods return `this` (the current page object) or the new Page Object navigated to.
* **Why it was used:**
  - **Natural Test Flow:** Creates a readable Domain-Specific Language (DSL) representing the user journey:
    ```java
    DashboardPage dashboard = loginPage
            .navigateToLoginPage()
            .loginAs("Admin", "admin123");

    AdminPage adminPage = dashboard.clickAdminMenu();
    ```
  - **Clean Code:** Eliminates redundant page object instantiations inside test methods.

### 5. Singleton Pattern
* **Class:** [`ConfigReader`](src/main/java/com/gamil/ui/config/ConfigReader.java)
* **What it does:** Guarantees that only a single, thread-safe instance of the configuration reader exists throughout the test execution lifecycle.
* **Why it was used:**
  - **Efficiency:** Reads `config.properties` from disk once and keeps properties loaded in memory.
  - **Global Access & System Override:** Provides a unified entry point to read configuration properties with automatic fallback to Maven/system properties (e.g. `-Dui.browser=firefox`).

---

## ⚡ Framework Features

- **Multi-Browser Support:** Chromium, Google Chrome, Mozilla Firefox, Microsoft Edge, WebKit (Safari).
- **Headless & Headed Modes:** Easily toggled via configuration file or command line.
- **RESTful API Client:** Powered by Playwright `APIRequestContext` with Jackson serialization/deserialization.
- **Data-Driven Testing:** Support for TestNG `@DataProvider` and JSON test data sources.
- **Auto-Wait & Resilient Locators:** Leverages Playwright's native auto-waiting mechanisms to eliminate flaky sleeps.

---

## 📦 Prerequisites

1. **Java JDK:** Java 17, 21, or 24 installed.
2. **Maven:** Apache Maven 3.6+ installed (or using the included wrapper).

---

## 🚀 How to Run Tests

### Running UI Tests

To execute the UI test suite on OrangeHRM:

```bash
# Run UI Tests with default configuration (Chromium, Headless)
./mvnw test -Dtest=OrangeHrmUserManagementTest

# Run UI Tests in Headed Mode (Browser UI visible)
./mvnw test -Dtest=OrangeHrmUserManagementTest -Dui.headless=false

# Run UI Tests with a specific browser (firefox, webkit, chrome, msedge)
./mvnw test -Dtest=OrangeHrmUserManagementTest -Dui.browser=firefox -Dui.headless=false
```

### Running API Tests

To execute the API test suites:

```bash
# Run End-to-End API Test with multiple datasets (CRUD lifecycle)
./mvnw test -Dtest=EndToEndApiTest

# Run Data-Driven API Test reading from testdata/users.json
./mvnw test -Dtest=DataDrivenApiTest

# Run FirstApiTest
./mvnw test -Dtest=FirstApiTest
```

### Running Full Test Suite via TestNG

To run the complete suite configured in [`testng.xml`](testng.xml):

```bash
./mvnw clean test
```

*(Note: If you have global `mvn` configured in your PATH, you can use `mvn` in place of `./mvnw`)*

---

## 🔍 Test Scenarios Covered

### UI Automation Flow (OrangeHRM)
* **Test Class:** [`OrangeHrmUserManagementTest`](src/test/java/com/gamil/ui/tests/OrangeHrmUserManagementTest.java)
* **Steps:**
  1. Open OrangeHRM portal: `https://opensource-demo.orangehrmlive.com/`
  2. Log in using valid credentials (`Admin` / `admin123`).
  3. Verify dashboard loads successfully.
  4. Navigate to the **Admin** module via sidebar navigation.
  5. Click the **+ Add** button to open the Add User form.
  6. Fill in user details (User Role, Employee Name search/select, Status, unique Username, Password, Confirm Password) generated via Builder Pattern.
  7. Click **Save** and wait for table redirection.
  8. Search for the newly created user in the System Users table and verify their presence in the search results.

### API Automation Flows
* **End-to-End User CRUD Lifecycle:** [`EndToEndApiTest`](src/test/java/com/gamil/api/tests/EndToEndApiTest.java)
  - `POST /users` - Create user with payload.
  - `GET /users` - Retrieve users list and verify created record.
  - `PUT /users/{id}` - Update user details.
  - `DELETE /users/{id}` - Delete user record.
  - Tested across **multiple test datasets** using TestNG `@DataProvider`.
* **Data-Driven API Test:** [`DataDrivenApiTest`](src/test/java/com/gamil/api/tests/DataDrivenApiTest.java)
  - Reads structured JSON datasets from [`users.json`](src/test/resources/testdata/users.json).
  - Automatically executes API creation and validation for each dataset entry.

---

## ⚙️ Configuration Reference

Edit [`src/main/resources/config.properties`](src/main/resources/config.properties) to adjust runtime settings:

```properties
# Application UI Configuration
ui.baseUrl=https://opensource-demo.orangehrmlive.com/
ui.browser=chromium       # options: chromium, firefox, webkit, chrome, msedge
ui.headless=true          # options: true, false
ui.slowMo=0               # slow motion delay in ms
ui.timeout=30000          # timeout in ms

# Application API Configuration
api.baseUrl=https://reqres.in/api
api.timeout=10000

# Default Credentials
default.username=Admin
default.password=admin123
```
