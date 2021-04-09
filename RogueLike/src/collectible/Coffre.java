package collectible;

public class Coffre extends Collectible {
    Collectible [] tabColl;
    public Coffre(){
        super( "Coffre");
    }
   public Coffre(Collectible [] tabColl){
        super( "Coffre");
        this.tabColl = tabColl;
    }

    public Collectible[] getTabColl() {
        return tabColl;
    }

    public void setTabColl(Collectible[] tabColl) {
        this.tabColl = tabColl;
    }
}
