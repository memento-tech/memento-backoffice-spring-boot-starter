
## Table of Content

- [1. Introduction](#introduction)
  - [1.1 Key Objectives](#key-objectives)
  - [1.2 Current Features](#current-features)
  - [1.3 Intended Audience](#intended-audience)
  - [1.4 Prerequisites](#prerequisites)
- [2. Getting Started](#getting-started)
  - [2.1 Installation](#installation)
  - [2.2 Quick Start Guide](#quick-start-guide)
  - [2.3 UI Guide](#ui-guide)
- [3. Technical Documentation](#technical-documentation)
  - [3.1 Authentication](#authentication)
  - [3.2 Translations Management Tool](#translations-management-tool)
  - [3.3 Media Management Tool](#media-management-tool)
  - [3.4 Functional Widgets](#functional-widgets)
  - [3.5 UI Widgets](#ui-widgets)
  - [3.6 Cron Job Management Tool](#cron-job-management-tool)
  - [3.7 Live Console](#live-console)
- [4. API Documentation](#api-documentation)
- [5. Contribution Guide](#contribution-guide)
- [6. License](#license)


## **Introduction**
 **Overview of the project**
 
The Backoffice Spring Boot Starter is built to make backend management simple and efficient. It serves as a **developer-friendly console** that eliminates the need to work directly with the database using tools like SQL or database management software and enables employees outside of development zone to work and monitor application. By focusing on streamlining workflows, it empowers teams to manage changes and resolve issues more effectively.
### **Key Objectives**
1.  **Simplify Backend Workflows**
    -   Perform common backend tasks without dealing directly with the database.
    -   Quickly identify and track changes made by the main application to the database.
2.  **Developer and QA Efficiency**
    -   Provide developers and QA engineers with tools to make their jobs easier and faster.
    -   Centralize backend operations to reduce dependency on external tools.
3.  **Future Growth**
    -   Expand to include features for **business users**, enabling them to monitor the app, analyze data, and collaborate with development teams.
    -   Remove dependency on Hibernate and implement abstract JPA solution in order to accommodate other JPA implementations like Spring Data JPA, MyBatis, EclipseLink etc.
### **Current Features**
-   **Plug-and-Play Integration**
    -   Get started quickly with minimal setup required.
-   **Translation Support**
    -   Manage and update application translations with ease.
-   **Media Management**
    -   Upload, organize, and manage media files like images and documents.
-   **Cron Job Support** (coming soon)
	-  Create and run simple and complex cron jobs and monitor their health

The Backoffice Spring Boot Starter bridges the gap between development, testing, and future business needs, making it a powerful and flexible solution for managing backend operations.
### **Intended Audience**
The Backoffice Spring Boot Starter is designed for **small companies**, **startups**, and **mid-sized companies** that want to accelerate development and streamline backend operations.
-   **For Startups**:
    -   Perfect for new projects looking to get up and running quickly.
    -   All you need is **Spring Boot** and **Hibernate**—the starter takes care of the rest.
-   **For Existing Projects**:
    -   Easily adaptable for mid-sized or established projects.
    -   Some adjustments may be required to integrate the starter seamlessly, but these will be addressed in future sections.
 
Whether you're launching a new project or enhancing an existing one, the Backoffice Spring Boot Starter provides a flexible, easy-to-use solution to boost your development process.
### **Prerequisites**
Before getting started with the Backoffice Spring Boot Starter, ensure you have the following knowledge and tools:
#### **Required Knowledge**
-   **Java**: A solid understanding of Java is essential for working with the backend.
-   **Spring Boot**: Even though familiarity with Spring Boot is mandatory, it's likely already part of your toolkit if you're building a Spring Boot application.
-   **Hibernate**: Understanding Hibernate is necessary for working with the starter's database integration.
-   **ReactJS**: The frontend uses React, and you'll need knowledge of it to extend the current implementation or customize features using your code.
#### **Tools Needed**
-   **JDK**: Java Development Kit, compatible with your project's Spring Boot version.
-   **Maven or Gradle**: For dependency management and building the project.
-   **Node.js**: Required for the React frontend development and build process.

With these prerequisites in place, you're ready to integrate and start using the starter.

## **Getting Started**

### **Installation**
Backoffice starter is meant to be easily used and installed in few steps. 

**Step 1: Adding dependency to memento-backoffice-spring-boot-starter**
```
pom.xml

<dependency>  
 <groupId>com.memento.tech</groupId>  
 <artifactId>memento-backoffice-spring-boot-starter</artifactId>  
 <version>${project.version}</version>  
</dependency>
```
```
build.gradle

dependencies { implementation "com.memento.tech:memento-backoffice-spring-boot-starter:${project.version}" }
```
**Step 2: Configuration of backoffice** 

In order for backoffice to work there are few configuration properties that need to be added to the application.yaml/application.properties file:
 - **memento.tech.backoffice.enabled**
   - this property will allow Spring Boot to load all required beans into context. With this property set to **true** backoffice will be available to the end user with basic functionalities.
 - **memento.tech.backoffice.media.enabled**
   - this property will enable backoffice to manage media. 
 - **memento.tech.backoffice.media.internal.storage.enabled**
   - this property will enable default implementation of media management. If there is no other implementation of media management provided by end user, this property is required alongside previous property. More on media management topic later.
 - **memento.tech.backoffice.media.file.import.upload.directory**
   - property will be used by media management tool to determin where media should be stored. This property is required when previous property is set to **true**.
 - **memento.tech.backoffice.media.mapping**
   - this property is used by media management tool. All medias provided (if allowed) will be visible via mapping http://appliication.domain/mediaMappingProperty/mediaId.
 - **memento.tech.backoffice.translation.enabled**
   - this property is used for enabling translation management tool provided by backoffice starter.
 - **memento.tech.backoffice.translation.useDefaultIfBlank**
   - if this property is set to true and no translations were found for specific language, default language translation will be provided. If default language translation was not found also, translation code will be returned as an indication that there is no translation provided for both searched and default language. If this property is set to false, only default language part will be omitted.
 - **memento.tech.backoffice.translation.default.language**
   - property used for setting default language. **Note** that languages provided must be in ISO standard.
 - **memento.tech.backoffice.auth.token.secret**
   - property used for setting token secret. Default value is provided but it is recommended that specific one is provided.
 - **memento.tech.backoffice.auth.cookie.name**
   - property used for setting http-only cookie name. Default cookie name is "backofficeAccessCookie". More on this in section Backoffice Auth Configuration.
 - **memento.tech.backoffice.auth.cookie.expiry**
   - property used for setting cookie expiry time in milliseconds. Default value is 0 milliseconds.
 - **memento.tech.backoffice.development.mode**
   - property used for creating backoffice development used. If set to true (default value is true). User will be created with username "test" and password "password". It is recommended to set this value to false on production environment.
### **Quick Start Guide**
https://github.com/memento-tech/memento-backoffice-spring-boot-starter/tree/main

Source code of Backoffice Spring Boot Starter can be found on link above. Coupled with source code there is a Quick Start Project called "demo-project". Developer can pull that demo-project with source code of the Backoffice Starter and can run it as any application. Bellow are the steps to run demo-project.

- **Pull source code using GitHub**
   - ``git clone https://github.com/memento-tech/memento-backoffice-spring-boot-starter.git``
- **IDE Import**
   - Using your preferred IDE import project using maven.
- **Maven Build**
   - Since this way backoffice starter is not prebuilt, run maven command: ``mvn clean install -pl backoffice-starter``
- **Run**
   - Finally, run demo-application through your preferred IDE. Backoffice UI will be available on address http://localhost:8080/backoffice/login

### **UI Guide**
In this section, pages will be listed and parts of the UI will be explained. UI is built with UX in mind so most of the components are pretty much self explanatory.

**Login Page** 
Login page is a simple page that contains only login form. For more information about authentication process of Backoffice Starter go to [Authentication section](#authentication)

<img width="1469" alt="backoffice-starter-login-page" src="https://github.com/user-attachments/assets/b3e0738b-3635-496f-af47-983300aa1798" />

**Console Page** 

Console page is the main and currently only page of Backoffice Starter. Page is divided into two sections:
 - console navigation
 - console data management

<img width="1469" alt="backoffice-console-start" src="https://github.com/user-attachments/assets/0571da46-c80d-4954-904e-54a149562466" />

**Console Navigation**

When first starting Backoffice Starter, console navigation will be empty with message displaying "Please refresh metadata" (image 1, left side). Clicking on the button "**Refresh metadata**" software will go through all entities provided and collect all information needed for data management. After few seconds console navigation will be populated with entities and groups of entities(image 1, right side). 

Button "**Refresh metadata**" should be used only when change is provided to the entities and their structure. Regular refresh of the page should still be done via F5 button or button provided by the browser.

When entity button is clicked (button without chevron), data management part will be changed according to the entity chosen. When group button is clicked (button with chevron on the right) list of entities of selected group will be shown bellow group button.

<div style="display=flex">
	<img width="248" alt="navigation-before-refresh" src="https://github.com/user-attachments/assets/8f7dc49c-6ec2-4428-8e01-4fbd4ff4d3b3" />
	<img width="248" alt="navigation-after-refresh" src="https://github.com/user-attachments/assets/4f8300bc-9f76-4815-8be6-29de729fe315" />
</div>

**Console Data Management**

Console Data Management section is divided into two sections:
 - toolbar (top of the page)
 - data management

When first starting Backoffice Starter, data management part will be empty and toolbar will contain only "Logout" button.

<img width="1220" alt="data-management-empty" src="https://github.com/user-attachments/assets/08ccf27d-f4c9-435d-b7cd-4ba001dacc1f" />

When metadata is collected and navigation has all entities, user can click on any entity in order to trigger data management section to reload and collect all required information for selected entity. Based on entity configuration, toolbar can contain creation button or other (user implemented) widgets.

Once user selects entity, records will be displayed on data management section if any. User can select number of records to show in lower toolbar. Lower toolbar contains selected entity records information and show options. On top are show options and user can select number of records to display per page, options are 10, 20, 50 and "all". On bottom information about total number of records found is displayed.

Records are displayed in main part of the data management section inside table. Table contains displayable columns configured via backoffice annotations. More information about available annotations can be found [here](#backoffice-annotations). Table also contains two additional columns, delete column and no column (simple order 1 to n).

To delete record, click on thrash icon on the left. Note that this action is not reversible.
To update record, click on any part of the record row, once clicked entity popup will be shown. Entity popup contains all information both updatable and non updatable.

<img width="1470" alt="data-management-populated" src="https://github.com/user-attachments/assets/fee2bbb9-abfb-420a-b48e-0305845d1d5b" />

**Entity Creation Popup** 

*TODO*

**Media Creation Popup** 

*TODO*

**Translation Creation Popup** 

*TODO*

## **Technical Documentation**
### **Authentication**
*coming soon*
### **Translations Management Tool**
*coming soon*
### **Media Management Tool**
*coming soon*
### **Functional Widgets**
*coming soon*
### **UI Widgets**
*coming soon*
### **Cron Job Management Tool**
*coming soon*
### **Live console**
*coming soon*

## **API Documentation**

-   Comprehensive list of endpoints (e.g., `/api/auth/login`, `/api/backoffice/**`).
-   Request and response formats.
-   Authentication and authorization requirements for each endpoint.

## **Contribution Guide**

-   How to contribute to the project.
-   Coding standards and guidelines.
-   Reporting bugs or suggesting features.

## **License**

```
Copyright 2024 Igor Stojanovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
