package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;
import gameEngien.generated.PRDAction;

public class kill extends action {

    private String m_entity;

    public kill(PRDAction action){
        m_entity = action.getEntity();
    }

    @Override
    public boolean activateAction (Entity i_entity){

        return true;
    }
}
