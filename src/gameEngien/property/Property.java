package gameEngien.property;

import gameEngien.property.propertyInterface.PropertyInterface;
import gameEngien.property.propertyInterface.propertyType;
import org.omg.CORBA.INVALID_ACTIVITY;

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
        throw new INVALID_ACTIVITY("Trying to add decimal value to property of type " + m_type);
    }
    @Override
    public void addToProperty(float add) {
        throw new INVALID_ACTIVITY("Trying to add float value to property of type " + m_type);
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
        throw new INVALID_ACTIVITY("Trying to set value of property type " + m_type + " with decimal value");
    }
    @Override
    public void setProperty(float v) {
        throw new INVALID_ACTIVITY("Trying to set value of property type " + m_type + " with float value");
    }
    @Override
    public void setProperty(boolean v) {
        throw new INVALID_ACTIVITY("Trying to set value of property type " + m_type + " with boolean value");
    }
    @Override
    public void setProperty(String v) {
        throw new INVALID_ACTIVITY("Trying to set value of property type " + m_type + " with string value");
    }
}
