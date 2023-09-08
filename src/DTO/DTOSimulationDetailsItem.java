package DTO;

import java.util.Map;
import java.util.Optional;

public class DTOSimulationDetailsItem implements DTOSimulationDetailsItemInterface{
    private String toPrint = "";
    @Override
    public Optional<Map<String, String>> getData() {
        return Optional.empty();
    }

    public void setToPrint(String print){
        toPrint = print;
    }
    @Override
    public String toString(){
        return toPrint;
    }
}
