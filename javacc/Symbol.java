
public class Symbol {
    String type;
    String value;
    boolean is_initialized;
    boolean is_static;
    boolean is_constant;

    public Symbol(String type, String value, boolean is_static, boolean is_constant){
        this.type = type;
        this.value = value;
        this.is_initialized = true;
        this.is_constant = is_constant;
        this.is_static = is_static;
    }

    public Symbol(String type, boolean is_static, boolean is_constant){
        this.type = type;
        this.is_initialized = false;
        this.is_constant = is_constant;
        this.is_static = is_static;
    }

    public void Initialize (String value){
        this.is_initialized = true;
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    public boolean isInitialized(){
        return this.is_initialized;
    }
    public boolean isStatic(){
        return this.is_static;
    }
    public boolean isConstant(){
        return this.is_constant;
    }
}