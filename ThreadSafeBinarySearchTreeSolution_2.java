import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReadWriteLock;

// This class implements a thread-safe binary search tree (BST) for storing key-value pairs in sorted order.
// The keys and values are represented as byte arrays. The BST supports insertion (put method) and retrieval (get method) operations.
public class ThreadSafeBinarySearchTreeSolution_2 {

    // Node class representing each node in the BST
    // Each node contains a key, a value, and references to left and right child nodes.
    private static class Node {
        byte[] key;
        byte[] value;
        Node left;
        Node right;

        Node(byte[] key, byte[] value) {
            this.key = key;
            this.value = value;
        }
    }

    public Node root; // Root of the BST
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    // Method to compare two byte arrays lexicographically
    private int compare(byte[] a, byte[] b) {
        int minLen = Math.min(a.length, b.length);

        // Comparing byte by byte
        for (int i = 0; i < minLen; i++) {
            int difference = Byte.compare(a[i], b[i]);
            if (difference != 0) {
                return difference;
            }
        }
        // If all compared bytes are equal, the shorter byte array will be considered smaller
        return Integer.compare(a.length, b.length);
    }

    // Thread-safe put method to insert/update a key-value pair in the BST
    public void put(byte[] key, byte[] value) {
        lock.writeLock().lock(); // Acquiring write lock

        try {
            if (root == null) {
                root = new Node(key, value); // If tree is empty we initialize the root
                return; // stopping further execution
            }

            Node parent = null;
            Node current = root;
            int cmp = 0;

            // Iterating over the nodes until we find the correct position for the new key
            while (current != null) {
                cmp = compare(key, current.key);
                if (cmp == 0) {
                    current.value = value; // key already exists, overwritting its value
                    return; // stopping further execution
                }
            
                parent = current;
                if (cmp < 0) {
                    current = current.left;
                }
                if (cmp > 0) {
                    current = current.right;
                }
            }
            if (cmp < 0) {
                parent.left = new Node(key, value);
            }
            if (cmp > 0) {
                parent.right = new Node(key, value);
            }
        } finally {
            lock.writeLock().unlock(); // Releasing write lock
        }
    }

    // Thread-safe get method to retrieve the value associated with a given key from the BST
    public byte[] get(byte[] key) {
        lock.readLock().lock(); // Acquiring read lock
        
        try {
            Node current = root;
            int cmp = 0;

            // Iterating over the nodes until we find the key or reach a null node
            while (current != null) {
                cmp = compare(key, current.key);
                if (cmp == 0) {
                    return current.value; // key found, returning its value
                }
                else if (cmp < 0) {
                    current = current.left;
                }
                else {
                    current = current.right;
                }
            }
            return null; // if key is not found, then the method returns null
        } finally {
            lock.readLock().unlock(); // Releasing read lock
        }
    }
}