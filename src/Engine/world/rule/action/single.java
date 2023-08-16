package Engine.world.rule.action;

import Engine.world.entity.Entity;
import Engine.generated.PRDCondition;
import Engine.world.expression.expression;
import Engine.world.expression.expressionType;
import Engine.world.expression.expressionWithFunc;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.Serializable;

import static Engine.utilites.Utilites.*;
import static Engine.utilites.Utilites.getEntityDifenichan;

public class single implements subCondition, Serializable {

    enum opertor{ EQUAL, UNEQUAL, BIGGER, LITTLE}
    private String m_entity;
    private String m_property;
    private opertor m_op;
    private expressionWithFunc m_exprecn;

    public single(PRDCondition condition) throws InvalidValue{
        m_entity = condition.getEntity();
        m_property = condition.getProperty();
        m_op = getOpFromString(condition.getOperator());
        m_exprecn = new expressionWithFunc();
        m_exprecn.convertValueInString(condition.getValue());
        cheackUserInput();
    }

    private void cheackUserInput() throws InvalidValue {
        checkEntityAndPropertyExist();
        checkTypeValid();
        //checkCompatibilityBetweenPropertyAndExpression();   //disable because it is exercise requirements
    }

    private void checkCompatibilityBetweenPropertyAndExpression() throws InvalidValue{
        if(m_exprecn.getType() == expressionType.STRING) {
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
        if(!(getEntityDifenichan(m_entity).getPropertys().get(m_exprecn).getType() == expressionType.INT || getEntityDifenichan(m_entity).getPropertys().get(m_exprecn).getType() == expressionType.FLOAT)){
            throw new InvalidValue("In action condition in single value is a property of the wrong type");
        }
    }

    private void checkExpressionIfFunction() throws InvalidValue{
        if (m_exprecn.getString().equals("environment")) {
            //exprecn temp = new exprecn();
            //temp.setValue(environment(m_exprecn.getParams(0).getString()));
            expressionType temp = getEnvironmentType(m_exprecn.getParams(0).getString());
            if (temp == expressionType.STRING || temp == expressionType.BOOL) {
                throw new InvalidValue("In action condition in single value is of the wrong type");
            }
        }
    }

    private void checkTypeValid() throws InvalidValue{
        if(m_exprecn.getType() == expressionType.BOOL){
            throw new InvalidValue("In action condition in single value is of the wrong type");
        }
        if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == expressionType.STRING || getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == expressionType.BOOL){
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
        expression propertyValue = new expression();
        propertyValue.setValue(entity.getProperty(m_property).getValue());
        if (m_exprecn.getType() == expressionType.INT || m_exprecn.getType() == expressionType.FLOAT) {
            return getOpValue(propertyValue, m_exprecn);    //cheak if propertyValue type string of bool later and throw excepcen
        } else if (m_exprecn.getType() == expressionType.STRING) {
            if (m_exprecn.isFunc()) {
                if (m_exprecn.getValue().equals("environment")) {
                    expression temp = new expression();
                    temp.setValue(environment(m_exprecn.getParams(0).getString()));
                    if (temp.getType() == expressionType.INT || temp.getType() == expressionType.FLOAT) {
                        return getOpValue(propertyValue, temp);    //cheak if propertyValue type string of bool later and throw excepcen
                    }
                    else {
                        throw new InvalidValue("In action condition in single value is of the wrong type");
                    }
                } else if(m_exprecn.getString().equals("random")){
                    expression temp = new expression();
                    temp.setValue(random(m_exprecn.getParams(0).getInt()));
                    if(temp.getType() == expressionType.INT) {
                        entity.getProperty(m_property).addToProperty(temp.getInt());
                    }
                    else{
                        throw new InvalidValue("In action condition in single value is of the wrong type");
                    }
                }
            } else {
                if (entity.isPropertyExists(m_exprecn.getString())) {
                    expression temp = new expression();
                    temp.setValue(entity.getProperty(m_exprecn.getString()).getValue());
                    if (temp.getType() == expressionType.INT || temp.getType() == expressionType.FLOAT) {
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
        } else if (m_exprecn.getType() == expressionType.BOOL) {
            throw new InvalidValue("In action condition in single property is of the wrong type");
            //return false; //excepcen
        }
        return false;   //do not get to here
    }

    private boolean getOpValue(expression expression1, expression expression2)throws InvalidValue{
        if(m_op == opertor.EQUAL){
            return getIsEqual(expression1, expression2);
        } else if (m_op == opertor.UNEQUAL) {
            return !getIsEqual(expression1, expression2);
        } else if (m_op == opertor.BIGGER) {
            return getIsBigger(expression1, expression2);
        } else if (m_op == opertor.LITTLE) {
            return getIsBigger(expression2, expression1); //swap between exprecn1 and eprecn2
        }
        return  false; //do not get here
    }
    
    private boolean getIsEqual(expression expression1, expression expression2)throws InvalidValue{
        if(expression1.getType() == expression2.getType() && expression1.getType() == expressionType.INT){
            return expression1.getInt() == expression2.getInt();
        } else if (expression1.getType() == expression2.getType() && expression1.getType() == expressionType.FLOAT) {
            return expression1.getFloat() == expression2.getFloat();
        } else if (expression1.getType() == expressionType.INT && expression2.getType() == expressionType.FLOAT) {
            return expression1.getInt() == expression2.getFloat();
        } else if (expression1.getType() == expressionType.FLOAT && expression2.getType() == expressionType.INT) {
            return expression1.getFloat() == expression2.getInt();
        }
        throw new InvalidValue("In action condition in single value is of the wrong type");
        //return false;//excpcen
    }

    private boolean getIsBigger(expression expression1, expression expression2)throws InvalidValue{
        if(expression1.getType() == expression2.getType() && expression1.getType() == expressionType.INT){
            return expression1.getInt() > expression2.getInt();
        } else if (expression1.getType() == expression2.getType() && expression1.getType() == expressionType.FLOAT) {
            return expression1.getFloat() > expression2.getFloat();
        } else if (expression1.getType() == expressionType.INT && expression2.getType() == expressionType.FLOAT) {
            return expression1.getInt() > expression2.getFloat();
        } else if (expression1.getType() == expressionType.FLOAT && expression2.getType() == expressionType.INT) {
            return expression1.getFloat() > expression2.getInt();
        }
        throw new InvalidValue("In action condition in single value is of the wrong type");
        //return false;//excpcen
    }
}
