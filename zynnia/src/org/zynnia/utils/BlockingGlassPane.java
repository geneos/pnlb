/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.utils;

import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

/**
 *
 * @author Alejandro Scott
 */
public class BlockingGlassPane extends JPanel implements MouseListener, KeyListener {

    public static final BlockingGlassPane GLASS_PANE = new BlockingGlassPane();
    private static final long serialVersionUID = -5344758920442881290L;

    public BlockingGlassPane() {
        addKeyListener(this);
        addMouseListener(this);
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        setOpaque(false);
    }

    //@Override
    public void keyPressed(final KeyEvent pArg0) {
    }

    //@Override
    public void keyReleased(final KeyEvent pArg0) {
    }

    //@Override
    public void keyTyped(final KeyEvent pArg0) {
    }

    //@Override
    public void mouseClicked(final MouseEvent pArg0) {
    }

    //@Override
    public void mouseEntered(final MouseEvent pArg0) {
    }

    //@Override
    public void mouseExited(final MouseEvent pArg0) {
    }

    //@Override
    public void mousePressed(final MouseEvent pArg0) {
    }

    //@Override
    public void mouseReleased(final MouseEvent pArg0) {
    }
}
