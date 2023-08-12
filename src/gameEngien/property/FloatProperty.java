package gameEngien.property;

import DTO.DTOEnvironmentVariablesValues;
import gameEngien.generated.PRDEnvProperty;
import gameEngien.property.propertyInterface.propertyType;
import gameEngien.rule.action.increase.exprecnType;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.io.Serializable;
import java.util.Random;

public class FloatProperty extends Property implements Serializable {

    private String m_name;
    private float m_property;
    private Double m_lowRange, m_highRang;
    private boolean haveRange;

    public FloatProperty(propertyDifenichan propertyDifenichan) throws InvalidValue{
        super(propertyType.FLOAT);
        m_name = propertyDifenichan.getName();
        haveRange = propertyDifenichan.haveRange();
        if(haveRange) {
            m_lowRange = propertyDifenichan.getLowRange();
            m_highRang = propertyDifenichan.getHighRange();
        }
        if(propertyDifenichan.getType() != exprecnType.FLOAT){
            //exepcen
        }
        if(propertyDifenichan.isRandom()){
            Random random = new Random();
            if(haveRange){
                m_property = (float) (random.nextFloat() * (m_highRang - m_lowRange) + m_lowRange);
            }
            else{
                m_property = random.nextInt() + random.nextFloat();
            }
        }
        else {
            m_property = propertyDifenichan.getInit().getFloat();
            if(haveRange && (m_property > m_highRang || m_property < m_lowRange)){
                throw new InvalidValue("In property " + m_name + " value is out of range");
            }
        }
    }

    public FloatProperty(EnvironmentDifenichan environmentDifenichan) throws InvalidValue{
        super(propertyType.FLOAT);
        m_name = environmentDifenichan.getName();
        haveRange = environmentDifenichan.haveRange();
        if(haveRange) {
            m_lowRange = environmentDifenichan.getLowRange();
            m_highRang = environmentDifenichan.getHighRange();
        }
        if(environmentDifenichan.getType() != exprecnType.FLOAT){
            //exepcen
        }
        if(environmentDifenichan.isRandom()){
            Random random = new Random();
            if(haveRange){
                m_property = (float) (random.nextFloat() * (m_highRang - m_lowRange) + m_lowRange);
            }
            else{
                m_property = random.nextInt() + random.nextFloat();
            }
        }
        else {
            m_property = environmentDifenichan.getInit().getFloat();
            if(haveRange && (m_property > m_highRang || m_property < m_lowRange)){
                throw new InvalidValue("In property " + m_name + " value is out of range");
            }
        }
    }

    public FloatProperty(PRDEnvProperty envProperty){
        super(propertyType.FLOAT);
        m_name = envProperty.getPRDName();
        m_lowRange = envProperty.getPRDRange().getFrom();
        m_highRang = envProperty.getPRDRange().getTo();
        m_property = 10;    //TODO get from user
    }

    @Override
    public void addToProperty(float add){
        if(m_property + add <= m_highRang && m_property + add >= m_lowRange)
            m_property += add;
    }
    @Override
    public String getName(){
        return m_name;
    }
    @Override
    public Object getValue(){
        return m_property;
    }
    @Override
    public void setProperty(float v){
        m_property = v;
    }
    @Override
    public void setProperty(int v){
        m_property = v;
    }
//    @Override
//    public DTOEnvironmentVariablesValues makeDtoEnvironment(){
//        return new DTOEnvironmentVariablesValues(m_name, m_property);
//    }
}
