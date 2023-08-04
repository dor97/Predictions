package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;
import gameEngien.generated.PRDAction;
import gameEngien.property.propertyInterface.PropertyInterface;
import gameEngien.rule.action.actionInterface.ActionInterface;

import static gameEngien.utilites.Utilites.environment;

public class Increase extends action {
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

    public Increase(PRDAction action) {
        m_by.convertValueInString(action.getBy());
        m_entity = action.getEntity();
        m_property = action.getProperty();
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
                if(m_by.getValue() == "environment"){
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
