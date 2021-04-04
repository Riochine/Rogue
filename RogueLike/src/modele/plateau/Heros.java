/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

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
            return jeu.getEntite(x, y).traversable();
        } else {
            return false;
        }
    }

    public void action() {
        System.out.println(inventaire);
    }

    public int getOrientation() {
        return orientation;
    }
}
