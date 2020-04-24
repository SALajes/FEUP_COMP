
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class Method {
    private String name;
    private String type;

    private Hashtable<String, Symbol> parameter_variables = new Hashtable<>();
    private Hashtable<String, Symbol> local_variables = new Hashtable<>();

    public Method(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public boolean addParameterVariable(String type, String identifier){
        if(!parameterVariableExists(identifier)) {
            parameter_variables.put(identifier, new Symbol(type, identifier));
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

    public void addLocalVariable(String identifier, Symbol variable){
        if(!localVariableExists(identifier)) {
            local_variables.put(identifier, variable);
        }
    }

    private boolean localVariableExists(String identifier) {
        return local_variables.containsKey(identifier);
    }

    public Symbol getLocalVariable(String identifier){
        if(localVariableExists(identifier))
            return local_variables.get(identifier);
        return null;
    }

    public void dump(){
        System.out.println("------- " + name + " returns " + type + " -------");
        System.out.println("------- parameters -------");
        Iterator it = parameter_variables.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Symbol symbol = (Symbol) pair.getValue();
            System.out.println(symbol.dump());
        }

        System.out.println("------- local -------");
        it = local_variables.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Symbol symbol = (Symbol) pair.getValue();
            System.out.println(symbol.dump());
        }
    }
}