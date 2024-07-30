import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * DataNodeHandler class handles individual client connections to the DataNode.
 * It processes incoming commands, performs the appropriate actions on the DataNode, and sends responses back to the client.
 * 
 * Author: Umar Mohammad
 */
class DataNodeHandler extends Thread {
    private Socket clientSocket; // Connection with the client
    private DataNode dataNode; // Reference to the DataNode that spawned this handler
    private BufferedReader inputReader; // Input stream for reading client commands
    private PrintWriter outputWriter; // Output stream for sending responses to the client

    /**
     * Constructor to initialize the handler with the client socket and DataNode reference.
     * 
     * @param clientSocket The client socket.
     * @param dataNode The DataNode instance.
     */
    public DataNodeHandler(Socket clientSocket, DataNode dataNode) {
        this.clientSocket = clientSocket;
        this.dataNode = dataNode;
    }

    /**
     * Reads a command from the client.
     * 
     * @return The command as a string.
     */
    private String readCommand() {
        String command = null;
        try {
            inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            command = inputReader.readLine().trim();
        } catch (Exception e) {
            System.err.println("Handler error while trying to read a command!");
            e.printStackTrace();
        }
        return command;
    }

    /**
     * Sends a message to the client.
     * 
     * @param message The message to send.
     */
    private void sendResponse(String message) {
        try {
            outputWriter = new PrintWriter(clientSocket.getOutputStream());
            outputWriter.print(message);
            outputWriter.flush();
        } catch (Exception e) {
            System.err.println("Handler error while trying to return message to client!");
            e.printStackTrace();
        }
    }

    /**
     * Ensures that connections are closed.
     */
    private void closeConnection() {
        try {
            clientSocket.close();
            inputReader.close();
            outputWriter.close();
        } catch (Exception e) {
            System.err.println("Handler error while trying to close the connection!");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // This is where the command parsing, DataNode commanding, and message return happens
        String command = readCommand();
        System.out.println("Port[" + this.clientSocket.getPort() + "] received message: " + command);

        String responseMessage = "DEFAULT";

        // Parse the command and perform the requested action
        String[] commandParts = command.split(" ", 2);
        String commandKey = commandParts[0];

        switch (commandKey.toUpperCase()) {
            case "ALLOC":
                // Allocate a block
                int allocatedBlock = dataNode.allocateBlock();
                responseMessage = String.valueOf(allocatedBlock);
                break;
            case "READ":
                // Read from a block
                int readBlockId = Integer.parseInt(commandParts[1]);
                String blockContents = dataNode.readBlock(readBlockId);
                responseMessage = blockContents;
                break;
            case "WRITE":
                // Write to a block
                String[] writeParts = commandParts[1].split(" ", 2);
                int writeBlockId = Integer.parseInt(writeParts[0]);
                String writeData = writeParts[1];
                dataNode.writeBlock(writeBlockId, writeData);
                responseMessage = "COMPLETE";
                break;
            default:
                // Error, invalid command
                responseMessage = "ERROR: Invalid Command";
                break;
        }

        // Send response to the client
        sendResponse(responseMessage);

        // Close the connection
        closeConnection();
    }
}
