// This class implements a thread-safe binary search tree (BST) for storing key-value pairs in sorted order.
// The keys and values are represented as byte arrays. The BST supports insertion (put method) and retrieval (get method) operations.
public class ThreadSafeBinarySearchTree {

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

    private Node root; // Root of the BST

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

    // Synchronized method to insert/update a key-value pair in the BST
    public synchronized void put(byte[] key, byte[] value) {
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
    }

    // Synchronized method to retrieve a value by key from the BST
    public synchronized byte[] get(byte[] key) {
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
    }
}