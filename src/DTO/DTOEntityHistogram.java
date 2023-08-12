package DTO;

import java.util.HashMap;
import java.util.Map;

public class DTOEntityHistogram {
    private String m_name;
    private Map<String, DTOPropertyHistogram> properties = new HashMap<>();

    public DTOEntityHistogram(String name){
        m_name = name;
    }

    public void addProperty(DTOPropertyHistogram propertyHistogram){
        properties.put(propertyHistogram.getName(), propertyHistogram);
    }

    public Map<String, DTOPropertyHistogram> getProperties(){
        return properties;
    }

    public DTOPropertyHistogram getProperty(String property){
        return properties.get(property);
    }

    public String getName(){
        return m_name;
    }
}
