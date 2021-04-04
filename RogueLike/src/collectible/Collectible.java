package collectible;

public abstract class Collectible {
    public String name;
    private int id;
    protected int idAffichage;

    public Collectible(int _id, String _name){
        idAffichage = 0;
        id = _id;
        name = _name;
    }
    public void rammaserCollectible() {

    }

    public String getName() {
        return name;
    }

    public  abstract void utiliserCollectible();
}
