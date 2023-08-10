package DTO;

import java.util.ArrayList;
import java.util.List;

public class DTORuleData {
    private String ruleName;
    private int ticksToWorkAt;
    private double probabilityToWork;
    private int numOfAction = 0;
    private List<String> actionsName = new ArrayList<>();

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
}
