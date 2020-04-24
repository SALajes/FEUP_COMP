
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class SymbolTable {
    private Hashtable<String, Symbol> global_variables = new Hashtable<>();
    private Hashtable<String, Method> methods = new Hashtable<>();

    public void addVariable(String scope, String type, String id){
        if(scope == "global")
            addGlobalVariable(type, id);
        else if(methodExists(scope))
            methods.get(scope).addLocalVariable(id, new Symbol(type, id));
    }

    public void addGlobalVariable(String type, String id){
        global_variables.put(id, new Symbol(type, id));
    }

    public void addArgument(String scope, String type, String id){
        if(methodExists(scope)) {
            methods.get(scope).addParameterVariable(type, id);
        }
    }

    private boolean globalVariableExists(String identifier) {
        return global_variables.containsKey(identifier);
    }

    public Symbol getGlobalVariable(String identifier){
        if(globalVariableExists(identifier))
            return global_variables.get(identifier);
        return null;
    }

    public void addMethod(String identifier, String type){
        if(!methodExists(identifier)) {
            methods.put(identifier, new Method(identifier, type));
        }
    }

    private boolean methodExists(String identifier) {
        return methods.containsKey(identifier);
    }

    public Method getParameterVariable(String identifier){
        if(methodExists(identifier))
            return methods.get(identifier);
        return null;
    }

    public void dump(){
        Iterator it = global_variables.entrySet().iterator();
        System.out.println("------- GLOBAL -------");
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Symbol symbol = (Symbol) pair.getValue();
            System.out.println(symbol.dump());
        }

        System.out.println("------- METHODS -------");
        it = methods.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Method method = (Method) pair.getValue();
            method.dump();
        }
    }
}