/**
 * ****************************************************************************
 * The contents of this file are subject to the Compiere License Version 1.1 ("License"); You may not use this file
 * except in compliance with the License You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for the specific language governing rights and limitations under the License. The
 * Original Code is Compiere ERP & CRM Smart Business Solution. The Initial Developer of the Original Code is Jorg
 * Janke. Portions created by Jorg Janke are Copyright (C) 1999-2005 Jorg Janke. All parts are Copyright (C) 1999-2005
 * ComPiere, Inc. All Rights Reserved. Contributor(s): ______________________________________.
 ****************************************************************************
 */
package org.compiere.print.layout;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;
import java.util.regex.*;
import org.compiere.model.*;
import org.compiere.print.*;
import org.compiere.util.*;

/**
 * Table Print Element. Maintains a logical cross page table, which is "broken up" when printing
 * <pre>
 *  The table is 3 pages wide, 2 pages high
 * 		+-----+-----+-----+
 *      | 1.1 | 1.2 | 1.3 |
 * 		+-----+-----+-----+
 *      | 2.1 | 2.2 | 2.3 |
 * 		+-----+-----+-----+
 *  Printed
 * 		+-----+-----+-----+
 *      |  1  |  2  |  3  |
 * 		+-----+-----+-----+
 *      |  4  |  5  |  6  |
 * 		+-----+-----+-----+
 * </pre>
 *
 * @author Jorg Janke
 * @version $Id: TableElement.java,v 1.45 2005/12/20 07:12:04 jjanke Exp $
 */
public class TableElement extends PrintElement {

    /**
     * Constructor. Created in LayoutEngine. The rowCol.. maps are organized as follows - Point (row,col) row - data if
     * 0..m - if -1 for the entire column column - data if 0..n - if -1 for the entire row i.e. Point (-1, -1) is the
     * default for the table
     *
     * @param columnHeader array with column headers (Key=ColumnName)
     * @param columnMaxWidth array with column max width - 0=no restrictions - negative=supress if null
     * @param columnMaxHeight array with row max height for a column - 0=no restrictions; -1=one row only
     * @param columnJustification field justification for column
     *
     * @param fixedWidth array with column fixed width
     * @param functionRows list of function rows
     * @param multiLineHeader if true, the header is not truncated at maxWidth
     *
     * @param data 2D array with data to be printed [row][col]
     * @param pk array of primary keys
     * @param pkColumnName primary key name
     *
     * @param pageNoStart page number of starting page
     * @param firstPage bounds on first page
     * @param nextPages bounds on following pages
     * @param repeatedColumns repeat first x columns on - X Axis follow pages
     * @param additionalLines map of old colum to below printed column
     *
     * @param rowColFont HashMap with Point as key with Font overwrite
     * @param rowColColor HashMap with Point as key with foreground Color overwrite
     * @param rowColBackground HashMap with Point as key with background Color overwrite
     * @param tFormat table format
     * @param pageBreak Arraylist of rows with page break
     */
    public TableElement(ValueNamePair[] columnHeader,
                        int[] columnMaxWidth, int[] columnMaxHeight, String[] columnJustification,
                        boolean[] fixedWidth, ArrayList<Integer> functionRows, boolean multiLineHeader,
                        Object[][] data, KeyNamePair[] pk, String pkColumnName,
                        int pageNoStart, Rectangle firstPage, Rectangle nextPages, int repeatedColumns, HashMap<Integer, Integer> additionalLines,
                        HashMap<Point, Font> rowColFont, HashMap<Point, Color> rowColColor, HashMap<Point, Color> rowColBackground,
                        MPrintTableFormat tFormat, ArrayList<Integer> pageBreak) {
        super();
        // instancio el objeto TransportLayout
        transportLayout = new TransportLayout();
        log.log(Level.FINE, "Cols={0}, Rows={1}", new Object[]{columnHeader.length, data.length});
        m_columnHeader = columnHeader;
        m_columnMaxWidth = columnMaxWidth;
        m_columnMaxHeight = columnMaxHeight;
        m_columnJustification = columnJustification;
        m_functionRows = functionRows;
        m_fixedWidth = fixedWidth;
        //
        m_multiLineHeader = multiLineHeader;

        m_data = data;
        m_pk = pk;
        m_pkColumnName = pkColumnName;
        //
        m_pageNoStart = pageNoStart;
        m_firstPage = firstPage;
        m_nextPages = nextPages;
        m_repeatedColumns = repeatedColumns;
        m_additionalLines = additionalLines;
        //	Used Fonts,Colots
        Point pAll = new Point(ALL, ALL);
        m_rowColFont = rowColFont;
        m_baseFont = (Font) m_rowColFont.get(pAll);
        if (m_baseFont == null) {
            m_baseFont = new Font(null);
        }
        m_rowColColor = rowColColor;
        m_baseColor = (Color) m_rowColColor.get(pAll);
        if (m_baseColor == null) {
            m_baseColor = Color.black;
        }
        m_rowColBackground = rowColBackground;
        m_baseBackground = (Color) m_rowColBackground.get(pAll);
        if (m_baseBackground == null) {
            m_baseBackground = Color.white;
        }
        m_tFormat = tFormat;

        //	Page Break - not two after each other
        m_pageBreak = pageBreak;
        for (int i = 0; i < m_pageBreak.size(); i++) {
            Integer row = (Integer) m_pageBreak.get(i);
            while ((i + 1) < m_pageBreak.size()) {
                Integer nextRow = (Integer) m_pageBreak.get(i + 1);
                if ((row.intValue() + 1) == nextRow.intValue()) {
                    log.fine("- removing PageBreak row=" + row);
                    m_pageBreak.remove(i);
                    row = nextRow;
                } else {
                    break;
                }
            }
        }	//	for all page breaks

        //	Load Image
        waitForLoad(LayoutEngine.IMAGE_TRUE);
        waitForLoad(LayoutEngine.IMAGE_FALSE);
    }	//	TableElement
    /**
     * Column Headers
     */
    private ValueNamePair[] m_columnHeader;
    /**
     * Max column widths
     */
    private int[] m_columnMaxWidth;
    /**
     * Max row height per column
     */
    private int[] m_columnMaxHeight;
    /**
     * Field Justification for Column
     */
    private String[] m_columnJustification;
    /**
     * True if column fixed length
     */
    private boolean[] m_fixedWidth;
    /**
     * Create multiple header lines if required
     */
    private boolean m_multiLineHeader;
    /**
     * List of Function Rows
     */
    private ArrayList<Integer> m_functionRows;
    /**
     * The Data
     */
    private Object[][] m_data;
    /**
     * Primary Keys
     */
    private KeyNamePair[] m_pk;
    /**
     * Primary Key Column Name
     */
    private String m_pkColumnName;
    /**
     * Starting page Number
     */
    private int m_pageNoStart;
    /**
     * Bounds of first Page
     */
    private Rectangle m_firstPage;
    /**
     * Bounds of next Pages
     */
    private Rectangle m_nextPages;
    /**
     * repeat first x columns on - X Axis follow pages
     */
    private int m_repeatedColumns;
    /**
     * base font for table
     */
    private Font m_baseFont;
    /**
     * HashMap with Point as key with Font overwrite
     */
    private HashMap<Point, Font> m_rowColFont;
    /**
     * base foreground color for table
     */
    private Color m_baseColor;
    /**
     * HashMap with Point as key with foreground Color overwrite
     */
    private HashMap<Point, Color> m_rowColColor;
    /**
     * base color for table
     */
    private Color m_baseBackground;
    /**
     * HashMap with Point as key with background Color overwrite
     */
    private HashMap<Point, Color> m_rowColBackground;
    /**
     * Format of Table
     */
    private MPrintTableFormat m_tFormat;
    /**
     * Page Break Rows
     */
    private ArrayList<Integer> m_pageBreak;
    /**
     * width of columns (float)
     */
    private ArrayList<Float> m_columnWidths = new ArrayList<Float>();
    /**
     * height of rows (float)
     */
    private ArrayList<Float> m_rowHeights = new ArrayList<Float>();
    /**
     * height of header
     */
    private int m_headerHeight = 0;
    /**
     * first data row number per page
     */
    private ArrayList<Integer> m_firstRowOnPage = new ArrayList<Integer>();
    /**
     * first column number per -> page
     */
    private ArrayList<Integer> m_firstColumnOnPage = new ArrayList<Integer>();
    /**
     * Height of page
     */
    private ArrayList<Float> m_pageHeight = new ArrayList<Float>();
    /**
     * Key: Point(row,col) - Value: NamePair
     */
    private HashMap<Point, NamePair> m_rowColDrillDown = new HashMap<Point, NamePair>();
    /**
     * Key: Integer (original Column) - Value: Integer (below column)
     */
    private HashMap<Integer, Integer> m_additionalLines;
    /**
     * Key: Point(row,col) - Value: ArrayList of data
     */
    private HashMap<Point, ArrayList<Object>> m_additionalLineData = new HashMap<Point, ArrayList<Object>>();
    /*
     * todo table element contiene una clase de información sobre el transporte por defecto la información es nula y no
     * afecta a los reportes
     */
    private TransportLayout transportLayout;
    private String LABEL_TRANSPORTE = "TRANSPORTE";
    /**
     * **********************************************************************
     */
    /**
     * Header Row Indicator
     */
    public static final int HEADER_ROW = -2;
    /**
     * Header Row Indicator
     */
    public static final int ALL = -1;
    /**
     * Horizontal - GAP between text & line
     */
    private static final int H_GAP = 2;
    /**
     * Vertical | GAP between text & line
     */
    private static final int V_GAP = 2;
    /**
     * HGap for Table *
     */
    private static final int tableHGap = 5;
    /**
     * Debug Print Paint
     */
    private static final boolean DEBUG_PRINT = false;

    public void addNewRows(int sizeTemp) {
        int cantRowsOnPage = m_firstRowOnPage.get(1) - m_firstRowOnPage.get(0);
        int lastRow = m_firstRowOnPage.get(m_firstRowOnPage.size() - 1);
        Integer nextRow = lastRow + cantRowsOnPage;
        if (nextRow <= sizeTemp) {
            m_firstRowOnPage.add(nextRow);
        }
    }

    public void passColumnas(int ni, Object[][] dataTemp) {
        for (int j = 0; j < m_columnHeader.length; j++) {
            if (j == 0) {
                dataTemp[ni][j] = LABEL_TRANSPORTE;
            } else {
                if (transportLayout.getColumns().contains(j)) {
                    dataTemp[ni][j] = transportLayout.getAcumColumn(j); //sumaTransporte(j,ni,dataTemp);
                } else {
                    dataTemp[ni][j] = "";
                }
            }
        }
    }

    public void desplazar() {
        ArrayList<Integer> firstRows = new ArrayList<Integer>();
        firstRows.add(m_firstRowOnPage.get(0));
        int desplazar;
        for (int index = 1; index < m_firstRowOnPage.size(); index++) {
            transportLayout.clean();
            Object[][] dataTemp = new Object[m_data.length + 2][m_columnHeader.length];
            desplazar = 0;
            for (int i = 0; i < dataTemp.length; i++) {
                System.out.println(i + " - " + index);
                if (i == m_firstRowOnPage.get(index).intValue() - 1) {
                    dataTemp[i][0] = "TRANSPORTE";
                    for (int j = 0; j < m_columnHeader.length; j++) {
                        if (transportLayout.getColumns().contains(j)) {
                            dataTemp[i][j] = transportLayout.getAcumColumn(j);
                        }
                    }
                    i++;
                    firstRows.add(new Integer(i));
                    dataTemp[i] = dataTemp[i - 1];
                    desplazar = desplazar + 2;
                } else {
                    dataTemp[i] = m_data[i - desplazar];
                    for (int j = 0; j < m_columnHeader.length; j++) {
                        if (transportLayout.getColumns().contains(j) && dataTemp[i][0] != null && !dataTemp[i][0].equals("TRANSPORTE")) {
                            transportLayout.sumar(j, (String) dataTemp[i][j]);
                        }
                    }
                }
            }

            m_data = dataTemp;
            m_firstRowOnPage = new ArrayList<Integer>();
            m_firstColumnOnPage = new ArrayList<Integer>();
            m_pageHeight = new ArrayList<Float>();
            m_columnWidths = new ArrayList<Float>();
            m_pageHeight = new ArrayList<Float>();
            m_rowHeights = new ArrayList<Float>();
            p_sizeCalculated = false;
            calculateSize();
        }
        m_firstRowOnPage = firstRows;
    }

    public void setTransport(HashMap trans, int sizeAcum) {
        if (!trans.isEmpty()) {
            this.transportLayout.setTransport(trans, sizeAcum);
        }
    }

    public TransportLayout getTansportLayout() {
        return transportLayout;
    }

    /**
     * ************************************************************************
     * Layout and Calculate Size. Set p_width & p_height
     *
     * @return true if calculated
     */
    protected boolean calculateSize() {
        if (p_sizeCalculated) {
            return true;
        }

        p_width = 0;
        m_additionalLineData = new HashMap<Point, ArrayList<Object>>();		//	reset

        //	Max Column Width = 50% of available width (used if maxWidth not set)
        float dynMxColumnWidth = m_firstPage.width / 2;

        //	Width caolculation
        int rows = m_data.length;
        int cols = m_columnHeader.length;
        //	Data Sizes and Header Sizes
        Dimension2DImpl[][] dataSizes = new Dimension2DImpl[rows][cols];
        Dimension2DImpl[] headerSizes = new Dimension2DImpl[cols];
        FontRenderContext frc = new FontRenderContext(null, true, true);

        //	data rows
        for (int dataCol = 0; dataCol < cols; dataCol++) {
            int col = dataCol;
            //	Print below existing column
            if (m_additionalLines.containsKey(new Integer(dataCol))) {
                col = ((Integer) m_additionalLines.get(new Integer(dataCol))).intValue();
                log.log(Level.FINEST, "DataColumn={0}, BelowColumn={1}", new Object[]{dataCol, col});
            }
            float colWidth = 0;
            for (int row = 0; row < rows; row++) {
                Object dataItem = m_data[row][dataCol];
                if (dataItem == null) {
                    dataSizes[row][dataCol] = new Dimension2DImpl();
                    continue;
                }
                String string = dataItem.toString();
                if (string.length() == 0) {
                    dataSizes[row][dataCol] = new Dimension2DImpl();
                    continue;
                }
                Font font = getFont(row, dataCol);

                //	Print below existing column
                if (col != dataCol) {
                    addAdditionalLines(row, col, dataItem);
                    dataSizes[row][dataCol] = new Dimension2DImpl();		//	don't print
                } else {
                    dataSizes[row][dataCol] = new Dimension2DImpl();
                }

                if (dataItem instanceof Boolean) {
                    dataSizes[row][col].addBelow(LayoutEngine.IMAGE_SIZE);
                    continue;
                } else if (dataItem instanceof ImageElement) {
                    dataSizes[row][col].addBelow(
                            new Dimension((int) ((ImageElement) dataItem).getWidth(),
                                          (int) ((ImageElement) dataItem).getHeight()));
                    continue;
                }
                //	No Width Limitations
                if (m_columnMaxWidth[col] == 0 || m_columnMaxWidth[col] == -1) {
                    //	if (HTMLElement.isHTML(string))
                    //		log.finest( "HTML (no) r=" + row + ",c=" + dataCol);
                    TextLayout layout = new TextLayout(string, font, frc);
                    float width = layout.getAdvance() + 2;	//	buffer
                    float height = layout.getAscent() + layout.getDescent() + layout.getLeading();
                    if (width > dynMxColumnWidth) {
                        m_columnMaxWidth[col] = (int) Math.ceil(dynMxColumnWidth);
                    } else if (colWidth < width) {
                        colWidth = width;
                    }
                    if (dataSizes[row][col] == null) {
                        dataSizes[row][col] = new Dimension2DImpl();
                        log.log(Level.SEVERE, "calculateSize - No Size for r={0},c={1}", new Object[]{row, col});
                    }
                    dataSizes[row][col].addBelow(width, height);
                }
                //	Width limitations
                if (m_columnMaxWidth[col] != 0 && m_columnMaxWidth[col] != -1) {
                    float height = 0;
                    //
                    if (HTMLElement.isHTML(string)) {
                        //	log.finest( "HTML (limit) r=" + row + ",c=" + dataCol);
                        HTMLRenderer renderer = HTMLRenderer.get(string);
                        colWidth = renderer.getWidth();
                        if (m_columnMaxHeight[col] == -1) //	one line only
                        {
                            height = renderer.getHeightOneLine();
                        } else {
                            height = renderer.getHeight();
                        }
                        renderer.setAllocation((int) colWidth, (int) height);
                        //	log.finest( "calculateSize HTML - " + renderer.getAllocation());
                        m_data[row][dataCol] = renderer;	//	replace for printing
                    } else {
                        String[] lines = Pattern.compile("$", Pattern.MULTILINE).split(string);
                        for (int lineNo = 0; lineNo < lines.length; lineNo++) {
                            AttributedString aString = new AttributedString(lines[lineNo]);
                            aString.addAttribute(TextAttribute.FONT, font);
                            AttributedCharacterIterator iter = aString.getIterator();
                            LineBreakMeasurer measurer = new LineBreakMeasurer(iter, frc);
                            while (measurer.getPosition() < iter.getEndIndex()) {
                                TextLayout layout = measurer.nextLayout(Math.abs(m_columnMaxWidth[col]));
                                float width = layout.getAdvance();
                                if (colWidth < width) {
                                    colWidth = width;
                                }
                                float lineHeight = layout.getAscent() + layout.getDescent() + layout.getLeading();
                                if (m_columnMaxHeight[col] == -1) //	one line only
                                {
                                    height = lineHeight;
                                    break;
                                } else if (m_columnMaxHeight[col] == 0 || (height + lineHeight) <= m_columnMaxHeight[col]) {
                                    height += lineHeight;
                                }
                            }
                        }	//	for all lines
                    }
                    if (m_fixedWidth[col]) {
                        colWidth = Math.abs(m_columnMaxWidth[col]);
                    }
                    dataSizes[row][col].addBelow(colWidth, height);
                }
                dataSizes[row][col].roundUp();
                if (dataItem instanceof NamePair) {
                    m_rowColDrillDown.put(new Point(row, col), (NamePair) dataItem);
                }
                //	System.out.println("Col=" + col + ", row=" + row + " => " + dataSizes[row][col] + " - ColWidth=" + colWidth);
            }	//	for all data rows

            //	Column Width  for Header
            String string = "";
            if (m_columnHeader[dataCol] != null) {
                string = m_columnHeader[dataCol].toString();
            }

            //	Print below existing column
            if (col != dataCol) {
                headerSizes[dataCol] = new Dimension2DImpl();
            } else if (colWidth == 0 && m_columnMaxWidth[dataCol] < 0 //	suppress Null
                       || string.length() == 0) {
                headerSizes[dataCol] = new Dimension2DImpl();
            } else {
                Font font = getFont(HEADER_ROW, dataCol);
                if (!font.isBold()) {
                    font = new Font(font.getName(), Font.BOLD, font.getSize());
                }
                //	No Width Limitations
                if (m_columnMaxWidth[dataCol] == 0 || m_columnMaxWidth[dataCol] == -1 || !m_multiLineHeader) {
                    TextLayout layout = new TextLayout(string, font, frc);
                    float width = layout.getAdvance() + 3;	//	buffer
                    float height = layout.getAscent() + layout.getDescent() + layout.getLeading();
                    if (width > dynMxColumnWidth) {
                        m_columnMaxWidth[dataCol] = (int) Math.ceil(dynMxColumnWidth);
                    } else if (colWidth < width) {
                        colWidth = width;
                    }
                    headerSizes[dataCol] = new Dimension2DImpl(width, height);
                }
                //	Width limitations
                if (m_columnMaxWidth[dataCol] != 0 && m_columnMaxWidth[dataCol] != -1) {
                    float height = 0;
                    //
                    String[] lines = Pattern.compile("$", Pattern.MULTILINE).split(string);
                    for (int lineNo = 0; lineNo < lines.length; lineNo++) {
                        AttributedString aString = new AttributedString(lines[lineNo]);
                        aString.addAttribute(TextAttribute.FONT, font);
                        AttributedCharacterIterator iter = aString.getIterator();
                        LineBreakMeasurer measurer = new LineBreakMeasurer(iter, frc);
                        colWidth = Math.abs(m_columnMaxWidth[dataCol]);
                        while (measurer.getPosition() < iter.getEndIndex()) {
                            TextLayout layout = measurer.nextLayout(colWidth);
                            float lineHeight = layout.getAscent() + layout.getDescent() + layout.getLeading();
                            if (!m_multiLineHeader) //	one line only
                            {
                                height = lineHeight;
                                break;
                            } else if (m_columnMaxHeight[dataCol] == 0
                                       || (height + lineHeight) <= m_columnMaxHeight[dataCol]) {
                                height += lineHeight;
                            }
                        }
                    }	//	for all header lines
                    headerSizes[dataCol] = new Dimension2DImpl(colWidth, height);
                }

            }	//	headerSize
            headerSizes[dataCol].roundUp();
            colWidth = (float) Math.ceil(colWidth);
            //	System.out.println("Col=" + dataCol + " => " + headerSizes[dataCol]);

            //	Round Column Width
            if (dataCol == 0) {
                colWidth += m_tFormat.getVLineStroke().floatValue();
            }
            if (colWidth != 0) {
                colWidth += (2 * H_GAP) + m_tFormat.getVLineStroke().floatValue();
            }

            //	Print below existing column
            if (col != dataCol) {
                m_columnWidths.add(new Float(0.0));		//	for the data column
                Float origWidth = (Float) m_columnWidths.get(col);
                if (origWidth == null) {
                    log.log(Level.SEVERE, "Column {0} below {1} - no value for orig width", new Object[]{dataCol, col});
                } else {
                    if (origWidth.compareTo(new Float(colWidth)) >= 0) {
                        log.log(Level.FINEST, "Same Width - Col={0} - OrigWidth={1} - Width={2} - Total={3}", new Object[]{col, origWidth, colWidth, p_width});
                    } else {
                        m_columnWidths.set(col, new Float(colWidth));
                        p_width += (colWidth - origWidth.floatValue());
                        log.log(Level.FINEST, "New Width - Col={0} - OrigWidth={1} - Width={2} - Total={3}", new Object[]{col, origWidth, colWidth, p_width});
                    }
                }
            } //	Add new Column
            else {
                m_columnWidths.add(new Float(colWidth));
                p_width += colWidth;
                log.log(Level.FINEST, "Width - Col={0} - Width={1} - Total={2}", new Object[]{dataCol, colWidth, p_width});
            }
        }	//	for all columns

        //	Height	**********
        p_height = 0;
        for (int row = 0; row < rows; row++) {
            float rowHeight = 0f;
            for (int col = 0; col < cols; col++) {
                if (dataSizes[row][col].height > rowHeight) {
                    rowHeight = (float) dataSizes[row][col].height;
                }
            }	//	for all columns
            rowHeight += m_tFormat.getLineStroke().floatValue() + (2 * V_GAP);
            m_rowHeights.add(new Float(rowHeight));
            p_height += rowHeight;
        }	//	for all rows
        //	HeaderRow
        m_headerHeight = 0;
        for (int col = 0; col < cols; col++) {
            if (headerSizes[col].height > m_headerHeight) {
                m_headerHeight = (int) headerSizes[col].height;
            }
        }	//	for all columns
        m_headerHeight += (4 * m_tFormat.getLineStroke().floatValue()) + (2 * V_GAP);	//	Thick lines
        p_height += m_headerHeight;

        //	Last row Lines
        p_height += m_tFormat.getLineStroke().floatValue() + tableHGap;	//	last fat line

        //	Page Layout	*******************************************************
        log.log(Level.FINE, "FirstPage={0}, NextPages={1}", new Object[]{m_firstPage, m_nextPages});
        //	One Page on Y | Axis
        if (m_firstPage.height >= p_height && m_pageBreak.isEmpty()) {
            log.log(Level.FINEST, "Page Y=1 - PageHeight={0} - TableHeight={1}", new Object[]{m_firstPage.height, p_height});
            m_firstRowOnPage.add(new Integer(0));	//	Y
            m_pageHeight.add(new Float(p_height));	//	Y index only
        } //	multiple pages on Y | Axis
        else {
            float availableHeight = 0f;
            float usedHeight = 0f;
            boolean firstPage = true;
            //	for all rows
            for (int row = 0; row < m_rowHeights.size(); row++) {
                float rowHeight = ((Float) m_rowHeights.get(row)).floatValue();
                //	Y page break
                boolean forcePageBreak = isPageBreak(row);

                //adjust for lastrow
				if (row + 1 == m_rowHeights.size()) {
					availableHeight -= m_tFormat.getLineStroke().floatValue();
				}

                if (availableHeight < rowHeight || forcePageBreak) {
                    availableHeight = firstPage ? m_firstPage.height : m_nextPages.height;
                    m_firstRowOnPage.add(new Integer(row));		//	Y
                    log.log(Level.FINEST, "Page Y={0} - Row={1} - force={2}", new Object[]{m_firstRowOnPage.size(), row, forcePageBreak});
                    if (!firstPage) {
                        m_pageHeight.add(new Float(usedHeight));//	Y index only
                        log.log(Level.FINEST, "Page Y={0} - PageHeight={1}", new Object[]{m_pageHeight.size(), usedHeight});
                    }
                    firstPage = false;
                    //
                    availableHeight -= m_headerHeight;
                    usedHeight += m_headerHeight;
                }
                availableHeight -= rowHeight;
                usedHeight += rowHeight;
            }	//	for all rows
            m_pageHeight.add(new Float(usedHeight));			//	Y index only
            log.log(Level.FINEST, "Page Y={0} - PageHeight={1}", new Object[]{m_pageHeight.size(), usedHeight});
        }	//	multiple Y | pages

        //	One page on - X Axis
        if (m_firstPage.width >= p_width) {
            log.log(Level.FINEST, "Page X=1 - PageWidth={0} - TableWidth={1}", new Object[]{m_firstPage.width, p_width});
            m_firstColumnOnPage.add(new Integer(0));	//	X
            //
            distributeColumns(m_firstPage.width - (int) p_width, 0, m_columnWidths.size());
        } //	multiple pages on - X Axis
        else {
            int availableWidth = 0;
            int lastStart = 0;
            for (int col = 0; col < m_columnWidths.size(); col++) {
                int columnWidth = ((Float) m_columnWidths.get(col)).intValue();
                //	X page preak
                if (availableWidth < columnWidth) {
                    if (col != 0) {
                        distributeColumns(availableWidth, lastStart, col);
                    }

                    m_firstColumnOnPage.add(new Integer(col));	//	X
                    log.log(Level.FINEST, "Page X={0} - Col={1}", new Object[]{m_firstColumnOnPage.size(), col});
                    lastStart = col;
                    availableWidth = m_firstPage.width; 		//	Width is the same on all pages
                    //
                    for (int repCol = 0; repCol < m_repeatedColumns && col > repCol; repCol++) {
                        float repColumnWidth = ((Float) m_columnWidths.get(repCol)).floatValue();
                        //	leave 50% of space available for non repeated columns
                        if (availableWidth < m_firstPage.width * 0.5) {
                            break;
                        }
                        availableWidth -= repColumnWidth;
                    }
                }	//	pageBreak
                availableWidth -= columnWidth;
            }	//	for acc columns
        }	//	multiple - X pages

        log.log(Level.FINE, "Pages={0} X={1}/Y={2} - Width={3}, Height={4}", new Object[]{getPageCount(), m_firstColumnOnPage.size(), m_firstRowOnPage.size(), p_width, p_height});
        p_sizeCalculated = true;
        return true;
    }	//	calculateSize

    /**
     * ************************************************************************
     * Distribute Columns to fill page
     *
     * @param availableWidth width to distribute
     * @param fromCol start column
     * @param toCol end column (not included)
     */
    private void distributeColumns(int availableWidth, int fromCol, int toCol) {
        log.log(Level.FINEST, "Available={0}, Columns {1}->{2}", new Object[]{availableWidth, fromCol, toCol});
        int start = fromCol;
        if (fromCol == 0 && m_repeatedColumns > 0) {
            start = m_repeatedColumns;
        }
        //	calculate total Width
        int totalWidth = availableWidth;
        for (int col = start; col < toCol; col++) {
            totalWidth += ((Float) m_columnWidths.get(col)).floatValue();
        }
        int remainingWidth = availableWidth;
        //	distribute proportionally (does not increase zero width columns)
        for (int x = 0; remainingWidth > 0 && x < 5; x++) //	max 4 iterations
        {
            log.log(Level.FINEST, "TotalWidth={0}, Remaining={1}", new Object[]{totalWidth, remainingWidth});
            for (int col = start; col < toCol && remainingWidth != 0; col++) {
                int columnWidth = ((Float) m_columnWidths.get(col)).intValue();
                if (columnWidth != 0) {
                    int additionalPart = columnWidth * availableWidth / totalWidth;
                    if (remainingWidth < additionalPart) {
                        m_columnWidths.set(col, new Float(columnWidth + remainingWidth));
                        remainingWidth = 0;
                    } else {
                        m_columnWidths.set(col, new Float(columnWidth + additionalPart));
                        remainingWidth -= additionalPart;
                    }
                    log.log(Level.FINEST, "  col={0} - From {1} to {2}", new Object[]{col, columnWidth, m_columnWidths.get(col)});
                }
            }
        }
        //	add remainder to last non 0 width column
        for (int c = toCol - 1; remainingWidth != 0 && c >= 0; c--) {
            int columnWidth = ((Float) m_columnWidths.get(c)).intValue();
            if (columnWidth > 0) {
                m_columnWidths.set(c, new Float(columnWidth + remainingWidth));
                log.log(Level.FINEST, "Final col={0} - From {1} to {2}", new Object[]{c, columnWidth, m_columnWidths.get(c)});
                remainingWidth = 0;
            }
        }
    }	//	distribute Columns

    /**
     * Check for for PageBreak
     *
     * @param row current row
     * @return true if row should be on new page
     */
    private boolean isPageBreak(int row) {
        for (int i = 0; i < m_pageBreak.size(); i++) {
            Integer rr = (Integer) m_pageBreak.get(i);
            if (rr.intValue() + 1 == row) {
                return true;
            } else if (rr.intValue() > row) {
                return false;
            }
        }
        return false;
    }	//	isPageBreak

    /**
     * For Multi-Page Tables, set Height to Height of last Page
     */
    public void setHeightToLastPage() {
        int lastLayoutPage = getPageCount() + m_pageNoStart - 1;
        log.log(Level.FINE, "PageCount - Table={0}(Start={1}) Layout={2} - Old Height={3}", new Object[]{getPageCount(), m_pageNoStart, lastLayoutPage, p_height});
        p_height = getHeight(lastLayoutPage);
        log.log(Level.FINE, "New Height={0}", p_height);
    }	//	setHeightToLastPage

    /**
     * ************************************************************************
     * Get Font. Based on Point (row,col).
     * <pre>
     *  Examples:
     * 	  From general to specific:
     *      (-1,-1) => for entire table
     *  	(-1, c) => for entire column c
     *  	(r, -1) => for entire row r (overwrites column)
     *      (r, c)  => for specific cell (highest priority)
     *    Header is row -2
     * 		(-2,-1) => for all header columns
     * 		(-2, c) => for header column c
     * </pre>
     *
     * @param row row
     * @param col column
     * @return Font for row/col
     */
    private Font getFont(int row, int col) {
        //	First specific position
        Font font = (Font) m_rowColFont.get(new Point(row, col));
        if (font != null) {
            return font;
        }
        //	Row Next
        font = (Font) m_rowColFont.get(new Point(row, ALL));
        if (font != null) {
            return font;
        }
        //	Column then
        font = (Font) m_rowColFont.get(new Point(ALL, col));
        if (font != null) {
            return font;
        }
        //	default
        return m_baseFont;
    }	//	getFont

    /**
     * Get Foreground Color.
     *
     * @param row row
     * @param col column
     * @return Color for row/col
     */
    private Color getColor(int row, int col) {
        //	First specific position
        Color color = (Color) m_rowColColor.get(new Point(row, col));
        if (color != null) {
            return color;
        }
        //	Row Next
        color = (Color) m_rowColColor.get(new Point(row, ALL));
        if (color != null) {
            return color;
        }
        //	Column then
        color = (Color) m_rowColColor.get(new Point(ALL, col));
        if (color != null) {
            return color;
        }
        //	default
        return m_baseColor;
    }	//	getFont

    /**
     * Get Foreground Color.
     *
     * @param row row
     * @param col column
     * @return Color for row/col
     */
    private Color getBackground(int row, int col) {
        //	First specific position
        Color color = (Color) m_rowColBackground.get(new Point(row, col));
        if (color != null) {
            return color;
        }
        //	Row Next
        color = (Color) m_rowColBackground.get(new Point(row, ALL));
        if (color != null) {
            return color;
        }
        //	Column then
        color = (Color) m_rowColBackground.get(new Point(ALL, col));
        if (color != null) {
            return color;
        }
        //	default
        return m_baseBackground;
    }	//	getFont

    /**
     * ************************************************************************
     * Get Calculated Height on page
     *
     * @param pageNo layout page number
     * @return Height
     */
    public float getHeight(int pageNo) {
        int pageIndex = getPageIndex(pageNo);
        int pageYindex = getPageYIndex(pageIndex);
        log.log(Level.FINE, "Page={0} - PageIndex={1}, PageYindex={2}", new Object[]{pageNo, pageIndex, pageYindex});
        float pageHeight = ((Float) m_pageHeight.get(pageYindex)).floatValue();
        float pageHeightPrevious = 0f;
        if (pageYindex > 0) {
            pageHeightPrevious = ((Float) m_pageHeight.get(pageYindex - 1)).floatValue();
        }
        float retValue = pageHeight - pageHeightPrevious;
        log.log(Level.FINE, "Page={0} - PageIndex={1}, PageYindex={2}, Height={3}", new Object[]{pageNo, pageIndex, pageYindex, String.valueOf(retValue)});
        return retValue;
    }	//	getHeight

    /**
     * Get Calculated Height on page
     *
     * @param pageNo page number
     * @return Height
     */
    public float getWidth(int pageNo) {
        int pageIndex = getPageIndex(pageNo);
        if (pageIndex == 0) {
            return m_firstPage.width;
        }
        return m_nextPages.width;
    }	//	getHeight

    /**
     * ************************************************************************
     * Get number of "real" pages.
     *
     * @return page count
     */
    public int getPageCount() {
        return m_firstRowOnPage.size() * m_firstColumnOnPage.size();
    }	//	getPageCount

    /**
     * Get zero based Page Index within Layout
     *
     * @param pageNo real page no
     * @return page index
     */
    protected int getPageIndex(int pageNo) {
        int index = pageNo - m_pageNoStart;
        if (index < 0) {
            log.log(Level.SEVERE, "index=" + index, new Exception());
        }
        return index;
    }	//	getPageIndex

    /**
     * ************************************************************************
     * Get X - Page Index. Zero Based; Page No is the "real" page No
     * <pre>
     *  The table is 3 pages wide, 2 pages high - index
     * 		+-----+-----+-----+
     *      | 0.0 | 0.1 | 0.2 |
     * 		+-----+-----+-----+
     *      | 1.0 | 1.1 | 1.2 |
     * 		+-----+-----+-----+
     *  Page Index
     * 		+-----+-----+-----+
     *      |  0  |  1  |  2  |
     * 		+-----+-----+-----+
     *      |  3  |  4  |  5  |
     * 		+-----+-----+-----+
     * </pre>
     *
     * @param pageIndex zero based page index
     * @return page index on X axis
     */
    protected int getPageXIndex(int pageIndex) {
        int noXpages = m_firstColumnOnPage.size();
        //	int noYpages = m_firstRowOnPage.size();
        int x = pageIndex % noXpages;
        return x;
    }	//	getPageXIndex

    /**
     * Get X - Page Count
     *
     * @return X page count
     */
    protected int getPageXCount() {
        return m_firstColumnOnPage.size();
    }	//	getPageXCount

    /**
     * Get Y | Page Index. Zero Based; Page No is the "real" page No
     * <pre>
     *  The table is 3 pages wide, 2 pages high - index
     * 		+-----+-----+-----+
     *      | 0.0 | 0.1 | 0.2 |
     * 		+-----+-----+-----+
     *      | 1.0 | 1.1 | 1.2 |
     * 		+-----+-----+-----+
     *  Page Index
     * 		+-----+-----+-----+
     *      |  0  |  1  |  2  |
     * 		+-----+-----+-----+
     *      |  3  |  4  |  5  |
     * 		+-----+-----+-----+
     * </pre>
     *
     * @param pageIndex zero based page index
     * @return page index on Y axis
     */
    protected int getPageYIndex(int pageIndex) {
        int noXpages = m_firstColumnOnPage.size();
        //	int noYpages = m_firstRowOnPage.size();
        int y = (pageIndex - (pageIndex % noXpages)) / noXpages;
        return y;
    }	//	getPageYIndex

    /**
     * Get Y | Page Count
     *
     * @return Y page count
     */
    protected int getPageYCount() {
        return m_firstRowOnPage.size();
    }	//	getPageYCount

    /**
     * ************************************************************************
     * Get Drill Down value
     *
     * @param relativePoint relative Point
     * @param pageNo page number
     * @return if found Qyery or null
     */
    public MQuery getDrillDown(Point relativePoint, int pageNo) {
        if (m_rowColDrillDown.isEmpty()) {
            return null;
        }
        if (!getBounds(pageNo).contains(relativePoint)) {
            return null;
        }
        int row = getRow(relativePoint.y, pageNo);
        if (row == -1) {
            return null;
        }
        int col = getCol(relativePoint.x, pageNo);
        if (col == -1) {
            return null;
        }
        log.log(Level.FINE, "Row={0}, Col={1}, PageNo={2}", new Object[]{row, col, pageNo});
        //
        NamePair pp = (NamePair) m_rowColDrillDown.get(new Point(row, col));
        if (pp == null) {
            return null;
        }
        String columnName = MQuery.getZoomColumnName(m_columnHeader[col].getID());
        String tableName = MQuery.getZoomTableName(columnName);
        Object code = pp.getID();
        if (pp instanceof KeyNamePair) {
            code = new Integer(((KeyNamePair) pp).getKey());
        }
        //
        MQuery query = new MQuery(tableName);
        query.addRestriction(columnName, MQuery.EQUAL, code, null, pp.toString());
        return query;
    }	//	getDrillDown

    /**
     * Get Drill Across value
     *
     * @param relativePoint relative Point
     * @param pageNo page number
     * @return if found Query or null
     */
    public MQuery getDrillAcross(Point relativePoint, int pageNo) {
        if (!getBounds(pageNo).contains(relativePoint)) {
            return null;
        }
        int row = getRow(relativePoint.y, pageNo);
        if (row == -1) {
            return null;
        }
        log.log(Level.FINE, "Row={0}, PageNo={1}", new Object[]{row, pageNo});
        //
        if (m_pk[row] == null) //	FunctionRows
        {
            return null;
        }
        return MQuery.getEqualQuery(m_pkColumnName, m_pk[row].getKey());
    }	//	getDrillAcross

    /**
     * Get relative Bounds of Element. (entire page, not just used portion)
     *
     * @param pageNo pageNo
     * @return bounds relative position on page
     */
    public Rectangle getBounds(int pageNo) {
        int pageIndex = getPageIndex(pageNo);
        int pageYindex = getPageYIndex(pageIndex);
        if (pageYindex == 0) {
            return m_firstPage;
        } else {
            return m_nextPages;
        }
    }	//	getBounds

    /**
     * Get Row for yPos
     *
     * @param yPos y position (page relative)
     * @param pageNo page number
     * @return row index or -1
     */
    private int getRow(int yPos, int pageNo) {
        int pageIndex = getPageIndex(pageNo);
        int pageYindex = getPageYIndex(pageIndex);
        //
        int curY = (pageYindex == 0 ? m_firstPage.y : m_nextPages.y) + m_headerHeight;
        if (yPos < curY) {
            return -1;		//	above
        }		//
        int firstRow = ((Integer) m_firstRowOnPage.get(pageYindex)).intValue();
        int nextPageRow = m_data.length;				//	no of rows
        if (pageYindex + 1 < m_firstRowOnPage.size()) {
            nextPageRow = ((Integer) m_firstRowOnPage.get(pageYindex + 1)).intValue();
        }
        //
        for (int row = firstRow; row < nextPageRow; row++) {
            int rowHeight = ((Float) m_rowHeights.get(row)).intValue();	//	includes 2*Gaps+Line
            if (yPos >= curY && yPos < (curY + rowHeight)) {
                return row;
            }
            curY += rowHeight;
        }
        //	below
        return -1;
    }	//	getRow

    /**
     * Get Column for xPos
     *
     * @param xPos x position (page relative)
     * @param pageNo page number
     * @return column index or -1
     */
    private int getCol(int xPos, int pageNo) {
        int pageIndex = getPageIndex(pageNo);
        int pageXindex = getPageXIndex(pageIndex);
        //
        int curX = pageXindex == 0 ? m_firstPage.x : m_nextPages.x;
        if (xPos < curX) {
            return -1;		//	too left
        }
        int firstColumn = ((Integer) m_firstColumnOnPage.get(pageXindex)).intValue();
        int nextPageColumn = m_columnHeader.length;		// no of cols
        if (pageXindex + 1 < m_firstColumnOnPage.size()) {
            nextPageColumn = ((Integer) m_firstColumnOnPage.get(pageXindex + 1)).intValue();
        }

        //	fixed volumns
        int regularColumnStart = firstColumn;
        for (int col = 0; col < m_repeatedColumns; col++) {
            int colWidth = ((Float) m_columnWidths.get(col)).intValue();		//	includes 2*Gaps+Line
            if (xPos >= curX && xPos < (curX + colWidth)) {
                return col;
            }
            curX += colWidth;
            if (regularColumnStart == col) {
                regularColumnStart++;
            }
        }
        //	regular columns
        for (int col = regularColumnStart; col < nextPageColumn; col++) {
            int colWidth = ((Float) m_columnWidths.get(col)).intValue();		//	includes 2*Gaps+Line
            if (xPos >= curX && xPos < (curX + colWidth)) {
                return col;
            }
            curX += colWidth;
        }	//	for all columns
        //	too right
        return -1;
    }	//	getCol

    /**
     * ************************************************************************
     * Paint/Print.
     *
     * @param g2D Graphics
     * @param pageNo page number for multi page support (0 = header/footer)
     * @param pageStart top left Location of page
     * @param ctx context
     * @param isView true if online view (IDs are links)
     */
    public void paint(Graphics2D g2D, int pageNo, Point2D pageStart, Properties ctx, boolean isView) {
        int pageIndex = getPageIndex(pageNo);
        int pageXindex = getPageXIndex(pageIndex);
        int pageYindex = getPageYIndex(pageIndex);
        //if (DEBUG_PRINT)
        log.log(Level.CONFIG, "Page={0} [x={1}, y={2}]", new Object[]{pageNo, pageXindex, pageYindex});
        //
        int idxX = m_firstColumnOnPage.size() <= pageXindex ? m_firstColumnOnPage.size() - 1 : pageXindex;
        int firstColumn = ((Integer) m_firstColumnOnPage.get(idxX)).intValue();
        int nextPageColumn = m_columnHeader.length;		// no of cols
        if (pageXindex + 1 < m_firstColumnOnPage.size()) {
            nextPageColumn = ((Integer) m_firstColumnOnPage.get(pageXindex + 1)).intValue();
        }
        //
        int idxY = m_firstRowOnPage.size() <= pageYindex ? m_firstRowOnPage.size() - 1 : pageYindex;
        int firstRow = ((Integer) m_firstRowOnPage.get(idxY)).intValue();

        int nextPageRow = m_data.length;				//	no of rows
        if (pageYindex + 1 < m_firstRowOnPage.size()) {
            nextPageRow = ((Integer) m_firstRowOnPage.get(pageYindex + 1)).intValue();
        }
        if (DEBUG_PRINT) {
            log.log(Level.FINEST, "Col={0}-{1}, Row={2}-{3}", new Object[]{firstColumn, nextPageColumn - 1, firstRow, nextPageRow - 1});
        }

        //	Top Left
        int startX = (int) pageStart.getX();
        int startY = (int) pageStart.getY();
        //	Table Start
        startX += pageXindex == 0 ? m_firstPage.x : m_nextPages.x;
        startY += pageYindex == 0 ? m_firstPage.y : m_nextPages.y;
//        startY += tableHGap;

        if (DEBUG_PRINT) {
            log.log(Level.FINEST, "PageStart={0}, StartTable x={1}, y={2}", new Object[]{pageStart, startX, startY});
        }

        //	paint first fixed volumns
        boolean firstColumnPrint = true;
        int regularColumnStart = firstColumn;
        for (int col = 0; col < m_repeatedColumns && col < m_columnWidths.size(); col++) {
            int colWidth = ((Float) m_columnWidths.get(col)).intValue();		//	includes 2*Gaps+Line
            if (colWidth != 0) {
                printColumn(g2D, col, startX, startY, firstColumnPrint, firstRow, nextPageRow, isView);
                startX += colWidth;
                firstColumnPrint = false;
            }
            if (regularColumnStart == col) {
                regularColumnStart++;
            }
        }

        //	paint columns
        for (int col = regularColumnStart; col < nextPageColumn; col++) {
            int colWidth = ((Float) m_columnWidths.get(col)).intValue();		//	includes 2*Gaps+Line
            if (colWidth != 0) {
                printColumn(g2D, col, startX, startY, firstColumnPrint, firstRow, nextPageRow, isView);
                startX += colWidth;
                firstColumnPrint = false;
            }
        }	//	for all columns

    }	//	paint

    /**
     * Print non zero width Column
     *
     * @param g2D graphics
     * @param col column index
     * @param origX start X
     * @param origY start Y
     * @param leftVline if true print left vertical line (for first column)
     * @param firstRow first row index
     * @param nextPageRow row index of next page
     * @param isView true if online view (IDs are links)
     */
    private void printColumn(Graphics2D g2D, int col,
                             final int origX, final int origY, boolean leftVline,
                             final int firstRow, final int nextPageRow, boolean isView) {
        int curX = origX;
        int curY = origY;	//	start from top
        //
        float colWidth = ((Float) m_columnWidths.get(col)).floatValue();		//	includes 2*Gaps+Line
        float netWidth = colWidth - (2 * H_GAP) - m_tFormat.getVLineStroke().floatValue();
        if (leftVline) {
            netWidth -= m_tFormat.getVLineStroke().floatValue();
        }
        int rowHeight = m_headerHeight;
        float netHeight = rowHeight - (4 * m_tFormat.getLineStroke().floatValue()) + (2 * V_GAP);

        if (DEBUG_PRINT) {
            log.log(Level.FINER, "#{0} - x={1}, y={2}, width={3}/{4}, HeaderHeight={5}/{6}", new Object[]{col, curX, curY, colWidth, netWidth, rowHeight, netHeight});
        }
        String alignment = m_columnJustification[col];

        //	paint header	***************************************************
        if (leftVline) //	draw left | line
        {
            g2D.setPaint(m_tFormat.getVLine_Color());
            g2D.setStroke(m_tFormat.getVLine_Stroke());
            if (m_tFormat.isPaintBoundaryLines()) //	 -> | (left)
            {
                g2D.drawLine(origX, (int) (origY + m_tFormat.getLineStroke().floatValue()),
                             origX, (int) (origY + rowHeight - (4 * m_tFormat.getLineStroke().floatValue())));
            }
            curX += m_tFormat.getVLineStroke().floatValue();
        }
        //	X - start line
        if (m_tFormat.isPaintHeaderLines()) {
            g2D.setPaint(m_tFormat.getHeaderLine_Color());
            g2D.setStroke(m_tFormat.getHeader_Stroke());
            g2D.drawLine(origX, origY, //	 -> - (top)
                         (int) (origX + colWidth - m_tFormat.getVLineStroke().floatValue()), origY);
        }
        curY += (2 * m_tFormat.getLineStroke().floatValue());	//	thick
        //	Background
        Color bg = getBackground(HEADER_ROW, col);
        if (!bg.equals(Color.white)) {
            g2D.setPaint(bg);
            g2D.fillRect(curX,
                         (int) (curY - m_tFormat.getLineStroke().floatValue()),
                         (int) (colWidth - m_tFormat.getVLineStroke().floatValue()),
                         (int) (rowHeight - (4 * m_tFormat.getLineStroke().floatValue())));
        }
        int tempCurY = curY;
        curX += H_GAP;		//	upper left gap
        curY += V_GAP;
        //	Header
        float usedHeight = 0;
        if (m_columnHeader[col].toString().length() > 0) {
            AttributedString aString = new AttributedString(m_columnHeader[col].toString());
            aString.addAttribute(TextAttribute.FONT, getFont(HEADER_ROW, col));
            aString.addAttribute(TextAttribute.FOREGROUND, getColor(HEADER_ROW, col));
            //
            boolean fastDraw = LayoutEngine.s_FASTDRAW;
            if (fastDraw && !isView && !Util.is8Bit(m_columnHeader[col].toString())) {
                fastDraw = false;
            }
            AttributedCharacterIterator iter = aString.getIterator();
            LineBreakMeasurer measurer = new LineBreakMeasurer(iter, g2D.getFontRenderContext());
            while (measurer.getPosition() < iter.getEndIndex()) //	print header
            {
                TextLayout layout = measurer.nextLayout(netWidth + 2);
                if (iter.getEndIndex() != measurer.getPosition()) {
                    fastDraw = false;
                }
                float lineHeight = layout.getAscent() + layout.getDescent() + layout.getLeading();
                if (m_columnMaxHeight[col] <= 0 //	-1 = FirstLineOnly
                    || (usedHeight + lineHeight) <= m_columnMaxHeight[col]) {
                    if (alignment.equals(MPrintFormatItem.FIELDALIGNMENTTYPE_Block)) {
                        layout = layout.getJustifiedLayout(netWidth + 2);
                        fastDraw = false;
                    }
                    curY += layout.getAscent();
                    float penX = curX;
                    if (alignment.equals(MPrintFormatItem.FIELDALIGNMENTTYPE_Center)) {
                        penX += (netWidth - layout.getAdvance()) / 2;
                    } else if ((alignment.equals(MPrintFormatItem.FIELDALIGNMENTTYPE_TrailingRight) && layout.isLeftToRight())
                               || (alignment.equals(MPrintFormatItem.FIELDALIGNMENTTYPE_LeadingLeft) && !layout.isLeftToRight())) {
                        penX += netWidth - layout.getAdvance();
                    }
                    //
                    if (fastDraw) {	//	Bug - set Font/Color explicitly
                        g2D.setFont(getFont(HEADER_ROW, col));
                        g2D.setColor(getColor(HEADER_ROW, col));
                        g2D.drawString(iter, penX, curY);
                    } else {
                        layout.draw(g2D, penX, curY);										//	-> text
                    }
                    curY += layout.getDescent() + layout.getLeading();
                    usedHeight += lineHeight;
                }
                if (!m_multiLineHeader) //	one line only
                {
                    break;
                }
            }
        }	//	length > 0
        curX += netWidth + H_GAP;
       // curY += V_GAP;
        curY = tempCurY + (int)(rowHeight-(4*m_tFormat.getLineStroke().floatValue()));
        //	Y end line
        g2D.setPaint(m_tFormat.getVLine_Color());
        g2D.setStroke(m_tFormat.getVLine_Stroke());
        if (m_tFormat.isPaintVLines()) //	 -> | (right)
        {
            g2D.drawLine(curX, (int) (origY + m_tFormat.getLineStroke().floatValue()),
                         curX, (int) (origY + rowHeight - (4 * m_tFormat.getLineStroke().floatValue())));
        }
        curX += m_tFormat.getVLineStroke().floatValue();
        //	X end line
        if (m_tFormat.isPaintHeaderLines()) {
            g2D.setPaint(m_tFormat.getHeaderLine_Color());
            g2D.setStroke(m_tFormat.getHeader_Stroke());
            g2D.drawLine(origX, curY, //	 -> - (button)
                         (int) (origX + colWidth - m_tFormat.getVLineStroke().floatValue()), curY);
        }
        curY += (2 * m_tFormat.getLineStroke().floatValue());	//	thick

        //	paint Data		***************************************************
        for (int row = firstRow; row < nextPageRow; row++) {
            rowHeight = ((Float) m_rowHeights.get(row)).intValue();	//	includes 2*Gaps+Line
            netHeight = rowHeight - (2 * V_GAP) - m_tFormat.getLineStroke().floatValue();
            int rowYstart = curY;

            curX = origX;
            if (leftVline) //	draw left | line
            {
                g2D.setPaint(m_tFormat.getVLine_Color());
                g2D.setStroke(m_tFormat.getVLine_Stroke());
                if (m_tFormat.isPaintBoundaryLines()) {
                    g2D.drawLine(curX, rowYstart, //	 -> | (left)
                                 curX, (int) (rowYstart + rowHeight - m_tFormat.getLineStroke().floatValue()));
                }
                curX += m_tFormat.getVLineStroke().floatValue();
            }
            //	Background
            bg = getBackground(row, col);
            if (!bg.equals(Color.white)) {
                g2D.setPaint(bg);
                g2D.fillRect(curX, curY,
                             (int) (colWidth - m_tFormat.getVLineStroke().floatValue()),
                             (int) (rowHeight - m_tFormat.getLineStroke().floatValue()));
            }
            curX += H_GAP;		//	upper left gap
            curY += V_GAP;

            //	actual data
            Object[] printItems = getPrintItems(row, col);
            float penY = curY;
            for (int index = 0; index < printItems.length; index++) {
                if (printItems[index] == null)
					; else if (printItems[index] instanceof ImageElement) {
                    g2D.drawImage(((ImageElement) printItems[index]).getImage(),
                                  curX, (int) penY, this);
                } else if (printItems[index] instanceof Boolean) {
                    int penX = curX + (int) ((netWidth - LayoutEngine.IMAGE_SIZE.width) / 2);	//	center
                    if (((Boolean) printItems[index]).booleanValue()) {
                        g2D.drawImage(LayoutEngine.IMAGE_TRUE, penX, (int) penY, this);
                    } else {
                        g2D.drawImage(LayoutEngine.IMAGE_FALSE, penX, (int) penY, this);
                    }
                    penY += LayoutEngine.IMAGE_SIZE.height;
                } else if (printItems[index] instanceof HTMLRenderer) {
                    HTMLRenderer renderer = (HTMLRenderer) printItems[index];
                    Rectangle allocation = new Rectangle((int) colWidth, (int) netHeight);
                    //	log.finest( "printColumn HTML - " + allocation);
                    g2D.translate(curX, penY);
                    renderer.paint(g2D, allocation);
                    g2D.translate(-curX, -penY);
                    penY += allocation.getHeight();
                } else {
                    String str = printItems[index].toString();
                    if (DEBUG_PRINT) {
                        log.log(Level.FINE, "row={0},col={1} - {2} 8Bit={3}", new Object[]{row, col, str, Util.is8Bit(str)});
                    }
                    if (str.length() > 0) {
                        usedHeight = 0;
                        String[] lines = Pattern.compile("$", Pattern.MULTILINE).split(str);
                        for (int lineNo = 0; lineNo < lines.length; lineNo++) {
                            AttributedString aString = new AttributedString(lines[lineNo]);
                            aString.addAttribute(TextAttribute.FONT, getFont(row, col));

                            if (isView && printItems[index] instanceof NamePair) //	ID
                            {
                                aString.addAttribute(TextAttribute.FOREGROUND, LINK_COLOR);
                                aString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL, 0, str.length());
                            } else {
                                aString.addAttribute(TextAttribute.FOREGROUND, getColor(row, col));
                            }

                            //
                            AttributedCharacterIterator iter = aString.getIterator();
                            boolean fastDraw = LayoutEngine.s_FASTDRAW;
                            if (fastDraw && !isView && !Util.is8Bit(lines[lineNo])) {
                                fastDraw = false;
                            }
                            LineBreakMeasurer measurer = new LineBreakMeasurer(iter, g2D.getFontRenderContext());
                            while (measurer.getPosition() < iter.getEndIndex()) //	print element
                            {
                                TextLayout layout = measurer.nextLayout(netWidth + 2);
                                if (iter.getEndIndex() != measurer.getPosition()) {
                                    fastDraw = false;
                                }
                                float lineHeight = layout.getAscent() + layout.getDescent() + layout.getLeading();
                                if ((m_columnMaxHeight[col] <= 0
                                     || (usedHeight + lineHeight) <= m_columnMaxHeight[col])
                                    && (usedHeight + lineHeight) <= netHeight) {
                                    if (alignment.equals(MPrintFormatItem.FIELDALIGNMENTTYPE_Block) && measurer.getPosition() < iter.getEndIndex()) {
                                        layout = layout.getJustifiedLayout(netWidth + 2);
                                        fastDraw = false;
                                    }
                                    penY += layout.getAscent();
                                    float penX = curX;
                                    if (alignment.equals(MPrintFormatItem.FIELDALIGNMENTTYPE_Center)) {
                                        penX += (netWidth - layout.getAdvance()) / 2;
                                    } else if ((alignment.equals(MPrintFormatItem.FIELDALIGNMENTTYPE_TrailingRight) && layout.isLeftToRight())
                                               || (alignment.equals(MPrintFormatItem.FIELDALIGNMENTTYPE_LeadingLeft) && !layout.isLeftToRight())) {
                                        penX += netWidth - layout.getAdvance();
                                    }
                                    //
                                    if (fastDraw) {	//	Bug - set Font/Color explicitly
                                        g2D.setFont(getFont(row, col));
                                        if (isView && printItems[index] instanceof NamePair) //	ID
                                        {
                                            g2D.setColor(LINK_COLOR);
                                            //	TextAttribute.UNDERLINE
                                        } else {
                                            g2D.setColor(getColor(row, col));
                                        }
                                        g2D.drawString(iter, penX, penY);
                                    } else {
                                        layout.draw(g2D, penX, penY);										//	-> text
                                    }
                                    if (DEBUG_PRINT) {
                                        log.log(Level.FINE, "row={0},col={1} - {2} - x={3},y={4}", new Object[]{row, col, str, penX, penY});
                                    }
                                    penY += layout.getDescent() + layout.getLeading();
                                    usedHeight += lineHeight;
                                    //
                                    if (m_columnMaxHeight[col] == -1) //	FirstLineOny
                                    {
                                        break;
                                    }
                                }
                            }	//	print element
                        }	//	for all lines
                    }	//	length > 0
                }	//	non boolean
            }	//	for all print items

            curY += netHeight + V_GAP;
            curX += netWidth + H_GAP;
            //	Y end line
            g2D.setPaint(m_tFormat.getVLine_Color());
            g2D.setStroke(m_tFormat.getVLine_Stroke());
            if (m_tFormat.isPaintVLines()) {
                g2D.drawLine(curX, rowYstart, //	 -> | (right)
                             curX, (int) (rowYstart + rowHeight - m_tFormat.getLineStroke().floatValue()));
            }
            curX += m_tFormat.getVLineStroke().floatValue();

            //	X end line
            if (row == m_data.length - 1) //	last Line
            {
                /**
                 * Bug fix - Bottom line was always displayed whether or not header lines was set to be visible
                 * @author ashley
                 */
                if (m_tFormat.isPaintHeaderLines())
                {
                    g2D.setPaint(m_tFormat.getHeaderLine_Color());
                    g2D.setStroke(m_tFormat.getHeader_Stroke());
                    g2D.drawLine(origX, curY,                   //   -> - (last line)
                        (int)(origX+colWidth-m_tFormat.getVLineStroke().floatValue()), curY);
                    curY += (2 * m_tFormat.getLineStroke().floatValue());   //  thick
                }
                else
                {
                    curY += m_tFormat.getLineStroke().floatValue();
                }

            } else {
                //	next line is a funcion column -> underline this
                boolean nextIsFunction = m_functionRows.contains(new Integer(row + 1));
                if (nextIsFunction && m_functionRows.contains(new Integer(row))) {
                    nextIsFunction = false;		//	this is a function line too
                }
                if (nextIsFunction) {
                    g2D.setPaint(m_tFormat.getFunctFG_Color());
                    g2D.setStroke(m_tFormat.getHLine_Stroke());
                    g2D.drawLine(origX, curY, //	 -> - (bottom)
                                 (int) (origX + colWidth - m_tFormat.getVLineStroke().floatValue()), curY);
                } else if (m_tFormat.isPaintHLines()) {
                    g2D.setPaint(m_tFormat.getHLine_Color());
                    g2D.setStroke(m_tFormat.getHLine_Stroke());
                    g2D.drawLine(origX, curY, //	 -> - (bottom)
                                 (int) (origX + colWidth - m_tFormat.getVLineStroke().floatValue()), curY);
                }
                curY += m_tFormat.getLineStroke().floatValue();
            }
        }	// for all rows

    }	//	printColumn

    /**
     * Add Additional Lines to row/col
     *
     * @param row row
     * @param col col
     * @param data data
     */
    private void addAdditionalLines(int row, int col, Object data) {
        Point key = new Point(row, col);
        ArrayList<Object> list = (ArrayList<Object>) m_additionalLineData.get(key);
        if (list == null) {
            list = new ArrayList<Object>();
        }
        list.add(data);
        m_additionalLineData.put(key, list);
    }	//	addAdditionalLines

    /**
     * Get Print Data including additional Lines
     *
     * @param row row
     * @param col col
     * @return non null array of print objects (may be empty)
     */
    private Object[] getPrintItems(int row, int col) {
        Point key = new Point(row, col);
        ArrayList<Object> list = (ArrayList<Object>) m_additionalLineData.get(key);
        if (list == null) {
            if (m_data[row][col] != null) {
                return new Object[]{m_data[row][col]};
            } else {
                return new Object[]{};
            }
        }
        //	multiple
        ArrayList<Object> retList = new ArrayList<Object>();
        retList.add(m_data[row][col]);
        retList.addAll(list);
        return retList.toArray();
    }	//	getPrintItems
}	//	TableElement
