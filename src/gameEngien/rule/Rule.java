package gameEngien.rule;

import gameEngien.entity.Entity;
import gameEngien.generated.PRDAction;
import gameEngien.generated.PRDRule;
import gameEngien.rule.action.actionInterface.ActionInterface;
import gameEngien.rule.action.increase.Increase;
import gameEngien.rule.action.increase.calculation;
import gameEngien.rule.action.increase.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rule {
    private String m_name;
    private Integer m_ticks;
    private double m_probability;
    private List<ActionInterface> m_actions;
    private Random random;

    public Rule(String name, int ticks, int probability){
        m_name = name;
        m_ticks = ticks;
        m_probability = probability;
        m_actions = new ArrayList<>();
        random = new Random();
    }

    public Rule(PRDRule rule){
        m_name = rule.getName();
        m_ticks = rule.getPRDActivation().getTicks() != null ? rule.getPRDActivation().getTicks() : 1;
        m_probability = rule.getPRDActivation().getProbability() != null ? rule.getPRDActivation().getProbability() : 1;
        random = new Random();
        m_actions = new ArrayList<>();
        for(PRDAction action : rule.getPRDActions().getPRDAction()){
            if(action.getType() == "increase"){
                m_actions.add(new Increase(action));
            } else if (action.getType() == "calculation") {
                m_actions.add(new calculation(action));
            }
            else if (action.getType() == "condition") {
                m_actions.add(new condition(action));
            }


        }
    }

    public ActionInterface getAction(String Action){
        return null;
    }

    public void addAction(ActionInterface ActionToAdd){
        m_actions.add(ActionToAdd);
    }
    public void activeRule(Entity entity, int tick){
        if(tick % m_ticks == 0 && random.nextDouble() < m_probability){
            for(ActionInterface action : m_actions){
                if(action.getEntityName() == entity.getName() && entity.isPropertyExists(action.getPropertyName())){
                    action.activateAction(entity);
                }
            }
        }
    }
}
