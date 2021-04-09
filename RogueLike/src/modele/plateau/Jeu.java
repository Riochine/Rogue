/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import collectible.Clef;
import collectible.Capsule;
import collectible.Coffre;
import collectible.Collectible;
import modele.plateau.entiteStatique.*;

import java.util.Observable;
import java.util.Random;


public class Jeu extends Observable implements Runnable {

    public static final int SIZE_X = 18;
    public static final int SIZE_Y = 16;

    //Constantes pour l'orientation du joueur
    private final int O_UP = 0;
    private final int O_RIGHT = 1;
    private final int O_DOWN = 2;
    private final int O_LEFT = 3;

    private int pause = 200; // période de rafraichissement

    private Heros heros;

    private EntiteStatique[][] grilleEntitesStatiques = new EntiteStatique[SIZE_X][SIZE_Y];
    private Collectible[][] grilleEntitesCollectibles = new Collectible[SIZE_X][SIZE_Y];

    public Jeu() {
        heros = new Heros(this, 1, 1);
        morceauDeNiveauStandard();
        morceauDeNiveau0(0,0);
        morceauDeNiveau1(5,0);
        morceauDeNiveau2(6,5);
        morceauDeNiveau3(11,5);
        morceauDeNiveau4(12,10);
    }

    public Heros getHeros() {
        return heros;
    }

    public EntiteStatique[][] getGrilleEntitesStatiques() {
        return grilleEntitesStatiques;
    }

	public EntiteStatique getEntiteStatique(int x, int y) {
		if (x < 0 || x >= SIZE_X || y < 0 || y >= SIZE_Y) {
			// L'entité demandée est en-dehors de la grille
			return null;
		}
		return grilleEntitesStatiques[x][y];
	}

    public Collectible[][] getGrilleEntitesCollectibles() {
        return grilleEntitesCollectibles;
    }

    public boolean CollectibleExiste(int x, int y) {
        return grilleEntitesCollectibles[x][y] != null;
    }

    public Collectible getEntiteCollectible(int x, int y) {
        if (x < 0 || x >= SIZE_X || y < 0 || y >= SIZE_Y) {
            // L'entité demandée est en-dehors de la grille
            return null;
        }
        return grilleEntitesCollectibles[x][y];
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

    private void tabRanCollectibleSurMap(Collectible [] tabcol, int debX, int debY){
        int tmp[];
        for (Collectible cl:tabcol){
            tmp = CoordsCaseNormale(debX, debY);
            addEntiteCollectible(cl ,tmp[0],tmp[1]);
        }
    }

    private int[] CoordsCaseNormale(int debX, int debY) {
        int[] coords = new int[2];
        boolean found = false;

        while(!found) {
            coords[0] = debX + 1 + (int)(Math.random() * 3);
            coords[1] = debY + 1 + (int)(Math.random() * 3);
            EntiteStatique e = getEntiteStatique(coords[0], coords[1]);
            found = ((e instanceof CaseNormale || e instanceof CaseUnique)
                    && getEntiteCollectible(coords[0], coords[1]) == null);
        }

        return coords;
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
        addEntiteStatique(new CaseUnique(this, 0), 3 + offsetX, 1 + offsetY);

        Collectible [] tabcol = {new Clef(0),new Capsule(),new Coffre()};
        tabRanCollectibleSurMap(tabcol,offsetX,offsetY);
    }
    private void morceauDeNiveau1(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY);

        addEntiteStatique(new Porte(this,0), 0 + offsetX, 2 + offsetY);
        addEntiteStatique(new Porte(this,2), 4 + offsetX, 2 + offsetY);
        addEntiteStatique(new Porte(this,3), 2 + offsetX, 4 + offsetY);
        addEntiteStatique(new CaseVide(this), 1 + offsetX, 3 + offsetY);
        addEntiteStatique(new CaseFeu(this), 1 + offsetX, 2 + offsetY);

        Collectible [] tabcol = {new Clef(2),new Clef(3),new Capsule()};
        tabRanCollectibleSurMap(tabcol,offsetX,offsetY);
    }
    private void morceauDeNiveau2(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY);

        addEntiteStatique(new Porte(this,3), 1 + offsetX, 0 + offsetY);
        addEntiteStatique(new Porte(this,5), 2 + offsetX, 4 + offsetY);
        addEntiteStatique(new Porte(this,6), 4 + offsetX, 2 + offsetY);
        addEntiteStatique(new CaseVide(this), 1 + offsetX, 3 + offsetY);
        addEntiteStatique(new CaseFeu(this), 1 + offsetX, 2 + offsetY);


        Collectible [] tabcol = {new Clef(5),new Clef(6),new Capsule(),new Coffre()};
        tabRanCollectibleSurMap(tabcol,offsetX,offsetY);
    }
    private void morceauDeNiveau3(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY);

        addEntiteStatique(new Porte(this,6), 0 + offsetX, 2 + offsetY);
        addEntiteStatique(new Porte(this,8), 4 + offsetX, 2 + offsetY);
        addEntiteStatique(new Porte(this,9), 2 + offsetX, 4 + offsetY);
        addEntiteStatique(new CaseVide(this), 1 + offsetX, 3 + offsetY);
        addEntiteStatique(new CaseFeu(this), 1 + offsetX, 2 + offsetY);

        Collectible [] tabcol = {new Clef(8),new Clef(9),new Capsule()};
        tabRanCollectibleSurMap(tabcol,offsetX,offsetY);
    }
    private void morceauDeNiveau4(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY);

        addEntiteStatique(new Porte(this,9), 1 + offsetX, 0 + offsetY);
        addEntiteStatique(new Porte(this,11), 4 + offsetX, 2 + offsetY);
        addEntiteStatique(new CaseVide(this), 1 + offsetX, 3 + offsetY);
        addEntiteStatique(new CaseFeu(this), 1 + offsetX, 2 + offsetY);

        Collectible [] tabcol = {new Clef(11),new Coffre(),new Capsule()};
        tabRanCollectibleSurMap(tabcol,offsetX,offsetY);
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

    private void addEntiteCollectible(Collectible e, int x, int y) {
        grilleEntitesCollectibles[x][y] = e;

    }

    /*
     * Différentes actions du joueur selon ce qu'il se trouve devant lui
     */
    public void action() {
        int x = getHeros().getX(), y = getHeros().getY();

        switch (getHeros().getOrientation()) {
            case O_UP: y--; break;
            case O_RIGHT: x++; break;
            case O_DOWN: y++; break;
            case O_LEFT: x--; break;
            default: break;
        }

        //Ramasser un collectible
        if(CollectibleExiste(x, y)) {
            System.out.println("Vous ramassez : " + getEntiteCollectible(x, y).getName());
            getHeros().ajoutInventaire(getEntiteCollectible(x, y));
            supprimerEntiteCollectible(x, y);
        }

        //Porte
        EntiteStatique e = getEntiteStatique(x, y);
        if(e instanceof Porte) {
            int idPorte = ((Porte) e).getIdPorte();
            if(getHeros().possedeClef(idPorte)) {
                getHeros().supprimerClef(idPorte);
                grilleEntitesStatiques[x][y] = new CaseNormale(this);

                switch (getHeros().getOrientation()) {
                    case O_UP: y--; break;
                    case O_RIGHT: x++; break;
                    case O_DOWN: y++; break;
                    case O_LEFT: x--; break;
                    default: break;
                }

                e = getEntiteStatique(x, y);
                if(e instanceof Porte)
                    if(idPorte == ((Porte) e).getIdPorte())
                        grilleEntitesStatiques[x][y] = new CaseNormale(this);
            }
        }
        else if (e instanceof CaseFeu) {
            if(getHeros().possedeCapsule()) {
                grilleEntitesStatiques[x][y] = new CaseNormale(this);
                getHeros().supprimerCapsule();
            }
        }
    }

    private void supprimerEntiteCollectible(int x, int y) {
        if(CollectibleExiste(x, y)) {
            grilleEntitesCollectibles[x][y] = null;
        }
    }
}
