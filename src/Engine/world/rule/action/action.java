package Engine.world.rule.action;

import Engine.world.entity.Entity;
import Engine.world.entity.property.PropertyInterface;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.io.Serializable;

public class action implements ActionInterface, Serializable {
    String actionName = "";
    @Override
    public String getEntityName() {
        return null;
    }

    @Override
    public String getPropertyName() {
        return null;
    }

    @Override
    public boolean activateAction(Entity entity)throws InvalidValue {
        return false;
    }

    @Override
    public boolean setValues(PropertyInterface v1, PropertyInterface v2) {
        return false;
    }

    @Override
    public String getName(){
        return actionName;
    }
}
