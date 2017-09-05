package com.noveogroup.tree;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinaryTreeImpl<K extends Comparable<K>, V> implements BinaryTree<K, V> {

    private Node<K, V> root;

    @Override
    public Iterator<V> iterator() {
        return new TreeIterator();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(getCountOfLeaves());
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.readInt();
        in.defaultReadObject();
    }

    private static class Node<K extends Comparable<K>, V> implements Comparable<Node<K, V>>, Serializable {

        private final K key;
        private V value;

        private BinaryTreeImpl.Node<K, V> left, right;
        private BinaryTreeImpl.Node<K, V> parent;

        public Node(K key, V value, Node<K, V> parent) throws NullKeyException {
            if (null == key) {
                throw new NullKeyException("The key must be not null!");
            }

            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        @Override
        public int compareTo(Node<K, V> o) {
            return key.compareTo(o.getKey());
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public BinaryTreeImpl.Node<K, V> getLeft() {
            return left;
        }

        public BinaryTreeImpl.Node<K, V> getRight() {
            return right;
        }

        public Node<K, V> getParent() {
            return parent;
        }

        public void setLeft(Node<K, V> left) {
            this.left = left;
        }

        public void setRight(Node<K, V> right) {
            this.right = right;
        }

        public void setParent(Node<K, V> parent) {
            this.parent = parent;
        }

        public boolean hasRight() {
            return null != right;
        }

        public boolean hasLeft() {
            return null != left;
        }

        public boolean hasParent() {
            return null != parent;
        }
    }

    public class TreeIterator implements Iterator<V>, Serializable {
        private BinaryTreeImpl.Node<K, V> next;
        private BinaryTreeImpl.Node<K, V> current;

        public TreeIterator() {
            next = getMin(root);
        }

        @Override
        public void remove() {
            removeElement(current);
        }

        @Override
        public boolean hasNext() {
            return null != next;
        }

        @Override
        public V next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Next element is null");
            }

            current = next;

            if (null != next.getRight()) {
                next = next.getRight();

                while (null != next.getLeft()) {
                    next = next.getLeft();
                }

                return current.getValue();
            } else {
                while (true) {
                    if (null == next.getParent()) {
                        next = null;
                        return current.getValue();
                    }

                    if (next.getParent().getLeft() == next) {
                        next = next.getParent();
                        return current.getValue();
                    }

                    next = next.getParent();
                }
            }
        }
    }

    /**
     * @throws NullKeyException if key is null. You can not use null for key
     * @throws ElementWithSuchKeyAlreadyExistsException if there is an element with such key in the tree
     */

    @Override
    public void addElement(K key, V element) throws NullKeyException, ElementWithSuchKeyAlreadyExistsException {
        if (null == key) {
            throw new NullKeyException("A key can not be null!");
        }

        addElement(key, element, root);
    }

    private void addElement(K key, V value, Node<K, V> base) throws NullKeyException, ElementWithSuchKeyAlreadyExistsException {
        if (null == key) {
            throw new NullKeyException("A key can not be null");
        }

        if (null == base) {
                root = new Node<>(key, value, null);
            } else {
            if (key.compareTo(base.getKey()) > 0) {
                if (!base.hasRight()) {
                    Node<K,V> rightNode = new Node<>(key, value, base);
                    base.setRight(rightNode);
                } else {
                    addElement(key, value, base.getRight());
                }
            } else {
                if (0 == key.compareTo(base.getKey())) {
                    throw new ElementWithSuchKeyAlreadyExistsException("The element with such key is already exist");
                }

                if (!base.hasLeft()) {
                    Node<K,V> leftNode = new Node<>(key, value, base);
                    base.setLeft(leftNode);
                } else {
                    addElement(key, value, base.getLeft());
                }
            }
        }
    }

    private Node<K, V> getElement(K key) throws NullKeyException, ImpossibleBinaryTreeException, ElementWithSuchKeyDoesNotExistException {
        return getElement(key, root);
    }

    private Node<K, V> getElement(K key, Node<K, V> currentNode) throws NullKeyException, ImpossibleBinaryTreeException, ElementWithSuchKeyDoesNotExistException {
        if (null == currentNode) {
            throw new ElementWithSuchKeyDoesNotExistException("Element with such key doesn't exist in the tree!");
        }

        if (null == key) {
            throw new NullKeyException("The key can not be null");
        }
        if (0 == key.compareTo(currentNode.getKey())) {
            return currentNode;
        }

        if (key.compareTo(currentNode.getKey()) > 0) {
            return getElement(key, currentNode.getRight());
        }

        if (key.compareTo(currentNode.getKey()) < 0) {
            return getElement(key, currentNode.getLeft());
        }

        throw new ImpossibleBinaryTreeException("It's impossible bug! If it happened, you are very unlucky!");
    }

    private void removeElement(Node<K, V> nodeToRemove) {
        Node<K, V> nodeToRemoveParent = nodeToRemove.getParent();

        if (null == nodeToRemove.getRight()) {
            Node<K, V> newNode = nodeToRemove.getLeft();

            if (null == nodeToRemoveParent) {
                root = nodeToRemove.getLeft();
                root.setParent(null);
            } else {
                if (nodeToRemoveParent.getRight() == nodeToRemove) {
                    nodeToRemoveParent.setRight(newNode);
                } else {
                    nodeToRemoveParent.setLeft(newNode);
                }
            }
        } else {
            Node<K, V> minNode = getMin(nodeToRemove.getRight()); //can not be null
            Node<K, V> minNodeParent = minNode.getParent();

            if (minNodeParent.getRight() == minNode) {
                minNodeParent.setRight(null);
            } else {
                minNodeParent.setLeft(null);
            }

            if (minNodeParent != nodeToRemove) {
                minNodeParent.setLeft(minNode.getRight());
                if (minNode.hasRight()) {
                    minNode.getRight().setParent(minNodeParent);
                }
            } else {
                nodeToRemove.setRight(minNode.getRight());
                if (minNode.hasRight()) {
                    minNode.getRight().setParent(nodeToRemove);
                }
            }

            Node<K, V> nodeToRemoveRightChild = nodeToRemove.getRight();
            Node<K, V> nodeToRemoveLeftChild = nodeToRemove.getLeft();

            minNode.setParent(nodeToRemoveParent);
            minNode.setRight(nodeToRemoveRightChild);
            minNode.setLeft(nodeToRemoveLeftChild);

            if (null != nodeToRemoveParent) {
                if (nodeToRemoveParent.getRight() == nodeToRemove) {
                    nodeToRemoveParent.setRight(minNode);
                } else {
                    nodeToRemoveParent.setLeft(minNode);
                }
            } else {
                root = minNode;
            }

            if (null != nodeToRemoveLeftChild) {
                nodeToRemoveLeftChild.setParent(minNode);
            }

            if (null != nodeToRemoveRightChild) {
                nodeToRemoveRightChild.setParent(minNode);
            }
        }
    }

    /**
     * @throws ElementWithSuchKeyDoesNotExistException if there isn't element with such key in the tree
     * @throws ImpossibleBinaryTreeException if you are very unlucky :)
     * @throws NullKeyException if the key is null. It's impossible to keep an element with null key in the tree
     */
    @Override
    public void removeElement(K key) throws ElementWithSuchKeyDoesNotExistException, ImpossibleBinaryTreeException, NullKeyException {
        Node<K, V> nodeToRemove = getElement(key);
        removeElement(nodeToRemove);
    }

    public int getCountOfLeaves() {
        return getCountOfLeaves(root);
    }

    private int getCountOfLeaves(Node<K, V> root) {
        if (null == root) {
            return 0;
        }

        int countOfLeftLeaves = 0;
        if (root.hasLeft()) {
            countOfLeftLeaves = getCountOfLeaves(root.getLeft());
        }

        int countOfRightLeaves = 0;
        if (root.hasRight()) {
            countOfRightLeaves = getCountOfLeaves(root.getRight());
        }


        int totalLeaves = countOfRightLeaves + countOfLeftLeaves;

        if (!root.hasLeft() && !root.hasRight()) {
            totalLeaves = 1;
        }

        return totalLeaves;
    }

    private Node<K, V> getMin(Node<K, V> root) {
        if (null == root) {
            return null;
        }

        while (null != root.getLeft()) {
            root = root.getLeft();
        }

        return root;
    }

}
