package gameEngien.rule;

import gameEngien.rule.action.actionInterface.ActionInterface;

import java.io.Serializable;
import java.util.List;

public class RuleDifenichan implements Serializable {
    private String m_name;
    private int m_ticks;
    private int m_probability;
    private List<ActionInterface> m_actions;


}
