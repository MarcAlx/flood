/**
 *
 * Created by Marc-Alexandre Blanchard - all right reserved ©
 *
 * 2014
 *
 */
package com.flood.game;

import com.toaster.engine.Toaster;
import com.toaster.enums.ApparitionStyle;
import com.toaster.enums.RemoveStyle;
import com.toaster.exceptions.TooManyToastException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author Crée par Marc-Alexandre Blanchard
 */
public class GameWindow extends JFrame implements ActionListener, WindowListener, KeyListener
{

    //Matrice de jeu
    private final int longueur = 27;// 2 cases vides, 24 cases de Jeu, 1 case vide
    private final int largeur = 26; // 1 case vide, 24 cases de jeu, 1 case vide
    private int[][] matrice;
    private final int epaisseurCarreau = 30;
    //Point de départ
    private final int startingX = 1;  //Demarrage à la Deuxieme case en largeur
    private final int startingY = 25; //Demarrage à la 26 case en Longeur 
    //Barre des menus
    private JMenuBar menuBar;
    private JMenu menuFlood;
    private JMenu menuAbout;
    private JMenuItem menuCreator;
    private JMenuItem menuVersion;
    private JMenuItem white;
    private JMenuItem blue;
    private JMenuItem yellow;
    private JMenuItem orange;
    private JMenuItem red;
    private JMenuItem green;
    private JMenuItem menuStart;
    private JMenuItem menuInstruction;
    private JMenuItem Vers;
    private JMenuItem AP;
    private JMenuItem menuQuit;
    //Attribut de jeu
    //Nb de coup joué durant la partie
    private int COUPJOUE;
    //Temps passé durant la partie
    private int TEMPSJOUE;
    //Maximun de coup jouable
    private final int MAXCOUP = 50;
    //Meilleurs nombre de coup en une partie
    private int COUPRECORD = 50;
    //Meilleurs temps initialisé à 10000 pour que le nouveau temps soit forcement plus petit et sauvegardé
    private int MEILLEURTEMPS = 10000;
    //Couleur actuel initialisation sans importance
    private int actualColor = 1;
    private Boolean ONPLAY = false;
    private ThreadTemps TT;
    //Pour afficher les instructions dans la fenetre d'instruction
    private final String Instructions = "Flood\nFlood the matrix with the same color in less than " + MAXCOUP + " color change.\nTo achieve it you have to flood the matrix by changing color,\nat first you can only change the color of the lower left corner of the matrix.\nAs you change color, you control each related square to the area you already control.\nPlay with 1,2,3,4,5,6 or via Menu. Press S or space to start.";
    //Version du jeu, destiné à être affiché
    private final String Version = "1.1";
    private final Toaster t;
    private JMenuItem menuScoreTime;
    private JMenuItem menuScorePoint;
    
    private ImageIcon icon;

    //Constructeur
    public GameWindow()
    {
        try
        {
            icon = new ImageIcon(ImageIO.read(getClass().getResource("/com/flood/images/flood_icon.png")));
        }
        catch (IOException ex)
        {
        }
        t = Toaster.getInstance();
        //Pour eviter les bugs d'interface graphique disparition de boutons du menu
        matrice = new int[largeur][longueur];
        remplirFond();
    }

    /**
     * Initalise une partie
     */
    public void init()
    {
        ONPLAY = false;
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException ex)
        {
        }
        //Initialisation des paramètres de jeu
        COUPJOUE = 0;
        TEMPSJOUE = 0;
        ONPLAY = true;

        //Remplissage de la zone de jeu
        matrice = new int[largeur][longueur];
        remplirFond();
        remplirCouleur();
        buildGame(getGraphics());

        //Démarrage du Thread
        TT = new ThreadTemps(this);
        TT.start();
    }

    /**
     * Remplissage du Fond avec des 0
     */
    private void remplirFond()
    {
        for (int i = 0; i < getLargeur(); i++)
        {
            for (int j = 0; j < getLongueur(); j++)
            {
                getMatrice()[i][j] = 0;
            }
        }
    }

    /**
     * Remplissage de l'aire de Jeu avec des couleurs aléatoires
     */
    private void remplirCouleur()
    {
        int MinColor = 1;
        int MaxColor = 7;
        //Ajout des couleurs
        for (int i = 1; i < getLargeur() - 1; i++)
        {
            for (int j = 2; j < getLongueur() - 1; j++)
            {
                donothing();
                getMatrice()[i][j] = (int) (Math.random() * (MaxColor - MinColor)) + MinColor;
            }
        }
        actualColor = getMatrice()[startingX][startingY];
    }

    /**
     * Construit l'interface graphique du jeu
     */
    public void buildGUI()
    {
        //Barre des menus
        menuBar = new JMenuBar();

        menuFlood = new JMenu("Flood");
        menuStart = new JMenuItem("Start (S)");
        menuFlood.add(menuStart);
        menuScoreTime = new JMenuItem("Best time : " + MEILLEURTEMPS + "s");
        menuFlood.add(menuScoreTime);
        menuScorePoint = new JMenuItem("Best : " + COUPRECORD + "/" + getMAXCOUP());
        menuFlood.add(menuScorePoint);
        menuInstruction = new JMenuItem("Instructions");
        menuFlood.add(menuInstruction);

        menuQuit = new JMenuItem("Quit");
        menuFlood.add(menuQuit);
        menuBar.add(menuFlood);

        white = new JMenuItem("White (1)");
        menuBar.add(white);

        blue = new JMenuItem("Blue (2)");
        menuBar.add(blue);

        yellow = new JMenuItem("Yellow (3)");
        menuBar.add(yellow);

        orange = new JMenuItem("Orange (4)");
        menuBar.add(orange);

        red = new JMenuItem("Red (5)");
        menuBar.add(red);

        green = new JMenuItem("Green (6)");
        menuBar.add(green);
        setJMenuBar(menuBar);

        menuAbout = new JMenu("About");
        menuCreator = new JMenuItem("Developped by Marc-Alexandre Blanchard");
        menuAbout.add(menuCreator);
        menuVersion = new JMenuItem("Version " + Version);
        menuAbout.add(menuVersion);
        menuBar.add(menuAbout);

        //Titre temporaire
        this.setTitle("Flood");

        this.toFront();
        this.setSize(800, 800);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.menuStart.addActionListener(this);
        this.menuQuit.addActionListener(this);
        this.menuCreator.addActionListener(this);
        this.white.addActionListener(this);
        this.blue.addActionListener(this);
        this.orange.addActionListener(this);
        this.red.addActionListener(this);
        this.yellow.addActionListener(this);
        this.green.addActionListener(this);
        this.menuInstruction.addActionListener(this);
        this.addWindowListener(this);
        this.addKeyListener(this);

    }

    /**
     * Construit l'aire de jeu, remplace l'overide de la méthode paint(Graphics
     * g) qui était appellé trop souvent Permet de controler l'afichage.
     *
     * @param g
     */
    public void buildGame(Graphics g)
    {
        this.setTitle("Flood - " + getCOUPJOUE() + "/" + getMAXCOUP() + " - " + getTEMPSJOUE() + "s");

        for (int i = 0; i < getLargeur(); i++)
        {
            for (int j = 0; j < getLongueur(); j++)
            {
                if (getMatrice()[i][j] == 1)
                {
                    g.setColor(Color.WHITE);
                    g.drawRect(i * epaisseurCarreau, j * epaisseurCarreau, epaisseurCarreau, epaisseurCarreau);
                    g.fillRect(i * epaisseurCarreau, j * epaisseurCarreau, epaisseurCarreau, epaisseurCarreau);
                }
                else if (getMatrice()[i][j] == 2)
                {
                    g.setColor(Color.BLUE);
                    g.drawRect(i * epaisseurCarreau, j * epaisseurCarreau, epaisseurCarreau, epaisseurCarreau);
                    g.fillRect(i * epaisseurCarreau, j * epaisseurCarreau, epaisseurCarreau, epaisseurCarreau);
                }
                else if (getMatrice()[i][j] == 3)
                {
                    g.setColor(Color.YELLOW);
                    g.drawRect(i * epaisseurCarreau, j * epaisseurCarreau, epaisseurCarreau, epaisseurCarreau);
                    g.fillRect(i * epaisseurCarreau, j * epaisseurCarreau, epaisseurCarreau, epaisseurCarreau);

                }
                else if (getMatrice()[i][j] == 4)
                {
                    g.setColor(Color.ORANGE);
                    g.drawRect(i * epaisseurCarreau, j * epaisseurCarreau, epaisseurCarreau, epaisseurCarreau);
                    g.fillRect(i * epaisseurCarreau, j * epaisseurCarreau, epaisseurCarreau, epaisseurCarreau);
                }
                else if (getMatrice()[i][j] == 5)
                {
                    g.setColor(Color.RED);
                    g.drawRect(i * epaisseurCarreau, j * epaisseurCarreau, epaisseurCarreau, epaisseurCarreau);
                    g.fillRect(i * epaisseurCarreau, j * epaisseurCarreau, epaisseurCarreau, epaisseurCarreau);
                }
                else if (getMatrice()[i][j] == 6)
                {
                    g.setColor(Color.GREEN);
                    g.drawRect(i * epaisseurCarreau, j * epaisseurCarreau, epaisseurCarreau, epaisseurCarreau);
                    g.fillRect(i * epaisseurCarreau, j * epaisseurCarreau, epaisseurCarreau, epaisseurCarreau);
                }
            }
        }
    }

    public void donothing()
    {
    }

    /**
     * Fonction récursive
     *
     * @param X
     * @param Y
     * @param NewColor
     */
    public void flood(int X, int Y, int NewColor)
    {
        //Si la case est de la couleur actuel et n'est pas de la nouvelle couleur
        //Avec cette vérification on ne floodera pas les bordures qui ne sont pas de la couleur actuelle
        if (getMatrice()[X][Y] == actualColor && getMatrice()[X][Y] != NewColor)
        {
            getMatrice()[X][Y] = NewColor;
            //Flood des cases connexe
            flood(X, Y - 1, NewColor);
            flood(X, Y + 1, NewColor);
            flood(X - 1, Y, NewColor);
            flood(X + 1, Y, NewColor);
        }
    }

    /**
     * Action associée aux commandes couleurs du menu 1 Blanc 2 Bleu 3 Jaune 4
     * Orange 5 Rouge 6 Vert
     *
     * @param Color
     */
    public void colorisation(int Color)
    {
        if (COUPJOUE <= MAXCOUP && ONPLAY == true)
        {
            //On ne peut flooder avec la couleur précédente ainsi on ne joue pas de coup inutile
            if (actualColor != Color)
            {
                flood(startingX, startingY, Color);
                buildGame(this.getGraphics());
                actualColor = Color;
                COUPJOUE++;
                this.setTitle("Flood - " + getCOUPJOUE() + "/" + getMAXCOUP() + " - " + getTEMPSJOUE() + "s");
            }

            //Si on a pas dépassé le nombre de coup à jouer et si on a gagné
            if (Win())
            {
                ONPLAY = false;
                //On actualise le score
                actualiserScoreCoups();
                actualiserScoreTemps();

                //Actualisation du Score dans le menu
                menuScorePoint.setText("Best time : " + MEILLEURTEMPS + "s");
                menuScoreTime.setText("Best : " + COUPRECORD + "/" + getMAXCOUP());

                int REP = JOptionPane.showConfirmDialog(this, "Win in " + getCOUPJOUE() + " and " + getTEMPSJOUE() + "s , Can you improve your score ?", "Win !", JOptionPane.YES_NO_OPTION);
                //Le choix dans la Joptionpane définit la suite du jeu
                if (REP == JOptionPane.YES_OPTION)
                {
                    this.init();
                }
                else
                {
                    this.quit();
                }
            }
            //Si c'est notre dernier coup et que l'on à pas encore gagné
            else if (COUPJOUE == MAXCOUP && !Win())
            {
                ONPLAY = false;
                int REP = JOptionPane.showConfirmDialog(this, "Game over.\n\nRestart ?", "Game over", JOptionPane.YES_NO_OPTION);
                //Le choix dans la Joptionpane définit la suite du jeu
                if (REP == JOptionPane.YES_OPTION)
                {
                    this.init();
                }
                else
                {
                    this.quit();
                }
            }

        }
    }

    /*
     Retourne vrai si la partie est gagné, faux sinon
     */
    public boolean Win()
    {
        for (int i = 1; i < getLargeur() - 1; i++)
        {
            for (int j = 2; j < getLongueur() - 1; j++)
            {
                //Si on tombe sur une case différente de la couleur actuel
                if (getMatrice()[i][j] != actualColor)
                {
                    return false;
                }
            }
        }
        //Si on est pas tombé sur une case de couleur différente on renvoie vrai et on arrete le jeu.
        ONPLAY = false;
        return true;
    }

    /**
     * Actualise le score : Nombre de coups
     *
     */
    public void actualiserScoreCoups()
    {
        if (this.COUPJOUE < this.COUPRECORD)
        {
            COUPRECORD = COUPJOUE;
        }
    }

    /**
     * Actualise le Score : Temps
     *
     */
    public void actualiserScoreTemps()
    {
        if (this.TEMPSJOUE < this.MEILLEURTEMPS)
        {
            MEILLEURTEMPS = TEMPSJOUE;
        }
    }

    /**
     * Affiche un message de bienvenue
     *
     */
    public void bienvenue()
    {
        JOptionPane.showMessageDialog(this, "Press S or space to start");
    }

    /**
     * Quitte le jeu
     */
    public void quit()
    {
        System.exit(0);
    }

    /**
     * @return the longueur
     */
    public int getLongueur()
    {
        return longueur;
    }

    /**
     * @return the largeur
     */
    public int getLargeur()
    {
        return largeur;
    }

    /**
     * @return the matrice
     */
    public int[][] getMatrice()
    {
        return matrice;
    }

    @Override
    public void windowClosing(WindowEvent we)
    {
    }

    @Override
    public void windowClosed(WindowEvent we)
    {
    }

    @Override
    public void windowIconified(WindowEvent we)
    {
    }

    @Override
    public void windowDeiconified(WindowEvent we)
    {
    }

    @Override
    public void windowActivated(WindowEvent we)
    {
    }

    @Override
    public void windowDeactivated(WindowEvent we)
    {
    }

    @Override
    public void windowOpened(WindowEvent we)
    {
        //Affiche un message de bienvenue
        this.bienvenue();
    }

    /**
     * @return the COUPJOUE
     */
    public int getCOUPJOUE()
    {
        return COUPJOUE;
    }

    /**
     * @return the MAXCOUP
     */
    public int getMAXCOUP()
    {
        return MAXCOUP;
    }

    /**
     * @return the TEMPSJOUE
     */
    public int getTEMPSJOUE()
    {
        return TEMPSJOUE;
    }

    /**
     * @param TEMPSJOUE the TEMPSJOUE to set
     */
    public void setTEMPSJOUE(int TEMPSJOUE)
    {
        this.TEMPSJOUE = TEMPSJOUE;
    }

    /**
     * @return the ONPLAY
     */
    public Boolean getONPLAY()
    {
        return ONPLAY;
    }

    @Override
    public void keyTyped(KeyEvent ke)
    {
    }

    @Override
    public void keyPressed(KeyEvent ke)
    {
        //Association des couleurs à une touche du clavier
        // 1 -> Blanc
        // 2 -> Bleu
        // 3 -> Jaune
        // 4 -> Orange
        // 5 -> Rouge
        // 6 -> Vert
        //Appuyer sur espace démarre le jeu
        if ((ke.getKeyCode() == KeyEvent.VK_1 && ONPLAY))
        {
            colorisation(1);
        }
        else if ((ke.getKeyCode() == KeyEvent.VK_2) && ONPLAY)
        {
            colorisation(2);
        }
        else if ((ke.getKeyCode() == KeyEvent.VK_3) && ONPLAY)
        {
            colorisation(3);
        }
        else if ((ke.getKeyCode() == KeyEvent.VK_4) && ONPLAY)
        {
            colorisation(4);
        }
        else if ((ke.getKeyCode() == KeyEvent.VK_5) && ONPLAY)
        {
            colorisation(5);
        }
        else if ((ke.getKeyCode() == KeyEvent.VK_6) && ONPLAY)
        {
            colorisation(6);
        }
        else if ((ke.getKeyCode() == KeyEvent.VK_SPACE) || (ke.getKeyCode() == KeyEvent.VK_S))
        {
            init();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke)
    {
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object comp = e.getSource();
        if (comp == menuCreator)
        {
            if (t.getNbToastOnScreen() < 1)
            {
                try
                {
                    t.Toast("Feedback / Contact ?\nmarc-alx@outlook.com",icon, ApparitionStyle.TrayLeft, RemoveStyle.TrayRight, 3000);
                }
                catch (TooManyToastException ex)
                {
                }
            }
        }
        else if (comp == menuStart)
        {
            this.init();
        }
        else if (comp == menuQuit)
        {
            this.quit();
        }
        else if (comp == white)
        {
            this.colorisation(1);
        }
        else if (comp == blue)
        {
            this.colorisation(2);
        }
        else if (comp == yellow)
        {
            this.colorisation(3);
        }
        else if (comp == orange)
        {
            this.colorisation(4);
        }
        else if (comp == red)
        {
            this.colorisation(5);
        }
        else if (comp == green)
        {
            this.colorisation(6);
        }
        else if (comp == menuInstruction)
        {
            JOptionPane.showMessageDialog(this, Instructions);
        }
    }
}
