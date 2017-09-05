package com.noveogroup.tree;

public class ElementWithSuchKeyDoesNotExistException extends BinaryTreeException {
    public ElementWithSuchKeyDoesNotExistException(String message) {
        super(message);
    }
}
