package gameEngien.entity;

import gameEngien.allReadyExistsException;
import gameEngien.generated.PRDEntity;
import gameEngien.generated.PRDProperty;
import gameEngien.property.propertyDifenichan;
import gameEngien.property.propertyInterface.PropertyInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityDifenichan implements Serializable {
    private String m_name;
    private int m_amount;
    private Map<String, propertyDifenichan> m_propertys;

    public EntityDifenichan(PRDEntity e) throws allReadyExistsException{
        m_name = e.getName();
        m_amount = e.getPRDPopulation();
        m_propertys = new HashMap<>();
        for(PRDProperty p : e.getPRDProperties().getPRDProperty()){
            if(m_propertys.containsKey(p.getPRDName())){
                throw new allReadyExistsException("property varuble " + p.getPRDName() + " all ready exists in entity" + e.getName());
            }
            m_propertys.put(p.getPRDName(), new propertyDifenichan(p));
        }
    }

    public void addProperty(propertyDifenichan propertyToAdd){
        m_propertys.put(propertyToAdd.getName(), propertyToAdd);
    }

    public int getAmount(){
        return m_amount;
    }
    public String getName(){
        return  m_name;
    }

    public Map<String, propertyDifenichan> getPropertys(){
        return  m_propertys;
    }

}
