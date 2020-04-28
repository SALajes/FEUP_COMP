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

    public boolean isClass(String identity){
        return identity.equals(class_name);
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

    public boolean methodExists(String identifier) {
        return methods.containsKey(identifier);
    }

    public Method getParameterVariable(String identifier){
        if(methodExists(identifier))
            return methods.get(identifier);
        return null;
    }

    public boolean checkMethod(String identity, String return_type){
        if(methodExists(identity)){
            return methods.get(identity).checkReturnType(return_type);
        }
        else return false;
    }

    public boolean checkVariable(String scope, String identity, String type){
        if(scope == "global")
            return checkGlobalVariable(identity, type);
        else if(methodExists(scope)) {
            switch(methods.get(scope).checkVariable(identity, type)){
                case 0:
                    return true;
                case 1:
                    return false;
                case 2:
                    return checkGlobalVariable(identity, type);
            }
        }
        return false;
    }

    private boolean checkGlobalVariable(String identity, String type) {
        if(global_variables.containsKey(identity)){
            return global_variables.get(identity).checkType(type);
        }
        return false;
    }

    public void dump(){
        Iterator it = imports.entrySet().iterator();
        System.out.println("------- Imports -------");
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ImportMethod imp = (ImportMethod) pair.getValue();
            System.out.println(imp.dump());
        }

        it = global_variables.entrySet().iterator();
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