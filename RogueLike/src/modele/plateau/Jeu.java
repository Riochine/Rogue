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

    public static final int SIZE_X = 15;
    public static final int SIZE_Y = 15;
    public static final int TAILLE_SALLE = 5;

    private int pause = 200; // période de rafraichissement

    private Heros heros;

    private EntiteStatique[][] grilleEntitesStatiques = new EntiteStatique[SIZE_X][SIZE_Y];
    private Collectible[][] grilleEntitesCollectibles = new Collectible[SIZE_X][SIZE_Y];

    private int [][]tabCoorSalle;
    private int salleCourante;

    public Jeu() {
        heros = new Heros(this, 1, 1);
        morceauDeNiveauStandard();
        salleCourante = 0;

        //construcion d'un tableau de coordonee de niveau
        tabCoorSalle = new int[][]{
                new int[] {0,0},
                new int[] {5,0},
                new int[] {10,0},
                new int[] {0,5},
                new int[] {5,5},
                new int[] {10,5},
                new int[] {0,10},
                new int[] {5,10},
                new int[] {10,10}
        };
        //placement des niveaux selon ces même coordonee
        morceauDeNiveau0(tabCoorSalle[0][0],tabCoorSalle[0][1]);
        morceauDeNiveau1(tabCoorSalle[1][0],tabCoorSalle[1][1]);
        morceauDeNiveau2(tabCoorSalle[2][0],tabCoorSalle[2][1]);
        morceauDeNiveau3(tabCoorSalle[3][0],tabCoorSalle[3][1]);
        morceauDeNiveau4(tabCoorSalle[4][0],tabCoorSalle[4][1]);
        morceauDeNiveau5(tabCoorSalle[5][0],tabCoorSalle[5][1]);
        morceauDeNiveau6(tabCoorSalle[6][0],tabCoorSalle[6][1]);
        morceauDeNiveau7(tabCoorSalle[7][0],tabCoorSalle[7][1]);
        morceauDeNiveau8(tabCoorSalle[8][0],tabCoorSalle[8][1]);
    }

    private int getSalleCourante(){
        return salleCourante;
    }

    //*****Pour le coté vue
    //on regarde si le x y du temoin est bien dans la plage de valeur (verif + TAILLE_SALLE)
    private boolean dansLaPlage(int temoin [] ,int verif []){
        return verif[0] >= temoin[0]
                && verif[0] < temoin[0] + TAILLE_SALLE
                && verif[1] >= temoin[1]
                && verif[1] < temoin[1] + TAILLE_SALLE;
    }
    //*****Pour le coté vue
    //on construit une nouvelle plage de valeur
    private int [][] contruitNouvellePlage(int offset []){
        return new int[][]{
                new int[]{offset[0],offset[0] + TAILLE_SALLE},
                new int[]{offset[1],offset[1] + TAILLE_SALLE}
        };
    }
    //*****Pour le coté vue
    //Si jamais notre heros sort des limites de la salle courante
    //  -suppression des capsules
    //  -Ajout d'une capsule
    //  -donne de nouvelles dimention pour l'affichage
    public int [][] nouvelleSalle(){
        for(int i = 0 ; i < tabCoorSalle.length ; i++) {
            if (
                    dansLaPlage(tabCoorSalle[i], new int[]{heros.getX(), heros.getY()})
                            && salleCourante != i) {
                salleCourante = i;
                heros.getInventaire().supprimerCapsuleTout();
                heros.getInventaire().ajouter(new Capsule());
                return contruitNouvellePlage(tabCoorSalle[i]);
            }
        }
        return contruitNouvellePlage(tabCoorSalle[salleCourante]);
    }

    public int [][] getTabCoorSalle(){
        return tabCoorSalle;
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
            Collectible c = getEntiteCollectible(coords[0], coords[1]);
            found = ((e instanceof CaseNormale || e instanceof CaseUnique) &&
                    (c == null || c instanceof Coffre));
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
        morceauDeNiveauMur(offsetX,offsetY,new String []{"oui"});

        addEntiteStatique(new Porte(this,0), 4 + offsetX, 2 + offsetY);
        addEntiteStatique(new CaseVide(this), 2 + offsetX, 3 + offsetY);
        addEntiteStatique(new CaseFeu(this), 2 + offsetX, 3 + offsetY);

        Collectible [] tabcol = {new Clef(0),new Capsule()};
        tabRanCollectibleSurMap(tabcol,offsetX,offsetY);
    }
    private void morceauDeNiveau1(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY,new String []{"oui"});


        addEntiteStatique(new CaseUnique(this,0), 2 + offsetX, 1 + offsetY);

        addEntiteStatique(new CaseVide(this), 2 + offsetX, 2 + offsetY);
        addEntiteStatique(new CaseUnique(this,0), 3 + offsetX, 2 + offsetY);
        addEntiteStatique(new Mur(this), 2 + offsetX, 3 + offsetY);
        addEntiteCollectible(new Clef(2), 3 + offsetX, 3 + offsetY);
        addEntiteStatique(new Porte(this,0), 0 + offsetX, 2 + offsetY);
        addEntiteStatique(new Porte(this,2), 1 + offsetX, 4 + offsetY);

        Collectible [] tabcol = {new Capsule()};
        tabRanCollectibleSurMap(tabcol,offsetX,offsetY);
    }
    private void morceauDeNiveau2(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY,new String []{"ouvertVerticale","haut"});
    }
    private void morceauDeNiveau3(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY,new String []{"ouvertVerticale","haut"});
    }
    private void morceauDeNiveau4(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY,new String []{"oui"});

        addEntiteStatique(new CaseUnique(this,0), 1 + offsetX, 1 + offsetY);
        addEntiteStatique(new CaseUnique(this,0), 1 + offsetX, 2 + offsetY);
        addEntiteStatique(new CaseUnique(this,0), 1 + offsetX, 3 + offsetY);
        addEntiteStatique(new CaseUnique(this,0), 2 + offsetX, 1 + offsetY);
        addEntiteStatique(new CaseUnique(this,0), 2 + offsetX, 2 + offsetY);
        addEntiteStatique(new CaseUnique(this,0), 2 + offsetX, 3 + offsetY);
        addEntiteStatique(new CaseUnique(this,0), 3 + offsetX, 1 + offsetY);
        addEntiteCollectible(new Clef(4), 3 + offsetX, 2 + offsetY);
        addEntiteStatique(new CaseFeu(this), 3 + offsetX, 3 + offsetY);

        addEntiteStatique(new Porte(this,2), 1 + offsetX, 0 + offsetY);
        addEntiteStatique(new Porte(this,4), 1 + offsetX, 4 + offsetY);


        Collectible [] tabcol = {};
        tabRanCollectibleSurMap(tabcol,offsetX,offsetY);
    }
    private void morceauDeNiveau5(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY,new String []{"ouvertVerticale"});
    }
    private void morceauDeNiveau6(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY,new String []{"gauche","bas"});
    }
    private void morceauDeNiveau7(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY,new String []{"ouvertHorizontale","droite"});

        addEntiteStatique(new Porte(this,4), 1 + offsetX, 0 + offsetY);
        addEntiteStatique(new Porte(this,8), 4 + offsetX, 2 + offsetY);
        addEntiteStatique(new CaseVide(this), 1 + offsetX, 3 + offsetY);
        addEntiteStatique(new CaseFeu(this), 1 + offsetX, 2 + offsetY);

        Collectible [] tabcol = {new Clef(8),new Capsule()};
        tabRanCollectibleSurMap(tabcol,offsetX,offsetY);
    }
    private void morceauDeNiveau8(int offsetX, int offsetY){
        morceauDeNiveauMur(offsetX,offsetY,new String []{"ouvertVerticale","bas"});

        addEntiteStatique(new Porte(this,8), 0+ offsetX, 2 + offsetY);
    }
    private void morceauDeNiveauMur(int offsetX, int offsetY, String []placementMur){
        for(String i:placementMur){
            switch(i) {
                case "oui":
                {
                    for (int x = offsetX ; x < TAILLE_SALLE + offsetX; x++) {
                        addEntiteStatique(new Mur(this), x, 0 + offsetY);
                        addEntiteStatique(new Mur(this), x, 4 + offsetY);
                    }
                    // murs extérieurs verticaux
                    for (int y = offsetY ; y < TAILLE_SALLE + offsetY; y++) {
                        addEntiteStatique(new Mur(this), 0 + offsetX, y);
                        addEntiteStatique(new Mur(this), 4 + offsetX, y);
                    }
                }
                case "ouvertVerticale": //gauche
                {
                    for (int y = offsetY ; y < TAILLE_SALLE + offsetY; y++) {
                        addEntiteStatique(new Mur(this), 0 + offsetX, y);
                    }
                }
                case "droite":
                {
                    for (int y = offsetY ; y < TAILLE_SALLE + offsetY; y++) {
                        addEntiteStatique(new Mur(this), 4 + offsetX, y);
                    }
                }
                    break;
                case "gauche":
                {
                    for (int y = offsetY ; y < TAILLE_SALLE + offsetY; y++) {
                        addEntiteStatique(new Mur(this), 0 + offsetX, y);
                    }
                }
                    break;
                case "ouvertHorizontale": //haut
                {
                    for (int x = offsetX ; x < TAILLE_SALLE + offsetX; x++) {
                        addEntiteStatique(new Mur(this), x ,0 + offsetY);
                    }
                }
                case "bas":
                {
                    for (int x = offsetX ; x < TAILLE_SALLE + offsetX; x++) {
                        addEntiteStatique(new Mur(this), x ,4 + offsetY);
                    }
                }
                    break;
                case "haut":
                {
                    for (int x = offsetX ; x < TAILLE_SALLE + offsetX; x++) {
                        addEntiteStatique(new Mur(this), x ,0 + offsetY);
                    }
                }
                    break;
                default:
                    // code block
            }
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
    private  void clearEntiteStatique(){
        grilleEntitesStatiques = new EntiteStatique[SIZE_X][SIZE_Y];
    }

    private void addEntiteCollectible(Collectible e, int x, int y) {
        if(getEntiteCollectible(x,y) instanceof Coffre)((Coffre) getEntiteCollectible(x,y)).addCollToCoffre(e);
        else grilleEntitesCollectibles[x][y] = e;

    }

    public void supprimerEntiteCollectible(int x, int y) {
        if(CollectibleExiste(x, y)) {
            grilleEntitesCollectibles[x][y] = null;
        }
    }

    public void setEntiteStatique(int x, int y, CaseNormale entite) {
        grilleEntitesStatiques[x][y] = entite;
    }
}
