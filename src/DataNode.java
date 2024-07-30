import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Queue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.ServerSocket;

/**
 * DataNode class represents a node in the Distributed File System that stores and manages data blocks.
 * It handles requests for allocating, reading, and writing data blocks, ensuring thread-safe operations using synchronization and locks.
 * 
 * Author: Umar Mohammad
 */
public class DataNode {
    private ServerSocket dataServer = null;
    private int port; // Port number for the DataNode, also serves as an identifier
    private static final int MAX_BLOCKS = 100; // Maximum number of blocks that the DataNode can manage
    private Queue<Integer> availableBlocksQueue; // Queue of available block IDs
    private HashMap<Integer, Block> usedBlocksMap; // Map of used blocks (block ID to Block object)
    private Path dataDirectory; // Directory for storing block files

    // Locks for synchronizing access to shared resources
    private final Object availableQueueLock = new Object();
    private final Object usedMapLock = new Object();

    public static void main(String[] args) throws InterruptedException {
        int port = parseCommandLineArguments(args);
        DataNode dataNode = new DataNode(port);
        dataNode.start();
        dataNode.stop();
    }

    /**
     * Constructor to initialize a DataNode with the specified port number.
     * 
     * @param port The port number for the DataNode.
     */
    public DataNode(int port) {
        this.port = port;
        initializeAvailableBlocksQueue();
        usedBlocksMap = new HashMap<>(MAX_BLOCKS);
        initializeDataDirectory();
    }

    /**
     * Initializes the available blocks queue with block IDs.
     */
    private void initializeAvailableBlocksQueue() {
        availableBlocksQueue = new LinkedList<>();
        for (int i = 0; i < MAX_BLOCKS; i++) {
            availableBlocksQueue.add(i);
        }
    }

    /**
     * Initializes the data directory for storing block files.
     */
    private void initializeDataDirectory() {
        dataDirectory = Paths.get("./data_" + port);
        System.out.println("Storing all files in: " + dataDirectory);
        if (Files.notExists(dataDirectory)) {
            try {
                Files.createDirectory(dataDirectory);
            } catch (IOException e) {
                System.err.println("Unable to create directory: " + dataDirectory);
                e.printStackTrace();
                System.exit(5);
            }
        }
    }

    /**
     * Parses command line arguments to get the port number.
     * 
     * @param args Command line arguments.
     * @return The port number.
     */
    private static int parseCommandLineArguments(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify port number as the first command line argument.");
            System.exit(0);
        }
        try {
            return Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Expected the first argument to be a port number (integer).");
            System.exit(1);
        }
        return 0; // This line will never be reached
    }

    /**
     * Starts the DataNode server to listen for client connections.
     */
    public void start() {
        try {
            dataServer = new ServerSocket(port);
            System.out.println("DataNode running on port: " + port);
            while (true) {
                new DataNodeHandler(dataServer.accept(), this).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to allocate port: " + port);
            System.exit(2);
        }
    }

    /**
     * Stops the DataNode server by closing the server socket.
     */
    public void stop() {
        try {
            dataServer.close();
        } catch (IOException e) {
            System.err.println("Issue stopping DataNode server...");
            System.exit(3);
        }
    }

    /**
     * Allocates a new block if available.
     * 
     * @return The allocated block ID, or -1 if no blocks are available.
     */
    public int allocateBlock() {
        int blockId = -1;
        synchronized (availableQueueLock) {
            if (!availableBlocksQueue.isEmpty()) {
                blockId = availableBlocksQueue.poll();
            }
        }
        if (blockId != -1) {
            String filename = dataDirectory.toString() + "/blk_" + blockId + ".bin";
            System.out.println("Allocating: " + filename);
            synchronized (usedMapLock) {
                Block block = new Block(filename);
                usedBlocksMap.put(blockId, block);
            }
            try {
                Path path = Paths.get(filename);
                Files.deleteIfExists(path);
                Files.createFile(path);
            } catch (IOException e) {
                System.err.println("Unable to open file: " + filename + " for block: " + blockId);
                e.printStackTrace();
            }
        }
        return blockId;
    }

    /**
     * Reads the contents of the specified block.
     * 
     * @param blockId The block ID.
     * @return The contents of the block, or null if the block ID is invalid.
     */
    public String readBlock(int blockId) {
        if (blockId >= MAX_BLOCKS || !usedBlocksMap.containsKey(blockId)) {
            System.err.println("Requested block not found or not in use: " + blockId);
            return null;
        }
        Block block = usedBlocksMap.get(blockId);
        Path path = Paths.get(block.getFilename());
        if (!Files.exists(path)) {
            return null;
        }
        byte[] data = null;
        block.getReadLock().lock();
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            System.err.println("Unable to read file: " + path);
            e.printStackTrace();
        } finally {
            block.getReadLock().unlock();
        }
        return new String(data);
    }

    /**
     * Writes the specified contents to the block.
     * 
     * @param blockId  The block ID.
     * @param contents The contents to write to the block.
     * @return True if the write operation was successful, false otherwise.
     */
    public boolean writeBlock(int blockId, String contents) {
        if (blockId >= MAX_BLOCKS || !usedBlocksMap.containsKey(blockId)) {
            System.err.println("Requested block not found: " + blockId);
            return false;
        }
        Block block = usedBlocksMap.get(blockId);
        Path path = Paths.get(block.getFilename());
        byte[] data = contents.getBytes();
        block.getWriteLock().lock();
        try {
            Files.write(path, data);
        } catch (IOException e) {
            System.err.println("Unable to write contents to block: " + blockId + " (file: " + path + ")");
            e.printStackTrace();
        } finally {
            block.getWriteLock().unlock();
        }
        return true;
    }

    /**
     * Checks if the DataNode has no available blocks.
     * 
     * @return True if there are no available blocks, false otherwise.
     */
    public boolean isFull() {
        return availableBlocksQueue.isEmpty();
    }

    /**
     * Returns the number of available blocks.
     * 
     * @return The number of available blocks.
     */
    public int getNumberOfEmptyBlocks() {
        return availableBlocksQueue.size();
    }

    /**
     * Prints the contents of all used blocks.
     */
    public void printContents() {
        System.out.println("Data Node " + port + " contents:");
        synchronized (usedMapLock) {
            for (Entry<Integer, Block> entry : usedBlocksMap.entrySet()) {
                System.out.println("Block " + entry.getKey() + ": " + readBlock(entry.getKey()));
            }
        }
    }

    /**
     * Test function to allocate and write data to blocks.
     */
    public void mockRun() {
        for (int i = 0; i < 11; i++) {
            int block = allocateBlock();
            if (block < 0) {
                System.out.println("Couldn't allocate data for block " + i);
                if (isFull()) {
                    System.out.println("Yes, we're full...");
                }
            } else {
                writeBlock(i, String.valueOf(i));
            }
            System.out.println(getNumberOfEmptyBlocks() + " blocks available for node one");
        }
        printContents();
    }
}
