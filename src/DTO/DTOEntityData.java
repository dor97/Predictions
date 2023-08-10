package DTO;

import gameEngien.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class DTOEntityData {
    private String EntityName;
    private int m_amount;
    private List<DTOPropertyData> PropertList = new ArrayList<>();

    public DTOEntityData(String name, int amount){
        EntityName = name;
        m_amount = amount;
    }

    public void addProperty(DTOPropertyData property){
        PropertList.add(property);
    }

    public String getName(){
        return EntityName;
    }

    public int getAmount(){
        return m_amount;
    }

    public List<DTOPropertyData> getPropertList(){
        return PropertList;
    }
}
