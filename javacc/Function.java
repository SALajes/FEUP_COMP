
import java.util.Hashtable;

public class Function {
    private Hashtable<String, Symbol> parameter_variables = new Hashtable<>();
    private Hashtable<String, Symbol> local_variables = new Hashtable<>();

    public boolean addParameterVariable(String identifier, Symbol variable){
        if(parameterVariableExists(identifier)) {
            parameter_variables.put(identifier, variable);
            return true;
        }
        return false;
    }

    private boolean parameterVariableExists(String identifier) {
        return parameter_variables.containsKey(identifier);
    }

    public Symbol getParameterVariable(String identifier){
        if(parameterVariableExists(identifier))
            return parameter_variables.get(identifier);
        return null;
    }

    public boolean addLocalVariable(String identifier, Symbol variable){
        if(localVariableExists(identifier)) {
            local_variables.put(identifier, variable);
            return true;
        }
        return false;
    }

    private boolean localVariableExists(String identifier) {
        return local_variables.containsKey(identifier);
    }

    public Symbol getLocalVariable(String identifier){
        if(localVariableExists(identifier))
            return local_variables.get(identifier);
        return null;
    }
}