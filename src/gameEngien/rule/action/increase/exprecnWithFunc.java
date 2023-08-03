package gameEngien.rule.action.increase;

import java.util.List;

public class exprecnWithFunc extends exprecn{

    private boolean m_isFunc = false;
    List<exprecn> params;

    public boolean isFunc(){
        return m_isFunc;
    }
    @Override
    public void convertValueInString(String value){
        super.convertValueInString(value);
        isFunc = false;
        if(getType() == exprecnType.STRING){
            if (value.contains("(") && value.contains(")")) {
                String trimmedInput = value.trim().replace("(", "").replace(")", "");
                String[] parts = trimmedInput.split("\\(");

                if (parts.length == 2) {
                    String functionName = parts[0];
                    String[] parameters = parts[1].split(",");

                    setValue(functionName);
                    m_isFunc = true;
                    // Now you have the function name and parameters

                    for (String parameter : parameters) {
                        exprecn temp = new exprecn();
                        temp.convertValueInString(parameter);
                        params.add(temp);
                    }
                    return; // No need to proceed further
                }
            }
        }
    }

    @Override
    public void setValue(int value) {
        super.setValue(value);
        m_isFunc = false;
    }

    @Override
    public void setValue(float value) {
        super.setValue(value);
        m_isFunc = false;
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
        m_isFunc = false;
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        m_isFunc = false;
    }

    @Override
    public void setValue(boolean value) {
        super.setValue(value);
        m_isFunc = false;
    }

    public exprecn getParams(int i){
        return params.get(i);
    }
}
