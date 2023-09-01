package DTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DTOActionData extends DTOSimulationDetailsItem {
    private String m_name;
    private Map<String, String> data;

    public DTOActionData(String name){
        data = new HashMap<>();
        data.put("SimulationDetailsItem", "Action");
        m_name = name;
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
        return m_name;
    }

}
