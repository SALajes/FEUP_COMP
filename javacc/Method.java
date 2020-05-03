
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
            Symbol symbol = new Symbol(parameters.get(i).second, parameters.get(i).first);
            symbol.initialize();
            parameter_variables.put(parameters.get(i).first, symbol);

            parameter_types.add(parameters.get(i).second);
        }
    }

    public Symbol getVariable(String identifier){
        if(local_variables.containsKey(identifier))
            return local_variables.get(identifier);
        else if(parameter_variables.containsKey(identifier))
            return parameter_variables.get(identifier);
        else return null;
    }

    public void addLocalVariable(String identifier, Symbol variable){
        if(!localVariableExists(identifier)) {
            local_variables.put(identifier, variable);
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

    public void invalidate() {
        this.invalid = true;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public Pair<String, String> getReturnType(ArrayList<String> arguments) {
        Pair<String, String> result = new Pair<>();
        System.out.println("method parameters size: " + parameter_variables.size() + "  TAMANHO DOS ARGS" + arguments.size());
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
        System.out.println("IM OK");
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