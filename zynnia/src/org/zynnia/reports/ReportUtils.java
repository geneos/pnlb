/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.reports;

import java.io.File;
import java.io.IOException;
import org.compiere.Compiere;
import org.compiere.util.Ini;
import org.zynnia.utils.Desktop;

/**
 *
 * @author Alejandro Scott
 */
public class ReportUtils {

    public static String getBasePathofTemplates() {
        String compiereHome = Compiere.getCompiereHome();
        if (compiereHome == null || compiereHome.equals(File.separator + "Compiere2")) {
            compiereHome = Ini.findCompiereHome();
        }
        String relativePath = concatPaths("utils", "reports");
        return concatPaths(compiereHome, relativePath).concat(File.separator);
    }

    public static String concatPaths(String path1, String path2) {
        return path1.concat(File.separator).concat(path2);
    }

    public static File createTemporalFile(String extension) throws IOException {
        // Create temp file.
        File tempFile = File.createTempFile("ZynDynamicReport", extension);
        // Delete temp file when program exits.
        tempFile.deleteOnExit();

        return tempFile;
    }

    public static void openFile(File reportFile) throws IOException {
        if (reportFile.exists()) {
            Desktop.open(reportFile);
		}
    }
}
