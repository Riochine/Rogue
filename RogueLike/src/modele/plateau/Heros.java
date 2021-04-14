/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import collectible.*;
import modele.plateau.entiteStatique.*;

import javax.swing.text.html.parser.Entity;

/**
 * Héros du jeu
 */
public class Heros {
    private int x;
    private int y;
    private int orientation;

    //Constantes pour l'orientation du joueur
    public final int O_UP = 0;
    public final int O_RIGHT = 1;
    public final int O_DOWN = 2;
    public final int O_LEFT = 3;

    private Inventaire inventaire;

    private Jeu jeu;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Heros(Jeu _jeu, int _x, int _y) {
        jeu = _jeu;
        x = _x;
        y = _y;
        inventaire = new Inventaire();
        orientation = O_DOWN; //Le joueur commence en regardant vers le bas
    }

    public void caseTypeA(int x,int y){//après
        EntiteStatique ets;
        ets = jeu.getEntiteStatique(x,y);
        if (ets instanceof CaseUnique && !((CaseUnique) ets).incrementPassage()){
            jeu.getGrilleEntitesStatiques()[x][y] = new CaseFeu(jeu);
        }
    }
    public void caseTypeB(int x,int y){//avant
        EntiteStatique ets = jeu.getEntiteStatique(x,y);
        int plage [][] = jeu.getPlage(x,y);
        if (ets instanceof CaseUnique && ((CaseUnique) ets).traversableInt() == 0){
            if(jeu.getEntiteCollectible(x,y) instanceof Coffre){
                Collectible colCoffre[] = ((Coffre) jeu.getEntiteCollectible(x,y)).getTabColl();
                jeu.getGrilleEntitesCollectibles()[x][y] = null;
                jeu.tabRanCollectibleSurMap(colCoffre,plage[0][0],plage[1][0]);
            }else if(jeu.getEntiteCollectible(x,y) instanceof Collectible){
                Collectible col = jeu.getEntiteCollectible(x,y);
                jeu.supprimerEntiteCollectible(x,y);
                jeu.tabRanCollectibleSurMap(new Collectible[]{col},plage[0][0],plage[1][0]);
            }
        }
    }


    public Inventaire getInventaire() {
        return inventaire;
    }

    private void droite() {
        if (traversable(x+1, y)) {
            x ++;
        }
    }

    private void gauche() {
        if (traversable(x-1, y)) {
            x --;
        }
    }

    private void bas() {
        if (traversable(x, y+1)) {
            y ++;
        }
    }

    private void haut() {
        if (traversable(x, y-1)) {
            y --;
        }
    }

    public void changer_direction(int newDir) {
        //On veut pouvoir s'orienter sans forcément avancer d'une case
        if(orientation == newDir)
        {
            caseTypeA(x,y);
            switch (orientation) {
                case O_UP:
                    haut();
                    break;
                case O_RIGHT:
                    droite();
                    break;
                case O_DOWN:
                    bas();
                    break;
                case O_LEFT:
                    gauche();
                    break;
                default:
                    orientation = O_DOWN;
                    break;
            }
            caseTypeB(x,y);
        }
        orientation = newDir;
    }

    private boolean traversable(int x, int y) {
        if (x >0 && x < jeu.SIZE_X && y > 0 && y < jeu.SIZE_Y) {
            return jeu.getEntiteStatique(x, y).traversable();
        } else {
            return false;
        }
    }

    public int getOrientation() {
        return orientation;
    }

    public void ajoutInventaire(Collectible entiteCollectible) {
        inventaire.ajouter(entiteCollectible);
    }

    public void afficherInventaire() {
        System.out.println(inventaire);
    }

    public boolean possedeClef(int id) {
        for (Collectible c : inventaire.getCollectibles()) {
            if(c instanceof Clef)
                if(c.getId() == id)
                    return true;
        }

        return false;
    }

    public void supprimerClef(int id) {
        inventaire.supprimerClef(id);
    }

    public boolean possedeCapsule() {
        for (Collectible c : inventaire.getCollectibles())
            if(c instanceof Capsule)
                    return true;

        return false;
    }

    public void supprimerCapsule() {
        inventaire.supprimerCapsule();
    }

    /*
     * Différentes actions du joueur selon ce qu'il se trouve devant lui
     */

    public void action() {
        int x = getX(), y = getY();

        //jeu.getTabRangLvl();

        switch (getOrientation()) {
            case O_UP: y--; break;
            case O_RIGHT: x++; break;
            case O_DOWN: y++; break;
            case O_LEFT: x--; break;
            default: break;
        }

        //Ramasser un collectible
        if(jeu.CollectibleExiste(x, y)) {
            Collectible col = jeu.getEntiteCollectible(x,y);
            if(col instanceof Coffre && ((Coffre) col).getTabColl() != null){  //si coffre, on prend l'interieur
                for(Collectible tmp:((Coffre) col).getTabColl()){
                    System.out.println("Vous ramassez : " + tmp.getName());
                    ajoutInventaire(tmp);
                }
                jeu.supprimerEntiteCollectible(x, y);
            }
            else{   //sinon on prend directement l'objet et on supprime son affichage
                System.out.println("Vous ramassez : " + jeu.getEntiteCollectible(x, y).getName());
                ajoutInventaire(jeu.getEntiteCollectible(x, y));
                jeu.supprimerEntiteCollectible(x, y);
            }
        }

        //Porte
        EntiteStatique e = jeu.getEntiteStatique(x, y);
        if(e instanceof Porte) {
            int idPorte = ((Porte) e).getIdPorte();
            if(possedeClef(idPorte)) {
                supprimerClef(idPorte);
                jeu.setEntiteStatique(x, y, new CaseNormale(jeu));

                switch (getOrientation()) {
                    case O_UP: y--; break;
                    case O_RIGHT: x++; break;
                    case O_DOWN: y++; break;
                    case O_LEFT: x--; break;
                    default: break;
                }

                e = jeu.getEntiteStatique(x, y);
                if(e instanceof Porte)
                    if(idPorte == ((Porte) e).getIdPorte())
                        jeu.setEntiteStatique(x, y, new CaseNormale(jeu));
            }
        }
        else if (e instanceof CaseFeu) {
            if(possedeCapsule()) {
                jeu.setEntiteStatique(x, y, new CaseNormale(jeu));
                supprimerCapsule();
            }
        }
        else if (e instanceof CaseVide) {
            switch (getOrientation()) {
                case O_UP: y--; break;
                case O_RIGHT: x++; break;
                case O_DOWN: y++; break;
                case O_LEFT: x--; break;
                default: break;
            }
            caseTypeB(x,y);
            e = jeu.getEntiteStatique(x, y);
            if(e instanceof CaseNormale || e instanceof CaseUnique)
            {
                this.x = x;
                this.y = y;
                caseTypeA(getX(),getY());
            }
        }
    }
}
