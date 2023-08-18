package Engine.world.rule.action;

import Engine.world.entity.Entity;
import Engine.generated.PRDAction;
import Engine.world.entity.property.PropertyInterface;
import Engine.world.expression.expression;
import Engine.world.expression.expressionType;
import Engine.world.expression.expressionWithFunc;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.Serializable;

import static Engine.utilites.Utilites.*;
import static Engine.utilites.Utilites.getEntityDifenichan;

public class calculation extends action implements Serializable {
    private String m_entity;
    private String m_property;
    private String m_value1, m_value2;
    private expressionWithFunc m_v1, m_v2;
    private boolean isMultiply = true;
    expression v1Expression = new expression(), v2Expression = new expression();

    public calculation(String entity, String property, String v1, String v2){
        m_entity = entity;
        m_property = property;
        m_value1 = v1;
        m_value2 = v2;
    }

    public calculation(PRDAction action) throws InvalidValue{
        m_entity = action.getEntity();
        m_property = action.getResultProp();
        if(action.getPRDMultiply() != null){
            m_value1 = action.getPRDMultiply().getArg1();
            m_value2 = action.getPRDMultiply().getArg2();
        }else{
            m_value1 = action.getPRDDivide().getArg1();
            m_value2 = action.getPRDDivide().getArg2();
            isMultiply = false;
        }
        m_v1 = new expressionWithFunc();
        m_v2 = new expressionWithFunc();
        m_v1.convertValueInString(m_value1);
        m_v2.convertValueInString(m_value2);
        actionName = action.getType();
        cheackUserInput();
    }

    private void cheackUserInput() throws InvalidValue{
        checkEntityAndPropertyExist();
        checkTypeValid(m_v1);
        checkTypeValid(m_v2);
        checkCompatibilityBetweenPropertyAndExpression(m_v1);
        checkCompatibilityBetweenPropertyAndExpression(m_v2);
    }

    private void checkCompatibilityBetweenPropertyAndExpression(expressionWithFunc arg) throws InvalidValue{
        if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() != arg.getType()){
            if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == expressionType.INT && arg.getType() == expressionType.FLOAT){
                throw new InvalidValue("In action calculation the property and ond of the expression are not compatible");
            }
            if(arg.getType() == expressionType.STRING) {
                if (!arg.isFunc()) {
                    checkExpressionIfProperty(arg);
                }
                else{
                    checkExpressionIfFunction(arg);
                }
            }
        }
    }

    private void checkExpressionIfProperty(expressionWithFunc arg) throws InvalidValue{
        if (!getEntityDifenichan(m_entity).getPropertys().containsKey(arg.getString())) {
            throw new InvalidValue("In action calculation there is an expression of the wrong type");
        }
        if(!(getEntityDifenichan(m_entity).getPropertys().get(arg.getString()).getType() == expressionType.INT)){
            if(!(getEntityDifenichan(m_entity).getPropertys().get(arg.getString()).getType() == expressionType.FLOAT)){
                throw new InvalidValue("In action calculation one of the expression is a property of the wrong type");
            }
            if(!(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == expressionType.FLOAT)){
                throw new InvalidValue("In action calculation one of the expression is a property of the wrong type");
            }
        }
    }

    private void checkExpressionIfFunction(expressionWithFunc arg) throws InvalidValue{
        if (arg.getString().equals("environment")) {
            //exprecn temp = new exprecn();
            //temp.setValue(environment(arg.getParams(0).getString()));
            expressionType temp = getEnvironmentType(arg.getParams(0).getString());
            if (temp == expressionType.STRING || temp == expressionType.BOOL) {
                throw new InvalidValue("In action calculation one of the expression is of the wrong type");
            }
            if (temp == expressionType.FLOAT && getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == expressionType.INT) {
                throw new InvalidValue("In action calculation the property and one of the expression are not compatible");
            }
        }
    }

    private void checkTypeValid(expressionWithFunc arg) throws InvalidValue{
        if(arg.getType() == expressionType.BOOL){
            throw new InvalidValue("In action calculation the value  by is of the wrong type");
        }
        if(getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == expressionType.STRING || getEntityDifenichan(m_entity).getPropertys().get(m_property).getType() == expressionType.BOOL){
            throw new InvalidValue("In action calculation got a wrong type property");
        }
    }

    private void checkEntityAndPropertyExist(){
        if(!isEntityDifenichanExists(m_entity)){
            throw new OBJECT_NOT_EXIST("In action calculation the entity " + m_entity + " does not exist.");
        }
        if(!getEntityDifenichan(m_entity).getPropertys().containsKey(m_property)){
            throw new OBJECT_NOT_EXIST("In action calculation the property " + m_property + " of entity " + m_entity +" does not exist.");
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

    private void setArgumentValue(expression argument, expressionWithFunc v, Entity entity) throws InvalidValue{
        if(v.getType() == expressionType.INT){
            argument.setValue(v.getInt());
        } else if (v.getType() == expressionType.FLOAT) {
            argument.setValue(v.getFloat());
        } else if (v.getType() == expressionType.STRING) {
            if (v.isFunc()) {
                if(v.getString().equals("environment")) {
                    expression temp = new expression();
                    temp.setValue(environment(v.getParams(0).getString()));
                    if (temp.getType() == expressionType.INT) {
                        argument.setValue(temp.getInt());
                    } else if (temp.getType() == expressionType.FLOAT) {
                        argument.setValue(temp.getFloat());
                    } else {
                        throw new InvalidValue("In action calculation can't use value arg");
                    }
                }
                else if(v.getString().equals("random")){
                    expression temp = new expression();
                    temp.setValue(random(v.getParams(0).getInt()));
                    if(temp.getType() == expressionType.INT) {
                        entity.getProperty(m_property).addToProperty(temp.getInt());
                    }
                    else {
                        throw new InvalidValue("In action calculation can't use value arg");
                    }
                }


            } else {
                if (entity.isPropertyExists(v.getString())) {
                    expression temp = new expression();
                    temp.setValue(entity.getProperty(v.getString()).getValue());
                    if (temp.getType() == expressionType.INT) {
                        argument.setValue(temp.getInt());
                    } else if (temp.getType() == expressionType.FLOAT) {
                        argument.setValue(temp.getFloat());
                    } else {
                        throw new InvalidValue("In action calculation can't use value arg");
                    }
                }
                else {
                    throw new InvalidValue("In action calculation can't use value arg");
                }
            }
        } else if (v.getType() == expressionType.BOOL) {
            throw new InvalidValue("In action calculation can't use value arg");
        }
        //do not get here
    }
    
    @Override
    public boolean activateAction(Entity entity) throws InvalidValue{
        expression v1 = new expression(), v2 = new expression();
        setArgumentValue(v1, m_v1, entity);
        setArgumentValue(v2, m_v2, entity);

        if(v1.getType() == v2.getType() && v2.getType() == expressionType.INT){
            setProperty(v1.getInt(), v2.getInt(), entity);
            return false;
        }
        if(v1.getType() == v2.getType() && v2.getType() == expressionType.FLOAT){
            setProperty(v1.getFloat(), v2.getFloat(), entity);
            return false;
        }
        if(v1.getType() == expressionType.INT && v2.getType() == expressionType.FLOAT){
            setProperty(v1.getInt() , v2.getFloat() , entity);
            return false;
        }
        if(v1.getType() == expressionType.FLOAT && v2.getType() == expressionType.INT){
            setProperty(v1.getFloat() , v2.getInt(), entity);
            return false;
        }
        throw new InvalidValue("In action calculation can't use value arg");
        //return false;
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
            if(v1 % v2 == 0) {
                entity.getProperty(m_property).setProperty(v1 / v2);
            }
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
