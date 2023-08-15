package Engine.world.rule.action;

import Engine.world.entity.Entity;
import Engine.world.entity.property.PropertyInterface;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

public interface ActionInterface {
    //public void setEntityAndProperty(Entity e, PropertyInterface p);
    public String getEntityName();
    public String getPropertyName();
    public boolean activateAction(Entity entity)throws InvalidValue;
    public boolean setValues(PropertyInterface v1, PropertyInterface v2);
    }
