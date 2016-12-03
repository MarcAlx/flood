/**
 *
 * App.java
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
public class App implements Runnable
{

    private final GameWindow gamewindow;

    public App(GameWindow gw)
    {
        this.gamewindow = gw;
    }

    @Override
    public void run()
    {
        gamewindow.buildGUI();
    }

}
