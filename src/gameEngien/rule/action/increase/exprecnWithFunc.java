package gameEngien.rule.action.increase;

import java.util.ArrayList;
import java.util.List;

public class exprecnWithFunc extends exprecn {

    private boolean m_isFunc = false;
    List<exprecn> params = new ArrayList<>();

    public boolean isFunc() {
        return m_isFunc;
    }

    @Override
    public void convertValueInString(String value) {
        super.convertValueInString(value);
        m_isFunc = false;
        if (getType() == exprecnType.STRING) {
            if (value.contains("(") && value.contains(")")) {
                int openParenIndex = value.indexOf('(');
                int closeParenIndex = value.indexOf(')');

                if (openParenIndex != -1 && closeParenIndex != -1) {
                    // Extract the function name
                    String functionName = value.substring(0, openParenIndex);

                    // Extract the parameter
                    String parameters = value.substring(openParenIndex + 1, closeParenIndex);

                    // Split the parameter if needed
                    String[] parameterParts = parameters.split(",");

                    setValue(functionName);
                    m_isFunc = true;
                    // Now you have the function name and parameters
                    params.clear();
                    for (String parameter : parameterParts) {
                        exprecn temp = new exprecn();
                        temp.convertValueInString(parameter.trim());
                        params.add(temp);
                    }
                }




                /*
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
                */

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
