package VueControleur;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;


import collectible.Capsule;
import collectible.Clef;
import collectible.Coffre;
import collectible.Collectible;
import modele.plateau.*;
import modele.plateau.entiteStatique.*;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction, etc.))
 *
 */
public class VueControleur extends JFrame implements Observer {
    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX; // taille de la grille affichée
    private int sizeY;

    // icones affichées dans la grille
    private ImageIcon[] icoHeros = new ImageIcon[4];
    private ImageIcon icoCaseNormale;
    private ImageIcon icoMur;
    private ImageIcon icoVide;
    private ImageIcon icoPorte;
    private ImageIcon icoFeu;
    private ImageIcon icoCoffre;
    private ImageIcon icoCapsule;
    private ImageIcon icoClef;
    private ImageIcon icoColonne;
    private ImageIcon icoUnique;

    private JLabel[][] tabJLabelgame; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)
    private JLabel[][] tabJLabelinventaire; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)
    private JLabel[][] tabJLabelinfo; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)


    public VueControleur(Jeu _jeu) {
        sizeX = _jeu.TAILLE_SALLE;
        sizeY = _jeu.TAILLE_SALLE;
        /*sizeX = _jeu.SIZE_X;
        sizeY = _jeu.SIZE_Y;*/
        jeu = _jeu;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();
    }
    public void resetJeu(){
        jeu = new Jeu();
    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : jeu.getHeros().changer_direction(jeu.getHeros().O_LEFT); break;
                    case KeyEvent.VK_RIGHT : jeu.getHeros().changer_direction(jeu.getHeros().O_RIGHT);break;
                    case KeyEvent.VK_DOWN : jeu.getHeros().changer_direction(jeu.getHeros().O_DOWN); break;
                    case KeyEvent.VK_UP : jeu.getHeros().changer_direction(jeu.getHeros().O_UP); break;
                    case KeyEvent.VK_I: jeu.getHeros().afficherInventaire(); break;
                    case KeyEvent.VK_SPACE : jeu.getHeros().action(); break;
                    case KeyEvent.VK_R : resetJeu(); break;
                }
            }
        });
    }


    private void chargerLesIcones() {
        icoHeros[0] = chargerIcone("Images/Player_UP.png");
        icoHeros[1] = chargerIcone("Images/Player_RIGHT.png");
        icoHeros[2] = chargerIcone("Images/Player_DOWN.png");
        icoHeros[3] = chargerIcone("Images/Player_LEFT.png");
        icoCaseNormale = chargerIcone("Images/Normale.png");
        icoMur = chargerIcone("Images/Mur.png");
        icoVide = chargerIcone("Images/Vide.png");
        icoPorte = chargerIcone("Images/Porte.png");
        icoFeu = chargerIcone("Images/Feu.png");
        icoCoffre = chargerIcone("Images/Coffre.png");
        icoCapsule= chargerIcone("Images/Capsule.png");
        icoClef = chargerIcone("Images/Clef.png");
        icoUnique = chargerIcone("Images/Unique.png");;
    }

    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleur.class.getName()).log(Level.SEVERE, urlIcone, ex);
            return null;
        }

        return new ImageIcon(image);
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Roguelike");
        setSize(600, 425);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JPanel masterPanel = new JPanel(new BorderLayout());
        JComponent gameJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille
        JComponent inventaireJLabels = new JPanel(new GridLayout(sizeY, 2)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille
        JComponent infoJlabel = new JPanel(new GridLayout(1, sizeX+2)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        masterPanel.add(gameJLabels,BorderLayout.CENTER);
        masterPanel.add(inventaireJLabels,BorderLayout.EAST);
        masterPanel.add(infoJlabel,BorderLayout.SOUTH);
        tabJLabelgame = new JLabel[sizeX][sizeY];
        tabJLabelinventaire = new JLabel[2][sizeY];
        tabJLabelinfo = new JLabel[sizeX + 2][1];

        //pré construction de la fenetre de jeu
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabelgame[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                gameJLabels.add(jlab);
            }
        }
        //pré construction de la fenetre d'inventaire
        for (int y = 0 ; y < sizeY ; y++) {
            for (int x = 0; x < 2; x++) {
                JLabel jlab = new JLabel();
                tabJLabelinventaire[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                inventaireJLabels.add(jlab);
            }
        }
            JLabel jlab = new JLabel();
            tabJLabelinfo[0][0] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
            infoJlabel.add(jlab);
        add(masterPanel);
    }

    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {
        int tabNewCoord [][] = jeu.nouvelleSalle();
        int minX = tabNewCoord[0][0];
        int maxX = tabNewCoord[0][1];
        int minY = tabNewCoord[1][0];
        int maxY = tabNewCoord[1][1];
        /*int minX = 0;
        int maxX = sizeX;
        int minY = 0;
        int maxY = sizeY;*/

        //Mise a jour du jeu
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
				EntiteStatique e = jeu.getEntiteStatique(x, y);
                Collectible c = jeu.getEntiteCollectible(x, y);

                //e texte du JLabel n'est pas reset automatiquement
                tabJLabelgame[x-minX][y-minY].setText("");

                if (e instanceof Mur) {
                    tabJLabelgame[x-minX][y-minY].setIcon(icoMur);
                } else if (e instanceof CaseVide) {
                    tabJLabelgame[x-minX][y-minY].setIcon(icoVide);
                }
                else if (e instanceof Porte) {
                    /*
                    On place l'icone de la porte;
                    Puis on ajoute son identifiant au JLabel, au centre;
                    On change la couleur pour plus de lisibilité.
                     */
                    tabJLabelgame[x-minX][y-minY].setIcon(icoPorte);
                    tabJLabelgame[x-minX][y-minY].setText(String.valueOf(((Porte) e).getIdPorte()));
                    tabJLabelgame[x-minX][y-minY].setHorizontalTextPosition(JLabel.CENTER);
                    tabJLabelgame[x-minX][y-minY].setForeground(Color.white);
                }
                else if (e instanceof CaseFeu) {
                    tabJLabelgame[x-minX][y-minY].setIcon(icoFeu);
                }
                else if (e instanceof CaseNormale) {
                    tabJLabelgame[x-minX][y-minY].setIcon(icoCaseNormale);
                }
                else if (e instanceof CaseUnique) {
                    tabJLabelgame[x-minX][y-minY].setIcon(icoUnique);
                }

                if(c instanceof Clef) {
                    tabJLabelgame[x-minX][y-minY].setIcon(icoClef);
                    tabJLabelgame[x-minX][y-minY].setText(String.valueOf(((Clef) c).getId()));
                    tabJLabelgame[x-minX][y-minY].setHorizontalTextPosition(JLabel.CENTER);
                    tabJLabelgame[x-minX][y-minY].setForeground(Color.black);
                } else if(c instanceof Capsule) {
                    tabJLabelgame[x-minX][y-minY].setIcon(icoCapsule);

                }else if(c instanceof Coffre) {
                    tabJLabelgame[x-minX][y-minY].setIcon(icoCoffre);

                }
            }
        }

        //mise a jour de l'inventaire
        int ci = 0;
        for(int y = 0 ; y < jeu.TAILLE_SALLE ; y++){
            for (int x = 0 ; x < 2 ; x++){
                if(ci < jeu.getHeros().getInventaire().getCollectiblesTab().length){
                    Collectible ic = jeu.getHeros().getInventaire().getCollectiblesTab()[ci];

                    int y_tmp = y + 1;

                    //reset le text du JLabel
                    tabJLabelinventaire[x][y_tmp].setText(null);

                    if(ic instanceof Clef) {
                        tabJLabelinventaire[x][y_tmp].setIcon(icoClef);
                        //tabJLabelinventaire[y].setHorizontalTextPosition(JLabel.CENTER);

                        tabJLabelinventaire[x][y_tmp].setText("Porte n° " + String.valueOf(((Clef) ic).getId()));
                        tabJLabelinventaire[x][y_tmp].setForeground(Color.black);
                    } else if(ic instanceof Capsule) {
                        tabJLabelinventaire[x][y_tmp].setIcon(icoCapsule);
                        tabJLabelinventaire[x][y_tmp].setText("Eau");

                    }else if(ic instanceof Coffre) {
                        tabJLabelinventaire[x][y_tmp].setIcon(icoCoffre);

                    }
                }else{
                    if(y + 1 < jeu.TAILLE_SALLE) {
                        tabJLabelinventaire[x][y + 1].setIcon(null);
                        tabJLabelinventaire[x][y + 1].setText(null);
                    }
                }
                ci++;
            }

        }

        tabJLabelinventaire[0][0].setText("-- Inventaire --");
        tabJLabelinfo[0][0].setText("touche d'action: espace | touche de reset: r | "+jeu.getNomNiveauCourant() + "|" + jeu.getNomSalleCourante());

        //mise a jour de la position du hero dans la fenettre
        tabJLabelgame[jeu.getHeros().getX()-minX][jeu.getHeros().getY()-minY].setIcon(icoHeros[jeu.getHeros().getOrientation()]);

    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
        /*
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
        */

    }
}
