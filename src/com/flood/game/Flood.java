/**
 *
 * Created by Marc-Alexandre Blanchard - all right reserved ©
 *
 * 2014
 *
 */
package com.flood.game;

import javax.swing.SwingUtilities;

/**
 *
 * @author Crée par Marc-Alexandre Blanchard
 */
public class Flood
{

    public static void main(String args[])
    {
        SwingUtilities.invokeLater(new App(new GameWindow()));
    }
}
