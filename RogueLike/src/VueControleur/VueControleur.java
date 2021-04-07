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

    //Constantes pour l'orientation du joueur
    private final int O_UP = 0;
    private final int O_RIGHT = 1;
    private final int O_DOWN = 2;
    private final int O_LEFT = 3;

    // icones affichées dans la grille
    private ImageIcon[] icoHero = new ImageIcon[4];
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
    private JLabel[] tabJLabelinventaire; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)


    public VueControleur(Jeu _jeu) {
        sizeX = jeu.SIZE_X;
        sizeY = _jeu.SIZE_Y;
        jeu = _jeu;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();
    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : jeu.getHeros().changer_direction(O_LEFT); break;
                    case KeyEvent.VK_RIGHT : jeu.getHeros().changer_direction(O_RIGHT);break;
                    case KeyEvent.VK_DOWN : jeu.getHeros().changer_direction(O_DOWN); break;
                    case KeyEvent.VK_UP : jeu.getHeros().changer_direction(O_UP); break;
                    case KeyEvent.VK_I: jeu.getHeros().afficherInventaire(); break;
                    case KeyEvent.VK_SPACE : jeu.action(); break;
                }
            }
        });
    }


    private void chargerLesIcones() {
        icoHero[0] = chargerIcone("Images/Player_UP.png");
        icoHero[1] = chargerIcone("Images/Player_RIGHT.png");
        icoHero[2] = chargerIcone("Images/Player_DOWN.png");
        icoHero[3] = chargerIcone("Images/Player_LEFT.png");
        icoCaseNormale = chargerIcone("Images/Normale.png");
        icoMur = chargerIcone("Images/Mur.png");
        icoVide = chargerIcone("Images/Vide.png");
        icoPorte = chargerIcone("Images/Porte.png");
        icoFeu = chargerIcone("Images/Feu.png");
        icoCoffre = chargerIcone("Images/Coffre.png");
        icoCapsule= chargerIcone("Images/Capsule.png");
        icoClef = chargerIcone("Images/Clef.png");
        icoUnique = icoCaseNormale;
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
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JPanel masterPanel = new JPanel(new BorderLayout());
        JComponent gameJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille
        JComponent inventaireJLabels = new JPanel(new GridLayout(sizeY, 1)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        masterPanel.add(gameJLabels,BorderLayout.CENTER);
        masterPanel.add(inventaireJLabels,BorderLayout.EAST);
        tabJLabelgame = new JLabel[sizeX][sizeY];
        tabJLabelinventaire = new JLabel[sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabelgame[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                gameJLabels.add(jlab);
            }
        }
        for (int y = 0 ; y < sizeY ; y++){
                JLabel jlab = new JLabel();
                tabJLabelinventaire[y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                inventaireJLabels.add(jlab);
        }
        add(masterPanel);
    }

    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
				EntiteStatique e = jeu.getEntiteStatique(x, y);
                Collectible c = jeu.getEntiteCollectible(x, y);

                if (e instanceof Mur) {
                    tabJLabelgame[x][y].setIcon(icoMur);
                } else if (e instanceof CaseVide) {
                    tabJLabelgame[x][y].setIcon(icoVide);
                }
                else if (e instanceof Porte) {
                    tabJLabelgame[x][y].setIcon(icoPorte);
                }
                else if (e instanceof CaseFeu) {
                    tabJLabelgame[x][y].setIcon(icoFeu);
                }
                else if (e instanceof CaseNormale) {
                    tabJLabelgame[x][y].setIcon(icoCaseNormale);
                }
                else if (e instanceof CaseUnique) {
                    tabJLabelgame[x][y].setIcon(icoUnique);
                }

                if(c instanceof Clef) {
                    tabJLabelgame[x][y].setIcon(icoClef);
                } else if(c instanceof Capsule) {
                    tabJLabelgame[x][y].setIcon(icoCapsule);

                }else if(c instanceof Coffre) {
                    tabJLabelgame[x][y].setIcon(icoCoffre);

                }
            }
        }
        for(int y = 0 ; y < sizeY ; y++){
            if(y < jeu.getHeros().getInventaire().getCollectiblesTab().length){
                Collectible ic = jeu.getHeros().getInventaire().getCollectiblesTab()[y];
                if(ic instanceof Clef) {
                    tabJLabelinventaire[y].setIcon(icoClef);
                } else if(ic instanceof Capsule) {
                    tabJLabelinventaire[y].setIcon(icoCapsule);

                }else if(ic instanceof Coffre) {
                    tabJLabelinventaire[y].setIcon(icoCoffre);

                }
            }else{
                tabJLabelinventaire[y].setIcon(icoCaseNormale);
            }

        }



        tabJLabelgame[jeu.getHeros().getX()][jeu.getHeros().getY()].setIcon(icoHero[jeu.getHeros().getOrientation()]);

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
