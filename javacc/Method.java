
import java.util.*;

public class Method {
    private String name;
    private String return_type;

    private int overloads = 0;
    private boolean invalid;

    private ArrayList<String> parameter_types;

    private Hashtable<String, Symbol> parameter_variables;
    private Hashtable<String, Symbol> local_variables = new Hashtable<>();

    public Method(String name, String type, ArrayList<Pair<String, String>> parameters) {
        this.name = name;
        this.return_type = type;
        this.invalid = false;

        this.parameter_variables = new Hashtable<>();
        this.parameter_types = new ArrayList<>();

        for(int i=0; i < parameters.size(); i++){
            Symbol symbol = new Symbol(parameters.get(i).second, parameters.get(i).first, i);
            symbol.initialize();
            parameter_variables.put(parameters.get(i).first, symbol);

            parameter_types.add(parameters.get(i).second);
        }
    }

    public String getName() {
        return name;
    }

    public Symbol getVariable(String identifier){
        if(local_variables.containsKey(identifier))
            return local_variables.get(identifier);
        else if(parameter_variables.containsKey(identifier))
            return parameter_variables.get(identifier);
        else return null;
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

    public boolean checkReturnType(String return_type){
        return this.return_type.equals(return_type);
    }

    public String getReturnType(){
        return this.return_type;
    }

    public boolean checkVariable(String identity) {
        return local_variables.containsKey(identity) || parameter_variables.containsKey(identity);
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

    public int getNumLocalVars() {
        return this.local_variables.size();
    }

    public int getNumParameters() {
        return this.parameter_variables.size();
    }

    public Hashtable<String, Symbol> getLocalVariables() {
        return local_variables;
    }

    public Hashtable<String, Symbol> getParameterVariables() {
        return parameter_variables;
    }

    public void invalidate() {
        this.invalid = true;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public ArrayList<String> getParameterTypes() {
        return parameter_types;
    }

    public Pair<String, String> getReturnType(ArrayList<String> arguments) {
        Pair<String, String> result = new Pair<>();

        if(arguments.size() != parameter_variables.size()) {
            result.first = "Parameters don't match that of method " + name + " : expected " + parameter_variables.size() + " received " + arguments.size();
            result.second = "";
            return result;
        }

        for(int i=0; i < arguments.size(); i++){
            if(parameter_types.get(i) != arguments.get(i)){
                result.first = "Argument " + i + " type does not match parameter type of method " + name + " : expected " + parameter_types.get(i) + " received " + arguments.get(i);
                result.second = "";
                return result;
            }
        }

        result.first = null;
        result.second = return_type;
        return result;
    }

    public String dump(){
        String print = "## " + name + " returns " + return_type + '\n';
        Iterator it;

        if(parameter_variables.size() > 0){
            print = print + ":parameters:\n";
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Method method = (Method) o;
        return Objects.equals(name, method.name) &&
                Objects.equals(parameter_types, method.parameter_types);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parameter_variables);
    }
}