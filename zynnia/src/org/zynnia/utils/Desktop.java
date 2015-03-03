/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.utils;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Alejandro Scott
 */
public abstract class Desktop {

    // hide the constructor.
    Desktop() {
    }

    // Created the appropriate instance
    public static void open(File file) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        // This uf/elseif/else code is used only once: here
        if (os.indexOf("windows") != -1 || os.indexOf("nt") != -1) {
            openFileWindowsDesktop(file);
        } else if (os.equals("windows 95") || os.equals("windows 98")) {
            openFileWindows9xDesktop(file);
        } else if (os.indexOf("linux") != -1) {
            openFileGnomeDesktop(file);
        } else {
            throw new UnsupportedOperationException(String.format("The platform %s is not supported ", os));
        }
    }

    private static void openFileWindowsDesktop(File file) throws IOException {
        // Runtime.getRuntime().exec: cmd /c start <file>
         Runtime.getRuntime().exec(String.format("cmd /c start %s", file.getAbsolutePath()));
    }

    private static void openFileWindows9xDesktop(File file) throws IOException {
        //Runtime.getRuntime().exec: command.com /C start <file>
         Runtime.getRuntime().exec(String.format("command.com /C start %s", file.getAbsolutePath()));
    }

    private static void openFileGnomeDesktop(File file) throws IOException {
        // Runtime.getRuntime().exec: execute gnome-open <file>
         Runtime.getRuntime().exec(String.format("gnome-open %s", file.getAbsolutePath()));
    }

}