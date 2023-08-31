package Engine.world.rule.action;

import Engine.utilites.Utilites;
import Engine.world.entity.Entity;
import Engine.generated.PRDCondition;
import Engine.InvalidValue;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class multiple implements subCondition, Serializable {

    enum logicalOp{ OR, AND}
    logicalOp m_logical;
    List<subCondition> m_conditions;

    public multiple(PRDCondition condition, Utilites util) throws InvalidValue {
        m_conditions = new ArrayList<>();
        if(condition.getLogical().equals("or")){
            m_logical = logicalOp.OR;
        }
        else if(condition.getLogical().equals("and")){
            m_logical = logicalOp.AND;
        }
        for(PRDCondition subCondition : condition.getPRDCondition()){
            if(subCondition.getSingularity().equals("multiple")){
                m_conditions.add(new multiple(subCondition, util));
            } else if (subCondition.getSingularity().equals("single")) {
                m_conditions.add(new single(subCondition, util));
            }
        }
    }
    @Override
    public boolean getBoolValue(Entity entity, int currTick)throws InvalidValue{
        if(m_logical == logicalOp.AND){
            for(subCondition condition : m_conditions){
                if(condition.getBoolValue(entity, currTick) == false){
                    return false;
                }
            }
            return true;
        } else if (m_logical == logicalOp.OR) {
            for(subCondition condition : m_conditions){
                if(condition.getBoolValue(entity, currTick) == false){
                    return true;
                }
            }
            return false;
        }
        else{ //do not get to here
            return false;
        }
    }
    @Override
    public boolean shouldIgnore(Entity entity){
        for(subCondition condition : m_conditions){
            if(!condition.shouldIgnore(entity)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean getBoolValue(Entity entity, Entity secondaryEntity, int currTick){
        if(m_logical == logicalOp.AND){
            for(subCondition condition : m_conditions){
                if(!condition.shouldIgnore(entity) && condition.getBoolValue(entity, secondaryEntity, currTick) == false){
                    return false;
                }
            }
            return true;
        } else if (m_logical == logicalOp.OR) {
            for(subCondition condition : m_conditions){
                if(!condition.shouldIgnore(entity) && condition.getBoolValue(entity, secondaryEntity, currTick) == false){
                    return true;
                }
            }
            return false;
        }
        else{ //do not get to here
            return false;
        }
    }


}
