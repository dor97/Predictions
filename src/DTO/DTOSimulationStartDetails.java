package DTO;

import java.util.ArrayList;
import java.util.List;

public class DTOSimulationStartDetails {
    private Integer simulationId;
    private List<DTOEnvironmentVariablesValues> environmentVariablesValues = new ArrayList<>();

    public DTOSimulationStartDetails(Integer simulationId, List<DTOEnvironmentVariablesValues> environmentVariablesValues){
        this.simulationId = simulationId;
        this.environmentVariablesValues = environmentVariablesValues;
    }

    public Integer getSimulationId(){
        return simulationId;
    }

    public List<DTOEnvironmentVariablesValues> getEnvironmentVariablesValues(){
        return environmentVariablesValues;
    }
}
