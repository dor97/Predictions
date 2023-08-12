package DTO;

public class DTOEnvironmentVariablesValues {

    private String m_name;
    private Object m_value;

    public DTOEnvironmentVariablesValues(String name, Object value){
        m_name = name;
        m_value = value;
    }

    public String getName(){
        return m_name;
    }

    public Object getValue(){
        return m_value;
    }
}
