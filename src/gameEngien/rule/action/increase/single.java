package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;
import gameEngien.generated.PRDAction;
import gameEngien.generated.PRDCondition;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.Serializable;

import static gameEngien.utilites.Utilites.*;
import static gameEngien.utilites.Utilites.getEntityDifenichan;

public class single implements subCondition, Serializable {

    enum opertor{ EQUAL, UNEQUAL, BIGGER, LITTLE}
    private String m_entity;
    private String m_property;
    private opertor m_op;
    private exprecnWithFunc m_exprecn;

    public single(PRDCondition condition) throws InvalidValue{
        m_entity = condition.getEntity();
        m_property = condition.getProperty();
        m_op = getOpFromString(condition.getOperator());
        m_exprecn = new exprecnWithFunc();
        m_exprecn.convertValueInString(condition.getValue());
        cheackUserInput();
    }

    private void cheackUserInput() throws InvalidValue {
        checkEntityAndPropertyExist();
        checkTypeValid();
        //checkCompatibilityBetweenPropertyAndExpression();   //disable because if exercise requirements
    }

    private void checkCompatibilityBetweenPropertyAndExpression() throws InvalidValue{
        if(m_exprecn.getType() == exprecnType.STRING) {
            if (!m_exprecn.isFunc()) {
                checkExpressionIfProperty();
            }
            else{
                checkExpressionIfFunction();
            }
        }
    }

    private void checkExpressionIfProperty() throws InvalidValue{
        if (!getEntityDifenichan(m_entity).getPropertys().containsValue(m_exprecn)) {
            throw new InvalidValue("In action condition in single value is of the wrong type");
        }
        if(!(getEntityDifenichan(m_entity).getPropertys().get(m_exprecn).getType() == exprecnType.INT || getEntityDifenichan(m_entity).getPropertys().get(m_exprecn).getType() == exprecnType.FLOAT)){
            throw new InvalidValue("In action condition in single value is a property of the wrong type");
        }
    }

    private void checkExpressionIfFunction() throws InvalidValue{
        if (m_exprecn.getString().equals("environment")) {
            exprecn temp = new exprecn();
            temp.setValue(environment(m_exprecn.getParams(0).getString()));
            if (temp.getType() == exprecnType.STRING || temp.getType() == exprecnType.BOOL) {
                throw new InvalidValue("In action condition in single value is of the wrong type");
            }
        }
    }

    private void checkTypeValid() throws InvalidValue{
        if(m_exprecn.getType() == exprecnType.BOOL){
            throw new InvalidValue("In action condition in single value is of the wrong type");
        }
        if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == exprecnType.STRING || getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == exprecnType.BOOL){
            throw new InvalidValue("In action condition in single got a wrong type property");
        }
    }

    private void checkEntityAndPropertyExist(){
        if(!isEntityDifenichanExists(m_entity)){
            throw new OBJECT_NOT_EXIST("In action condition in single the entity " + m_entity + " does not exist.");
        }
        if(!getEntityDifenichan(m_entity).getPropertys().containsKey(m_property)){
            throw new OBJECT_NOT_EXIST("In action condition in single the property " + m_property + "of entity " + m_entity +" does not exist.");
        }
    }

    private opertor getOpFromString(String op){
        if(op.equals("=")){
            return opertor.EQUAL;
        } else if(op.equals("!=")){
            return opertor.UNEQUAL;
        } else if (op.equals("bt")){
            return opertor.BIGGER;
        } else if (op.equals("lt")) {
            return opertor.LITTLE;
        }
        return opertor.LITTLE;  //do not get to here
    }

    @Override
    public boolean getBoolValue(Entity entity) throws InvalidValue{
        exprecn propertyValue = new exprecn();
        propertyValue.setValue(entity.getProperty(m_property).getValue());
        if (m_exprecn.getType() == exprecnType.INT || m_exprecn.getType() == exprecnType.FLOAT) {
            return getOpValue(propertyValue, m_exprecn);    //cheak if propertyValue type string of bool later and throw excepcen
        } else if (m_exprecn.getType() == exprecnType.STRING) {
            if (m_exprecn.isFunc()) {
                if (m_exprecn.getValue().equals("environment")) {
                    exprecn temp = new exprecn();
                    temp.setValue(environment(m_exprecn.getParams(0).getString()));
                    if (temp.getType() == exprecnType.INT || temp.getType() == exprecnType.FLOAT) {
                        return getOpValue(propertyValue, temp);    //cheak if propertyValue type string of bool later and throw excepcen
                    }
                    else {
                        throw new InvalidValue("In action condition in single value is of the wrong type");
                    }
                } else if(m_exprecn.getString().equals("random")){
                    exprecn temp = new exprecn();
                    temp.setValue(random(m_exprecn.getParams(0).getInt()));
                    if(temp.getType() == exprecnType.INT) {
                        entity.getProperty(m_property).addToProperty(temp.getInt());
                    }
                    //excepcen
                }
            } else {
                if (entity.isPropertyExists(m_exprecn.getString())) {
                    exprecn temp = new exprecn();
                    temp.setValue(entity.getProperty(m_exprecn.getString()).getValue());
                    if (temp.getType() == exprecnType.INT || temp.getType() == exprecnType.FLOAT) {
                        return getOpValue(propertyValue, temp);    //cheak if propertyValue type string of bool later and trow excepcen
                    }
                    else {
                        throw new InvalidValue("In action condition in single property is of the wrong type");
                    }
                }
                else{
                    throw new InvalidValue("In action condition in single property is of the wrong type");
                }
            }
        } else if (m_exprecn.getType() == exprecnType.BOOL) {
            throw new InvalidValue("In action condition in single property is of the wrong type");
            //return false; //excepcen
        }
        return false;   //do not get to here
    }

    private boolean getOpValue(exprecn exprecn1, exprecn exprecn2)throws InvalidValue{
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
    
    private boolean getIsEqual(exprecn exprecn1, exprecn exprecn2)throws InvalidValue{
        if(exprecn1.getType() == exprecn2.getType() && exprecn1.getType() == exprecnType.INT){
            return exprecn1.getInt() == exprecn2.getInt();
        } else if (exprecn1.getType() == exprecn2.getType() && exprecn1.getType() == exprecnType.FLOAT) {
            return exprecn1.getFloat() == exprecn2.getFloat();
        } else if (exprecn1.getType() == exprecnType.INT && exprecn2.getType() == exprecnType.FLOAT) {
            return exprecn1.getInt() == exprecn2.getFloat();
        } else if (exprecn1.getType() == exprecnType.FLOAT && exprecn2.getType() == exprecnType.INT) {
            return exprecn1.getFloat() == exprecn2.getInt();
        }
        throw new InvalidValue("In action condition in single value is of the wrong type");
        //return false;//excpcen
    }

    private boolean getIsBigger(exprecn exprecn1, exprecn exprecn2)throws InvalidValue{
        if(exprecn1.getType() == exprecn2.getType() && exprecn1.getType() == exprecnType.INT){
            return exprecn1.getInt() > exprecn2.getInt();
        } else if (exprecn1.getType() == exprecn2.getType() && exprecn1.getType() == exprecnType.FLOAT) {
            return exprecn1.getFloat() > exprecn2.getFloat();
        } else if (exprecn1.getType() == exprecnType.INT && exprecn2.getType() == exprecnType.FLOAT) {
            return exprecn1.getInt() > exprecn2.getFloat();
        } else if (exprecn1.getType() == exprecnType.FLOAT && exprecn2.getType() == exprecnType.INT) {
            return exprecn1.getFloat() > exprecn2.getInt();
        }
        throw new InvalidValue("In action condition in single value is of the wrong type");
        //return false;//excpcen
    }
}
