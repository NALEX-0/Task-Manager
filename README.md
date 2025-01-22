# MediaLab Assistant  

**MediaLab Assistant** is a **JavaFX-based** task management application that allows users to create, update, and manage tasks with categories, priorities, and notifications. This project was developed as part of the **Multimedia Technologies** course during the **7th semester** at the **School of Electrical and Computer Engineering, NTUA**.  

---

## **Features**  

### **Task Management**  
- Create, update, and delete tasks.  
- Assign **categories** and **priorities** to tasks.  
- Filter tasks by **status** (e.g., Completed, Delayed).  
- **Search** tasks by title, description, category, priority, or due date.  

### **JavaFX Graphical Interface**  
- Interactive **tabbed** UI for **Tasks, Categories, and Priorities**.  
- **Search bar** for real-time task filtering.  
- **Action buttons** to add, update, and delete tasks.  
- **Popup alerts** for delayed tasks upon startup.  


### **Command Line Interface (CLI)**  
- Supports the same operations as the GUI using **simple commands**.  
- To start CLI mode, run the `MainCli.java` file

---

## **How to Run**  

### 1️⃣ **Clone the Repository**  
```bash
git clone https://github.com/NALEX-0/Task-Manager
cd Task-Manager
```

### 2️⃣ **Install Dependencies**  
```bash
mvn clean install
```

### 3️⃣ **Run the Application**  
```bash
mvn javafx:run
```

---

## **Technologies Used**  
- **Java** – Core programming language  
- **JavaFX** – GUI framework for desktop applications  
- **Maven** – Dependency and project management  
- **Jackson Library** – JSON-based data persistence  
- [**BootstrapFX**](https://github.com/kordamp/bootstrapfx) – JavaFX styling framework
- **Javadoc** – Code documentation  

---

## **Testing**
- **Test Data Management**: [**#**](https://github.com/NALEX-0/Task-Manager/tree/main/src/test/java/com/example/taskmanager)
  - `AddData.java` – Populates the application with sample data for testing.  
  - `DeleteData.java` – Deletes all stored data!!!
- **Unit Tests**: [**#**](https://github.com/NALEX-0/Task-Manager/tree/main/src/test/java/com/example/taskmanager)
  - Includes test cases for **task**, **categories**, **priorities** and other key operations. 

---

