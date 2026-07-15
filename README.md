# Festival Management System 

This is a desktop **Java Swing** application integrated with a **MySQL database** to manage music and cultural festivals and their ticket sales. Built using the **Data Access Object (DAO) pattern**, the system allows administrators to register events, manage tickets, search, update records, and safely delete data

### Features & Architecture

*   **Database & Transactions:** Uses a relational MySQL schema. To maintain data integrity, the system implements manual **JDBC transaction management (`commit`/`rollback`)**. When deleting a festival, it automatically sweeps and deletes all associated tickets first; if any error occurs, the entire operation rolls back.
*   **User Interface (GUI):** Built using Java Swing (`BorderLayout`, `GridLayout`). The input screen features a dynamic `JList` that lets you add and preview multiple ticket types on the fly before saving the event.
*   **Complete CRUD:** Supports adding, viewing, searching, modifying, and deleting festival records.

### Technology Stack


*   **Java SE** — Core programming language for application logic and window frame components.
*   **Java Swing & AWT** — Toolkit used to design the user interfaces, prompt dialogs, and dynamic lists.
*   **MySQL & JDBC** — Relational database management system coupled with MySQL Connector/J driver to manage connections and execute prepared SQL statements.
