package Engine.world.entity.property;

import DTO.DTOEnvironmentVariables;
import DTO.DTOPropertyType;
import Engine.generated.PRDEnvProperty;
import Engine.world.expression.expression;
import Engine.world.expression.expressionType;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.io.Serializable;

public class EnvironmentDifenichan implements Serializable {
    private String m_name;
    private expressionType m_type;
    private double m_lowRange, m_highRang;
    private boolean m_randomlyIneceat = true;
    private expression m_init;
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
        m_init = new expression();
        //m_init.convertValueInString(p.getPRDValue().getInit());
        //if(m_type != m_init.getType()){
        //   //exepcen
        //}

    }

    public expressionType getType(){
        return m_type;
    }

    public double getLowRange(){
        return m_lowRange;
    }

    public double getHighRange(){
        return m_highRang;
    }

    public expression getInit(){
        return m_init;
    }

    public boolean isRandom(){
        return m_randomlyIneceat;
    }

    public String getName(){
        return m_name;
    }

    private expressionType myType(String t){
        if(t.equals("decimal")){
            return expressionType.INT;
        } else if (t.equals("float")) {
            return expressionType.FLOAT;
        } else if (t.equals("string")) {
            return expressionType.STRING;
        } else if (t.equals("boolean")) {
            return expressionType.BOOL;
        }
        else{
            return  null;
            //exepcen
        }
    }

    public DTOEnvironmentVariables makeDtoEnvironment(){
        DTOPropertyType type;
        if(m_type == expressionType.INT) {
            type = DTOPropertyType.INT;
        } else if (m_type == expressionType.FLOAT) {
            type = DTOPropertyType.FLOAT;
        } else if (m_type == expressionType.STRING) {
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
            if(m_init.getType() == expressionType.INT){
                if(m_init.getInt() > m_highRang || m_init.getInt() < m_lowRange){
                    throw new InvalidValue("In environment variabale " + m_name + " got value out of range");
                }
            } else if (m_init.getType() == expressionType.FLOAT) {
                if(m_init.getFloat() > m_highRang || m_init.getFloat() < m_lowRange){
                    throw new InvalidValue("In environment variabale " + m_name + " got value out of range");
                }
            }else {
                throw new InvalidValue("In environment variabale " + m_name + " of type " + m_type + " got range");
            }
        }
        m_randomlyIneceat = false;
    }

    public expressionType getDtoType(DTOPropertyType propertyType){
        if(propertyType == DTOPropertyType.INT){
            return expressionType.INT;
        } else if (propertyType == DTOPropertyType.FLOAT) {
            return expressionType.FLOAT;
        } else if (propertyType == DTOPropertyType.STRING) {
            return expressionType.STRING;
        }else {
            return expressionType.BOOL;
        }
    }

    public boolean haveRange(){
        return m_haveRange;
    }
}