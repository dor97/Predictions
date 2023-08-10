package gameEngien.rule.action.increase;

import java.io.Serializable;

public class exprecn implements Serializable {
    private Integer valueInt = null;
    private Float valueFloat = null;
    private String valueString = null;
    private Boolean valueBool = null;
    private exprecnType m_type = null;

    public exprecn(){

    }
    public exprecn(int value){
        valueInt = value;
        m_type = exprecnType.INT;
    }
    public exprecn(Object value){
        if(value instanceof Integer){
            valueInt = (Integer) value;
            m_type = exprecnType.INT;
        }
        if(value instanceof Float){
            valueFloat = (Float) value;
            m_type = exprecnType.FLOAT;
        }
        if(value instanceof String){
            valueString = (String)value;
            m_type = exprecnType.STRING;
        }
        if(value instanceof  Boolean){
            valueBool = (Boolean)value;
            m_type = exprecnType.BOOL;
        }
        //exepcen
    }

    public void setValue(Object value){
        if(value instanceof Integer){
            valueInt = (Integer) value;
            m_type = exprecnType.INT;
        }
        if(value instanceof Float){
            valueFloat = (Float) value;
            m_type = exprecnType.FLOAT;
        }
        if(value instanceof String){
            valueString = (String)value;
            m_type = exprecnType.STRING;
        }
        if(value instanceof  Boolean){
            valueBool = (Boolean)value;
            m_type = exprecnType.BOOL;
        }
        //exepcen
    }

    public void setValue(int value){
        valueInt = value;
        m_type = exprecnType.INT;
    }
    public void setValue(float value){
        valueFloat = value;
        m_type = exprecnType.FLOAT;
    }
    public void setValue(boolean value){
        valueBool = value;
        m_type = exprecnType.BOOL;
    }
    public void setValue(String value){
        valueString = value;
        m_type = exprecnType.STRING;
    }

    public void convertValueInString(String value){
        try {
            setValue(Integer.parseInt(value));
            return; // No need to proceed further
        } catch (NumberFormatException e) {
            // Not an int
        }

        // Try to convert to float
        try {
            setValue(Float.parseFloat(value));
            return; // No need to proceed further
        } catch (NumberFormatException e) {
            // Not a float
        }

        // Try to convert to boolean
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            setValue(Boolean.parseBoolean(value));
            return; // No need to proceed further
        }
        setValue(value);
    }

    public Object getValue(){
        switch (m_type){
            case INT:
                return valueInt;
            case FLOAT:
                return valueFloat;
            case STRING:
                return valueString;
            case BOOL:
                return valueBool;
            default:
                return null;
        }
    }

    public int getInt(){
        return valueInt;
    }
    public float getFloat(){
        return valueFloat;
    }
    public String getString() {return valueString;}
    public boolean getBool(){return valueBool;}
    public exprecnType getType(){
        return m_type;
    }
}
