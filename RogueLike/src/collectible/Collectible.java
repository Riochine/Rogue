package collectible;

public abstract class Collectible {
    private int id;
    protected int idAffichage;
    public Collectible(int _id){
        idAffichage = 0;
        id = _id;
    }
    public void rammaserCollectible() {

    }

    public  abstract void utiliserCollectible();
}
