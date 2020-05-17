import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class SymbolTable {
    private Hashtable<String, Symbol> global_variables = new Hashtable<>();
    private Hashtable<String, Method> methods = new Hashtable<>();
    private Hashtable<String, ImportMethod> imports = new Hashtable<>();

    public String getClassName() {
        return class_name;
    }

    public String class_name;
    public String extend_class;

    public boolean isClass(String identifier){
        return identifier.equals(class_name);
    }

    public boolean extendsClass(){
        return extend_class!=null;
    }

    public void addImportMethod(String class_name, String method, String return_type, ArrayList<String> parameters, boolean isStatic){
        String key = class_name+"."+method;
        if(imports.containsKey(key)){
            imports.get(key).incrementOverloads();
            key = key + imports.get(key).getOverloads();
        }
        imports.put(key, new ImportMethod(class_name, method, return_type, parameters, isStatic));
    }

    public String addMethod(String identifier, String type, ArrayList<Pair<String, String>> parameters){
        if(!methodExists(identifier)) {
            methods.put(identifier, new Method(identifier, type, parameters));
        }
        else{
            int num_overloads = methods.get(identifier).getOverloads();
            Method new_method = new Method(identifier, type, parameters);

            for(int i=0; i <= num_overloads; i++){
                String method = identifier;
                if(i>0)
                    method = method + i;
                if(methods.get(method).equals(new_method))
                    new_method.invalidate();
            }

            methods.get(identifier).incrementOverloads();
            methods.put(identifier+methods.get(identifier).getOverloads() , new_method);//era aqui que o method nao tava a guardar como querias
            return identifier+methods.get(identifier).getOverloads();
        }
        return identifier;
    }

    public boolean checkImportMethod(String class_name, String method){
        return this.imports.containsKey(class_name+"."+method);
    }

    public boolean checkImportClass(String class_name){
        return this.imports.containsKey(class_name);
    }

    public Pair<String, String> getImportReturnType(String class_name, String method, ArrayList<String> arguments){
        return getImportReturnType(class_name, method, arguments, false);
    }

    public Pair<String, String> getImportReturnType(String class_name, String method, ArrayList<String> arguments, boolean extend_verification){
        Pair<String, String> result = new Pair<>();

        if(checkImportMethod(class_name, method)){
            String identifier = class_name+"."+method;
            int num_overloads = imports.get(identifier).getOverloads();

            for(int i=0; i <= num_overloads; i++) {

                if (i > 0)
                    identifier = identifier + i;

                result = this.imports.get(identifier).getReturnType(arguments);

                if(result.first == null && result.second != ""){
                    return result;
                }
            }
        }
        else{
            result.first = "Imported method or class does not exist.";
            if(extend_verification)
                result.first = null;
            result.second = "";
        }

        return result;
    }

    public boolean isMethodImport(String idendity, ArrayList<String> arguments) {
        Pair<String, String> result = new Pair<>();

        if(methodExists(idendity)) {
            String methodName = idendity;
            int num_overloads = this.methods.get(idendity).getOverloads();

            for (int i = 0; i <= num_overloads; i++) {
                if(i > 0)
                    methodName = idendity + i;

                    result = this.methods.get(methodName).getReturnType(arguments);

                    if(result.first == null && !result.second.equals(""))
                        return false;
            }
        }

        return true;
    }

    public Pair<String, String> getMethodReturnType(String method, ArrayList<String> arguments){
        Pair<String, String> result = new Pair<>();
        boolean method_not_found = false;

        if(methodExists(method)){
            String identifier = method;
            int num_overloads = methods.get(identifier).getOverloads();

            for(int i=0; i <= num_overloads; i++) {
                if (i > 0)
                    identifier = method + i;

                result = this.methods.get(identifier).getReturnType(arguments);

                if(result.first == null && result.second != ""){
                    return result;
                }
            }
        }
        else{
            result.first = "Method does not exist.";
            result.second = "";
            method_not_found = true;
        }

        if(result.second=="" && extendsClass()) {
            Pair<String, String> aux = getImportReturnType(extend_class, method, arguments, true);

            if(aux.second == "" && aux.first != null && method_not_found) {
                result.first = aux.first;
            }
            else{
                result.first = aux.first;
                result.second = aux.second;
            }
        }

        return result;
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
            methods.get(scope).addLocalVariable(type, identifier);
    }

    public void addGlobalVariable(String type, String identifier){
        global_variables.put(identifier, new Symbol(type, identifier));
    }

    boolean globalVariableExists(String identifier) {
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

    Method getMethod(String id) {
        return methods.get(id);
    }

    ImportMethod getImportMethod(String className, String methodName) {
        String key = className+"."+methodName;
        return imports.get(key);
    }

    public boolean checkMethodType(String identifier, String return_type){
        if(methodExists(identifier)){
            return methods.get(identifier).checkReturnType(return_type);
        }
        else return false;
    }

    String getGlobalVarType(String identity){
        if(global_variables.containsKey(identity)){
            return global_variables.get(identity).getType();
        }
        return "";
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

    Hashtable<String, Symbol> getGlobal_variables() {
        return global_variables;
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