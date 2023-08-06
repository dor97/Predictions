package gameEngien.rule.action.increase;

import gameEngien.entity.Entity;
import gameEngien.generated.PRDAction;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.Serializable;

import static gameEngien.utilites.Utilites.getEntityDifenichan;
import static gameEngien.utilites.Utilites.isEntityDifenichanExists;

public class kill extends action implements Serializable {

    private String m_entity;

    public kill(PRDAction action) throws  InvalidValue{
        m_entity = action.getEntity();
        cheackUserInput();
    }

    private void cheackUserInput() throws InvalidValue {
        checkEntityExist();
    }

    private void checkEntityExist(){
        if(!isEntityDifenichanExists(m_entity)){
            throw new OBJECT_NOT_EXIST("In action kill the entity " + m_entity + " does not exist.");
        }
    }

    @Override
    public boolean activateAction (Entity i_entity){

        return true;
    }
}
