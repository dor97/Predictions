package DTO;

public class DTOEnvironmentVariables {

    private String m_variableName;
    private DTOPropertyType m_variableType;
    private double m_highRange, m_lowRange;
    private boolean m_haveRange, m_RandomlyInatiated;
    private String m_value;


    public DTOEnvironmentVariables(String name, DTOPropertyType type, boolean haveRange){
        m_variableName = name;
        m_variableType = type;
        m_haveRange = haveRange;
    }

    public DTOEnvironmentVariables(String name, DTOPropertyType type, boolean haveRange, double highRange, double lowRange){
        m_variableName = name;
        m_variableType = type;
        m_haveRange = haveRange;
        m_highRange = highRange;
        m_lowRange = lowRange;
    }

    public String getVariableName(){
        return m_variableName;
    }

    public DTOPropertyType getVariableType(){
        return m_variableType;
    }

    public boolean haveRange(){
        return m_haveRange;
    }

    public boolean isRandomlyInatiated(){
        return m_RandomlyInatiated;
    }

    public double getHighRange(){
        return m_highRange;
    }

    public double getLowRange(){
        return m_lowRange;
    }

    public void setValue(String value){
        m_value = value;
    }

    public String getValue(){
        return m_value;
    }
}
