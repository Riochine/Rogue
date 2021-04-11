package collectible;

import java.util.ArrayList;

public class Coffre extends Collectible {
    ArrayList<Collectible> tabColl;
    public Coffre(){
        super( "Coffre");
        tabColl = new ArrayList<Collectible>();
    }
    public Coffre(Collectible [] _tabColl){
        super( "Coffre");
        tabColl = new ArrayList<Collectible>();
        setTabColl(_tabColl);
    }

    public Collectible[] getTabColl() {
        Collectible[] tabCollRet = new Collectible[tabColl.size()];
        int i = 0;
        for(Collectible cl:tabColl){
            tabCollRet[i] = cl;
            i++;
        }
        return tabCollRet;
    }

    public void setTabColl(Collectible[] _tabColl) {
            if(_tabColl != null)for (Collectible col : _tabColl) tabColl.add(col);
    }
    public void addCollToCoffre(Collectible col){
        tabColl.add(col);
    }
}
