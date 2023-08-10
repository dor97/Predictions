package gameEngien.rule;

import DTO.DTORuleData;
import gameEngien.entity.Entity;
import gameEngien.generated.PRDAction;
import gameEngien.generated.PRDRule;
import gameEngien.rule.action.actionInterface.ActionInterface;
import gameEngien.rule.action.increase.*;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rule implements Serializable {
    private String m_name;
    private Integer m_ticks = 1;
    private double m_probability = 1;
    private List<ActionInterface> m_actions;
    private Random random;

    public Rule(String name, int ticks, int probability){
        m_name = name;
        m_ticks = ticks;
        m_probability = probability;
        m_actions = new ArrayList<>();
        random = new Random();
    }

    public Rule(PRDRule rule) throws InvalidValue {
        m_name = rule.getName();
        try{
            m_ticks = rule.getPRDActivation().getTicks() != null ? rule.getPRDActivation().getTicks() : 1;
        }
        catch (NullPointerException e){

        }
        try {
            m_probability = rule.getPRDActivation().getProbability() != null ? rule.getPRDActivation().getProbability() : 1;
        }
        catch (NullPointerException e){

        }
        random = new Random();
        m_actions = new ArrayList<>();
        for(PRDAction action : rule.getPRDActions().getPRDAction()){
            try {
                if (action.getType().equals("increase") || action.getType().equals("decrease")) {
                    m_actions.add(new addValue(action));
                } else if (action.getType().equals("calculation")) {
                    m_actions.add(new calculation(action));
                } else if (action.getType().equals("condition")) {
                    m_actions.add(new condition(action));
                } else if (action.getType().equals("set")) {
                    m_actions.add(new set(action));
                } else if (action.getType().equals("kill")) {
                    m_actions.add(new kill(action));
                }
            }
            catch (OBJECT_NOT_EXIST e){
                throw new OBJECT_NOT_EXIST(e.getMessage() + " referred to in rule " + m_name);
            }
            catch (ArithmeticException e){
                throw new ArithmeticException(e.getMessage() + " referred to in rule " + m_name);
            }
            //catch (Exception e){
            //    throw new Exception(e.getMessage() + " referred to in rule " + m_name);
            //}

        }
    }

    public ActionInterface getAction(String Action){
        return null;
    }

    public void addAction(ActionInterface ActionToAdd){
        m_actions.add(ActionToAdd);
    }
    public boolean activeRule(Entity entity)throws InvalidValue{
        for(ActionInterface action : m_actions){
            if(action.getEntityName().equals(entity.getName()) && (entity.isPropertyExists(action.getPropertyName()) || action.getPropertyName() == null)){
                if (action.activateAction(entity)){
                    return true;
                }
            }
        }
        return false;
    }
    
    public double getProbability(){
        return m_probability;
    }
    
    public int getTick(){
        return m_ticks;
    }

    public DTORuleData makeDtoRule(){
        DTORuleData DTO = new DTORuleData(m_name, m_ticks, m_probability);

        for(ActionInterface action : m_actions){
            DTO.addActionName("");
            DTO.increaseNumberOfActionByOne();
        }

        return DTO;
    }
}
