package com.noveogroup.model;

public class Car implements TreeElement {
    private int id;

    public Car(int id) {
        this.id = id;
    }

    private int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        return id == car.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
