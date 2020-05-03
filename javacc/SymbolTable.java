import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class SymbolTable {
    private Hashtable<String, Symbol> global_variables = new Hashtable<>();
    private Hashtable<String, Method> methods = new Hashtable<>();
    private Hashtable<String, ImportMethod> imports = new Hashtable<>();

    public String class_name;
    public String extend_class;

    public boolean isClass(String identifier){
        return identifier.equals(class_name);
    }

    public boolean extendsClass(){
        return extend_class!=null;
    }

    public void addImportMethod(String class_name, String method, String return_type, ArrayList<String> parameters){
        String key = class_name+"."+method;
        if(imports.containsKey(key)){
            imports.get(key).incrementOverloads();
            key = key + imports.get(key).getOverloads();
            imports.put(key, new ImportMethod(class_name, method, return_type, parameters));
        }
        else imports.put(key, new ImportMethod(class_name, method, return_type, parameters));
    }

    public boolean checkImportMethod(String class_name, String method){
        return this.imports.containsKey(class_name+"."+method);
    }

    public boolean checkImportClass(String class_name){
        return this.imports.containsKey(class_name);
    }

    public String getImportReturnType(String class_name, String method){
        if(checkImportMethod(class_name, method)){
            return this.imports.get(class_name+"."+method).getReturnType();
        }
        return "";
    }

    public String getReturnMethodInExtendClass(String method){
        return getImportReturnType(extend_class, method);
    }

    public String getMethodReturnType(String method){
        if(methodExists(method)){
            return this.methods.get(method).getReturnType();
        }
        return "";
    }

    public Symbol getVariable(String scope, String identifier){
        if(identifier == null) return null;

        if(scope == "global")
            return getGlobalVariable(identifier);
        else if(methodExists(scope)) {
            Symbol var = methods.get(scope).getVariable(identifier);
            if(var == null)
                return getGlobalVariable(identifier);
            else return var;
        }
        else return null;
    }

    public void addVariable(String scope, String type, String identifier){
        if(scope == "global")
            addGlobalVariable(type, identifier);
        else if(methodExists(scope))
            methods.get(scope).addLocalVariable(identifier, new Symbol(type, identifier));
    }

    public void addGlobalVariable(String type, String identifier){
        global_variables.put(identifier, new Symbol(type, identifier));
    }

    public String addMethod(String identifier, String type, Hashtable<String,Symbol> parameters){
        if(!methodExists(identifier)) {
            methods.put(identifier, new Method(identifier, type, parameters));
        }
        else{
            int num_overloads = methods.get(identifier).getOverloads();
            Method new_method = new Method(identifier, type, parameters);

            for(int i=0; i <= num_overloads; i++){
                String method = identifier;
                if(i>0)
                    method = method + "_" + i;
                if(methods.get(method).equals(new_method))
                    new_method.invalidate();
            }

            methods.get(identifier).incrementOverloads();
            methods.put(identifier+methods.get(identifier).getOverloads() , new_method);
            return identifier+methods.get(identifier).getOverloads();
        }
        return identifier;
    }

    private boolean globalVariableExists(String identifier) {
        return global_variables.containsKey(identifier);
    }

    public Symbol getGlobalVariable(String identifier){
        if(globalVariableExists(identifier))
            return global_variables.get(identifier);
        return null;
    }

    public boolean methodExists(String identifier) {
        return methods.containsKey(identifier);
    }

    public boolean checkMethodType(String identifier, String return_type){
        if(methodExists(identifier)){
            return methods.get(identifier).checkReturnType(return_type);
        }
        else return false;
    }

    public boolean checkVariableType(String scope, String identifier, String type){
        if(scope == "global")
            return checkGlobalVariable(identifier, type);
        else if(methodExists(scope)) {
            switch(methods.get(scope).checkVariableType(identifier, type)){
                case 0:
                    return true;
                case 1:
                    return false;
                case 2:
                    return checkGlobalVariable(identifier, type);
            }
        }
        return false;
    }

    private boolean checkGlobalVariable(String identifier, String type) {
        if(global_variables.containsKey(identifier)){
            return global_variables.get(identifier).checkType(type);
        }
        return false;
    }

    public String getVariableType(String scope , String identifier){
        Symbol var = getVariable(scope, identifier);
        if(var != null)
            return var.getType();
        else return "";
    }

    public boolean doesVariableExist(String scope , String identifier){
        Symbol var = getVariable(scope, identifier);
        if(var != null)
            return true;
        else return false;
    }

    public boolean isVariableInitialized(String identifier , String scope){
        Symbol var = getVariable(scope, identifier);

        if(var != null)
            return var.isInitialized();
        else return true;
    }

    public void initializeVariable(String identifier , String scope){
        getVariable(scope, identifier).initialize();
    }

    public String checkMethodParameters(String method_name, ArrayList<String> arguments) {
        return methods.get(method_name).compareArguments(arguments);
    }

    public String checkExtendedMethodParameters(String method_name, ArrayList<String> arguments) {
        return imports.get(extend_class+"."+method_name).compareArguments(arguments);
    }

    public String checkImportMethodParameters(String class_name, String method_name, ArrayList<String> arguments) {
        return imports.get(class_name+"."+method_name).compareArguments(arguments);
    }

    public boolean isInvalidMethod(String scope) {
        return methods.get(scope).isInvalid();
    }

    public void dump(){
        System.out.println("\n=== SYMBOL TABLE ===");

        Iterator it;

        if(imports.size() > 0){
            it = imports.entrySet().iterator();
            System.out.println("------ IMPORT ------");
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                ImportMethod imp = (ImportMethod) pair.getValue();
                System.out.println(imp.dump());
            }
        }

        if(global_variables.size() > 0){
            it = global_variables.entrySet().iterator();
            System.out.println("------ GLOBAL ------");
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Symbol symbol = (Symbol) pair.getValue();
                System.out.println(symbol.dump());
            }
        }

        if(methods.size() > 0){
            System.out.println("------ METHOD ------");
            it = methods.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Method method = (Method) pair.getValue();
                System.out.println(method.dump());
            }
        }

        System.out.println("====== FINISH ======");
    }
}