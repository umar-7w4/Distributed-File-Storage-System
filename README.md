### Introduction

#### Project Overview

**Distributed File System**

The Distributed File System (DFS) is designed to manage and store data across multiple networked nodes, ensuring high availability, reliability, and scalability. This project simulates a simplified version of a DFS, commonly used in large-scale computing environments to distribute data storage and processing across several servers, enabling efficient data management and access.

**Purpose:**

The primary purpose of this project is to demonstrate the implementation of a distributed file system that can handle client requests for reading and writing data. The system is designed to distribute data blocks across multiple DataNodes and coordinate these operations through a central NameNode. This setup is inspired by real-world distributed systems like Hadoop's HDFS (Hadoop Distributed File System).

**Scope:**

The scope of this project includes:
1. **NameNode:** A central coordinator that manages metadata about file locations and coordinates communication with DataNodes.
2. **DataNodes:** Storage nodes that handle the actual data storage and retrieval, managed by the NameNode.
3. **Client:** An interface for users to interact with the system, allowing them to perform read and write operations on files stored in the DFS.
4. **Networking and Concurrency:** Implementation of network communication between nodes and clients, and handling concurrent client requests.

#### Goals and Objectives

**Main Goals:**

1. **Develop a Functional Distributed File System:** Implement a DFS that can distribute file storage across multiple DataNodes and coordinate these operations through a NameNode.
2. **Ensure Data Availability and Reliability:** Distribute data blocks across multiple nodes to ensure data redundancy and reliability.
3. **Handle Concurrent Client Requests:** Implement mechanisms to manage multiple client requests simultaneously, ensuring thread safety and data consistency.
4. **Implement Robust Error Handling:** Ensure the system can gracefully handle errors and maintain data integrity.

**Specific Objectives:**

1. **NameNode Implementation:**
   - Manage metadata about file locations and data blocks.
   - Coordinate read and write operations between clients and DataNodes.
   - Handle client connections and process requests.

2. **DataNode Implementation:**
   - Store and manage data blocks.
   - Handle read and write operations as directed by the NameNode.
   - Ensure data consistency and integrity.

3. **Client Interface:**
   - Provide a user-friendly interface for interacting with the DFS.
   - Allow users to perform read, write, and shutdown operations.
   - Handle network communication with the NameNode.

4. **Networking and Concurrency:**
   - Implement socket programming for communication between nodes and clients.
   - Ensure thread safety and manage concurrent client requests using multithreading.
   - Implement synchronization mechanisms to prevent race conditions and data inconsistencies.

5. **Robust Error Handling and Graceful Shutdown:**
   - Implement error handling to manage network failures, data inconsistencies, and other potential issues.
   - Implement a graceful shutdown mechanism to safely terminate the system, ensuring all operations are completed and resources are released.


### Getting Started

#### Prerequisites

**Software Requirements:**
1. **Java Development Kit (JDK):**
   - Version: JDK 8 or higher
   - Download: [Oracle JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or [OpenJDK](https://openjdk.java.net/)
   
2. **Integrated Development Environment (IDE):**
   - Recommended: IntelliJ IDEA, Eclipse, or Visual Studio Code
   
3. **Build Tools:**
   - Apache Maven (optional, if you prefer using Maven for dependency management and build automation)
   - Download: [Apache Maven](https://maven.apache.org/download.cgi)
   
4. **Version Control System (optional):**
   - Git
   - Download: [Git](https://git-scm.com/downloads)
   
**Hardware Requirements:**
- **Processor:** Intel i5 or equivalent
- **RAM:** 4 GB minimum (8 GB recommended for smoother performance)
- **Disk Space:** 500 MB for project files and dependencies

**Other Requirements:**
- **Network Connection:** Required for downloading dependencies and for testing network communication between nodes

#### Installation Guide

**Step 1: Install JDK**
1. Download and install the JDK from the Oracle or OpenJDK website.
2. Set the `JAVA_HOME` environment variable to point to the JDK installation directory.
3. Add the JDK `bin` directory to your system's `PATH` variable.

**Step 2: Install an IDE**
1. Download and install your preferred IDE (IntelliJ IDEA, Eclipse, or Visual Studio Code).
2. Configure the IDE to use the installed JDK.

**Step 3: Set Up the Project Directory**
1. Create a new directory for the project.
   ```sh
   mkdir DistributedFileSystem
   cd DistributedFileSystem
   ```
   
2. Initialize a Git repository (optional).
   ```sh
   git init
   ```

**Step 4: Download Project Files**
1. Clone the project repository from GitHub (if available).
   ```sh
   git clone <repository-url>
   ```
   
2. If not using Git, download the project files and place them in the project directory.

**Step 5: Compile the Project**
1. Open a terminal or command prompt.
2. Navigate to the project directory.
3. Compile the Java files.
   ```sh
   javac -d bin src/*.java
   ```

#### Quick Start

**Step 1: Start the NameNode**
1. Open a terminal or command prompt.
2. Navigate to the project directory.
3. Run the NameNode.
   ```sh
   java -cp bin NameNode
   ```
   
**Step 2: Start DataNodes**
1. Open additional terminal windows for each DataNode.
2. Navigate to the project directory in each terminal.
3. Run the DataNode instances with different ports.
   ```sh
   java -cp bin DataNode 65530
   java -cp bin DataNode 65531
   java -cp bin DataNode 65532
   ```
   
**Step 3: Run the Client**
1. Open another terminal window.
2. Navigate to the project directory.
3. Run the Client and follow the prompts to perform operations.
   ```sh
   java -cp bin Client
   ```

**Example Client Commands:**
- **Append Data to a File:**
  ```sh
  ::append file.txt Hi man, How are you?
  ```
  
- **Read Data from a File:**
  ```sh
  ::read file.txt
  ```
  
- **Shutdown the System:**
  ```sh
  ::shutdown
  ```

### Architecture

#### System Architecture

**Overview:**

The Distributed File System (DFS) is designed with a client-server architecture consisting of three main components: the NameNode, DataNodes, and Client. The system manages file storage and retrieval across multiple networked nodes, ensuring high availability, reliability, and scalability.

**Architecture Diagram:**

```plaintext
+------------+         +------------+         +------------+
|   Client   | <-----> |  NameNode  | <-----> |  DataNode  |
+------------+         +------------+         +------------+
                             ^                      ^
                             |                      |
                             |                      |
                             |                      |
                       +------------+         +------------+
                       |  DataNode  |         |  DataNode  |
                       +------------+         +------------+
```

**Explanation:**

1. **Client:**
   - The Client is the interface through which users interact with the DFS. It sends requests to the NameNode to read or append data to files.

2. **NameNode:**
   - The NameNode acts as the central coordinator. It manages metadata about file locations and coordinates communication with DataNodes to handle client requests.

3. **DataNodes:**
   - DataNodes are storage nodes responsible for storing actual data blocks. They handle read and write operations as directed by the NameNode.

**Workflow:**

1. **Client Request:**
   - The Client sends a request to the NameNode (e.g., read, append, or shutdown).
   
2. **Request Handling:**
   - The NameNode processes the request, updates metadata, and coordinates with DataNodes for data operations.
   
3. **Data Operations:**
   - DataNodes perform the necessary data storage or retrieval operations and communicate the results back to the NameNode.
   
4. **Response:**
   - The NameNode sends a response back to the Client, completing the operation.

#### Modules and Components

**1. NameNode**

**Responsibilities:**
- Manage metadata about file locations and data blocks.
- Coordinate read and write operations between clients and DataNodes.
- Handle client connections and process requests.

**Components:**
- **ServerSocket:** Listens for incoming client connections.
- **Handler Threads:** Manages individual client requests in separate threads.
- **Metadata Storage:** Stores information about file locations and associated data blocks.

**Key Methods:**
- `start(int port)`: Starts the NameNode server on the specified port.
- `initiateShutdown()`: Initiates the shutdown process.
- `append(String filename, String content, NameNodeHandlerClient dataNodeClient)`: Appends content to a file.
- `read(String filename, NameNodeHandlerClient dataNodeClient)`: Reads content from a file.

**2. DataNode**

**Responsibilities:**
- Store and manage data blocks.
- Handle read and write operations as directed by the NameNode.
- Ensure data consistency and integrity.

**Components:**
- **ServerSocket:** Listens for incoming connections from the NameNode.
- **Handler Threads:** Manages individual requests from the NameNode in separate threads.
- **Block Storage:** Stores data blocks in files.

**Key Methods:**
- `alloc()`: Allocates a new data block.
- `read(int blk_id)`: Reads data from a specified block.
- `write(int blk_id, String contents)`: Writes data to a specified block.
- `start(int port)`: Starts the DataNode server on the specified port.

**3. Client**

**Responsibilities:**
- Provide a user-friendly interface for interacting with the DFS.
- Allow users to perform read, write, and shutdown operations.
- Handle network communication with the NameNode.

**Components:**
- **Socket:** Establishes a connection to the NameNode.
- **Input/Output Streams:** Sends requests to and receives responses from the NameNode.

**Key Methods:**
- `startConnection(String ip, int port)`: Starts a connection to the NameNode.
- `sendMessage(String msg)`: Sends a message to the NameNode and returns the response.
- `stopConnection()`: Closes the connection to the NameNode.
- `handleReadCommand(String filename)`: Handles read operations.
- `handleAppendCommand(String filename, String content)`: Handles append operations.
- `handleShutdownCommand()`: Handles shutdown operations.

**Relationships:**

- **Client to NameNode:**
  - The Client sends read, append, and shutdown requests to the NameNode.
  
- **NameNode to DataNodes:**
  - The NameNode coordinates with DataNodes to perform data storage and retrieval operations.

- **DataNodes:**
  - DataNodes are independent of each other but work collectively to store data blocks distributed by the NameNode.

**Summary:**

The Distributed File System is composed of three main components: the Client, NameNode, and DataNodes. The Client provides an interface for users to interact with the system, the NameNode acts as the central coordinator managing metadata and coordinating data operations, and the DataNodes handle the actual storage and retrieval of data blocks. This architecture ensures efficient data management and scalability, with a clear separation of responsibilities among the components. The system supports concurrent client handling, data distribution, robust error handling, and graceful shutdown, making it a reliable and scalable solution for distributed file storage.

