package DTO;

import gameEngien.rule.action.increase.exprecn;
import gameEngien.rule.action.increase.exprecnType;

public class DTOEnvironmentVariables {

    String m_variableName;
    DTOPropertyType m_variableType;
    double m_highRange, m_lowRange;
    boolean m_haveRange, m_RandomlyInatiated;
    exprecn value;


    public DTOEnvironmentVariables(String name, DTOPropertyType type, boolean haveRange, boolean RandomlyInatiated){
        m_variableName = name;
        m_variableType = type;
        m_haveRange = haveRange;
        m_RandomlyInatiated = RandomlyInatiated;
    }

    public DTOEnvironmentVariables(String name, DTOPropertyType type, boolean haveRange, boolean RandomlyInatiated, double highRange, double lowRange){
        m_variableName = name;
        m_variableType = type;
        m_haveRange = haveRange;
        m_RandomlyInatiated = RandomlyInatiated;
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

    public void setValue(Object value){
        if(value instanceof Integer){
            value = (Integer) value;
            m_variableType = DTOPropertyType.INT;
        }
        if(value instanceof Float){
            value = (Float) value;
            m_variableType = DTOPropertyType.FLOAT;
        }
        if(value instanceof String){
            value = (String)value;
            m_variableType = DTOPropertyType.STRING;
        }
        if(value instanceof  Boolean){
            value = (Boolean)value;
            m_variableType = DTOPropertyType.BOOL;
        }
        //exepcen
    }
}
