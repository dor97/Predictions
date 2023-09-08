package DTO;

import java.util.Map;
import java.util.Optional;

public class DTOGrid extends DTOSimulationDetailsItem{
    private Map<String, String> data;

    public void putData(String key, String value){
        data.put(key, value);
    }
    @Override
    public Optional<Map<String, String>> getData() {
        return Optional.of(data);
    }
    @Override
    public String toString(){
        return "Grid";
    }
}
