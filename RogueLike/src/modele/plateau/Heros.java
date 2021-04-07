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
    private final int O_UP = 0;
    private final int O_RIGHT = 1;
    private final int O_DOWN = 2;
    private final int O_LEFT = 3;

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

    //action

    //interagation case
    public void caseType(int x,int y){
        EntiteStatique ets;
        ets = jeu.getEntiteStatique(x,y);
        if (ets instanceof CaseUnique && !((CaseUnique) ets).incrementPassage()){
            jeu.getGrilleEntitesStatiques()[x][y] = new CaseFeu(jeu);
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
            caseType(x,y);
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

    public void action() {

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
}
