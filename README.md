# OpenCart UI Automation Framework

![Playwright](https://img.shields.io/badge/Playwright-2.3.0-blue)
![Cucumber](https://img.shields.io/badge/Cucumber-7.11.1-green)
![Allure](https://img.shields.io/badge/Allure-2.23.0-red)

## 📌 Overview

Automated UI test for OpenCart registration functionality using:
- **Playwright** for reliable browser automation
- **Cucumber** for behavior-driven development (BDD)
- **Allure** for beautiful test reporting
- **Maven** for dependency management

## 🚀 Quick Start

### Prerequisites
- Java JDK 11+
- Maven 3.8+
- Node.js 16+ (for Playwright)



### Installation
```bash
git clone https://github.com/your-repo/opencart-automation.git
cd opencart-automation
mvn clean install
npx playwright install

Project Structure
text
src/
├── test/
│   ├── java/com/opencart/
│   │   ├── runners/        # Test runners
│   │   ├── stepdefinitions # Cucumber step definitions
│   │   ├── pages/          # Page objects
│   │   └── utils/          # Utilities
│   └── resources/
│       ├── features/       # Cucumber feature files
│       └── allure/         # Allure environment config
```


### 🏃 Running Tests
```bash
Basic Execution
 --> mvn clean test

With Options
 --> mvn test \
  -Dbrowser=firefox \         # chromium (default), firefox 
  -Dheadless=false \          # Show browser window
  -Dcucumber.filter.tags="@Registration"
  
  
```