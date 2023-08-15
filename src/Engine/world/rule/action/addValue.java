package Engine.world.rule.action;

import Engine.world.entity.Entity;
import Engine.generated.PRDAction;
import Engine.world.expression.expression;
import Engine.world.expression.expressionType;
import Engine.world.expression.expressionWithFunc;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.Serializable;

import static Engine.utilites.Utilites.*;

public class addValue extends action implements Serializable {  //increase or decrease
    private String m_entity;
    private String m_property;
    private expressionWithFunc m_by;
    int sign;
    private String actionName = "";

    public addValue(String entity, String property, String by) {
        m_entity = entity;
        m_property = property;
        m_by.convertValueInString(by);
    }

    public addValue(PRDAction action) throws InvalidValue {
        m_by = new expressionWithFunc();
        m_by.convertValueInString(action.getBy());
        m_entity = action.getEntity();
        m_property = action.getProperty();
        sign = action.getType().equals("increase") ? 1 : -1;
        actionName = action.getType();
        cheackUserInput();
    }

    private void cheackUserInput() throws InvalidValue{
        checkEntityAndPropertyExist();
        checkTypeValid();
        checkCompatibilityBetweenPropertyAndExpression();
    }

    private void checkCompatibilityBetweenPropertyAndExpression() throws InvalidValue{
        if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() != m_by.getType()){
            if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == expressionType.INT && m_by.getType() == expressionType.FLOAT){
                throw new InvalidValue("In action " + actionName +" the property and the value by are not compatible");
            }
            if(m_by.getType() == expressionType.STRING) {
                if (!m_by.isFunc()) {
                    checkExpressionIfProperty();
                }
                else{
                    checkExpressionIfFunction();
                }
            }
        }
    }

    private void checkExpressionIfProperty() throws InvalidValue{
        if (!getEntityDifenichan(m_entity).getPropertys().containsValue(m_by)) {
            throw new InvalidValue("In action " + actionName + " the value by is of the wrong type");
        }
        if(!(getEntityDifenichan(m_entity).getPropertys().get(m_by).getType() == expressionType.INT)){
            if(!(getEntityDifenichan(m_entity).getPropertys().get(m_by).getType() == expressionType.FLOAT)){
                throw new InvalidValue("In action " + actionName + " the value by is a property of the wrong type");
            }
            if(!(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == expressionType.FLOAT)){
                throw new InvalidValue("In action " + actionName + " the value by is a property of the wrong type");
            }
        }
    }

    private void checkExpressionIfFunction() throws InvalidValue{
        if (m_by.getString().equals("environment")) {
            //exprecn temp = new exprecn();
            //temp.setValue(environment(m_by.getParams(0).getString()));
            expressionType temp = getEnvironmentType(m_by.getParams(0).getString());
            if (temp == expressionType.STRING || temp == expressionType.BOOL) {
                throw new InvalidValue("In action " + actionName + " the value by is of the wrong type");
            }
            if (temp == expressionType.FLOAT && getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == expressionType.INT) {
                throw new InvalidValue("In action " + actionName + " the property and the value by are not compatible");
            }
        }
    }

    private void checkTypeValid() throws InvalidValue{
        if(m_by.getType() == expressionType.BOOL){
            throw new InvalidValue("In action " + actionName + " the value  by is of the wrong type");
        }
        if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == expressionType.STRING || getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == expressionType.BOOL){
            throw new InvalidValue("In action " + actionName + " got a wrong type property");
        }
    }

    private void checkEntityAndPropertyExist(){
        if(!isEntityDifenichanExists(m_entity)){
            throw new OBJECT_NOT_EXIST("In action " + actionName + " the entity " + m_entity + " does not exist.");
        }
        if(!getEntityDifenichan(m_entity).getPropertys().containsKey(m_property)){
            throw new OBJECT_NOT_EXIST("In action " + actionName + " the property " + m_property + " of entity " + m_entity +" does not exist.");
        }
    }

    @Override
    public String getEntityName(){
        return m_entity;
    }
    @Override
    public String getPropertyName(){
        return m_property;
    }
    @Override
    public boolean activateAction(Entity entity)throws InvalidValue{
        if(m_by.getType() == expressionType.INT) {
            entity.getProperty(m_property).addToProperty(sign * m_by.getInt());
        }
        else if (m_by.getType() == expressionType.FLOAT) {
            entity.getProperty(m_property).addToProperty(sign * m_by.getFloat());
        }
        else if(m_by.getType() == expressionType.STRING){
            if(m_by.isFunc()){
                if(m_by.getString().equals("environment")){
                    expression temp = new expression();
                    temp.setValue(environment(m_by.getParams(0).getString()));
                    if(temp.getType() == expressionType.INT) {
                        entity.getProperty(m_property).addToProperty(sign * temp.getInt());
                    }
                    else if (temp.getType() == expressionType.FLOAT) {
                        entity.getProperty(m_property).addToProperty(sign * temp.getFloat());
                    }
                    else{
                        throw new InvalidValue("In action " + actionName + "can't use value by");
                    }
                } else if(m_by.getString().equals("random")){
                    expression temp = new expression();
                    temp.setValue(random(m_by.getParams(0).getInt()));
                    if(temp.getType() == expressionType.INT) {
                        entity.getProperty(m_property).addToProperty(sign * temp.getInt());
                    }
                    throw new InvalidValue("In action " + actionName + "can't use value by");
                }



            }
            else {
                if(entity.isPropertyExists(m_by.getString())){
                    expression temp = new expression();
                    temp.setValue(entity.getProperty(m_by.getString()).getValue());
                    if(m_by.getType() == expressionType.INT) {
                        entity.getProperty(m_property).addToProperty(sign * m_by.getInt());
                    }
                    else if (m_by.getType() == expressionType.FLOAT) {
                        entity.getProperty(m_property).addToProperty(sign * m_by.getFloat());
                    }
                    else{
                        throw new InvalidValue("In action " + actionName + "can't use value by");
                    }
                }
            }

            throw new InvalidValue("In action " + actionName + "can't use value by");
        }
        else if (m_by.getType() == expressionType.BOOL){
            throw new InvalidValue("In action " + actionName + "can't use value by");
        }
        return false;
    }
}
