package DTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DTOPropertyData extends DTOSimulationDetailsItem{
    private String propertyName;
    private DTOPropertyType propertyType;
    private double m_highRange, m_lowRange;
    private boolean m_haveRange, m_RandomlyInatiated;
    private Map<String, String> data = new HashMap<>();

    public DTOPropertyData(String name, DTOPropertyType type, boolean haveRange, boolean RandomlyInatiated){
        propertyName = name;
        propertyType = type;
        m_haveRange = haveRange;
        m_RandomlyInatiated = RandomlyInatiated;
        data.put("SimulationDetailsItem", "Action");
    }

    public DTOPropertyData(String name, DTOPropertyType type, boolean haveRange, boolean RandomlyInatiated, double highRange, double lowRange){
        propertyName = name;
        propertyType = type;
        m_haveRange = haveRange;
        m_RandomlyInatiated = RandomlyInatiated;
        m_highRange = highRange;
        m_lowRange = lowRange;
        data.put("SimulationDetailsItem", "Action");
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

    public void putData(String key, String value){
        data.put(key, value);
    }
    @Override
    public Optional<Map<String, String>> getData() {
        return Optional.of(data);
    }
    @Override
    public String toString(){
        return propertyName;
    }
}
