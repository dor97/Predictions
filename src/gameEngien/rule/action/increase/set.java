package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;
import gameEngien.generated.PRDAction;
import gameEngien.property.Property;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.Serializable;

import static gameEngien.utilites.Utilites.*;
import static gameEngien.utilites.Utilites.getEntityDifenichan;

public class set extends action implements Serializable {

    private String m_entity;
    private String m_property;
    private exprecnWithFunc m_value;

    public set(PRDAction action) throws InvalidValue{
        m_value = new exprecnWithFunc();
        m_entity = action.getEntity();
        m_property = action.getProperty();
        m_value.convertValueInString(action.getValue());
        cheackUserInput();
    }

    private void cheackUserInput() throws InvalidValue {
        checkEntityAndPropertyExist();
        checkCompatibilityBetweenPropertyAndExpression();
    }

    private void checkCompatibilityBetweenPropertyAndExpression() throws InvalidValue{
        if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() != m_value.getType()){
            if(!(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == exprecnType.FLOAT && m_value.getType() == exprecnType.INT)){
                if(m_value.getType() == exprecnType.STRING) {
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
        if (!getEntityDifenichan(m_entity).getPropertys().containsValue(m_value)) {  //value is string
            throw new InvalidValue("In action set the value is of the wrong type");
        }
        if(getEntityDifenichan(m_entity).getPropertys().get(m_value).getType() != getEntityDifenichan(m_entity).getPropertys().get(m_property).getType()){
            if(!(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == exprecnType.FLOAT && getEntityDifenichan(m_entity).getPropertys().get(m_value).getType() == exprecnType.INT)){
                throw new InvalidValue("In action set the value is a property of the wrong type");
            }
        }
    }

    private void checkExpressionIfFunction() throws InvalidValue {
        if (m_value.getString().equals("environment")) {
            exprecn temp = new exprecn();
            temp.setValue(environment(m_value.getParams(0).getString()));
            if (getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() != m_value.getType()) {
                if (!(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == exprecnType.FLOAT && m_value.getType() == exprecnType.INT)) {
                    throw new InvalidValue("In action set the value is of the wrong type");
                }
            }
        }
    }

    private void checkEntityAndPropertyExist(){
        if(!isEntityDifenichanExists(m_entity)){
            throw new OBJECT_NOT_EXIST("In action set the entity " + m_entity + " does not exist.");
        }
        if(!getEntityDifenichan(m_entity).getPropertys().containsValue(m_property)){
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

        if(m_value.getType() == exprecnType.INT){
            i_entity.getProperty(m_property).setProperty(m_value.getInt());
        } else if (m_value.getType() == exprecnType.FLOAT) {
            i_entity.getProperty(m_property).setProperty(m_value.getFloat());
        } else if (m_value.getType() == exprecnType.BOOL) {
            i_entity.getProperty(m_property).setProperty(m_value.getBool());
        }else {
            if (m_value.isFunc()){
                if(m_value.getValue().equals("environment")){
                    exprecn temp = new exprecn();
                    temp.setValue(environment(m_value.getParams(0).getString()));
                    if(temp.getType() == exprecnType.INT) {
                        i_entity.getProperty(m_property).setProperty(temp.getInt());
                    }
                    else if (temp.getType() == exprecnType.FLOAT) {
                        i_entity.getProperty(m_property).setProperty(temp.getFloat());
                    }
                    else if (temp.getType() == exprecnType.BOOL){
                        i_entity.getProperty(m_property).setProperty(temp.getBool());
                    }
                    else{
                        i_entity.getProperty(m_property).setProperty(temp.getString());
                    }
                }
                else if(m_value.getString().equals("random")){
                    exprecn temp = new exprecn();
                    temp.setValue(random(m_value.getParams(0).getInt()));
                    if(temp.getType() == exprecnType.INT) {
                        i_entity.getProperty(m_property).addToProperty(temp.getInt());
                    }
                    //excepcen
                }



            }
            else {
                if(i_entity.isPropertyExists(m_property)){
                    exprecn temp = new exprecn();
                    temp.setValue(i_entity.getProperty(m_property).getValue());
                    if(m_value.getType() == exprecnType.INT) {
                        i_entity.getProperty(m_property).setProperty(m_value.getInt());
                    } else if (m_value.getType() == exprecnType.FLOAT) {
                        i_entity.getProperty(m_property).setProperty(m_value.getFloat());
                    } else if (m_value.getType() == exprecnType.BOOL) {
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
