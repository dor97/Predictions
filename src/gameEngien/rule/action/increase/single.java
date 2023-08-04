package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;
import gameEngien.generated.PRDCondition;

import static gameEngien.utilites.Utilites.environment;

public class single implements subCondition {

    enum opertor{ EQUAL, UNEQUAL, BIGGER, LITTLE}
    private String m_entity;
    private String m_property;
    private opertor m_op;
    private exprecnWithFunc m_exprecn;

    public single(PRDCondition condition){
        m_entity = condition.getEntity();
        m_property = condition.getProperty();
        m_op = getOpFromString(condition.getOperator());
        m_exprecn.convertValueInString(condition.getValue());
    }

    private opertor getOpFromString(String op){
        if(op == "="){
            return opertor.EQUAL;
        }
        else if(op == "!="){
            return opertor.UNEQUAL;
        }
        else if (op == "bt"){
            return opertor.BIGGER;
        } else if (op == "lt") {
            return opertor.LITTLE;
        }
        return opertor.LITTLE;  //do not get here
    }

    @Override
    public boolean getBoolValue(Entity entity) {
        exprecn propertyValue = new exprecn();
        propertyValue.setValue(entity.getProperty(m_property).getValue());
        if (m_exprecn.getType() == exprecnType.INT || m_exprecn.getType() == exprecnType.FLOAT) {
            return getOpValue(propertyValue, m_exprecn);    //cheak if propertyValue type string of bool later and trow excepcen
        } else if (m_exprecn.getType() == exprecnType.STRING) {
            if (m_exprecn.isFunc()) {
                if (m_exprecn.getValue() == "environment") {
                    exprecn temp = new exprecn();
                    temp.setValue(environment(m_exprecn.getParams(0).getString()));
                    if (temp.getType() == exprecnType.INT || temp.getType() == exprecnType.FLOAT) {
                        return getOpValue(propertyValue, temp);    //cheak if propertyValue type string of bool later and trow excepcen
                    }
                    else {
                        //excpcen
                    }
                }
            } else {
                if (entity.isPropertyExists(m_exprecn.getString())) {
                    exprecn temp = new exprecn();
                    temp.setValue(entity.getProperty(m_exprecn.getString()).getValue());
                    if (temp.getType() == exprecnType.INT || temp.getType() == exprecnType.FLOAT) {
                        return getOpValue(propertyValue, temp);    //cheak if propertyValue type string of bool later and trow excepcen
                    }
                    else {
                        //excpcen
                    }
                }
            }
        } else if (m_exprecn.getType() == exprecnType.BOOL) {
            return false; //excepcen
        }
        return false;   //do not get here
    }

    private boolean getOpValue(exprecn exprecn1, exprecn exprecn2){
        if(m_op == opertor.EQUAL){
            return getIsEqual(exprecn1, exprecn2);
        } else if (m_op == opertor.UNEQUAL) {
            return !getIsEqual(exprecn1, exprecn2);
        } else if (m_op == opertor.BIGGER) {
            return getIsBigger(exprecn1, exprecn2);
        } else if (m_op == opertor.LITTLE) {
            return getIsBigger(exprecn2, exprecn1); //swap between exprecn1 and eprecn2
        }
        return  false; //do not get here
    }
    
    private boolean getIsEqual(exprecn exprecn1, exprecn exprecn2){
        if(exprecn1.getType() == exprecn2.getType() && exprecn1.getType() == exprecnType.INT){
            return exprecn1.getInt() == exprecn2.getInt();
        } else if (exprecn1.getType() == exprecn2.getType() && exprecn1.getType() == exprecnType.FLOAT) {
            return exprecn1.getFloat() == exprecn2.getFloat();
        } else if (exprecn1.getType() == exprecnType.INT && exprecn2.getType() == exprecnType.FLOAT) {
            return exprecn1.getInt() == exprecn2.getFloat();
        } else if (exprecn1.getType() == exprecnType.FLOAT && exprecn2.getType() == exprecnType.INT) {
            return exprecn1.getFloat() == exprecn2.getInt();
        }
        return false;//excpcen
    }

    private boolean getIsBigger(exprecn exprecn1, exprecn exprecn2){
        if(exprecn1.getType() == exprecn2.getType() && exprecn1.getType() == exprecnType.INT){
            return exprecn1.getInt() > exprecn2.getInt();
        } else if (exprecn1.getType() == exprecn2.getType() && exprecn1.getType() == exprecnType.FLOAT) {
            return exprecn1.getFloat() > exprecn2.getFloat();
        } else if (exprecn1.getType() == exprecnType.INT && exprecn2.getType() == exprecnType.FLOAT) {
            return exprecn1.getInt() > exprecn2.getFloat();
        } else if (exprecn1.getType() == exprecnType.FLOAT && exprecn2.getType() == exprecnType.INT) {
            return exprecn1.getFloat() > exprecn2.getInt();
        }
        return false;//excpcen
    }
}
