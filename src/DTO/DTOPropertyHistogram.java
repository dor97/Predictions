package DTO;

public class DTOPropertyHistogram {
    private String m_name;
    private Object m_value;
    private DTOPropertyType m_type;

    public DTOPropertyHistogram(String name, Object value, DTOPropertyType type){
        m_name = name;
        m_value = value;
        m_type = type;
    }

    public String getName(){
        return m_name;
    }

    public Object getValue(){
        return m_value;
    }

    public DTOPropertyType getType(){
        return m_type;
    }
}
