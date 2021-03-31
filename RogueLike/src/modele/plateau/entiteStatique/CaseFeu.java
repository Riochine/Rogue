package modele.plateau.entiteStatique;

import modele.plateau.Jeu;

public class CaseFeu extends EntiteStatique{
    public CaseFeu(Jeu _jeu) {
        super(_jeu);
    }

    @Override
    public boolean traversable() {
        return false;
    }
}
