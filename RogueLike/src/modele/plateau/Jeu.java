/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import collectible.Coffre;
import modele.plateau.entiteStatique.*;

import java.util.Observable;


public class Jeu extends Observable implements Runnable {

    public static final int SIZE_X = 18;
    public static final int SIZE_Y = 16;

    private int pause = 200; // période de rafraichissement

    private Heros heros;

    private EntiteStatique[][] grilleEntitesStatiques = new EntiteStatique[SIZE_X][SIZE_Y];

    public Jeu() {
        heros = new Heros(this, 1, 1);
        morceauDeNiveau0(0,0);
        morceauDeNiveau1(5,0);
        morceauDeNiveau2(6,5);
        morceauDeNiveau3(11,5);
        morceauDeNiveau4(12,10);
        morceauDeNiveauStandard();
    }

    public Heros getHeros() {
        return heros;
    }

    public EntiteStatique[][] getGrille() {
        return grilleEntitesStatiques;
    }

	public EntiteStatique getEntite(int x, int y) {
		if (x < 0 || x >= SIZE_X || y < 0 || y >= SIZE_Y) {
			// L'entité demandée est en-dehors de la grille
			return null;
		}
		return grilleEntitesStatiques[x][y];
	}

    private void initialisationDesEntites() {
        heros = new Heros(this, 4, 4);



        // murs extérieurs horizontaux
        for (int x = 0; x < 20; x++) {
            addEntiteStatique(new Mur(this), x, 0);
            addEntiteStatique(new Mur(this), x, 9);
        }

        // murs extérieurs verticaux
        for (int y = 1; y < 9; y++) {
            addEntiteStatique(new Mur(this), 0, y);
            addEntiteStatique(new Mur(this), 19, y);
        }

        addEntiteStatique(new Mur(this), 2, 6);
        addEntiteStatique(new Mur(this), 3, 6);

        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                if (grilleEntitesStatiques[x][y] == null) {
                    grilleEntitesStatiques[x][y] = new CaseNormale(this);
                }

            }
        }


    }

    private void morceauDeNiveauStandard(){
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                if (grilleEntitesStatiques[x][y] == null) {
                    grilleEntitesStatiques[x][y] = new CaseNormale(this);
                }

            }
        }
    }
    private void morceauDeNiveau0(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY);

        addEntiteStatique(new Porte(this,0), 4 + offsetX, 2 + offsetY);
        addEntiteStatique(new CaseVide(this), 1 + offsetX, 3 + offsetY);
        addEntiteStatique(new CaseFeu(this), 1 + offsetX, 2 + offsetY);
    }
    private void morceauDeNiveau1(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY);

        addEntiteStatique(new Porte(this,1), 0 + offsetX, 2 + offsetY);
        addEntiteStatique(new Porte(this,2), 4 + offsetX, 2 + offsetY);
        addEntiteStatique(new Porte(this,3), 2 + offsetX, 4 + offsetY);
        addEntiteStatique(new CaseVide(this), 1 + offsetX, 3 + offsetY);
        addEntiteStatique(new CaseFeu(this), 1 + offsetX, 2 + offsetY);
    }
    private void morceauDeNiveau2(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY);

        addEntiteStatique(new Porte(this,4), 1 + offsetX, 0 + offsetY);
        addEntiteStatique(new Porte(this,5), 2 + offsetX, 4 + offsetY);
        addEntiteStatique(new Porte(this,6), 4 + offsetX, 2 + offsetY);
        addEntiteStatique(new CaseVide(this), 1 + offsetX, 3 + offsetY);
        addEntiteStatique(new CaseFeu(this), 1 + offsetX, 2 + offsetY);
    }
    private void morceauDeNiveau3(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY);

        addEntiteStatique(new Porte(this,7), 0 + offsetX, 2 + offsetY);
        addEntiteStatique(new Porte(this,8), 4 + offsetX, 2 + offsetY);
        addEntiteStatique(new Porte(this,9), 2 + offsetX, 4 + offsetY);
        addEntiteStatique(new CaseVide(this), 1 + offsetX, 3 + offsetY);
        addEntiteStatique(new CaseFeu(this), 1 + offsetX, 2 + offsetY);
    }
    private void morceauDeNiveau4(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY);

        addEntiteStatique(new Porte(this,10), 1 + offsetX, 0 + offsetY);
        addEntiteStatique(new Porte(this,11), 4 + offsetX, 2 + offsetY);
        addEntiteStatique(new CaseVide(this), 1 + offsetX, 3 + offsetY);
        addEntiteStatique(new CaseFeu(this), 1 + offsetX, 2 + offsetY);
    }
    private void morceauDeNiveauMur(int offsetX, int offsetY){
        for (int x = offsetX; x < 5 + offsetX; x++) {
            addEntiteStatique(new Mur(this), x, 0 + offsetY);
            addEntiteStatique(new Mur(this), x, 4 + offsetY);
        }
        // murs extérieurs verticaux
        for (int y = offsetY +1 ; y < 4 + offsetY; y++) {
            addEntiteStatique(new Mur(this), 0 + offsetX, y);
            addEntiteStatique(new Mur(this), 4 + offsetX, y);
        }
    }

    public void start() {
        new Thread(this).start();
    }

    public void run() {

        while(true) {

            setChanged();
            notifyObservers();

            try {
                Thread.sleep(pause);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


    private void addEntiteStatique(EntiteStatique e, int x, int y) {
        grilleEntitesStatiques[x][y] = e;

    }

}
