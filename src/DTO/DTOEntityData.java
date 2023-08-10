package DTO;

import gameEngien.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class DTOEntityData {
    String EntityName;
    int m_amount;
    List<DTOPropertyData> PropertList = new ArrayList<>();

    public DTOEntityData(String name, int amount){
        EntityName = name;
        m_amount = amount;
    }

    public void addProperty(DTOPropertyData property){
        PropertList.add(property);
    }
}
