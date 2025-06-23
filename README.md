# electronic-store
#  Electronic Store – Backend (Spring Boot)

A secure and scalable backend for an electronic e-commerce platform, built using Java, Spring Boot, and MySQL. This project includes complete user management, product handling, image upload, pagination, filtering, and role-based access control using JWT Authentication.

---

## 🚀 Features

- ✅ User Registration and Login
- ✅ JWT Authentication and Refresh Token Flow
- ✅ Role-based Access (Admin/User)
- ✅ Product Management (Add, Update, Delete, Get)
- ✅ Image Upload for Products (File System/Cloud)
- ✅ Product Filtering by Category and Keyword
- ✅ Pagination for Product Listings
- ✅ Global Exception Handling
- ✅ MySQL Database Integration
- ✅ Tested with Postman (secured endpoints)

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Spring Security (JWT)
- MySQL
- JPA & Hibernate
- Postman (API Testing)
- Maven

---

##  Project Structure
src
└── main
├── java
│ └── com.electronicstore
│ ├── controller
│ ├── service
│ ├── repository
│ ├── model
│ ├── config
│ └── payload
└── resources
└── application.properties

---

## Security Highlights

- JWT token generation, parsing, and authentication
- Role-based access using Spring Security annotations
- Refresh token implementation for persistent sessions
- Secure endpoints with token validation filters

---

##  API Testing

Tested thoroughly using **Postman**:
- Authenticated endpoints require Bearer token
- Tested login, signup, product CRUD, role-based access
- Filtering and pagination tested with query parameters
- Image upload tested via `multipart/form-data`

---

##  Database Schema (MySQL)

- Tables:
  - `users`
  - `products`
  - `roles`
  - `categories`
  - `orders`
- Entity mappings handled via **JPA annotations**

---

##  How to Run Locally


# Clone the repository
git clone https://github.com/komal102004/electronic-store.git

# Navigate to project directory
cd electronic-store

# Open in your preferred IDE (e.g., IntelliJ IDEA)

# Set up database credentials in application.properties:
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.url=jdbc:mysql://localhost:3306/electronic_store_db

# Run the application
mvn spring-boot:run
## Future Scope
Swagger
Cloud Deployment (Render/AWS)
Role Management Dashboard (Admin Panel)
Frontend Integration (React/Angular)
Advanced Search with Sorting

## Developed By
Komal Agrahari
Final Year B.Tech (IT)
Babu Banarasi Das Engineering College, Lucknow
GitHub Repo:https://github.com/komal102004/electronic-store
Email:komalagrahari1020@gmail.com
