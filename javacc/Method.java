
import java.util.*;

public class Method {
    private String name;
    private String return_type;

    private int overloads = 0;
    private boolean invalid;

    private Hashtable<String, Symbol> parameter_variables = new Hashtable<>();
    private Hashtable<String, Symbol> local_variables = new Hashtable<>();

    public Method(String name, String type, Hashtable<String, Symbol> parameters) {
        this.name = name;
        this.return_type = type;
        this.parameter_variables = parameters;
        this.invalid = false;
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

    public String compareArguments(ArrayList<String> arguments) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Method method = (Method) o;
        return Objects.equals(name, method.name) &&
                Objects.equals(parameter_variables, method.parameter_variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parameter_variables);
    }
}