import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ImportMethod {
    private String class_name;
    private String method;
    private ArrayList<String> parameter_types;
    private String return_type;
    private int overloads = 0;
    private boolean isStatic = false;

    public ImportMethod(String class_name, String method, String return_type, ArrayList<String> parameter_types, boolean isStatic){
        this.class_name=class_name;
        this.method=method;
        this.return_type = (return_type==null? "void" : return_type);
        this.parameter_types = parameter_types;
        this.isStatic = isStatic;
    }

    public int getOverloads() {
        return overloads;
    }

    public void incrementOverloads() {
        this.overloads++;
    }

    public String getClassName() {
        return class_name;
    }

    public ArrayList<String> getParameters() {
        return parameter_types;
    }

    public String getReturnType(){
        return return_type;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public String dump(){
        String print = ":: " + class_name + "." + method + " returns " + return_type + "\n:parameters: (";

        for(int i = 0; i < parameter_types.size(); i++){
            if(i!=0)
                print += " , ";
            print = print + parameter_types.get(i);
        }

        print = print + ")\n";

        return print;
    }

    public Pair<String, String> getReturnType(ArrayList<String> arguments) {
        Pair<String, String> result = new Pair<>();

        if(arguments.size() != parameter_types.size()) {
            result.first = "Parameters don't match that of method " + class_name + "." + method + " : expected " + parameter_types.size() + " received " + arguments.size();
            result.second = "";
            return result;
        }

        for(int i=0; i < arguments.size(); i++){
            if(parameter_types.get(i) != arguments.get(i)){
                result.first = "Argument " + i + " type does not match parameter type of method " + class_name + "." + method + " : expected " + parameter_types.get(i) + " received " + arguments.get(i);
                result.second = "";
                return result;
            }
        }

        result.first = null;
        result.second = return_type;
        return result;
    }
}
