package gameEngien.entity;

import gameEngien.generated.PRDEntity;
import gameEngien.property.decimalProperty.DecimalProperty;
import gameEngien.property.propertyDifenichan;
import gameEngien.property.propertyInterface.PropertyInterface;
import gameEngien.rule.action.increase.exprecnType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity {
    private String m_name;
    private Map<String, PropertyInterface> m_propertys;


    public Entity(EntityDifenichan entity){
        m_name = entity.getName();
        m_propertys = new HashMap<>();
        for(propertyDifenichan propertyDifenichan : entity.getPropertys()){
            if(propertyDifenichan.getType() == exprecnType.INT){
                m_propertys.put(propertyDifenichan.getName(), new DecimalProperty(propertyDifenichan));
            }
            else{

            }
        }
    }

    public Entity(String name){ //delete
        m_name = name;
    }
    public PropertyInterface getProperty(String name){
        if(m_propertys.containsKey(name)){
            return m_propertys.get(name);
        }
        return null;
    }

    public String getName(){
        return m_name;
    }

    public boolean isPropertyExists(String name){
        return m_propertys.containsKey(name);
    }

    public void addProperty(PropertyInterface propertyToAdd){
        m_propertys.put(propertyToAdd.getName(), propertyToAdd);
    }

    //public void actionOnProperty(PropertyInterface p){
    //    p.addToProperty();
    //}
}
