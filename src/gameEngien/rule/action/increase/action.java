package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;
import gameEngien.property.propertyInterface.PropertyInterface;
import gameEngien.rule.action.actionInterface.ActionInterface;

public class action implements ActionInterface {
    @Override
    public String getEntityName() {
        return null;
    }

    @Override
    public String getPropertyName() {
        return null;
    }

    @Override
    public boolean activateAction(Entity entity) {
        return false;
    }

    @Override
    public boolean setValues(PropertyInterface v1, PropertyInterface v2) {
        return false;
    }
}
