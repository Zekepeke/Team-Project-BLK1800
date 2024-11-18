# Team-Project-BLK1800

## Project Overview
BLK1800 open-ended project, meaning that outside the minimum 
requirements the team is free to design the software as they
see fit. The assignment is to build a fully 
functioning Social Media Platform.

---

## Getting Started

### Compiling and Running the Project
1. **Clone the Repository**
   ```bash
   git clone https://github.com/Zekepeke/Team-Project-BLK1800
   cd Team-Project-BLK1800
2. **Clone the Repository**
   ```bash
   java -cp bin App

## Submission Details

| Team Member | Submission Details              |
|-------------|---------------------------------|
| Zeke Linares  | Submitted Vocareum phase 1   |
| DY Park   | N/A         |
| Kai Liu   | N/A                             |
| Advait Duggi   | N/A                             |

## Class Descriptions
---

### 1. User Class
* **Functionality**:
    * Manages user information, including name, bio, and password.
    * Handles friend requests, allowing users to send, accept, or decline requests.
   
* **Testing**:
    * Verify friend request functionality, ensuring users can send, accept, 
    * and decline requests correctly.
    * Validate that user details (name, bio, etc.) can be updated 
    * successfully.
* **Relationships**:
    * Interacts with the `Messages` class by allowing only friends or 
      approved users to send messages.
    * Uses the `Userbased` interface to ensure consistent handling of 
  user data and interactions.

---

### 2. Messages Class
* **Functionality**:
    * Represents messages sent between users, including sender, receiver, and content.
    * Supports message deletion, allowing users to remove unwanted messages.
* **Testing**:
    * Verify message sending and receiving to ensure that users can exchange messages accurately.
    * Check file storage to confirm messages are saved and retrieved 
  accurately based on the specified file path.
* **Relationships**:
    * Implements the `Messagable` interface, ensuring standardized 
  message handling.
    * Interacts with the `User` class by restricting messages to 
  friends or all users based on user settings.
---

### 3. App Class
* **Functionality**:
    * The main part of the program
    * Should be the gui and other fun stuff
* **Testing**:
    * N/A
    * N/A
* **Relationships**:
  * Connects messages and users as it is needed for the whole app to work
---

# Client-Server Communication System

This project implements a basic client-server communication system using sockets in Java. The system allows for user authentication, searching for other users, retrieving profile information, and managing friend lists.

## Features

1. **Client-Side Operations**
   - User authentication (login and signup)
   - Search for users
   - Retrieve profile information
   - View the friend list

2. **Socket Communication**
   - Handles low-level socket communication between client and server.
   - Supports data streaming and handshake operations.
3. **Server Operations**
   - Looks into reading and writing to the datbase
   - Manages all the backend functionality
## Project Structure

### Classes

#### 1. `ClientSide.java`
- **Location**: `src/Client/ClientSide.java`
- Handles the client-side logic.
- Validates user credentials and interacts with the server.
- Provides methods:
  - `search()`: Searches for users.
  - `profile()`: Fetches profile details.
  - `listOfFriends()`: Retrieves the user's friend list.
  - `validUserAndPassword()`: Validates usernames and passwords based on specific rules.

#### 2. `Server.java`
- **Location**: `src/Server/Server.java`
- Implements the server-side functionality.
- Manages client connections and processes incoming requests.

#### 3. `SocketIO.java`
- **Location**: `src/SocketIO.java`
- Manages socket-based input/output communication.
- Provides utilities for reading and writing data streams.
- Key methods:
  - `write()`: Sends data to the server.
  - `read()`: Reads data from the server.
  - `sendHandShake()`: Initiates a handshake operation.
  - `readCondition()`: Reads server response conditions.

## Installation

1. Clone this repository.
   ```bash
   git clone https://github.com/username/project.git

To run the files, go to src/App.java and run the main fuction there (this opens up the client side). Then run the src/Server/server.java's main function as well. Running both of these will boot up the app and begin the process for the user.

Test cases are also included in the tests directory
