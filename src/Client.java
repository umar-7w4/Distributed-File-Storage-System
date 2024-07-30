import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

/**
 * Client class to interact with the Distributed File System.
 * It handles the network communication with the server to perform read and write operations on files.
 * 
 * Author: Umar Mohammad
 */
public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("::");
        String input;

        while ((input = scanner.nextLine()) != null) {
            String[] tokens = input.split(" ");
            if (tokens[0].equalsIgnoreCase("read") && tokens.length > 1) {
                handleReadCommand(tokens[1]);
            } else if (tokens[0].equalsIgnoreCase("append") && tokens.length > 2) {
                handleAppendCommand(tokens[1], input.split(" ", 3)[2]);
            } else if (tokens[0].equalsIgnoreCase("shutdown")) {
                handleShutdownCommand();
            } else {
                System.out.println("Invalid Input");
            }
            System.out.print("::");
        }
        scanner.close();
    }

    private static void handleReadCommand(String filename) {
        Client client = new Client();
        client.startConnection("127.0.0.1", 5558);
        String response = client.sendMessage("READ " + filename);
        System.out.println("Response from server: " + response);
        client.sendMessage(".");
        client.stopConnection();
    }

    private static void handleAppendCommand(String filename, String content) {
        Client client = new Client();
        client.startConnection("127.0.0.1", 5558);
        String response = client.sendMessage("APPEND " + filename + " " + content);
        System.out.println("Response from server: " + response);
        client.sendMessage(".");
        client.stopConnection();
    }

    private static void handleShutdownCommand() {
        Client client = new Client();
        client.startConnection("127.0.0.1", 5558);
        String response = client.sendMessage("shutdown");
        System.out.println("Response from server: " + response);
        client.sendMessage(".");
        client.stopConnection();
    }

    /**
     * Starts a connection to the server with the given IP and port.
     * 
     * @param ip The IP address of the server.
     * @param port The port number of the server.
     */
    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error starting connection");
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to the server and returns the response.
     * 
     * @param msg The message to send to the server.
     * @return The response from the server.
     */
    public String sendMessage(String msg) {
        String response = "";
        try {
            out.println(msg);
            response = in.readLine();
        } catch (SocketException e) {
            System.err.println("Connection reset by server. Server might be down.");
            response = "ERROR";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
    
    /**
     * Closes the connection to the server.
     */
    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
