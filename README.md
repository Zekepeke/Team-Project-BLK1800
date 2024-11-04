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
---

