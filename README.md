**Analytics Dashboard CRUD Operations**
A robust automated testing framework designed to validate CRUD (Create, Read, Update, Delete) operations for an Analytics Dashboard. This project utilizes Java and TestNG to ensure data integrity and functional reliability across the dashboard's core features.

**🚀 Features**
Full CRUD Validation: Automated test cases for creating, retrieving, updating, and deleting analytics data.

Maven Integration: Dependencies and build lifecycles managed via pom.xml.

Scalable Architecture: Organized source code structure separating test logic from configuration.

Detailed Reporting: Integrated with TestNG for comprehensive execution logs and results.

**🛠️ Tech Stack**
Language: Java

Testing Framework: TestNG

Build Tool: Maven

Scripts: Custom shell/batch scripts for environment setup

**📁 Project Structure**
Plaintext
├── src/                # Main application and test logic
├── scripts/            # Helper scripts for execution
├── lib/                # External libraries and dependencies
├── testng.xml          # Test suite configuration
└── pom.xml             # Maven project configuration
**⚙️ Getting Started**
Prerequisites
Java SDK 11 or higher

Apache Maven installed and configured

An IDE (IntelliJ IDEA, Eclipse, or VS Code)

**Installation & Setup**
Clone the repository:

Bash
git clone https://github.com/sreekanthbalireddi/Anaytics-Dashboard-CRUD-Operations.git
Navigate to the project directory:

Bash
cd Anaytics-Dashboard-CRUD-Operations
Install dependencies:

Bash
mvn clean install
Running Tests
To execute the entire test suite, run:

Bash
mvn test -DsuiteXmlFile=testng.xml
**📝 License**
This project is for educational/demonstration purposes. Feel free to use and modify it for your own learning!

Author: sreekanthbalireddi
