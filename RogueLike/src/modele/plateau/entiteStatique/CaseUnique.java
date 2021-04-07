package modele.plateau.entiteStatique;

import modele.plateau.Jeu;

public class CaseUnique extends EntiteStatique{

    int nbPassage = 0;
    int passageMax;
    public CaseUnique(Jeu _jeu,int max){
        super(_jeu);
        passageMax = max;
    }

    public boolean incrementPassage(){
        nbPassage++;
        return traversable();
    }
    @Override
    public boolean traversable() {
        return nbPassage <= passageMax;
    }
}
