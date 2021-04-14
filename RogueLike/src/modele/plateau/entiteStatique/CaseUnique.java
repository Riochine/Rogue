package modele.plateau.entiteStatique;

import modele.plateau.Jeu;

public class CaseUnique extends EntiteStatique{

    private int nbPassage = 0;
    private int passageMax;
    public CaseUnique(Jeu _jeu,int _max){
        super(_jeu);
        passageMax = _max;
    }

    public boolean incrementPassage(){
        nbPassage++;
        return traversable();
    }

    @Override
    public boolean traversable() {
        return traversableBool();
    }

    public boolean traversableBool() {
        return nbPassage <= passageMax;
    }
    public int traversableInt() {
        return passageMax - nbPassage;
    }
}
