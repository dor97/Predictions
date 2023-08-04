package gameEngien.rule.action.actionInterface;

import gameEngien.entity.Entity;
import gameEngien.property.propertyInterface.PropertyInterface;

public interface ActionInterface {
    //public void setEntityAndProperty(Entity e, PropertyInterface p);
    public String getEntityName();
    public String getPropertyName();
    public boolean activateAction(Entity entity);
    public boolean setValues(PropertyInterface v1, PropertyInterface v2);
    }
