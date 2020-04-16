
import java.util.Hashtable;

public class SymbolTable {
    private Hashtable<String, Symbol> global_variables = new Hashtable<>();
    private Hashtable<String, Function> functions = new Hashtable<>();

    public boolean addGlobalVariable(String identifier, Symbol variable){
        if(globalVariableExists(identifier)) {
            global_variables.put(identifier, variable);
            return true;
        }
        return false;
    }

    private boolean globalVariableExists(String identifier) {
        return global_variables.containsKey(identifier);
    }

    public Symbol getGlobalVariable(String identifier){
        if(globalVariableExists(identifier))
            return global_variables.get(identifier);
        return null;
    }

    public boolean addFunction(String identifier, Function function){
        if(functionExists(identifier)) {
            functions.put(identifier, function);
            return true;
        }
        return false;
    }

    private boolean functionExists(String identifier) {
        return functions.containsKey(identifier);
    }

    public Function getParameterVariable(String identifier){
        if(functionExists(identifier))
            return functions.get(identifier);
        return null;
    }
}