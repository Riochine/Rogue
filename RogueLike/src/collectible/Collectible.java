package collectible;

public abstract class Collectible {
    private int id;
    public Collectible(int _id){
        id = _id;
    }
    public void rammaserCollectible() {

    }

    public  abstract void utiliserCollectible();
}
