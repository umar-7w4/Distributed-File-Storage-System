import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Block class representing a block of data in the distributed file system.
 * It contains information about the file where the block's data is stored
 * and provides mechanisms to handle concurrent read and write operations using locks.
 * 
 * Author: Umar Mohammad
 */
public class Block {
    private String filename; // File where data is stored in a data node
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock(); // Reader side of readWriteLock
    private final Lock writeLock = readWriteLock.writeLock(); // Writer side of readWriteLock

    /**
     * Constructor to create a block with the specified filename.
     * 
     * @param filename The name of the file where the block's data is stored.
     */
    public Block(String filename) {
        this.filename = filename;
    }

    /**
     * Gets the filename where the block's data is stored.
     * 
     * @return The filename.
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * Gets the read lock associated with this block.
     * 
     * @return The read lock.
     */
    public Lock getReadLock() {
        return this.readLock;
    }

    /**
     * Gets the write lock associated with this block.
     * 
     * @return The write lock.
     */
    public Lock getWriteLock() {
        return this.writeLock;
    }
}
