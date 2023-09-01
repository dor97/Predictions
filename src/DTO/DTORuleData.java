package DTO;

import java.util.ArrayList;
import java.util.List;

public class DTORuleData extends DTOSimulationDetailsItem{
    private String ruleName;
    private int ticksToWorkAt;
    private double probabilityToWork;
    private int numOfAction = 0;
    private List<String> actionsName = new ArrayList<>();
    private List<DTOActionData> actions = new ArrayList<>();

    public DTORuleData(String name, int ticks, double probability){
        ruleName = name;
        ticksToWorkAt = ticks;
        probabilityToWork = probability;
    }

    public void increaseNumberOfActionByOne(){
        numOfAction++;
    }

    public void addActionName(String name){
        actionsName.add(name);
    }

    public int getTicksToWorkAt(){
        return ticksToWorkAt;
    }

    public double getProbabilityToWork(){
        return probabilityToWork;
    }

    public int getNumOfAction(){
        return numOfAction;
    }

    public String getRuleName(){
        return ruleName;
    }

    public List<String> getActionsName(){
        return actionsName;
    }

    public void addAction(DTOActionData actionData){
        actions.add(actionData);
    }

    public List<DTOActionData> getActions(){
        return actions;
    }

    @Override
    public String toString(){
        return ruleName;
    }
}
