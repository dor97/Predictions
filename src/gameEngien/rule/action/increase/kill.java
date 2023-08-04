package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;

public class kill extends action {

    private String m_entity;

    @Override
    public boolean activateAction (Entity i_entity){
        return true;
    }
}
