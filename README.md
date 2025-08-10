# CircleHub

**CircleHub** is a community-driven social networking platform built with **Java (Spring Boot)**, designed for users to connect, share posts, and interact with each other in a safe and engaging environment.  

It includes **user authentication**, **post creation**, **token-based features**, and **real-time community engagement tools**.  

---

##  Features

###  User Management
- Secure **User Registration & Login** (with password hashing)
- Session-based authentication using **JWT tokens**
- Editable **profile** (display name, email, profile picture)
- Token balance tracking for premium interactions

###  Posts & Community Interaction
- Create, edit, and delete posts
- View recent posts on a **community dashboard**
- View individual post details
- Token-based premium posting

###  Security & Token System
- Hashed password storage for security
- Token-based user authentication
- Token balance management for premium features

###  Admin Tools (Optional)
- View all registered users
- Moderate posts
- Manage token rewards

---
##  Tech Stack

| Layer               | Technology Used |
|---------------------|-----------------|
| **Backend**         | Java 17, Spring Boot |
| **Database**        | MySQL (JDBC) |
| **Authentication**  | JWT (JSON Web Token) |
| **ORM Layer**       | DAO (Data Access Objects) |
| **Frontend (UI)**   | JSP / Thymeleaf / HTML-CSS |
| **Build Tool**      | Maven |

---

##  Project Structure
CircleHub/
│
├── src/main/java/com/circlehub/
│ ├── controller/ # Controllers for handling requests
│ ├── dao/ # DAO classes for database interaction
│ ├── model/ # Entity classes (User, Post, Token)
│ ├── service/ # Business logic (AuthService, TokenService, etc.)
│ ├── util/ # Utility classes (DBConnection, Password hashing, etc.)
│
├── src/main/resources/
│ ├── application.properties 
│ ├── fxml/ 
│ ├── icons/ 
│ ├── logo/ 
│ ├── placeholders/ 
│ ├── tokens/ 
│
└── pom.xml 

