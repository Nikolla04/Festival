# **Smart Warehouse Management System**
## **Overview**
A lightweight, concurrent Spring Boot and Java-based Warehouse Management System (WMS) designed to demonstrate advanced Software Architectures, Database Design, and Multi-threading capabilities in Java. 

The application provides a REST API integrated with a MySQL database and features an interactive HTML/JS Frontend Dashboard that visualizes warehouse operations and real-time inventory monitoring.

##   Features & Architectural Patterns

*   **RESTful Web Services:** Exposes clean API endpoints for managing product categories and inventory items.
*   **Object-Relational Mapping (ORM):** Utilizes Spring Data JPA (Hibernate) for robust database interaction.
*   **Asynchronous Multi-threading (Java Threads):** Implements a dedicated background thread runner. When item stock falls below the defined critical threshold, a non-blocking background thread (`Thread-1`) is dispatched to handle automated supplier reordering simulation without freezing the main application runtime.
*   **Single-Page Application (SPA) Frontend:** Responsive HTML5/CSS3/JavaScript dashboard that interacts with the Spring Boot backend via the Fetch API, complete with a simulated "Thread Monitor Console" for real-time log tracking.

## Tech Stack

*   **Backend:** Java, Spring Boot (Spring Web, Spring Data JPA)
*   **Database:** MySQL (with HikariCP connection pooling)
*   **Frontend:** HTML5, CSS3, JavaScript 
*   **Build Tool:** Maven
