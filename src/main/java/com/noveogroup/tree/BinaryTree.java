package com.noveogroup.tree;

import java.io.Serializable;

public interface BinaryTree<K extends Comparable<K>, V> extends Iterable<V>, Serializable {
    void addElement(K key, V element) throws BinaryTreeException;
    void removeElement(K key) throws BinaryTreeException;
}
