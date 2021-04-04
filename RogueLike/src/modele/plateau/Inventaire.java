package modele.plateau;

import collectible.Collectible;

import java.util.ArrayList;
import java.util.List;

public class Inventaire {
    private List<Collectible> inventaire;

    public Inventaire() {
        inventaire = new ArrayList<Collectible>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Inventaire : \n");

        for (Collectible c : inventaire) {
            sb.append("-").append(c.getName());
            sb.append("\n");
        }

        return sb.toString();
    }

    public void ajouter(Collectible entiteCollectible) {
        inventaire.add(entiteCollectible);
    }

    public List<Collectible> getCollectibles() {
        return inventaire;
    }

    public void supprimerCollectible(int id) {
        inventaire.removeIf(c -> c.getId() == id);
    }
}
