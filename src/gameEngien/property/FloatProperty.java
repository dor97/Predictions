package gameEngien.property;

import gameEngien.rule.action.increase.exprecnType;

import java.util.Random;

public class FloatProperty extends Property{

    private String m_name;
    private float m_property;
    private Double m_lowRange, m_highRang;

    public FloatProperty(propertyDifenichan propertyDifenichan){
        m_name = propertyDifenichan.getName();
        m_lowRange = propertyDifenichan.getLowRange();
        m_highRang = propertyDifenichan.getHighRange();
        if(propertyDifenichan.getType() != exprecnType.FLOAT){
            //exepcen
        }
        if(propertyDifenichan.isRandom()){
            Random random = new Random();
            if(m_lowRange != null){
                m_property = (float) (random.nextFloat() * (m_highRang - m_lowRange) + m_lowRange);
            }
            else{
                m_property = random.nextInt() + random.nextFloat();
            }
        }
        else {
            m_property = propertyDifenichan.getInit().getFloat();
        }
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
}
