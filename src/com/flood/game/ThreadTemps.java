/**
 *
 * Created by Marc-Alexandre Blanchard - all right reserved ©
 *
 * 2014
 *
 */
package com.flood.game;

/**
 *
 * @author Crée par Marc-Alexandre Blanchard
 */
public class ThreadTemps extends Thread
{

    private final GameWindow GW;

    public ThreadTemps(GameWindow G)
    {
        this.GW = G;
    }

    @Override
    public void run()
    {
        //On boucle tant que le jeu est actif
        //Toute les secondes :
        while (this.GW.getONPLAY())
        {
            try
            {
                Thread.sleep(100);
                //Ajout d'une seconde
                GW.setTEMPSJOUE(GW.getTEMPSJOUE() + 1);
                //Actualisation du titre de la fenetre
                GW.setTitle("Flood - Nombre de coups : " + GW.getCOUPJOUE() + "/" + GW.getMAXCOUP() + " Temps de la partie : " + GW.getTEMPSJOUE() + "s");
            }
            catch (InterruptedException ex)
            {
            }
        }

    }
}
