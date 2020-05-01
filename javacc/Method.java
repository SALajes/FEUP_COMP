
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class Method {
    private String name;
    private String return_type = "";

    private int overloads = 0;

    private Hashtable<String, Symbol> parameter_variables = new Hashtable<>();
    private Hashtable<String, Symbol> local_variables = new Hashtable<>();

    public Method(String name, String type) {
        this.name = name;
        this.return_type = type;
        System.out.println("METHOD RETURN TYPE: " + this.return_type);
    }

    public String getName() {
        return name;
    }

    public String getReturnType(){
        return return_type;
    }

    public boolean addParameterVariable(String type, String identifier){
        if(!parameterVariableExists(identifier)) {
            Symbol parameter = new Symbol(type, identifier, parameter_variables.size());
            parameter.initialize();
            parameter_variables.put(identifier, parameter);
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

    public void addLocalVariable(String type, String identifier){
        if(!localVariableExists(identifier)) {
            int indexOffset = parameter_variables.size() + local_variables.size();
            local_variables.put(identifier, new Symbol(type, identifier, indexOffset));
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

    public String getVariableType(String identity){
        if(local_variables.containsKey(identity))
            return local_variables.get(identity).getType();
        else if(parameter_variables.containsKey(identity))
            return parameter_variables.get(identity).getType();
        else return "";
    }

    // Given an identity it returns a variable, first checks if its a local then if its a parameter variable
    public Symbol getVariable(String identity){
        if(local_variables.containsKey(identity))
            return local_variables.get(identity);

        return parameter_variables.get(identity);
    }

    public boolean checkVariable(String identity) {
        return local_variables.containsKey(identity) || parameter_variables.containsKey(identity);
    }

    public int checkVariable(String identity, String type){
        if(local_variables.containsKey(identity))
            if(local_variables.get(identity).checkType(type))
                return 0;
            else return 1;
        else if(parameter_variables.containsKey(identity))
            if(parameter_variables.get(identity).checkType(type))
                return 0;
            else return 1;
        else return 2;
    }

    public boolean isInitialized(String variable){
        Symbol var = local_variables.get(variable);

        if(var != null){
            return var.isInitialized();
        }

        var = parameter_variables.get(variable);

        if(var != null){
            return var.isInitialized();
        }

        return false;
    }

    public boolean initialize(String variable){
        if(local_variables.containsKey(variable)){
            local_variables.get(variable).initialize();
            return true;
        }else if (parameter_variables.containsKey(variable)){
            parameter_variables.get(variable).initialize();
            return true;
        }
        return false;
    }

    public boolean doesVariableExist(String identity){
        if(!local_variables.containsKey(identity)){
           return parameter_variables.containsKey(identity);
        }
        else return true;
    }

    public int getOverloads() {
        return overloads;
    }

    public void incrementOverloads() {
        this.overloads++;
    }

    public int getNumLocalVars() {
        return this.local_variables.size();
    }

    public int getNumParameters() {
        return this.parameter_variables.size();
    }

    public Hashtable<String, Symbol> getLocalVariables() {
        return local_variables;
    }

    public void dump(){
        System.out.println("------- " + name + " returns " + return_type + " -------");
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