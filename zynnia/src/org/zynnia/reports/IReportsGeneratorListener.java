/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.reports;

/**
 *
 * @author alejandro
 */
public interface IReportsGeneratorListener {

    void preExecuteReportGenerator();

    void postExecuteReportGenerator(boolean hasErrors);

}
