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
import static Engine.utilites.Utilites.getEntityDifenichan;

public class set extends action implements Serializable {

    private String m_entity;
    private String m_property;
    private expressionWithFunc m_value;

    public set(PRDAction action) throws InvalidValue{
        m_value = new expressionWithFunc();
        m_entity = action.getEntity();
        m_property = action.getProperty();
        m_value.convertValueInString(action.getValue());
        actionName = action.getType();
        cheackUserInput();
    }

    private void cheackUserInput() throws InvalidValue {
        checkEntityAndPropertyExist();
        //checkCompatibilityBetweenPropertyAndExpression(); //disable because if exercise requirements
    }

    private void checkCompatibilityBetweenPropertyAndExpression() throws InvalidValue{
        if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() != m_value.getType()){
            if(!(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == expressionType.FLOAT && m_value.getType() == expressionType.INT)){
                if(m_value.getType() == expressionType.STRING) {
                    if (!m_value.isFunc()) {
                        checkExpressionIfProperty();
                    }
                    else{
                        checkExpressionIfFunction();
                    }
                }
                else {
                    throw new InvalidValue("In action set the property and value are not compatible");
                }
            }

        }
    }

    private void checkExpressionIfProperty() throws InvalidValue{
        if (!getEntityDifenichan(m_entity).getPropertys().containsKey(m_value.getString())) {  //value is string
            throw new InvalidValue("In action set the value is of the wrong type");
        }
        if(getEntityDifenichan(m_entity).getPropertys().get(m_value.getString()).getType() != getEntityDifenichan(m_entity).getPropertys().get(m_property).getType()){
            if(!(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == expressionType.FLOAT && getEntityDifenichan(m_entity).getPropertys().get(m_value).getType() == expressionType.INT)){
                throw new InvalidValue("In action set the value is a property of the wrong type");
            }
        }
    }

    private void checkExpressionIfFunction() throws InvalidValue {
        if (m_value.getString().equals("environment")) {
            //exprecn temp = new exprecn();
            //temp.setValue(environment(m_value.getParams(0).getString()));
            expressionType temp = getEnvironmentType(m_value.getParams(0).getString());
            if (getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() != temp) {
                if (!(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == expressionType.FLOAT && m_value.getType() == expressionType.INT)) {
                    throw new InvalidValue("In action set the value is of the wrong type");
                }
            }
        }
    }

    private void checkEntityAndPropertyExist(){
        if(!isEntityDifenichanExists(m_entity)){
            throw new OBJECT_NOT_EXIST("In action set the entity " + m_entity + " does not exist.");
        }
        if(!getEntityDifenichan(m_entity).getPropertys().containsKey(m_property)){
            throw new OBJECT_NOT_EXIST("In action set the property " + m_property + "of entity " + m_entity +" does not exist.");
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
    public boolean activateAction(Entity i_entity) {
        if(m_value.getType() == expressionType.INT){
            i_entity.getProperty(m_property).setProperty(m_value.getInt());
        } else if (m_value.getType() == expressionType.FLOAT) {
            i_entity.getProperty(m_property).setProperty(m_value.getFloat());
        } else if (m_value.getType() == expressionType.BOOL) {
            i_entity.getProperty(m_property).setProperty(m_value.getBool());
        }else {
            if (m_value.isFunc()){
                if(m_value.getValue().equals("environment")){
                    expression temp = new expression();
                    temp.setValue(environment(m_value.getParams(0).getString()));
                    if(temp.getType() == expressionType.INT) {
                        i_entity.getProperty(m_property).setProperty(temp.getInt());
                    } else if (temp.getType() == expressionType.FLOAT) {
                        i_entity.getProperty(m_property).setProperty(temp.getFloat());
                    } else if (temp.getType() == expressionType.BOOL){
                        i_entity.getProperty(m_property).setProperty(temp.getBool());
                    } else{
                        i_entity.getProperty(m_property).setProperty(temp.getString());
                    }
                }
                else if(m_value.getString().equals("random")){
                    expression temp = new expression();
                    temp.setValue(random(m_value.getParams(0).getInt()));
                    if(temp.getType() == expressionType.INT) {
                        i_entity.getProperty(m_property).addToProperty(temp.getInt());
                    }
                    //excepcen
                }
            }
            else {
                if(i_entity.isPropertyExists(m_property)){
                    expression temp = new expression();
                    temp.setValue(i_entity.getProperty(m_property).getValue());
                    if(m_value.getType() == expressionType.INT) {
                        i_entity.getProperty(m_property).setProperty(m_value.getInt());
                    } else if (m_value.getType() == expressionType.FLOAT) {
                        i_entity.getProperty(m_property).setProperty(m_value.getFloat());
                    } else if (m_value.getType() == expressionType.BOOL) {
                        i_entity.getProperty(m_property).setProperty(m_value.getBool());
                    } else{
                        i_entity.getProperty(m_property).setProperty(m_value.getString());
                    }
                }
                else {
                    i_entity.getProperty(m_property).setProperty(m_value.getString());
                }
            }
        }

        return false;
    }
}
