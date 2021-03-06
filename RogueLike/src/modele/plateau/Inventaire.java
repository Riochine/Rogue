package modele.plateau;

import collectible.Capsule;
import collectible.Coffre;
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
    public Collectible[] getCollectiblesTab() {
        Collectible inventaireTab[] = new Collectible[inventaire.size()];
        int i = 0;
        for(Collectible cl : inventaire){
            inventaireTab[i] = cl;
            i++;
        }
        return inventaireTab;
    }

    public void supprimerClef(int id) {
        inventaire.removeIf(c -> c.getId() == id);
    }

    public void supprimerCapsule() {
        int i = 0;
        for(Collectible col :inventaire) {
            if (col instanceof Capsule){
                inventaire.remove(i);
                break;
            }
            i++;
        }
    }
    public void supprimerCapsuleTout(){
        inventaire.removeIf(c ->c instanceof  Capsule);
    }
}
