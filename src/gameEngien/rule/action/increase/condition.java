package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;
import gameEngien.generated.PRDAction;
import gameEngien.generated.PRDCondition;
import gameEngien.rule.action.actionInterface.ActionInterface;

import java.util.ArrayList;
import java.util.List;

public class condition extends action{
    private String m_entity;
    private subCondition m_subCon;
    private List<ActionInterface> m_then = null;
    private List<ActionInterface> m_else = null;

    public condition(PRDAction action){
        m_entity = action.getPRDCondition().getEntity();
        if(action.getPRDCondition().getPRDCondition().get(0).getSingularity() == "multiple"){
            m_subCon = new multiple(action.getPRDCondition().getPRDCondition().get(0));
        }
        else if (action.getPRDCondition().getPRDCondition().get(0).getSingularity() == "single"){
            m_subCon = new single(action.getPRDCondition().getPRDCondition().get(0));
        }
        m_then = new ArrayList<>();
        for(PRDAction act : action.getPRDThen().getPRDAction()){
            if(act.getType() == "increase"){
                m_then.add(new Increase(action));
            } else if (act.getType() == "calculation") {
                m_then.add(new calculation(action));
            } else if (act.getType() == "condition") {
                m_then.add(new condition(action));
            }

        }
        if(action.getPRDElse() != null){
            m_else = new ArrayList<>();
            for(PRDAction act : action.getPRDElse().getPRDAction()){
                if(act.getType() == "increase"){
                    m_then.add(new Increase(action));
                } else if (act.getType() == "calculation") {
                    m_then.add(new calculation(action));
                } else if (act.getType() == "condition") {
                    m_then.add(new condition(action));
                }

            }
        }
    }
    @Override
    public boolean activateAction(Entity entity) {
        if (m_subCon.getBoolValue(entity)) {
            for (ActionInterface action : m_then) {
                //if(action.getEntityName() == entity.getName() && entity.isPropertyExists(action.getPropertyName())){
                action.activateAction(entity);
                //}
            }
        } else {
            if (m_else != null) {
                for (ActionInterface action : m_else) {
                    //if(action.getEntityName() == entity.getName() && entity.isPropertyExists(action.getPropertyName())){
                    action.activateAction(entity);
                    //}
                }
            }
        }

        return false;
    }

}
