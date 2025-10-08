public class ThreadSafeBinarySearchTree {

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

    private Node root;

    private int compare(byte[] a, byte[] b) {
        int minLen = Math.min(a.length, b.length);
        for (int i = 0; i < minLen; i++) {
            int difference = Byte.compare(a[i], b[i]);
            if (difference != 0) {
                return difference;
            }
        }
        return Integer.compare(a.length, b.length);
    }

    public synchronized void put(byte[] key, byte[] value) {
        if (root == null) {
            root = new Node(key, value); // If tree is empty we initialize the root
            return;
        }
        
        Node parent = null;
        Node current = root;
        int cmp = 0;

        while (current != null) {
            cmp = compare(key, current.key);
            if (cmp == 0) {
                current.value = value; // key already exists, overwritting its value
                return;
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

    public synchronized byte[] get(byte[] key) {
        Node current = root;
        int cmp = 0;

        while (current != null) {
            cmp = compare(key, current.key);
            if (cmp == 0) {
                return current.value;
            }
            else if (cmp < 0) {
                current = current.left;
            }
            else {
                current = current.right;
            }
        }
        return null;
    }
}