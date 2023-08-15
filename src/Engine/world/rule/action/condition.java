package Engine.world.rule.action;

import Engine.world.entity.Entity;
import Engine.generated.PRDAction;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class condition extends action implements Serializable {
    private String m_entity;
    private subCondition m_subCon;
    private List<ActionInterface> m_then = null;
    private List<ActionInterface> m_else = null;

    public condition(PRDAction action) throws InvalidValue {
        m_entity = action.getEntity();
        if(action.getPRDCondition().getSingularity().equals("multiple")){
            m_subCon = new multiple(action.getPRDCondition());
        }
        else if (action.getPRDCondition().getSingularity().equals("single")){
            m_subCon = new single(action.getPRDCondition());
        }
        m_then = new ArrayList<>();
        for(PRDAction thanAction : action.getPRDThen().getPRDAction()){
            if (thanAction.getType().equals("increase") || thanAction.getType().equals("decrease")) {
                m_then.add(new addValue(thanAction));
            } else if (thanAction.getType().equals("calculation")) {
                m_then.add(new calculation(thanAction));
            } else if (thanAction.getType().equals("condition")) {
                m_then.add(new condition(thanAction));
            } else if (thanAction.getType().equals("set")) {
                m_then.add(new set(thanAction));
            } else if (thanAction.getType().equals("kill")) {
                m_then.add(new kill(thanAction));
            }

        }
        if(action.getPRDElse() != null){
            m_else = new ArrayList<>();
            for(PRDAction elseAction : action.getPRDElse().getPRDAction()){
                if (elseAction.getType().equals("increase") || elseAction.getType().equals("decrease")) {
                    m_else.add(new addValue(elseAction));
                } else if (elseAction.getType().equals("calculation")) {
                    m_else.add(new calculation(elseAction));
                } else if (elseAction.getType().equals("condition")) {
                    m_else.add(new condition(elseAction));
                } else if (elseAction.getType().equals("set")) {
                    m_else.add(new set(elseAction));
                } else if (elseAction.getType().equals("kill")) {
                    m_else.add(new kill(elseAction));
                }
            }
        }
    }
    @Override
    public boolean activateAction(Entity entity) throws InvalidValue{
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
