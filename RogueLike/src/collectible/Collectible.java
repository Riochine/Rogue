package collectible;

public abstract class Collectible {
    protected String name;
    protected int id;
    public Collectible(String _name){
        name = _name;
        id = -1;
    }
    public Collectible(int _id, String _name){
        name = _name;
        id = _id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
