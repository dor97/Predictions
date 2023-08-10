package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;
import gameEngien.property.propertyInterface.PropertyInterface;
import gameEngien.rule.action.actionInterface.ActionInterface;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.io.Serializable;

public class action implements ActionInterface, Serializable {
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
}
