package DTO;

import java.util.ArrayList;
import java.util.List;

public class DTOSimulationDetails {

    private List<DTOEntityData> entitysList = new ArrayList<>();

    private List<DTORuleData> RulesList = new ArrayList<>();

    private List<DTOTerminationData> terminationList = new ArrayList<>();


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
}
