package gameEngien.property;

import gameEngien.property.propertyInterface.PropertyInterface;

import java.util.PrimitiveIterator;

public class Property implements PropertyInterface {

    @Override
    public boolean addToProperty(int add) {
        return false;
    }

    @Override
    public boolean addToProperty(float add) {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setProperty(int v) {

    }

    @Override
    public void setProperty(float v) {

    }
}
