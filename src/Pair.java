/**
 * Pair class represents a pair of DataNode identifier and block number.
 * It is used to keep track of which DataNode stores which block of data in the distributed file system.
 * 
 * Author: Umar Mohammad
 */
public class Pair {
    private String dataNodeId; // Identifier for the DataNode
    private int blockNumber; // Block number within the DataNode

    /**
     * Constructor to initialize the Pair with the specified DataNode ID and block number.
     * 
     * @param dataNodeId The identifier for the DataNode.
     * @param blockNumber The block number within the DataNode.
     */
    public Pair(String dataNodeId, int blockNumber) {
        this.dataNodeId = dataNodeId;
        this.blockNumber = blockNumber;
    }

    /**
     * Gets the DataNode identifier.
     * 
     * @return The DataNode identifier.
     */
    public String getDataNodeId() {
        return dataNodeId;
    }

    /**
     * Sets the DataNode identifier.
     * 
     * @param dataNodeId The DataNode identifier.
     */
    public void setDataNodeId(String dataNodeId) {
        this.dataNodeId = dataNodeId;
    }

    /**
     * Gets the block number.
     * 
     * @return The block number.
     */
    public int getBlockNumber() {
        return blockNumber;
    }

    /**
     * Sets the block number.
     * 
     * @param blockNumber The block number.
     */
    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }
}
