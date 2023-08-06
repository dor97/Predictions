package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;
import gameEngien.generated.PRDAction;
import gameEngien.property.propertyInterface.PropertyInterface;
import gameEngien.rule.action.actionInterface.ActionInterface;
import gameEngien.utilites.Utilites;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.Serializable;

import static gameEngien.utilites.Utilites.*;
import static gameEngien.utilites.Utilites.getEntityDifenichan;

public class calculation extends action implements Serializable {
    private String m_entity;
    private String m_property;
    //private Entity m_e;
    //private PropertyInterface m_p;
    private String m_value1, m_value2;
    //private typeOfExprecen m_v1Type, m_v2Type;
    //private PropertyInterface m_v1 = null, m_v2 = null;
    private exprecnWithFunc m_v1, m_v2;
    private boolean isMultiply = true;
    exprecn v1Exprecn = new exprecn(), v2Exprecn = new exprecn();

    public calculation(String entity, String property, String v1, String v2){
        m_entity = entity;
        m_property = property;
        m_value1 = v1;
        m_value2 = v2;
    }

    public calculation(PRDAction action) throws InvalidValue{
        m_entity = action.getEntity();
        m_property = action.getProperty();
        m_value1 = action.getPRDMultiply().getArg1();
        m_value2 = action.getPRDMultiply().getArg2();
        if(m_value1 == null){
            action.getPRDDivide().getArg1();
            isMultiply = false;
        }
        if(m_value2 == null){
            action.getPRDDivide().getArg2();
            isMultiply = false;
        }
        m_v1 = new exprecnWithFunc();
        m_v2 = new exprecnWithFunc();
        m_v1.convertValueInString(m_value1);
        m_v2.convertValueInString(m_value2);
        cheackUserInput();
    }

    private void cheackUserInput() throws InvalidValue{
        checkEntityAndPropertyExist();
        checkTypeValid(m_v1);
        checkTypeValid(m_v2);
        checkCompatibilityBetweenPropertyAndExpression(m_v1);
        checkCompatibilityBetweenPropertyAndExpression(m_v2);
    }

    private void checkCompatibilityBetweenPropertyAndExpression(exprecnWithFunc arg) throws InvalidValue{
        if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() != arg.getType()){
            if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == exprecnType.INT && arg.getType() == exprecnType.FLOAT){
                throw new InvalidValue("In action calculation the property and ond of the expression are not compatible");
            }
            if(arg.getType() == exprecnType.STRING) {
                if (!arg.isFunc()) {
                    checkExpressionIfProperty(arg);
                }
                else{
                    checkExpressionIfFunction(arg);
                }
            }
        }
    }

    private void checkExpressionIfProperty(exprecnWithFunc arg) throws InvalidValue{
        if (!getEntityDifenichan(m_entity).getPropertys().containsValue(arg)) {
            throw new InvalidValue("In action calculation there is an expression of the wrong type");
        }
        if(!(getEntityDifenichan(m_entity).getPropertys().get(arg).getType() == exprecnType.INT)){
            if(!(getEntityDifenichan(m_entity).getPropertys().get(arg).getType() == exprecnType.FLOAT)){
                throw new InvalidValue("In action calculation one of the expression is a property of the wrong type");
            }
            if(!(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == exprecnType.FLOAT)){
                throw new InvalidValue("In action calculation one of the expression is a property of the wrong type");
            }
        }
    }

    private void checkExpressionIfFunction(exprecnWithFunc arg) throws InvalidValue{
        if (arg.getString().equals("environment")) {
            exprecn temp = new exprecn();
            temp.setValue(environment(arg.getParams(0).getString()));
            if (temp.getType() == exprecnType.STRING || temp.getType() == exprecnType.BOOL) {
                throw new InvalidValue("In action calculation one of the expression is of the wrong type");
            }
            if (temp.getType() == exprecnType.FLOAT && getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == exprecnType.INT) {
                throw new InvalidValue("In action calculation the property and one of the expression are not compatible");
            }
        }
    }

    private void checkTypeValid(exprecnWithFunc arg) throws InvalidValue{
        if(arg.getType() == exprecnType.BOOL){
            throw new InvalidValue("In action calculation the value  by is of the wrong type");
        }
        if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == exprecnType.STRING || getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == exprecnType.BOOL){
            throw new InvalidValue("In action calculation got a wrong type property");
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

    private void setArgumentValue(exprecn argument, exprecnWithFunc v, Entity entity){
        if(v.getType() == exprecnType.INT){
            argument.setValue(v.getInt());
        } else if (v.getType() == exprecnType.FLOAT) {
            argument.setValue(v.getFloat());
        } else if (v.getType() == exprecnType.STRING) {
            if (v.isFunc()) {
                if(v.getString().equals("environment")) {
                    exprecn temp = new exprecn();
                    temp.setValue(environment(v.getParams(0).getString()));
                    if (temp.getType() == exprecnType.INT) {
                        argument.setValue(temp.getInt());
                    } else if (temp.getType() == exprecnType.FLOAT) {
                        argument.setValue(temp.getFloat());
                    } else {
                        //exepen
                    }
                }
                else if(v.getString().equals("random")){
                    exprecn temp = new exprecn();
                    temp.setValue(random(v.getParams(0).getInt()));
                    if(temp.getType() == exprecnType.INT) {
                        entity.getProperty(m_property).addToProperty(temp.getInt());
                    }
                    //excepcen
                }


            } else {
                if (entity.isPropertyExists(v.getString())) {
                    exprecn temp = new exprecn();
                    temp.setValue(entity.getProperty(v.getString()).getValue());
                    if (temp.getType() == exprecnType.INT) {
                        argument.setValue(temp.getInt());
                    } else if (temp.getType() == exprecnType.FLOAT) {
                        argument.setValue(temp.getFloat());
                    } else {
                        //expcen
                    }
                }
            }
        } else if (v.getType() == exprecnType.BOOL) {
            //excepcen
        }
        //do not get here
    }
    
    @Override
    public boolean activateAction(Entity entity){
        exprecn v1 = new exprecn(), v2 = new exprecn();
        setArgumentValue(v1, m_v1, entity);
        setArgumentValue(v2, m_v2, entity);

        if(v1.getType() == v2.getType() && v2.getType() == exprecnType.INT){
            setProperty(v1.getInt(), v2.getInt(), entity);
            return false;
        }
        if(v1.getType() == v2.getType() && v2.getType() == exprecnType.FLOAT){
            setProperty(v1.getFloat(), v2.getFloat(), entity);
            return false;
        }
        if(v1.getType() == exprecnType.INT && v2.getType() ==exprecnType.FLOAT){
            setProperty(v1.getInt() , v2.getFloat() , entity);
            return false;
        }
        if(v1.getType() == exprecnType.FLOAT && v2.getType() ==exprecnType.INT){
            setProperty(v1.getFloat() , v2.getInt(), entity);
            return false;
        }
        return false;
        //exepcen
    }

    public void setProperty(int v1, int v2, Entity entity){
        if(isMultiply){
            entity.getProperty(m_property).setProperty(v1 * v2);
        }
        else{
            if(v2 == 0){
                throw new ArithmeticException("In action calculation with entity " + m_entity + "and property" + m_property + " ,divided by zero in");
            }
            entity.getProperty(m_property).setProperty(v1 / v2);
        }

    }

    public void setProperty(float v1, float v2, Entity entity){
        if(isMultiply){
            entity.getProperty(m_property).setProperty(v1 * v2);
        }
        else{
            if(v2 == 0){
                throw new ArithmeticException("In action calculation with entity " + m_entity + "and property" + m_property + " ,divided by zero in");
            }
            entity.getProperty(m_property).setProperty(v1 / v2);
        }
    }

    @Override
    public boolean setValues(PropertyInterface v1, PropertyInterface v2) {

        return false;
    }
}
