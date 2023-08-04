package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;
import gameEngien.generated.PRDAction;
import gameEngien.property.Property;

import static gameEngien.utilites.Utilites.environment;

public class set extends action{

    private String m_entity;
    private String m_property;
    private exprecnWithFunc m_value;

    public set(PRDAction action){
        m_value = new exprecnWithFunc();
        m_entity = action.getEntity();
        m_property = action.getProperty();
        m_value.convertValueInString(action.getValue());
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
                if(m_value.getValue() == "environment"){
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
                else {
                    //TODO: add condition to other function;
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
