package gameEngien.property;

import gameEngien.generated.PRDEnvProperty;
import gameEngien.property.Property;
import gameEngien.property.propertyDifenichan;
import gameEngien.property.propertyInterface.PropertyInterface;
import gameEngien.property.propertyInterface.propertyType;
import gameEngien.rule.action.increase.exprecnType;

import java.io.Serializable;
import java.util.Random;

public class DecimalProperty extends Property implements Serializable {
    private String m_name;
    private int m_property;
    private Double m_lowRange, m_highRang;

    public DecimalProperty(propertyDifenichan propertyDifenichan){
        super(propertyType.INT);
        m_name = propertyDifenichan.getName();
        m_lowRange = propertyDifenichan.getLowRange();
        m_highRang = propertyDifenichan.getHighRange();
        if(propertyDifenichan.getType() != exprecnType.INT){
            //exepcen
        }
        if(propertyDifenichan.isRandom()){
            Random random = new Random();
            if(m_lowRange != null){
                m_property = random.nextInt((int) (m_highRang - m_lowRange) + 1) + (int)m_lowRange.doubleValue();
            }
            else{
                m_property = random.nextInt();
            }
        }
        else {
            m_property = propertyDifenichan.getInit().getInt();
        }

    }

    public DecimalProperty(PRDEnvProperty envProperty){
        super(propertyType.INT);
        m_name = envProperty.getPRDName();
        m_lowRange = envProperty.getPRDRange().getFrom();
        m_highRang = envProperty.getPRDRange().getTo();
        m_property = 10;    //TODO get from user

    }

    public DecimalProperty(String name, int property, double lowRange, double highRange){
        super(propertyType.INT);
        m_name = name;
        m_lowRange = lowRange;
        m_highRang = highRange;
        m_property = property;
    }

    @Override
    public void addToProperty(int add){
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
    public void setProperty(int v){
        m_property = v;
    }
}
