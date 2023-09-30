package DTO;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DTOSimulationDetails extends DTOSimulationDetailsItem{

    private List<DTOEntityData> entitysList = new ArrayList<>();

    private List<DTORuleData> RulesList = new ArrayList<>();

    private DTOTerminationData termination = new DTOTerminationData();

    private List<DTOEnvironmentVariables> environmentVariables = new ArrayList<>();
    private DTOGrid gridSize = new DTOGrid();
    private String name = "world";

    public void setName(String name){
        this.name = name;
    }

    public DTOGrid getGridSize(){
        return gridSize;
    }

    public void setGridSize(Integer x, Integer y){
        gridSize.putData("Size", x.toString() + "X" + y.toString());
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

    public void setTermination(DTOTerminationData termination){
        this.termination = termination;
    }

    public DTOTerminationData getTerminationList(){
        return termination;
    }

    @Override
    public String toString(){
        return name;
    }
}
