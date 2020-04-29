
public class Symbol {
    private String type;
    private String id;
    private int index;

    public Symbol(String type, String id){
        this.type = type;
        this.id = id;
    }

    public Symbol(String type, String id, int index){
        this.type = type;
        this.id = id;
        this.index = index +1;
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

    public int getIndex() {
        return index;
    }

    public String dump(){
        return type + " " + id;
    }
}