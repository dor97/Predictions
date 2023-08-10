package DTO;

public class DTOPropertyData {
    String propertyName;
    DTOPropertyType propertyType;
    double m_highRange, m_lowRange;
    boolean m_haveRange, m_RandomlyInatiated;

    public DTOPropertyData(String name, DTOPropertyType type, boolean haveRange, boolean RandomlyInatiated){
        propertyName = name;
        propertyType = type;
        m_haveRange = haveRange;
        m_RandomlyInatiated = RandomlyInatiated;
    }

    public DTOPropertyData(String name, DTOPropertyType type, boolean haveRange, boolean RandomlyInatiated, double highRange, double lowRange){
        propertyName = name;
        propertyType = type;
        m_haveRange = haveRange;
        m_RandomlyInatiated = RandomlyInatiated;
        m_highRange = highRange;
        m_lowRange = lowRange;
    }

    public String getName(){
        return propertyName;
    }

    public DTOPropertyType getType(){
        return propertyType;
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
}
