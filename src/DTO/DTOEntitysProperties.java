package DTO;

import java.util.List;

public class DTOEntitysProperties {
    private String m_name;
    private List<DTOProperty> m_properties;

    public DTOEntitysProperties(String name, List<DTOProperty> properties){
        m_name = name;
        m_properties = properties;
    }

    public String getName(){
        return m_name;
    }

    public List<DTOProperty> getProperties(){
        return m_properties;
    }

}
