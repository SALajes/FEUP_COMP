
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class Method {
    private String name;
    private String return_type;

    private int overloads = 0;

    private Hashtable<String, Symbol> parameter_variables = new Hashtable<>();
    private Hashtable<String, Symbol> local_variables = new Hashtable<>();

    public Method(String name, String type) {
        this.name = name;
        this.return_type = type;
    }

    public Symbol getVariable(String identifier){
        if(local_variables.containsKey(identifier))
            return local_variables.get(identifier);
        else if(parameter_variables.containsKey(identifier))
            return parameter_variables.get(identifier);
        else return null;
    }

    public void addParameterVariable(String type, String identifier){
        if(!parameterVariableExists(identifier)) {
            Symbol parameter = new Symbol(type, identifier);
            parameter.initialize();
            parameter_variables.put(identifier, parameter);
        }
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

    public boolean checkReturnType(String return_type){
        return this.return_type.equals(return_type);
    }

    public String getReturnType(){
        return this.return_type;
    }

    public int checkVariableType(String identifier, String type){
        if(local_variables.containsKey(identifier))
            if(local_variables.get(identifier).checkType(type))
                return 0;
            else return 1;
        else if(parameter_variables.containsKey(identifier))
            if(parameter_variables.get(identifier).checkType(type))
                return 0;
            else return 1;
        else return 2;
    }

    public int getOverloads() {
        return overloads;
    }

    public void incrementOverloads() {
        this.overloads++;
    }

    public String dump(){
        String print = "## " + name + " returns " + return_type;
        Iterator it;

        if(parameter_variables.size() > 0){
            print = print + "\n:parameters:\n";
            it = parameter_variables.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Symbol symbol = (Symbol) pair.getValue();
                print = print + "  -> " + symbol.dump() + '\n';
            }
        }

        if(local_variables.size() > 0){
            print = print + ":locals:\n";
            it = local_variables.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Symbol symbol = (Symbol) pair.getValue();
                print = print + "  -> " + symbol.dump() + '\n';
            }
        }

        return print;
    }
}