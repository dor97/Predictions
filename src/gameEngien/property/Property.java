package gameEngien.property;

import gameEngien.property.propertyInterface.PropertyInterface;
import gameEngien.property.propertyInterface.propertyType;

import java.io.Serializable;
import java.util.PrimitiveIterator;

public class Property implements PropertyInterface, Serializable {
    private propertyType m_type;
    public Property(propertyType type){
        m_type = type;
    }
    @Override
    public propertyType getType(){
        return m_type;
    }

    @Override
    public void addToProperty(int add) {
    }

    @Override
    public void addToProperty(float add) {
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
    @Override
    public void setProperty(boolean v) {

    }

    @Override
    public void setProperty(String v) {

    }

}
