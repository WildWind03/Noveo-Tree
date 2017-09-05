package com.noveogroup.tree;

public class ElementWithSuchKeyAlreadyExistsException extends BinaryTreeException {
    public ElementWithSuchKeyAlreadyExistsException(String message) {
        super(message);
    }
}
