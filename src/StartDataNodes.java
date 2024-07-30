import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

/**
 * StartDataNodes class is responsible for starting multiple DataNode instances,
 * each listening on a different port. It creates new processes for each DataNode
 * and directs their output to separate log files.
 * 
 * Author: Umar Mohammad
 */
public class StartDataNodes {

    public static void main(String[] args) throws IOException {
        // Ports for the DataNode instances
        int[] ports = {65530, 65531, 65532};
        for (int port : ports) {
            startDataNode(port, "DNode_" + port + ".log"); // Create a log file for each DataNode
        }

        System.out.println("All data nodes are running...");
    }

    /**
     * Starts a DataNode instance in its own process.
     * 
     * @param port The port number for the DataNode.
     * @param logFile The log file name for redirecting output and error streams.
     * @throws IOException If an I/O error occurs.
     */
    public static void startDataNode(int port, String logFile) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-classpath", System.getProperty("java.class.path"), "DataNode", String.valueOf(port)
        );
        File log = new File(logFile);
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(Redirect.appendTo(log));
        processBuilder.start();
    }
}
