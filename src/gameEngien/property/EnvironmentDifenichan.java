package gameEngien.property;

import DTO.DTOEnvironmentVariables;
import DTO.DTOPropertyData;
import DTO.DTOPropertyType;
import gameEngien.generated.PRDEnvProperty;
import gameEngien.generated.PRDProperty;
import gameEngien.rule.action.increase.exprecn;
import gameEngien.rule.action.increase.exprecnType;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

public class EnvironmentDifenichan {
    private String m_name;
    private exprecnType m_type;
    private double m_lowRange, m_highRang;
    private boolean m_randomlyIneceat = true;
    private exprecn m_init;
    private boolean m_haveRange = false;

    public EnvironmentDifenichan(PRDEnvProperty p){
        m_name = p.getPRDName();
        m_type = myType(p.getType());
        if(p.getPRDRange() != null) {
            m_haveRange = true;
            m_lowRange = p.getPRDRange().getFrom();
            m_highRang = p.getPRDRange().getTo();
            if(m_highRang < m_lowRange){
                //excepcen
            }
        }
        //m_randomlyIneceat = p.getPRDValue().isRandomInitialize();
        //if(m_randomlyIneceat == true){
        //    return;
        //}
        m_init = new exprecn();
        //m_init.convertValueInString(p.getPRDValue().getInit());
        //if(m_type != m_init.getType()){
        //   //exepcen
        //}

    }

    public exprecnType getType(){
        return m_type;
    }

    public double getLowRange(){
        return m_lowRange;
    }

    public double getHighRange(){
        return m_highRang;
    }

    public exprecn getInit(){
        return m_init;
    }

    public boolean isRandom(){
        return m_randomlyIneceat;
    }

    public String getName(){
        return m_name;
    }

    private exprecnType myType(String t){
        if(t.equals("decimal")){
            return exprecnType.INT;
        } else if (t.equals("float")) {
            return exprecnType.FLOAT;
        } else if (t.equals("string")) {
            return exprecnType.STRING;
        } else if (t.equals("boolean")) {
            return exprecnType.BOOL;
        }
        else{
            return  null;
            //exepcen
        }
    }

    public DTOEnvironmentVariables makeDtoEnvironment(){
        DTOPropertyType type;
        if(m_type == exprecnType.INT) {
            type = DTOPropertyType.INT;
        } else if (m_type ==exprecnType.FLOAT) {
            type = DTOPropertyType.FLOAT;
        } else if (m_type == exprecnType.STRING) {
            type = DTOPropertyType.STRING;
        } else {
            type = DTOPropertyType.BOOL;
        }
        DTOEnvironmentVariables DTO;
        if(m_haveRange){
            DTO = new DTOEnvironmentVariables(m_name, type, m_haveRange, m_highRang, m_lowRange);
        }
        else{
            DTO = new DTOEnvironmentVariables(m_name, type, m_haveRange);
        }
        return DTO;
    }

    public void setWithDto(DTOEnvironmentVariables environmentVariables) throws InvalidValue{
        m_init.convertValueInString(environmentVariables.getValue());

        if(m_init.getType() != getDtoType(environmentVariables.getVariableType())){
            throw new InvalidValue("In environment variabale " + m_name + " got wrong value type");
        }

        if(m_haveRange){
            if(m_init.getType() == exprecnType.INT){
                if(m_init.getInt() > m_highRang || m_init.getInt() < m_lowRange){
                    throw new InvalidValue("In environment variabale " + m_name + " got value out of range");
                }
            } else if (m_init.getType() == exprecnType.FLOAT) {
                if(m_init.getFloat() > m_highRang || m_init.getFloat() < m_lowRange){
                    throw new InvalidValue("In environment variabale " + m_name + " got value out of range");
                }
            }else {
                throw new InvalidValue("In environment variabale " + m_name + " of type " + m_type + " got range");
            }
        }
        m_randomlyIneceat = false;
    }

    public exprecnType getDtoType(DTOPropertyType propertyType){
        if(propertyType == DTOPropertyType.INT){
            return exprecnType.INT;
        } else if (propertyType == DTOPropertyType.FLOAT) {
            return exprecnType.FLOAT;
        } else if (propertyType == DTOPropertyType.STRING) {
            return exprecnType.STRING;
        }else {
            return exprecnType.BOOL;
        }
    }

    public boolean haveRange(){
        return m_haveRange;
    }
}
