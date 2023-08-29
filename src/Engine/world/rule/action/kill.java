package Engine.world.rule.action;

import Engine.utilites.Utilites;
import Engine.world.entity.Entity;
import Engine.generated.PRDAction;
import Engine.InvalidValue;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import java.io.Serializable;
import java.util.*;


public class kill extends action implements Serializable {

    private String m_entityName;
    private Utilites m_util;

    public kill(PRDAction action, Utilites util, String ruleName) throws  InvalidValue{
        super(action, util, ruleName);
        m_entityName = action.getEntity();
        //actionName = action.getType();
        m_util = util;
        cheackUserInput();
    }

    private void cheackUserInput() throws InvalidValue {
        checkEntityExist();
    }

    private void checkEntityExist(){
        if(!m_util.isEntityDifenichanExists(m_entityName)){
            throw new OBJECT_NOT_EXIST("In action kill the entity " + m_entityName + " does not exist.");
        }
    }

    @Override
    public Map<String , List<Entity>> activateAction (Entity i_entity, int currTick){
        List<Entity> secondaryEntities = null;
        if(getCountForSecondaryEntities() != 0 && !getSecondaryName().equals(m_entityName)){
            secondaryEntities = getSecondaryEntities();
        }
        Map<String , List<Entity>> res = new HashMap<>();
        if(secondaryEntities == null || secondaryEntities.size() == 0){
            res.put("kill", new ArrayList<>(Arrays.asList(i_entity)));
        }else{
            if(m_entityName.equals(i_entity.getName())){
                res.put("kill", new ArrayList<>(Arrays.asList(i_entity)));
            }else {
                res.put("kill", secondaryEntities);
            }
        }
        return res;
    }
}
