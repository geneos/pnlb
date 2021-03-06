package org.eevolution.form;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
//import org.jfree.chart.labels.StandardCategoryLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.TextAnchor;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.compiere.apps.*;
import org.compiere.util.*;
import org.eevolution.form.action.PopupAction;
import org.eevolution.form.action.ZoomMenuAction;
import org.eevolution.form.crp.CRPSDatasetFactory;
import org.eevolution.form.crp.CRPModel;
import org.compiere.model.*;

import org.compiere.apps.form.FormFrame;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;

import org.eevolution.tools.worker.SingleWorker;
import org.eevolution.tools.swing.SwingTool;
import org.eevolution.model.MMPCOrderNode;

/**
 * @author VIT4B
 * @version 1.0, Diciembre 12 2006
 */

public class CSCRP extends CAbstractForm {

	class ActionHandler implements ActionListener {

		public void actionPerformed (ActionEvent e) {

			if (e.getActionCommand().equals(ConfirmPanel.A_OK)) {

				 SwingTool.setCursorsFromParent(getWindow(), true);

				 final ActionEvent evt = e;
				 worker = new SingleWorker() {

						protected Object doIt() {

							handleActionEvent(evt);
							return null;
						}
				 };
				 worker.start();
			}
	        if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL)) {

	        	dispose();
	        }
		}
	}

	class TreeHandler extends MouseInputAdapter implements TreeSelectionListener {

		public void mouseClicked(MouseEvent e) {

			if(model.getTree().getPathForLocation(e.getX(), e.getY()) == null) {

				return;
			}

			SwingTool.setCursorsFromChild(e.getComponent(), true);

			final MouseEvent evt = e;
			worker = new SingleWorker() {

				protected Object doIt() {

					handleTreeEvent(evt);
					return null;
				}
			};

			worker.start();
		}

		public void mouseMoved(MouseEvent e) {

			//m_tree.setToolTipText(msg.getToolTipText(e));
		}

	    public void valueChanged(TreeSelectionEvent event) {
	    }
	}

	class FrameHandler extends WindowAdapter {

		public void windowClosing(WindowEvent e) {

			dispose();
		}
	}

	class LabelGenerator extends StandardCategoryItemLabelGenerator
	{

	    public String generateItemLabel(CategoryDataset categorydataset, int i, int j) {

	        return categorydataset.getRowKey(i).toString();
	    }

	}

	private VLookup resource;
    private VDate dateFrom;
    private VDate dateTo;
    private ChartPanel chartPanel;
	private JSplitPane contentPanel;

	private SingleWorker worker;

	protected CRPModel model;
	protected JPopupMenu popup;

	public CSCRP() {

		super();
	}

	public void init (int WindowNo, FormFrame frame) {

		super.init(WindowNo, frame);

		fillPicks();
		jbInit();
	}


	private void jbInit() {

		dateFrom = new VDate("DateFrom", true, false, true, DisplayType.Date, "DateFrom");
	    dateTo = new VDate("DateTo", true, false, true, DisplayType.Date, "DateTo");

		CPanel northPanel = new CPanel();
		northPanel.setLayout(new java.awt.GridBagLayout());

		northPanel.add(
        		new CLabel(Msg.translate(Env.getCtx(), "S_Resource_ID")),
        		new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0)
        );
        northPanel.add(
        		resource,
        		new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0)
        );

        northPanel.add(
        		new CLabel(Msg.translate(Env.getCtx(), "DateFrom")),
        		new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0)
        );
        northPanel.add(
        		dateFrom,
        		new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0)
        );

        northPanel.add(
        		new CLabel(Msg.translate(Env.getCtx(), "DateTo")),
        		new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0)
        );
        northPanel.add(
        		dateTo,
        		new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0)
        );

        ConfirmPanel confirmPanel = new ConfirmPanel(true);
        confirmPanel.addActionListener(new ActionHandler());

		contentPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        contentPanel.setPreferredSize(new Dimension(800, 600));

        getWindow().getContentPane().add(northPanel, BorderLayout.NORTH);
        getWindow().getContentPane().add(contentPanel, BorderLayout.CENTER);
        getWindow().getContentPane().add(confirmPanel, BorderLayout.SOUTH);
	}

	private void fillPicks() {

		Properties ctx = Env.getCtx();

		// Hardcoded Column ID - Manufacturing Resource ID
		MLookup resourceL = MLookupFactory.get (ctx, getWindowNo(), 0, 1000148, DisplayType.TableDir);
        resource = new VLookup ("S_Resource_ID", false, false, true, resourceL);
	}

	protected JPopupMenu createPopup(JTree tree) {

		JPopupMenu pm = new JPopupMenu();
		PopupAction action = null;

		try {

			action = new ZoomMenuAction(tree);
			pm.add(action);
		}
		catch(Exception e) {

			e.printStackTrace();
		}

		return pm;
	}

	private void handleTreeEvent(MouseEvent e) {

		if(e.getButton() == MouseEvent.BUTTON3) {

			model.getTree().setSelectionPath(model.getTree().getPathForLocation(e.getX(), e.getY()));

			DefaultMutableTreeNode node = (DefaultMutableTreeNode)model.getTree().getSelectionPath().getLastPathComponent();

			if(!(node.getUserObject() instanceof Date) && !(node.getUserObject() instanceof MMPCOrderNode)) {

				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		SwingTool.setCursorsFromChild(e.getComponent(), false);
	}

	private void handleActionEvent(ActionEvent e) {

		 Timestamp df = getDateFrom();
		 Timestamp dt = getDateTo();
		 MResource r = getResource();

		 if (df != null && dt != null && r != null) {

			model = CRPSDatasetFactory.get(df, dt, r);

		    JFreeChart jfreechart = createChart(model.getDataset(),	getChartTitle(), getSourceUOM());

		    chartPanel = new ChartPanel(jfreechart, false);
		 	contentPanel.setLeftComponent(chartPanel);

		 	JTree tree = model.getTree();
		 	tree.addMouseListener(new TreeHandler());
		 	contentPanel.setRightComponent(new JScrollPane(tree));
		 	popup = createPopup(tree);

		 	contentPanel.setVisible(true);

		 	contentPanel.validate();
		 	contentPanel.repaint();
		 }

		 SwingTool.setCursorsFromParent(getWindow(), false);
	}

	private String getChartTitle() {

	 	MResource r = getResource();
	 	String title = r.getName() != null ? r.getName() : "";
	 	title = title +  " " + r.getDescription() != null ? r.getDescription() : "";

	 	return title;
	}

	public Timestamp getDateFrom() {

		Timestamp t = null;

		if(dateFrom.getValue() != null) {

			t = (Timestamp)dateFrom.getValue();
		}

		return t;
	}

	public Timestamp getDateTo() {

		Timestamp t = null;

		if(dateTo.getValue() != null) {

			t = (Timestamp)dateTo.getValue();
		}

		return t;
	}

	public MUOM getSourceUOM() {

	 	MResource r = getResource();
		int uom_id = r.getResourceType().getC_UOM_ID();

	 	return (uom_id > 0) ? new MUOM(Env.getCtx(),uom_id, null) : null;
	}

	public MResource getResource() {

		MResource r = null;

		if(resource.getValue() != null) {

			r = new MResource(Env.getCtx(), ((Integer)resource.getValue()).intValue(), null);
		}

		return r;
	}

	public MUOM getTargetUOM() {

		MUOM u = null;

		if(resource.getValue() != null) {

			u = new MUOM(Env.getCtx(), ((Integer)resource.getValue()).intValue(), null);
		}

		return u;
	}

	private JFreeChart createChart(CategoryDataset dataset, String title, MUOM uom) {

		JFreeChart chart = ChartFactory.createBarChart3D(
				title ,
				Msg.translate(Env.getCtx(), "Day"),    			// X-Axis label
				Msg.translate(Env.getCtx(), (uom == null) ? "" : uom.getName()),   	// Y-Axis label
				dataset,         								// Dataset
				PlotOrientation.VERTICAL, 						// orientation
				true,                     						// include legend
				true,                     						// tooltips?
				false                     						// URLs?
		);

		chart.setBackgroundPaint(Color.WHITE);
		chart.setAntiAlias(true);
		chart.setBorderVisible(true);

		CategoryPlot plot = chart.getCategoryPlot();
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.GRAY);

		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.GRAY);


		BarRenderer3D barrenderer  = (BarRenderer3D)plot.getRenderer();
		barrenderer.setDrawBarOutline(false);
                barrenderer.setBaseItemLabelGenerator(new LabelGenerator());
		//barrenderer.setBaseLabelGenerator(new LabelGenerator());
		barrenderer.setBaseItemLabelsVisible(true);
        barrenderer.setSeriesPaint(0, new Color(200, 200, 200, 128));
        barrenderer.setSeriesPaint(1, new Color(60, 60, 100, 128));

        ItemLabelPosition itemlabelposition = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_CENTER);
        barrenderer.setPositiveItemLabelPosition(itemlabelposition);

		CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );

        return chart;
	}

	public void dispose() {

		super.dispose();

		if(resource != null) {

			resource.dispose();
		}
		resource = null;

		if(dateFrom != null) {

			dateFrom.dispose();
		}
		dateFrom = null;

		if(dateTo != null) {

			dateTo.dispose();
		}
		dateTo = null;

		if(worker != null) {

			worker.stop();
		}
		worker = null;

	    chartPanel = null;
		contentPanel = null;
		popup = null;
	}
}

