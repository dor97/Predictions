package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;
import gameEngien.entity.EntityDifenichan;
import gameEngien.generated.PRDAction;
import gameEngien.property.propertyInterface.PropertyInterface;
import gameEngien.rule.action.actionInterface.ActionInterface;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.Serializable;

import static gameEngien.utilites.Utilites.*;

public class Increase extends action implements Serializable {
    private String m_entity;
    private String m_property;
    private exprecnWithFunc m_by;
    //private Entity m_e;
    //private PropertyInterface m_p;

    public Increase(String entity, String property, String by) {
        m_entity = entity;
        m_property = property;
        m_by.convertValueInString(by);
    }

    public Increase(PRDAction action) throws InvalidValue {
        m_by = new exprecnWithFunc();
        m_by.convertValueInString(action.getBy());
        m_entity = action.getEntity();
        m_property = action.getProperty();
        cheackUserInput();
    }

    private void cheackUserInput() throws InvalidValue{
        checkEntityAndPropertyExist();
        checkTypeValid();
        checkCompatibilityBetweenPropertyAndExpression();
    }

    private void checkCompatibilityBetweenPropertyAndExpression() throws InvalidValue{
        if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() != m_by.getType()){
            if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == exprecnType.INT && m_by.getType() == exprecnType.FLOAT){
                throw new InvalidValue("In action increase the property and the value by are not compatible");
            }
            if(m_by.getType() == exprecnType.STRING) {
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
            throw new InvalidValue("In action increase the value by is of the wrong type");
        }
        if(!(getEntityDifenichan(m_entity).getPropertys().get(m_by).getType() == exprecnType.INT)){
            if(!(getEntityDifenichan(m_entity).getPropertys().get(m_by).getType() == exprecnType.FLOAT)){
                throw new InvalidValue("In action increase the value by is a property of the wrong type");
            }
            if(!(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == exprecnType.FLOAT)){
                throw new InvalidValue("In action increase the value by is a property of the wrong type");
            }
        }
    }

    private void checkExpressionIfFunction() throws InvalidValue{
        if (m_by.getString().equals("environment")) {
            exprecn temp = new exprecn();
            temp.setValue(environment(m_by.getParams(0).getString()));
            if (temp.getType() == exprecnType.STRING || temp.getType() == exprecnType.BOOL) {
                throw new InvalidValue("In action increase the value by is of the wrong type");
            }
            if (temp.getType() == exprecnType.FLOAT && getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == exprecnType.INT) {
                throw new InvalidValue("In action increase the property and the value by are not compatible");
            }
        }
    }

    private void checkTypeValid() throws InvalidValue{
        if(m_by.getType() == exprecnType.BOOL){
            throw new InvalidValue("In action increase the value  by is of the wrong type");
        }
        if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == exprecnType.STRING || getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == exprecnType.BOOL){
            throw new InvalidValue("In action increase got a wrong type property");
        }
    }

    private void checkEntityAndPropertyExist(){
        if(!isEntityDifenichanExists(m_entity)){
            throw new OBJECT_NOT_EXIST("In action increase the entity " + m_entity + " does not exist.");
        }
        if(!getEntityDifenichan(m_entity).getPropertys().containsValue(m_property)){
            throw new OBJECT_NOT_EXIST("In action increase the property " + m_property + "of entity " + m_entity +" does not exist.");
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
    public boolean activateAction(Entity entity){
        if(m_by.getType() == exprecnType.INT) {
            entity.getProperty(m_property).addToProperty(m_by.getInt());
        }
        else if (m_by.getType() == exprecnType.FLOAT) {
            entity.getProperty(m_property).addToProperty(m_by.getFloat());
        }
        else if(m_by.getType() == exprecnType.STRING){
            if(m_by.isFunc()){
                if(m_by.getString().equals("environment")){
                    exprecn temp = new exprecn();
                    temp.setValue(environment(m_by.getParams(0).getString()));
                    if(temp.getType() == exprecnType.INT) {
                        entity.getProperty(m_property).addToProperty(temp.getInt());
                    }
                    else if (temp.getType() == exprecnType.FLOAT) {
                        entity.getProperty(m_property).addToProperty(temp.getFloat());
                    }
                    else{
                        //exepen
                        }
                } else if(m_by.getString().equals("random")){
                    exprecn temp = new exprecn();
                    temp.setValue(random(m_by.getParams(0).getInt()));
                    if(temp.getType() == exprecnType.INT) {
                        entity.getProperty(m_property).addToProperty(temp.getInt());
                    }
                    //excepcen
                }



            }
            else {
                if(entity.isPropertyExists(m_by.getString())){
                    exprecn temp = new exprecn();
                    temp.setValue(entity.getProperty(m_by.getString()).getValue());
                    if(m_by.getType() == exprecnType.INT) {
                        entity.getProperty(m_property).addToProperty(m_by.getInt());
                    }
                    else if (m_by.getType() == exprecnType.FLOAT) {
                        entity.getProperty(m_property).addToProperty(m_by.getFloat());
                    }
                    else{
                        //expcen
                    }
                }
            }

            //expcen
        }
        else if (m_by.getType() == exprecnType.BOOL){
            //excepcen
        }
        return false;
    }
}
