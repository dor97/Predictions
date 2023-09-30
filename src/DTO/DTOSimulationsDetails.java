package DTO;

import java.util.HashMap;
import java.util.Map;

public class DTOSimulationsDetails extends DTOSimulationDetailsItem{
    private Map<String, DTOSimulationDetails> simulationsDetails = new HashMap<>();

    public DTOSimulationsDetails(){

    }

    public DTOSimulationsDetails(Map<String, DTOSimulationDetails> simulationsDetails){
        this.simulationsDetails = simulationsDetails;
    }

    public void addSimulationDetails(String name, DTOSimulationDetails simulationDetails){
        simulationsDetails.put(name, simulationDetails);
    }

    public Map<String, DTOSimulationDetails> getSimulationsDetails(){
        return simulationsDetails;
    }

    @Override
    public String toString(){
        return "details";
    }

}
