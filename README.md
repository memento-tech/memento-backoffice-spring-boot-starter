
### Table of Content
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
  - [3.1 Annotations Overview](#annotations-overview)
  - [3.2 Authentication](#authentication)
  - [3.3 Translations Management Tool](#translations-management-tool)
  - [3.4 Media Management Tool](#media-management-tool)
  - [3.5 Functional Widgets](#functional-widgets)
  - [3.6 UI Widgets](#ui-widgets)
  - [3.7 Cron Job Management Tool](#cron-job-management-tool)
  - [3.8 Live Console](#live-console)
- [4. Contribution Guide](#contribution-guide)
- [5. License](#license)


## **Introduction**
 **Overview of the project**
 
The Backoffice Spring Boot Starter is designed to simplify backend management, providing a developer-friendly interface that eliminates the need for direct database interaction through SQL queries or database management tools. It also enables non-developers (such as business users and administrators) to monitor and manage the application effectively.

By streamlining workflows, the Backoffice Starter enhances productivity, allowing teams to implement changes, track database modifications, and resolve issues more efficiently.

### **Key Objectives**
1.  **Simplify Backend Workflows**
    -   Perform essential backend tasks without direct database access.
    -   Easily track and manage changes made by the main application.
2.  **Developer and QA Efficiency**
    -   Provide centralized tools for developers and QA engineers to speed up their workflows.
    -   Reduce reliance on external database management tools.
3.  **Future Growth**
    -   Expand functionality to include features for business users, enabling them to analyze data and collaborate with development teams.
    -   Decouple from Hibernate and implement an abstract JPA solution to support multiple JPA implementations like Spring Data JPA, MyBatis, EclipseLink, and others.
### **Current Features**
-   **Plug-and-Play Integration**
    -   Get started quickly with minimal setup and configuration.
-   **Translation Management**
    -   Easily manage and update translations across the application.
-   **Media Management**
    -   Upload, organize, and manage media files (e.g., images, documents).
-   **Cron Job Support** (coming soon)
	-  Create, schedule, and monitor both simple and complex cron jobs.

The **Backoffice Spring Boot Starter** bridges the gap between development, testing, and business operations, providing a powerful and flexible solution for managing backend processes.  

### **Intended Audience**  
The Backoffice Spring Boot Starter is designed for **small businesses**, **startups**, and **mid-sized companies** looking to accelerate development and streamline backend operations.  

- **For Startups**:  
  - Ideal for new projects that need a quick and efficient setup.  
  - Requires only **Spring Boot** and **Hibernate**—the starter handles the rest.  

- **For Existing Projects**:  
  - Easily adaptable for mid-sized or established applications.  
  - Some adjustments may be needed for seamless integration, which will be covered in later sections.  

Whether you're launching a new project or improving an existing one, the Backoffice Spring Boot Starter offers a **flexible and developer-friendly** solution to enhance backend workflows.  

### **Prerequisites**  
Before getting started, ensure you have the following knowledge and tools.  

#### **Required Knowledge**  
- **Java** – A strong understanding of Java is essential for backend development.  
- **Spring Boot** – While prior experience with Spring Boot is required, it's a core technology for most backend applications.  
- **Hibernate** – Since the starter integrates with Hibernate, familiarity with it is necessary.  
- **ReactJS** – The frontend is built with React, so basic knowledge is required for customization and feature extensions.  

#### **Tools Needed**  
- **JDK** – A Java Development Kit compatible with your project's Spring Boot version.  
- **Maven or Gradle** – For dependency management and project builds.  
- **Node.js** – Required for React frontend development and build processes.  

With these prerequisites in place, you're ready to integrate and start using the Backoffice Spring Boot Starter. 🚀  

## **Getting Started**

### **Installation**
The Backoffice Spring Boot Starter is designed for easy installation and setup in just a few steps.  

**Step 1: Adding dependency to memento-backoffice-spring-boot-starter**

Add the following dependency to your project based on your build system:  

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

To enable the Backoffice module, add the following configuration properties to your application.yaml or application.properties file.

 - **memento.tech.backoffice.enabled**
   - Enables Backoffice and loads all required beans into the Spring context. Set to true to activate Backoffice with basic functionalities.
 - **memento.tech.backoffice.media.enabled**
   - Enables media management within Backoffice.
 - **memento.tech.backoffice.media.internal.storage.enabled**
   - Enables the default implementation for media storage. If no custom media management is provided by the user, this property must be set to true.
 - **memento.tech.backoffice.media.file.import.upload.directory**
   - Defines the storage location for uploaded media files. Required when internal storage is enabled.
 - **memento.tech.backoffice.media.mapping**
   - Defines the URL mapping for accessing uploaded media. Example format: http://application.domain/{mediaMappingProperty}/{mediaId}.
 - **memento.tech.backoffice.translation.enabled**
   - Enables the translation management tool.
 - **memento.tech.backoffice.translation.useDefaultIfBlank**
   - If set to true, and a translation is missing for a specific language, the default language translation will be used. If no default translation is found, the translation key itself will be returned. If set to false, only the requested translation will be checked.
 - **memento.tech.backoffice.translation.default.language**
   - Sets the default language for translations (must follow ISO language standards).
 - **memento.tech.backoffice.auth.token.secret**
   - Defines the authentication token secret. A default value is provided, but it is recommended to set a custom one.
 - **memento.tech.backoffice.auth.cookie.name**
   - Sets the HTTP-only cookie name. The default is "backofficeAccessCookie". More details can be found in the Backoffice Auth Configuration section.
 - **memento.tech.backoffice.auth.cookie.expiry**
   - Defines the cookie expiration time in milliseconds. The default value is 0 milliseconds.
 - **memento.tech.backoffice.development.mode**
   - Enables development mode. If set to true (default), a test user is created with username: "test" and password: "password". It is recommended to disable this (false) in production environments.

### **Quick Start Guide**
https://github.com/memento-tech/memento-backoffice-spring-boot-starter/tree/main

The source code of the **Backoffice Spring Boot Starter** is available at the provided link. Along with the source code, there is a **Quick Start Project** called `"demo-project"`. Developers can clone the `demo-project` along with the Backoffice Starter source code and run it as a standard Spring Boot application.  

- **Pull source code using GitHub**
   - ``git clone https://github.com/memento-tech/memento-backoffice-spring-boot-starter.git``
- **IDE Import**
   - Using your preferred IDE import project using maven.
- **Maven Build**
   - Since this way backoffice starter is not prebuilt, run maven command: ``mvn clean install``
- **Run**
   - Finally, run demo-application through your preferred IDE. Backoffice UI will be available on address http://localhost:8080/backoffice/login

The demo-project provides a fully functional example, allowing developers to explore the features and configurations of the Backoffice Spring Boot Starter before integrating it into their own applications.

### **UI Guide**
This section provides an overview of the Backoffice UI, explaining its pages and key components. The UI is designed with **UX in mind**, ensuring that most elements are intuitive and self-explanatory.

### **Login Page** 

The **Login Page** is a minimalistic interface containing only the login form. It serves as the entry point for accessing the Backoffice.

For more details on the authentication process and login mechanics, refer to the [Authentication section](#authentication).

<p align="center">
	<img width="1469" alt="backoffice-starter-login-page" src="https://github.com/user-attachments/assets/b3e0738b-3635-496f-af47-983300aa1798" />
</p>

### **Console Page** 

The **Console Page** is the central and currently the only page of the Backoffice Starter. It is structured into two main sections: 
 - **Console Navigation** – Provides access to different management tools and features.  
 - **Console Data Management** – Displays and allows interaction with backend data.  

<p align="center">
	<img width="1469" alt="backoffice-console-start" src="https://github.com/user-attachments/assets/0571da46-c80d-4954-904e-54a149562466" />
</p>

#### **Console Navigation**

When first launching the Backoffice Starter, the **Console Navigation** panel will be empty, displaying a message:  
*"Please refresh metadata"* (see **Image 1, left side**).  

Clicking the **"Refresh Metadata"** button will trigger the software to scan all available entities and gather the necessary information for data management. After a few seconds, the **Console Navigation** will populate with entities and groups of entities (**Image 1, right side**).  

#### **Usage Guidelines:**  
- The **"Refresh Metadata"** button should only be used when changes are made to entities or their structure.  
- Regular page refreshes should be done using **F5** or the browser’s built-in refresh button.  

#### **Interacting with Console Navigation:**  
- **Entity Buttons (No Chevron):** Clicking an entity button will update the **Console Data Management** section to reflect data from the selected entity.  
- **Group Buttons (With Chevron):** Clicking a group button will expand the group and reveal the list of entities under that category.  

<p align="center">
	<img width="248" alt="navigation-before-refresh" src="https://github.com/user-attachments/assets/8f7dc49c-6ec2-4428-8e01-4fbd4ff4d3b3" />
	<img width="248" alt="navigation-after-refresh" src="https://github.com/user-attachments/assets/4f8300bc-9f76-4815-8be6-29de729fe315" />
</p>

### **Console Data Management**  

The **Console Data Management** section is structured into two main parts:  
- **Toolbar (Top of the Page)**  
- **Data Management Panel**  

#### **Initial State**  
When first launching the Backoffice Starter, the **Data Management** panel will be empty, and the **Toolbar** will contain only a **"Logout"** button.  

Once an entity is selected from the **Console Navigation**, the **Data Management** section will populate with tools and data relevant to that entity.  

<p align="center">
	<img width="1220" alt="data-management-empty" src="https://github.com/user-attachments/assets/08ccf27d-f4c9-435d-b7cd-4ba001dacc1f" />
</p>

### **Entity Selection & Data Management Panel**  

Once metadata is collected and all entities appear in the navigation menu, users can click on any entity to trigger the **Data Management** section to reload and fetch the necessary information for the selected entity.  

#### **Toolbar Functionality**  
The **Toolbar** will adapt based on the selected entity's configuration:  
- It may contain a **"Create"** button if entity creation is enabled.  
- Other widgets implemented by the user may also appear here.  

#### **Displaying Records**  
Once an entity is selected, its records (if available) will be displayed in the **Data Management** panel.  

##### **Lower Toolbar**  
- The **lower toolbar** provides options for adjusting how many records are displayed per page.  
- Users can select between **10, 20, 50, or "All"** records per page.  
- The total number of available records is displayed at the bottom of the section.  

##### **Table Layout**  
Records are presented in a **table** within the **Data Management** section.  
- The table includes **displayable columns** configured via **Backoffice annotations** (more details [here](#backoffice-annotations)).  
- Two additional columns are always present:  
  - **Delete Column** (Trash Icon)  
  - **No Column** (Row Number, ordered from 1 to n)  

#### **Managing Records**  
- **Deleting Records**  
  - Click the **trash icon** in the **Delete Column** to remove a record.  
  - ⚠️ **This action is irreversible.**  

- **Updating Records**  
  - Click on any row in the table to open the **Entity Popup**.  
  - The **Entity Popup** displays all details of the record, including both **editable and non-editable fields**.  


<p align="center">
	<img width="1470" alt="data-management-populated" src="https://github.com/user-attachments/assets/fee2bbb9-abfb-420a-b48e-0305845d1d5b" />
</p>

**Entity Creation Popup**

### **Entity Creation Popup**  

The **Entity Creation Popup** is used for adding new entities.  

#### **General Entity Creation**  
- All entities (except **Media** and **Translation**) use the **default** entity creation popup.  
- If an entity has **creation enabled**, it can be added through this popup.  
- The popup **does not always include all entity fields**, as users can customize which fields appear in the creation form to meet specific requirements.  

<p align="center">
	<img width="1063" alt="EntityCreationPopup" src="https://github.com/user-attachments/assets/53007ec9-bd69-4099-9daf-a69fd1d4c620" />
</p>

#### **Media Creation Popup**  
- The **Media Creation Popup** is specifically designed for adding media files.  
- It contains **two fields**:  
  1. **Description** – A user-friendly text to describe the media.  
  2. **Media File Import** – A field to upload the media file.  
- The **media name** is automatically generated using the formula:  
  **`randomUUID-originalFileName`**  
- The generated name is primarily for internal reference, as **all media operations** are handled through **media objects** rather than filenames.


<p align="center">
	<img width="486" alt="MediaCreationPopup" src="https://github.com/user-attachments/assets/83110b88-0989-4814-bc03-d21181afb537" />
</p>

#### **Translation Creation Popup**  
- The **Translation Creation Popup** is designed for managing translations.  
- It includes **at least two fields**:  
  1. **Translation Code**  
  2. **Translation Text**  
- The number of fields varies **based on the number of languages** present.  
- Although the **Translation Code** format is **not restricted**, it is recommended to follow this structure:  
  **`project.feature.subfeature.distinguishing.term`**  

<p align="center">
	<img width="448" alt="TranslationCreationPopup" src="https://github.com/user-attachments/assets/cd6bd603-158f-48d7-9cbb-7b44c3258740" />
</p>

## **Technical Documentation**

## Backoffice Starter Annotations

The backoffice starter includes a set of annotations designed to control the behavior, visibility, and properties of entities and their fields within the backoffice interface. These annotations are located in the package `com.memento.tech.backoffice.annotations`.

## **Annotations Overview**

### 1. `@BackofficeTitle` Annotation

**Target:** `TYPE`, `FIELD`  
**Retention Policy:** `RUNTIME`

The `@BackofficeTitle` annotation defines titles for entities and their fields in the backoffice UI.

- **Type Level:** When applied to a class, it sets the label for the entity in navigation elements (e.g., sidebar buttons, menu items).
- **Field Level:** When applied to a field, it sets the label for the field in UI elements (e.g., input labels, table headers, data management section).

#### Usage:

```java
@BackofficeTitle("Username")
public class User {
    @BackofficeTitle("Username Field")
    private String username;
}
```

### 2. `@BackofficeOrderPriority` Annotation

**Target:** `TYPE`, `FIELD`  
**Retention Policy:** `RUNTIME`

The `@BackofficeOrderPriority` annotation determines the ordering of entities in navigation and fields in tables and popups. Higher values indicate higher priority.

- **Default Value:** `0`

#### Usage:

```java
@BackofficeOrderPriority(100)
public class User {
    @BackofficeOrderPriority(50)
    private String username;
}
```

### 3. `@BackofficeGroup` Annotation

**Target:** `TYPE`  
**Retention Policy:** `RUNTIME`

The `@BackofficeGroup` annotation groups related entities in the navigation menu.

- **`title` (Required):** The title of the group.
- **`order` (Optional, Default: `0`):** Defines the order of the group within the navigation.

#### Usage:

```java
@BackofficeGroup(title = "ExampleGroup", order = 100)
public class ExampleEntity {
    // Entity fields here
}
```

### 4. `@BackofficePasswordFlag` Annotation

**Target:** `FIELD`  
**Retention Policy:** `RUNTIME`

The `@BackofficePasswordFlag` annotation is a marker used to designate password fields in entities. This ensures they are rendered as password input fields (hidden characters) on the frontend.

#### Usage:

```java
@BackofficePasswordFlag
private String password;
```

### 5. `@BackofficeFieldForShowInList` Annotation

**Target:** `TYPE`  
**Retention Policy:** `RUNTIME`

The `@BackofficeFieldForShowInList` annotation defines which field should be displayed when showing a list of related entities.

- **`value` (Required):** The field to be displayed in the list (e.g., `title` for a song title).

#### Usage:

```java
@BackofficeFieldForShowInList("title")
public class Artist {
    private List<Song> songs;
}
```

### 6. `@BackofficeDisableCreation` Annotation

**Target:** `TYPE`  
**Retention Policy:** `RUNTIME`

The `@BackofficeDisableCreation` annotation is a marker annotation that prevents the creation of an entity in the backoffice. When this annotation is applied to a class, the "Create" button will not appear in the toolbar.

#### Usage:

```java
@BackofficeDisableCreation
public class Artist {
    // Entity fields here
}
```

### 7. `@BackofficeExclude` Annotation

**Target:** `TYPE`, `FIELD`  
**Retention Policy:** `RUNTIME`

The `@BackofficeExclude` annotation is a marker annotation used to exclude entities or fields from the backoffice UI. It can be applied at the class level (to hide the entire entity) or field level (to hide individual fields in tables or popups).

#### Usage:

```java
@BackofficeExclude
private String username;

@BackofficeExclude
public class Artist {
    // Entity fields here
}
```

### 8. `@BackofficeCreationFieldExclude` Annotation

**Target:** `FIELD`  
**Retention Policy:** `RUNTIME`

The `@BackofficeCreationFieldExclude` annotation is similar to `@BackofficeExclude`, but it only excludes fields from the entity creation popup.

#### Usage:

```java
@BackofficeCreationFieldExclude
private String username;
```

### 9. `@BackofficeForbidUpdate` Annotation

**Target:** `FIELD`  
**Retention Policy:** `RUNTIME`

The `@BackofficeForbidUpdate` annotation is a marker annotation that prevents fields from being updated via the backoffice UI. It is useful for fields that can be updated via other parts of the system but should be protected in the backoffice.

#### Usage:

```java
@BackofficeForbidUpdate
private String username;
```

---

## Summary of Annotations

| Annotation                         | Target(s)        | Purpose                                                    |
|-------------------------------------|------------------|------------------------------------------------------------|
| `@BackofficeTitle`                  | `TYPE`, `FIELD`  | Sets title for entities (in navigation) and fields (in UI) |
| `@BackofficeOrderPriority`          | `TYPE`, `FIELD`  | Defines the order/priority of entities and fields          |
| `@BackofficeGroup`                  | `TYPE`           | Groups entities together in navigation                     |
| `@BackofficePasswordFlag`           | `FIELD`          | Marks password fields for password input behavior          |
| `@BackofficeFieldForShowInList`     | `TYPE`           | Defines field to show in list for related entities         |
| `@BackofficeDisableCreation`        | `TYPE`           | Disables creation of the entity in the backoffice UI       |
| `@BackofficeExclude`                | `TYPE`, `FIELD`  | Excludes entities or fields from the backoffice interface  |
| `@BackofficeCreationFieldExclude`   | `FIELD`          | Excludes fields from the entity creation popup             |
| `@BackofficeForbidUpdate`           | `FIELD`          | Prevents field from being updated in the backoffice UI     |

This annotation documentation should help developers implement their own entities and control the behavior of those entities within the backoffice system effectively.

## **Authentication**

### **Overview**

The authentication system is based on HTTP-only cookies. A JWT token is placed inside an HTTP-only cookie and sent as a response. Each request to the backend is validated against the JWT token, and the system checks for an `ADMIN` role.

### **Configuration Properties**

| Property                                     | Default Value      | Description                                        |
| -------------------------------------------- | ------------------ | -------------------------------------------------- |
| `memento.tech.backoffice.auth.token.secret`  | *Predefined*       | Configurable JWT token secret.                     |
| `memento.tech.backoffice.auth.cookie.expiry` | `0` (milliseconds) | Configurable cookie expiry time.                   |
| `memento.tech.backoffice.auth.cookie.name`   | *Predefined*       | Configurable cookie name.                          |
| `memento.tech.backoffice.development.mode`   | `true`             | Enables test user credentials in development mode. |

### **Security Measures**

- **BCryptPasswordEncoder** is used for password encryption.
- The session creation policy is **STATELESS**.
- Unauthorized users attempting to access the back office are redirected to `/backoffice/login`.
- When `memento.tech.backoffice.development.mode` is `true`, a test user with `username: test` and `password: password` is created. This should be disabled in production.

## **Translations Management Tool**

### **Overview**

The Translation Management Tool provides a mechanism for handling multi-language support. If a translation is unavailable for a given translation code, the system returns the translation code itself, helping developers identify missing translations.

### **Configuration Properties**

| Property                                     | Default Value      | Description                                        |
| -------------------------------------------- | ------------------ | -------------------------------------------------- |
| `memento.tech.backoffice.translation.defaultLocale`  | `en`       | Default locale for translations.                     |
| `memento.tech.backoffice.translation.supportedLocales` | `en,fr,de` | List of supported locales.                   |

### **Usage**

Developers should use the `Translation` class inside entity definitions like this:

```java
private Translation description;
```

It is recommended to add `cascade = CascadeType.ALL, orphanRemoval = true` to entity associations for proper translation management.

### **Behavior**

- Developers query a translation using a translation code.
- The tool returns a **pure string** for display on the frontend.
- If no translation is found, the **translation code is returned**, allowing developers to track missing translations.

## **Media Management Tool**

### **Overview**

The Media Management Tool provides default media storage functionality, storing media files in a specified directory. Developers can implement custom storage solutions by implementing the `FileStorageService` interface.

### **Configuration Properties**

| Property                                     | Default Value      | Description                                        |
| -------------------------------------------- | ------------------ | -------------------------------------------------- |
| `memento.tech.backoffice.media.storagePath`  | `/uploads/`        | Directory where media files are stored.           |
| `memento.tech.backoffice.media.maxSize`     | `10MB`             | Maximum allowed file size for uploads.            |

### **Custom Implementation**

Developers can override the default storage by implementing the following interface:

```java
package com.memento.tech.backoffice.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileStorageService {
    byte[] getFile(final String fileName) throws IOException;
    String saveFile(final MultipartFile multipartFile) throws IOException;
    void removeFile(final String fileName) throws IOException;
}
```

Once implemented, the media management system will function seamlessly with the custom storage mechanism.

## **Functional Widgets**

### **Overview**

Functional Widgets provide interactive buttons in the UI to perform specific functions. They can be configured at:

- **Entity Level:** Button appears in the toolbar and applies to all records of an entity.
- **Record Level:** Button appears inside the entity popup and applies to a single record.

### **Usage**

To use a widget, developers should annotate methods with:

```java
@RecordFunctionButton(widgetId = "companyApproveWidget", buttonLabel = "Approve", functionBeanClass = PartnerApproveFunctionButtonHandler.class)
```

Additionally, a record should be added to the database, for example through an `import.sql` file.

### **Implementation**

Developers need to implement the `RecordFunctionButtonHandler` interface:

```java
package com.memento.tech.backoffice.widget;

public interface RecordFunctionButtonHandler<T> extends WidgetHandler<T> {
    void handle(T entity);
}
```

Additionally, the `WidgetHandler` interface determines whether the widget should be displayed:

```java
package com.memento.tech.backoffice.widget;

public interface WidgetHandler<T> {
    boolean show(T entity);
}
```

### **Widget Database Structure**

Widgets are stored in the database with the following structure:

```java
@Entity
@Table(indexes = {@Index(columnList = "id", unique = true)})
public class Widget extends BaseEntity implements BackofficeConfigurationMarker {
    @Column(nullable = false, unique = true)
    private String widgetId;

    private boolean entityLevel;
    private boolean recordLevel;
    private String label;

    @Column(nullable = false, unique = true)
    private Class<? extends WidgetHandler> widgetHandlerClass;

    @Column(nullable = false, unique = true)
    private String handlerMapping;
}
```

### **Handler Mapping**

A static handler mapping is used to trigger widget actions:

```java
String WIDGET_API_PREFIX = "/api/backoffice/widget";
String RECORD_FUNCTION_API_SUFFIX = "/function/record";
String ENTITY_FUNCTION_API_SUFFIX = "/function/entity";
```

*Note: Entity-Level Widgets are planned but not yet implemented. They will allow actions to be performed on multiple records at once.*

## **UI Widgets**

UI Widgets are not yet implemented.

## **Cron Job Management Tool**

Cron Job Management is not yet implemented.

## **Live Console**

Live Console is not yet implemented.

## **Conclusion**

This documentation provides an overview of authentication, translation management, media management, functional widgets, and entity-level widgets. Further enhancements will be added as new features are implemented.

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
