# Pahana Bookstore Management System

## Overview

Pahana is a full-stack web application designed for managing a bookstore. It supports functionalities such as customer management, book inventory browsing, bill calculation, and bill viewing, with role-based access for Admins and Cashiers. The system features a **React-based frontend** and a **Java Servlet-based backend**, interacting with a **MySQL database**.

---

## Features

* User authentication with role-based access (Admin, Cashier).
* Customer management (add, edit, delete, fetch).
* Book catalog browsing and searching by name.
* Bill calculation and viewing for customers.
* Dark mode support for the UI.
* Receipt printing functionality.

---

## Technologies Used

### Frontend

* **React** (18+): JavaScript library for building user interfaces.
* **Tailwind CSS**: Utility-first CSS framework for styling.
* **React Icons**: For icons (e.g., `FaSearch`).
* **Theme Context**: Custom context for dark/light mode toggling.
* **Fetch API**: For HTTP requests to the backend.

### Backend

* **Java** (JDK 17+ recommended).
* **Jakarta EE (Servlets)**: For handling HTTP requests (`HttpServlet`).
* **JDBC**: For database connectivity (via `DatabaseConnection` class).
* **JSON.org**: For JSON handling (`org.json.JSONObject`).
* **DAO Pattern**: For database operations.
* **Factory Pattern**: For DAO instantiation (`DaoFactory`).
* **Validation**: Custom validators (e.g., `CustomerValidator`).

### Database

* **MySQL 8+**
* Tables:

  * `customers`: Stores customer details (`account_number`, `name`, `address`, `phone`, `units`).
  * `items`: Stores book details (`id`, `name`, `price`, `author`, `image_url`, `category`).
  * `bills`: Stores bill details (`id`, `customer_id`, `bill_amount`, `bill_date`, `status`).
  * `users`: Stores user credentials and roles (`id`, `username`, `password`, `role`).

### Other Tools

* **Tomcat**: Servlet container for deploying the backend.
* **Node.js + npm**: For frontend development and build.
* **Maven**: For managing backend dependencies.
* **Git**: For version control.

---

## Database Schema

```sql
-- Table structure for table `customers`
CREATE TABLE `customers` (
  `account_number` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `address` varchar(200) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `units` int(11) DEFAULT 0,
  PRIMARY KEY (`account_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Table structure for table `items`
CREATE TABLE `items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `author` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Table structure for table `bills`
CREATE TABLE `bills` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) NOT NULL,
  `bill_amount` decimal(10,2) NOT NULL,
  `bill_date` date NOT NULL,
  `status` enum('PENDING','PAID','CANCELLED') DEFAULT 'PENDING',
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `bills_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`account_number`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Table structure for table `users`
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `role` varchar(20) NOT NULL CHECK (`role` IN ('Admin','Cashier')),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
```

---

## Prerequisites

* **Java Development Kit (JDK)**: Version 17+
* **Node.js**: Version 18+
* **MySQL Server**: Version 8+
* **Git**
* **Maven**
* **Tomcat**
* IDE: IntelliJ IDEA / Eclipse (backend), VS Code (frontend)

---

## How to Clone the Repository

```bash
git clone https://github.com/your-username/pahana-bookstore.git
cd pahana-bookstore
```

Expected structure:

```
/frontend   # React application source
/backend    # Java servlet source
/database   # SQL scripts for database setup
```

---

## How to Set Up and Run Locally

### 1. Database Setup

```sql
CREATE DATABASE pahana_db;
```

Run provided SQL to create tables and insert sample data.
Update **DatabaseConnection.java** with your MySQL credentials.

### 2. Backend Setup

```bash
cd backend
mvn clean install
```

Deploy WAR to Tomcat (default port: **8081**).
APIs available at: `http://localhost:8081/pahana-backend/`

### 3. Frontend Setup

```bash
cd frontend
npm install
npm start
```

Frontend available at: `http://localhost:3000`

Update API calls to point to backend URL (default: `http://localhost:8081/pahana-backend/`).

### 4. Running the Application

* Start MySQL server.
* Start backend (Tomcat).
* Start frontend.
* Open: `http://localhost:3000`
* Example credentials:

  * **Admin**: `admin / pass123`
  * **Cashier**: `Cashier / pass123`

### 5. Testing

#### Backend APIs (Postman examples)

* `POST http://localhost:8081/pahana-backend/editCustomer`
* `GET http://localhost:8081/pahana-backend/getAllBooks`
* `POST http://localhost:8081/pahana-backend/calculateBill`
* `GET http://localhost:8081/pahana-backend/viewBills?customerId=<id>`

#### Frontend

* Test features like editing customers, browsing books, calculating bills, and viewing bills.
* Verify image URLs load (e.g., `http://localhost:8081/pahana-backend/Uploads/HarryPotter.jpg`).

---

## Deployment

* **Backend**: Deploy WAR to Tomcat on AWS/Heroku.
* **Frontend**: Build using `npm run build`, host on Netlify/Vercel.
* **Database**: Use managed MySQL (AWS RDS, DigitalOcean).
* Update backend `DatabaseConnection.java` with production DB credentials.

---

## Notes

* Passwords in `users` table are **plain text** (for demo). Use **hashed passwords** (BCrypt) and **JWT authentication** in production.
* Ensure backend serves static files at `/pahana-backend/Uploads/`.
* Prices displayed in **LKR (Sri Lankan Rupee)** in frontend.

---

## Contributing

1. Fork the repo.
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit: `git commit -m 'Add your feature'`
4. Push: `git push origin feature/your-feature`
5. Open a Pull Request.

---

## License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## Contact

For issues or questions, contact: **[your-email@example.com](mailto:your-email@example.com)**
