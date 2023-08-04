package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;
import gameEngien.generated.PRDAction;
import gameEngien.property.propertyInterface.PropertyInterface;
import gameEngien.rule.action.actionInterface.ActionInterface;
import gameEngien.utilites.Utilites;

import static gameEngien.utilites.Utilites.environment;

public class calculation extends action {
    private String m_entity;
    private String m_property;
    private Entity m_e;
    private PropertyInterface m_p;
    private String m_value1, m_value2;
    private typeOfExprecen m_v1Type, m_v2Type;
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

    public calculation(PRDAction action) {
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
        m_v1.convertValueInString(m_value1);
        m_v2.convertValueInString(m_value2);
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
                if(v.getString() == "environment") {
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
    public void activateAction(Entity entity){
        exprecn v1 = new exprecn(), v2 = new exprecn();
        setArgumentValue(v1, m_v1, entity);
        setArgumentValue(v2, m_v2, entity);

        if(v1.getType() == v2.getType() && v2.getType() == exprecnType.INT){
            setProperty(v1.getInt(), v2.getInt());
            return;
        }
        if(v1.getType() == v2.getType() && v2.getType() == exprecnType.FLOAT){
            setProperty(v1.getFloat(), v2.getFloat());
            return;
        }
        if(v1.getType() == exprecnType.INT && v2.getType() ==exprecnType.FLOAT){
            setProperty(v1.getInt() , v2.getFloat());
            return;
        }
        if(v1.getType() == exprecnType.FLOAT && v2.getType() ==exprecnType.INT){
            setProperty(v1.getFloat() , v2.getInt());
            return;
        }
        //exepcen
    }

    public void setProperty(int v1, int v2){
        if(isMultiply){
            m_p.setProperty(v1 * v2);
        }
        else{
            m_p.setProperty(v1 / v2);
        }

    }

    public void setProperty(float v1, float v2){
        if(isMultiply){
            m_p.setProperty(v1 * v2);
        }
        else{
            m_p.setProperty(v1 / v2);
        }
    }

    @Override
    public boolean setValues(PropertyInterface v1, PropertyInterface v2) {

        return false;
    }
}
