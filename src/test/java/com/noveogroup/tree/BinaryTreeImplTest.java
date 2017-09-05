package com.noveogroup.tree;

import com.noveogroup.model.Car;
import com.noveogroup.model.Lada;
import com.noveogroup.model.Mercedes;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.Iterator;

public class BinaryTreeImplTest {
    @Test
    public void addElement() throws Exception {
        BinaryTree<Integer, Car> myTree = new BinaryTreeImpl<>();
        Car car1 = new Car(1);
        Car car2 = new Car(2);
        Car car3 = new Car(3);
        Car car4 = new Car(4);

        myTree.addElement(1, car1);
        myTree.addElement(2, car2);
        myTree.addElement(3, car3);
        myTree.addElement(0, car4);

        Iterator treeIterator = myTree.iterator();

        Assert.assertEquals(treeIterator.next(), car4);
        Assert.assertEquals(treeIterator.next(), car1);
        Assert.assertEquals(treeIterator.next(), car2);
        Assert.assertEquals(treeIterator.next(), car3);
    }

    @Test(expected = NullKeyException.class)
    public void addElementTestNullKey() throws BinaryTreeException {
        BinaryTree<Integer, Car> myTree = new BinaryTreeImpl<>();
        myTree.addElement(null, new Mercedes(300));
    }

    @Test(expected = ElementWithSuchKeyAlreadyExistsException.class)
    public void testTheSameKeysInTheTree() throws BinaryTreeException {
        BinaryTree<Integer, Car> myTree = new BinaryTreeImpl<>();
        myTree.addElement(3, new Car(5));
        myTree.addElement(3, new Lada(10));
    }

    @Test(expected = ElementWithSuchKeyDoesNotExistException.class)
    public void testElementDoesNotExistInTheTreeException() throws BinaryTreeException {
        BinaryTree<Long, Lada> ladaTree = new BinaryTreeImpl<>();
        ladaTree.addElement(10L, new Lada(10));
        ladaTree.removeElement(12L);
    }

    @Test
    public void testRemoveElements() throws BinaryTreeException {
        BinaryTree<Integer, Car> myTree = new BinaryTreeImpl<>();
        Car car1 = new Car(1);
        Car car2 = new Car(2);
        Car car3 = new Car(3);
        Car car4 = new Car(4);
        Car car5 = new Car(5);

        myTree.addElement(1, car1);
        myTree.addElement(2, car2);
        myTree.addElement(5, car3);
        myTree.addElement(0, car4);
        myTree.addElement(4, car5);

        Iterator treeIterator = myTree.iterator();

        Assert.assertEquals(treeIterator.next(), car4);
        Assert.assertEquals(treeIterator.next(), car1);
        Assert.assertEquals(treeIterator.next(), car2);
        Assert.assertEquals(treeIterator.next(), car5);
        Assert.assertEquals(treeIterator.next(), car3);

        myTree.removeElement(2);

        Iterator treeIterator2 = myTree.iterator();

        Assert.assertEquals(treeIterator2.next(), car4);
        Assert.assertEquals(treeIterator2.next(), car1);
        Assert.assertEquals(treeIterator2.next(), car5);
        Assert.assertEquals(treeIterator2.next(), car3);

        myTree.removeElement(0);
        treeIterator2 = myTree.iterator();

        Assert.assertEquals(treeIterator2.next(), car1);
        Assert.assertEquals(treeIterator2.next(), car5);
        Assert.assertEquals(treeIterator2.next(), car3);
    }

    @Test
    public void checkCountOfLeaves() throws BinaryTreeException {
        BinaryTreeImpl<Integer, Car> myTree = new BinaryTreeImpl<>();
        Car car1 = new Car(1);
        Car car2 = new Car(2);
        Car car3 = new Car(3);
        Car car4 = new Car(4);
        Car car5 = new Car(5);

        myTree.addElement(1, car1);
        myTree.addElement(2, car2);
        myTree.addElement(5, car3);
        myTree.addElement(0, car4);
        myTree.addElement(4, car5);

        Assert.assertEquals(2, myTree.getCountOfLeaves());
    }

    @Test
    public void checkSerialization() throws BinaryTreeException, IOException, ClassNotFoundException {
        BinaryTreeImpl<Integer, Car> myTree = new BinaryTreeImpl<>();

        Car car1 = new Car(1);
        Car car2 = new Car(2);
        Car car3 = new Car(3);
        Car car4 = new Car(4);
        Car car5 = new Car(5);

        myTree.addElement(1, car1);
        myTree.addElement(2, car2);
        myTree.addElement(5, car3);
        myTree.addElement(0, car4);
        myTree.addElement(4, car5);

        File testFile = new File("test.txt");

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(testFile))) {
            objectOutputStream.writeObject(myTree);
        }

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(testFile))) {
            BinaryTreeImpl<Integer, Car> binaryTree = (BinaryTreeImpl<Integer, Car>) objectInputStream.readObject();
            Iterator treeIterator = binaryTree.iterator();

            Assert.assertTrue(treeIterator.next().equals(car4));
            Assert.assertTrue(treeIterator.next().equals(car1));
            Assert.assertTrue(treeIterator.next().equals(car2));
            Assert.assertTrue(treeIterator.next().equals(car5));
            Assert.assertTrue(treeIterator.next().equals(car3));
        }
    }

}