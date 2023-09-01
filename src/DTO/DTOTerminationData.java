package DTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DTOTerminationData extends DTOSimulationDetailsItem{
    private terminationType m_type;
    private int m_count;

    private Map<String, String> data = new HashMap<>();

    public DTOTerminationData(terminationType type, int count){
        data.put("SimulationDetailsItem", "Action");
        m_type = type;
        m_count = count;
    }

    public terminationType getType(){
        return m_type;
    }

    public int getCount(){
        return m_count;
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
        return "Termination";
    }
}
