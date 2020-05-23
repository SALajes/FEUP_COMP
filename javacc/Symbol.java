
public class Symbol {
    private String type;
    private String id;
    private int index;
    private boolean is_initialized;

    public Symbol(String type, String id){
        this.type = type;
        this.id = id;
        is_initialized = false;
    }

    public Symbol(String type, String id, int index){
        this.type = type;
        this.id = id;
        this.index = index +1;
        is_initialized=false;
    }

    public String getType() {
        return this.type;
    }

    public boolean checkType(String type) {
        return this.type.equals(type);
    }

    public String getId() {
        return id;
    }

    public int getIndex() {
        return index;
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

    public void decIndex(){
        this.index--;
    }
}