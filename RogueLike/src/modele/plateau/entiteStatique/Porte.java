package modele.plateau.entiteStatique;
import modele.plateau.*;

public class Porte extends EntiteStatique {
    private int idPorte;
    public Porte(Jeu _jeu, int _id){
        super(_jeu);
        idPorte = _id;

    }

    public int getIdPorte() {
        return idPorte;
    }

    @Override
    public boolean traversable() {
        return false;
    }
}
