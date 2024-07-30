/**
 * Central class that acts as a client to interact with the Distributed File System.
 * It demonstrates how multiple clients can send requests to the server to perform read and write operations on files.
 * 
 * Author: Umar Mohammad
 */
public class Central extends Client {

    public static void main(String[] args) {
        Client client1 = new Client();
        client1.startConnection("127.0.0.1", 6667);
        String response1 = client1.sendMessage("Hello World");
        System.out.println("Response from server: " + response1);
        client1.stopConnection();

        // Client for file operations greater than 4 MB
        Client client2 = new Client();
        client2.startConnection("127.0.0.1", 6667);
        String response2 = client2.sendMessage("READ greaterThanFourMB.txt");
        System.out.println("Response from server: " + response2);
        String response3 = client2.sendMessage("APPEND greaterThanFourMB.txt My name is");
        System.out.println("Response from server: " + response3);
        client2.stopConnection();

        // Client for file operations less than 4 MB
        Client client3 = new Client();
        client3.startConnection("127.0.0.1", 6667);
        String response4 = client3.sendMessage("READ D19.txt");
        System.out.println("Response from server: " + response4);
        String response5 = client3.sendMessage("APPEND D19.txt Hi");
        System.out.println("Response from server: " + response5);
        client3.stopConnection();
    }
}
