package collectible;

public abstract class Collectible {
    protected String name;
    protected int id;
    protected int idAffichage;

    public Collectible(int _id, String _name){
        idAffichage = 0;
        id = _id;
        name = _name;
    }
    public void ramasserCollectible() {

    }

    public String getName() {
        return name;
    }

    public  abstract void utiliserCollectible();

    public int getId() {
        return id;
    }
}
