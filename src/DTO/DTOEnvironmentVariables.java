package DTO;

import gameEngien.rule.action.increase.exprecn;
import gameEngien.rule.action.increase.exprecnType;

public class DTOEnvironmentVariables {

    String m_variableName;
    DTOPropertyType m_variableType;
    double m_highRange, m_lowRange;
    boolean m_haveRange, m_RandomlyInatiated;
    String m_value;


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

    public String getValue(){
        return m_value;
    }

    public void setValue(String value){
        m_value = value;
    }
}
