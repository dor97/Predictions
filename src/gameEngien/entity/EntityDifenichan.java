package gameEngien.entity;

import gameEngien.generated.PRDEntity;
import gameEngien.generated.PRDProperty;
import gameEngien.property.propertyDifenichan;
import gameEngien.property.propertyInterface.PropertyInterface;

import java.util.List;

public class EntityDifenichan {
    private String m_name;
    private int m_amount;
    private List<propertyDifenichan> m_propertys;

    public EntityDifenichan(PRDEntity e){
        m_name = e.getName();
        m_amount = e.getPRDPopulation();
        for(PRDProperty p : e.getPRDProperties().getPRDProperty()){
            m_propertys.add(new propertyDifenichan(p));
        }
    }

    public void addProperty(propertyDifenichan propertyToAdd){
        m_propertys.add(propertyToAdd);
    }

    public int getAmount(){
        return m_amount;
    }
    public String getName(){
        return  m_name;
    }

    public List<propertyDifenichan> getPropertys(){
        return  m_propertys;
    }

}
