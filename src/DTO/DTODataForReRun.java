package DTO;

import java.util.Map;

public class DTODataForReRun {
    private Map<String, Integer> entitiesPopulation;
    private Map<String, Object> environmentsValues;

    public void setEntitiesPopulation(Map<String, Integer> entitiesPopulation){
        this.entitiesPopulation = entitiesPopulation;
    }

    public void setEnvironmentsValues(Map<String, Object> environmentsValues){
        this.environmentsValues = environmentsValues;
    }

    public Map<String, Integer> getEntitiesPopulation(){
        return entitiesPopulation;
    }

    public Map<String, Object> getEnvironmentsValues(){
        return environmentsValues;
    }
}
