package DTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DTOTerminationData extends DTOSimulationDetailsItem{
    private Map<String, String> data = new HashMap<>();

    public DTOTerminationData(){
        data.put("SimulationDetailsItem", "Termination");
    }

//    public terminationType getType(){
//        return m_type;
//    }
//
//    public int getCount(){
//        return m_count;
//    }

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
