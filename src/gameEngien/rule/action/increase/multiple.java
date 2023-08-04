package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;
import gameEngien.generated.PRDCondition;

import java.util.List;

public class multiple implements subCondition {

    enum logicalOp{ OR, AND}
    logicalOp m_logical;
    List<subCondition> m_conditions;

    public multiple(PRDCondition condition){
        if(condition.getLogical() == "or"){
            m_logical = logicalOp.OR;
        }
        else if(condition.getLogical() == "and"){
            m_logical = logicalOp.AND;
        }
        for(PRDCondition subCondition : condition.getPRDCondition()){
            if(subCondition.getSingularity() == "multiple"){
                m_conditions.add(new multiple(subCondition));
            } else if (subCondition.getSingularity() == "single") {
                m_conditions.add(new single(subCondition));
            }
        }
    }
    @Override
    public boolean getBoolValue(Entity entity){
        if(m_logical == logicalOp.AND){
            for(subCondition condition : m_conditions){
                if(condition.getBoolValue(entity) == false){
                    return false;
                }
            }
            return true;
        } else if (m_logical == logicalOp.OR) {
            for(subCondition condition : m_conditions){
                if(condition.getBoolValue(entity) == false){
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
