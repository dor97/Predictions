package DTO;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DTOSimulationDetails extends DTOSimulationDetailsItem{

    private List<DTOEntityData> entitysList = new ArrayList<>();

    private List<DTORuleData> RulesList = new ArrayList<>();

    private List<DTOTerminationData> terminationList = new ArrayList<>();

    private List<DTOEnvironmentVariables> environmentVariables = new ArrayList<>();
    private Pair<Integer, Integer> gridSize = null;


    public Pair<Integer, Integer> getGridSize(){
        return gridSize;
    }

    public void setGridSize(Integer x, Integer y){
        new Pair<>(x, y);
    }
    public void addEnvironment(DTOEnvironmentVariables environment){
        environmentVariables.add(environment);
    }
    public void setEnvironments(List<DTOEnvironmentVariables> environments){
        environmentVariables = environments;
    }

    public List<DTOEnvironmentVariables> getEnvironmentVariables(){return environmentVariables;}

    public void addEntity(DTOEntityData entity){
        entitysList.add(entity);
    }

    public List<DTOEntityData> getEntitysList(){
        return entitysList;
    }

    public void addRule(DTORuleData rule){
        RulesList.add(rule);
    }

    public List<DTORuleData> getRulesList(){
        return RulesList;
    }

    public void addTermination(DTOTerminationData termination){
        terminationList.add(termination);
    }

    public List<DTOTerminationData> getTerminationList(){
        return terminationList;
    }

    @Override
    public String toString(){
        return "world";
    }
}
