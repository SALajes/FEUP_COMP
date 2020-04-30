
public class Symbol {
    private String type;
    private String id;
    private boolean is_initialized;

    public Symbol(String type, String id){
        this.type = type;
        this.id = id;
        is_initialized = false;
    }

    public String getType() {
        return type;
    }

    public boolean checkType(String type) {
        return this.type.equals(type);
    }

    public String getId() {
        return id;
    }

    public String dump(){
        return type + " " + id;
    }

    public boolean isInitialized() {
        return is_initialized;
    }

    public void initialize() {
        this.is_initialized = true;
    }
}