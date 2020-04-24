
public class Symbol {
    private String type;
    private String id;

    public Symbol(String type, String id){
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String dump(){
        return type + " " + id;
    }
}