# Java Chat Application (Client-Server)

This is a real-time chat application built using **Java Swing** and **Socket Programming**. It allows communication between a server and multiple clients through a simple graphical interface.

---

## 🚀 Features

- Real-time messaging
- GUI using Java Swing
- Client-Server architecture
- Sound notification on send/receive
- Left-right message alignment
- Multithreading support
- Single JAR launcher (Server or Client)

---

## 🛠️ Technologies Used

- Java
- Swing
- Socket Programming
- Multithreading

---

## 📂 Project Structure

ChatApplication/
│
├── src/
│   ├── Launcher.java
│   │
│   ├── server/
│   │   ├── Server.java
│   │   └── MainServer.java
│   │
│   └── client/
│       ├── Client.java
│       └── MainClient.java
│
├── sounds/
│   ├── send.wav
│   └── receive.wav
│
├── ChatApp.jar
└── README.md


---

## ▶️ How to Run

### Using JAR
```bash
java -jar ChatApp.jar


Choose:

1. Start Server then again run ChatApp.jar and 
2. Select Start Client
