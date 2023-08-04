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
        m_entity = action.getEntity();
        if(action.getPRDCondition().getSingularity().equals("multiple")){
            m_subCon = new multiple(action.getPRDCondition());
        }
        else if (action.getPRDCondition().getSingularity().equals("single")){
            m_subCon = new single(action.getPRDCondition());
        }
        m_then = new ArrayList<>();
        for(PRDAction act : action.getPRDThen().getPRDAction()){
            if(act.getType().equals("increase")){
                m_then.add(new Increase(act));
            } else if (act.getType().equals("calculation")) {
                m_then.add(new calculation(act));
            } else if (act.getType().equals("condition")) {
                m_then.add(new condition(act));
            } else if(act.getType().equals("kill")){
                m_then.add(new kill(act));
            }

        }
        if(action.getPRDElse() != null){
            m_else = new ArrayList<>();
            for(PRDAction act : action.getPRDElse().getPRDAction()){
                if(act.getType().equals("increase")){
                    m_then.add(new Increase(act));
                } else if (act.getType().equals("calculation")) {
                    m_then.add(new calculation(act));
                } else if (act.getType().equals("condition")) {
                    m_then.add(new condition(act));
                } else if(act.getType().equals("kill")){
                    m_then.add(new kill(act));
                }

            }
        }
    }
    @Override
    public boolean activateAction(Entity entity) {
        if (m_subCon.getBoolValue(entity)) {
            for (ActionInterface action : m_then) {
                //if(action.getEntityName() == entity.getName() && entity.isPropertyExists(action.getPropertyName())){
                if(action.activateAction(entity)){
                    return true;
                }
                //}
            }
        } else {
            if (m_else != null) {
                for (ActionInterface action : m_else) {
                    //if(action.getEntityName() == entity.getName() && entity.isPropertyExists(action.getPropertyName())){
                    if(action.activateAction(entity)){
                        return true;
                    }
                    //}
                }
            }
        }

        return false;
    }

    @Override
    public String getEntityName(){
        return m_entity;
    }

}
