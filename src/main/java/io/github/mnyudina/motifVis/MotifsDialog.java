package io.github.mnyudina.motifVis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.PajekNetReader;
import io.github.mnyudina.motifs.util.CreateKoefMass;
import io.github.mnyudina.motifs.util.FormatUtils;
import io.github.mnyudina.motifs.util.MySparseGraph;
import io.github.mnyudina.subgraph.EnumFrames3;
import io.github.mnyudina.subgraph.EnumFrames3p;
import io.github.mnyudina.subgraph.EnumFrames4;
import io.github.mnyudina.subgraph.EnumFrames4p;
import io.github.mnyudina.subgraph.AbstractExecutor;
import io.github.mnyudina.subgraph.RandSF3;
import io.github.mnyudina.subgraph.RandSF3p;
import io.github.mnyudina.subgraph.RandSF4;
import io.github.mnyudina.subgraph.RandSF4p;

import javax.swing.UIManager;
import javax.swing.RowFilter.ComparisonType;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JProgressBar;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.Toolkit;

/**
 * 
 * @author Gleepa
 *
 */
public class MotifsDialog extends JFrame {
	private int numNet = 1;
	private int numSamples = 100000;
	private int exchangesPerEdges = 3;
	private int exchangesAttempts = 3;
	private int size = 4;
	MyPictureCellRenderer picRenderer = new MyPictureCellRenderer();

	private Double N_G[];
	private Double SKO_G[];
	private Double N_GR[][];// use only while calculation
	private Double Z_SCORE[];
	private Double R[];
	private Double R_[];
	private Double COUNT_G[];

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	class MyDialog extends JDialog {

		JTable table;
		TableRowSorter<TableModel> sorter;
		JSpinner ZcoreFilter, RFilter, atLeast;
		JCheckBox viewZ, viewR, viewR_, viewSKO, viewPicture;
		JRadioButton rdbtnNum, rdbtnR, rdbtnZ;

		private void setTableParameters() {

			long t = System.currentTimeMillis();
			boolean showZScore = viewZ.isSelected();
			boolean showR = viewR.isSelected();
			;
			boolean showR_ = viewR_.isSelected();
			;
			boolean showSKO = viewSKO.isSelected();
			;
			boolean showPic = viewPicture.isSelected();
			;

			if (showPic) {
				// table.getColumnModel().getColumn(0).setPreferredWidth(60);
				picRenderer.setHeight(50);
				table.getColumnModel().getColumn(0).setMinWidth(55);
				table.getColumnModel().getColumn(0).setMaxWidth(55);

				table.getColumnModel().getColumn(1).setMinWidth(0);
				table.getColumnModel().getColumn(1).setMaxWidth(0);

			} else {
				picRenderer.setHeight(20);

				table.getColumnModel().getColumn(0).setMinWidth(0);
				table.getColumnModel().getColumn(0).setMaxWidth(0);

				table.getColumnModel().getColumn(1).setMinWidth(55);
				table.getColumnModel().getColumn(1).setMaxWidth(55);
			}
			;
			table.getColumnModel().getColumn(2).setPreferredWidth(70);
			table.getColumnModel().getColumn(3).setPreferredWidth(70);
			table.getColumnModel().getColumn(4).setPreferredWidth(50);
			table.getColumnModel().getColumn(5).setPreferredWidth(50);
			table.getColumnModel().getColumn(6).setPreferredWidth(50);

			table.getColumnModel().getColumn(7).setPreferredWidth(0);
			table.getColumnModel().getColumn(7).setMinWidth(0);
			table.getColumnModel().getColumn(7).setMaxWidth(0);

			if (showZScore) {
				table.getColumnModel().getColumn(6).setMinWidth(50);
				table.getColumnModel().getColumn(6).setMaxWidth(180);

			} else {
				table.getColumnModel().getColumn(6).setMinWidth(0);
				table.getColumnModel().getColumn(6).setMaxWidth(0);
			}

			if (showR) {
				table.getColumnModel().getColumn(4).setMinWidth(50);
				table.getColumnModel().getColumn(4).setMaxWidth(180);

			} else {
				table.getColumnModel().getColumn(4).setMinWidth(0);
				table.getColumnModel().getColumn(4).setMaxWidth(0);
			}

			if (showR_) {
				table.getColumnModel().getColumn(5).setMinWidth(50);
				table.getColumnModel().getColumn(5).setMaxWidth(180);

			} else {
				table.getColumnModel().getColumn(5).setMinWidth(0);
				table.getColumnModel().getColumn(5).setMaxWidth(0);
			}

			if (showSKO) {
				table.getColumnModel().getColumn(3).setMinWidth(50);
				table.getColumnModel().getColumn(3).setMaxWidth(180);

			} else {
				table.getColumnModel().getColumn(3).setMinWidth(0);
				table.getColumnModel().getColumn(3).setMaxWidth(0);
			}

			// table.getColumnModel().getColumn(2).setMinWidth(0);
			// table.getColumnModel().getColumn(2).setMaxWidth(0);

		}

		private void setSorterParameters() {
			Double Zcore = Double.parseDouble(ZcoreFilter.getValue().toString());
			Double R_ = Double.parseDouble(RFilter.getValue().toString());
			Double leastN = Double.parseDouble(atLeast.getValue().toString());

			// sorter.setRowFilter(RowFilter.numberFilter(ComparisonType.AFTER, leastN, 1));
			if (rdbtnNum.isSelected())
				sorter.setRowFilter(RowFilter.numberFilter(ComparisonType.AFTER, leastN, 7));
			else if (rdbtnR.isSelected())
				sorter.setRowFilter(RowFilter.numberFilter(ComparisonType.AFTER, R_, 5));
			else if (rdbtnZ.isSelected())
				sorter.setRowFilter(RowFilter.numberFilter(ComparisonType.AFTER, Zcore, 6));

		}

		public MyDialog() {
			super(me, "View motifs", true);
			this.setLocationRelativeTo(me);
			Object[][] rows;
			JPanel[] pans;
			if (size == 4) {
				rows = new Object[218][8];
				pans = create4IdPanel();
			} else {
				rows = new Object[16][8];
				pans = create3IdPanel();
			}

			for (int i = 0; i < rows.length; i++) {

				rows[i][0] = pans[i];
				rows[i][1] = (double) i;
				rows[i][2] = N_G[i];
				rows[i][3] = SKO_G[i];
				rows[i][4] = R[i];
				rows[i][5] = R_[i];
				rows[i][6] = Z_SCORE[i];
				rows[i][7] = COUNT_G[i];

			}
			String columns[] = { "Picture", "Motif ID", "N_G", "SD", "R", "R'", "Z-Score", "COUNT_G" };
			DefaultTableModel model = new DefaultTableModel(rows, columns) {
				public Class getColumnClass(int column) {
					Class returnValue;
					if ((column > 0) && (column < getColumnCount())) {
						returnValue = Double.class;// getValueAt(0, column).getClass();
					} else if (column == 0) {
						returnValue = getValueAt(0, column).getClass();
					} else {
						returnValue = Object.class;
					}
					return returnValue;
				}
			};
			table = new JTable(model) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};

			table.getColumn("Picture").setCellRenderer(picRenderer);

			sorter = new TableRowSorter<TableModel>(model);
			table.setRowSorter(sorter);
			JScrollPane pane = new JScrollPane(table);
			pane.setPreferredSize(new Dimension(300, 500));

			JPanel controls = new JPanel();
			controls.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filters",
					TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			controls.setBounds(2, 2, 150, 120);
			controls.setPreferredSize(new Dimension(150, 120));
			controls.setLayout(null);
			ChangeListener listenerRaw = new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					setSorterParameters();
				}
			};

			JLabel ZcoreFilterLabel = new JLabel("Z-score bigger then ");
			ZcoreFilterLabel.setBounds(40, 20, 120, 20);
			controls.add(ZcoreFilterLabel);

			ZcoreFilter = new JSpinner();
			ZcoreFilter.setModel(new SpinnerNumberModel(2, 0.1, 5000, 0.5));
			ZcoreFilter.setBounds(160, 20, 85, 20);
			ZcoreFilter.addChangeListener(listenerRaw);
			controls.add(ZcoreFilter);

			JLabel lblRFilter = new JLabel("R' bigger then ");
			lblRFilter.setBounds(40, 50, 120, 20);
			controls.add(lblRFilter);

			RFilter = new JSpinner();
			RFilter.setModel(new SpinnerNumberModel(1.1, 0.1, 100, 0.1));
			RFilter.setBounds(160, 50, 85, 20);
			RFilter.addChangeListener(listenerRaw);

			controls.add(RFilter);

			JLabel lblAtLeast = new JLabel("Motifs found at least");
			lblAtLeast.setBounds(40, 80, 120, 20);
			controls.add(lblAtLeast);
			atLeast = new JSpinner();
			atLeast.setModel(new SpinnerNumberModel(1, -1, 100000, 1));
			atLeast.setBounds(160, 80, 85, 20);
			atLeast.addChangeListener(listenerRaw);
			controls.add(atLeast);
//-----------------------------------
			rdbtnZ = new JRadioButton("1", false);
			rdbtnZ.setBounds(20, 20, 20, 20);
			controls.add(rdbtnZ);

			rdbtnR = new JRadioButton("2", false);
			rdbtnR.setBounds(20, 50, 20, 20);
			controls.add(rdbtnR);

			rdbtnNum = new JRadioButton("3", true);
			rdbtnNum.setBounds(20, 80, 20, 20);
			controls.add(rdbtnNum);

			rdbtnZ.addChangeListener(listenerRaw);
			rdbtnR.addChangeListener(listenerRaw);
			rdbtnNum.addChangeListener(listenerRaw);

//--------------------------------------

			ButtonGroup groupFilters = new ButtonGroup();
			groupFilters.add(rdbtnZ);
			groupFilters.add(rdbtnR);
			groupFilters.add(rdbtnNum);

			setSorterParameters();

			viewZ = new JCheckBox("View Z-score", true);
			viewZ.setBounds(310, 10, 120, 20);
			controls.add(viewZ);

			viewR = new JCheckBox("View R", true);
			viewR.setBounds(310, 35, 70, 20);
			controls.add(viewR);

			viewR_ = new JCheckBox("View R'", true);
			viewR_.setBounds(385, 35, 70, 20);
			controls.add(viewR_);

			viewSKO = new JCheckBox("View Accuracy", true);
			viewSKO.setBounds(310, 60, 120, 20);
			controls.add(viewSKO);

			viewPicture = new JCheckBox("View Pictures", false);
			viewPicture.setBounds(310, 85, 120, 20);
			controls.add(viewPicture);
			// viewPicture.setEnabled(false);

			ChangeListener listenerColomn = new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					setTableParameters();
				}
			};
			viewZ.addChangeListener(listenerColomn);
			viewR.addChangeListener(listenerColomn);
			viewR_.addChangeListener(listenerColomn);
			viewSKO.addChangeListener(listenerColomn);
			viewPicture.addChangeListener(listenerColomn);

			JPanel controlPanel = new JPanel();
			controlPanel.setPreferredSize(new Dimension(150, 120));
			controlPanel.add(controls);
			setTableParameters();

			this.setLayout(new BorderLayout());
			this.add(controls, BorderLayout.NORTH);
			this.add(pane, BorderLayout.CENTER);
			this.setPreferredSize(new Dimension(500, 500));
			this.pack();
		}

		private JPanel[] create3IdPanel() {
			JPanel[] pans = new JPanel[218];
			int count = 0;
			Integer v1 = new Integer(1);
			Integer v2 = new Integer(2);
			Integer v3 = new Integer(3);
			Integer[] mass = { v1, v2, v3 };
			for (int i = 0; i < 4; i++) {
				for (int ii = 0; ii < 4; ii++) {
					for (int iii = 0; iii < 4; iii++) {
						Graph<Integer, Integer> graph = new SparseGraph<>();
						graph.addVertex(v1);
						graph.addVertex(v2);
						graph.addVertex(v3);
						if (i == 1)
							graph.addEdge(1, v1, v2, EdgeType.DIRECTED);
						else if (i == 2)
							graph.addEdge(1, v2, v1, EdgeType.DIRECTED);
						else if (i == 3)
							graph.addEdge(1, v1, v2, EdgeType.UNDIRECTED);

						if (ii == 1)
							graph.addEdge(2, v1, v3, EdgeType.DIRECTED);
						else if (ii == 2)
							graph.addEdge(2, v3, v1, EdgeType.DIRECTED);
						else if (ii == 3)
							graph.addEdge(2, v1, v3, EdgeType.UNDIRECTED);

						if (iii == 1)
							graph.addEdge(3, v3, v2, EdgeType.DIRECTED);
						else if (iii == 2)
							graph.addEdge(3, v2, v3, EdgeType.DIRECTED);
						else if (iii == 3)
							graph.addEdge(3, v3, v2, EdgeType.UNDIRECTED);

						Integer[] vert = (Integer[]) new Integer[4];
						vert[0] = v1;
						vert[1] = v2;
						vert[2] = v3;
						int code = 0;
						ArrayList<Pair<Integer>> pairsList = new ArrayList<Pair<Integer>>();

						for (int k = 0; k < vert.length - 1; k++) {
							for (int j = k + 1; j < vert.length; j++) {
								Integer o1 = graph.findEdge(vert[k], vert[j]);
								if (o1 != null) {
									code |= CreateKoefMass.igraph_i_isoclass_3_idx[3 * k + j];
									pairsList.add(new Pair<Integer>(k, j));
								}
								Integer o2 = graph.findEdge(vert[j], vert[k]);
								if (o2 != null) {
									code |= CreateKoefMass.igraph_i_isoclass_3_idx[3 * j + k];
									pairsList.add(new Pair<Integer>(j, k));

								}
							}
						}
						int iso = CreateKoefMass.igraph_i_isoclass2_3[code];
						if (pans[iso] == null) {
							pans[iso] = new JMy3Panel(pairsList);
							// pans[iso].dr

						}
					}
				}
			}

			return pans;
		}

		private JPanel[] create4IdPanel() {
			JPanel[] pans = new JPanel[218];
			int count = 0;
			Integer v1 = new Integer(1);
			Integer v2 = new Integer(2);
			Integer v3 = new Integer(3);
			Integer v4 = new Integer(4);

			Integer[] mass = { v1, v2, v3, v4 };
			for (int i = 0; i < 4; i++) {
				for (int ii = 0; ii < 4; ii++) {
					for (int iii = 0; iii < 4; iii++) {
						for (int iiii = 0; iiii < 4; iiii++) {
							for (int iiiii = 0; iiiii < 4; iiiii++) {
								for (int iiiiii = 0; iiiiii < 4; iiiiii++) {
									Graph<Integer, Integer> graph = new SparseGraph<>();
									graph.addVertex(v1);
									graph.addVertex(v2);
									graph.addVertex(v3);
									graph.addVertex(v4);
									if (i == 1)
										graph.addEdge(1, v1, v2, EdgeType.DIRECTED);
									else if (i == 2)
										graph.addEdge(1, v2, v1, EdgeType.DIRECTED);
									else if (i == 3)
										graph.addEdge(1, v1, v2, EdgeType.UNDIRECTED);
									if (ii == 1)
										graph.addEdge(2, v1, v3, EdgeType.DIRECTED);
									else if (ii == 2)
										graph.addEdge(2, v3, v1, EdgeType.DIRECTED);
									else if (ii == 3)
										graph.addEdge(2, v1, v3, EdgeType.UNDIRECTED);
									if (iii == 1)
										graph.addEdge(3, v3, v4, EdgeType.DIRECTED);
									else if (iii == 2)
										graph.addEdge(3, v4, v3, EdgeType.DIRECTED);
									else if (iii == 3)
										graph.addEdge(3, v3, v4, EdgeType.UNDIRECTED);
									if (iiii == 1)
										graph.addEdge(4, v4, v2, EdgeType.DIRECTED);
									else if (iiii == 2)
										graph.addEdge(4, v2, v4, EdgeType.DIRECTED);
									else if (iiii == 3)
										graph.addEdge(4, v4, v2, EdgeType.UNDIRECTED);
									if (iiiii == 1)
										graph.addEdge(5, v3, v2, EdgeType.DIRECTED);
									else if (iiiii == 2)
										graph.addEdge(5, v2, v3, EdgeType.DIRECTED);
									else if (iiiii == 3)
										graph.addEdge(5, v3, v2, EdgeType.UNDIRECTED);
									if (iiiiii == 1)
										graph.addEdge(6, v1, v4, EdgeType.DIRECTED);
									else if (iiiiii == 2)
										graph.addEdge(6, v4, v1, EdgeType.DIRECTED);
									else if (iiiiii == 3)
										graph.addEdge(6, v1, v4, EdgeType.UNDIRECTED);

									Integer[] vert = (Integer[]) new Integer[4];
									vert[0] = v1;
									vert[1] = v2;
									vert[2] = v3;
									vert[3] = v4;
									int code = 0;
									ArrayList<Pair<Integer>> pairsList = new ArrayList<Pair<Integer>>();

									for (int k = 0; k < vert.length - 1; k++) {
										for (int j = k + 1; j < vert.length; j++) {
											Integer o1 = graph.findEdge(vert[k], vert[j]);
											if (o1 != null) {
												code |= CreateKoefMass.arr_idx[4 * k + j];
												pairsList.add(new Pair<Integer>(k, j));
											}
											Integer o2 = graph.findEdge(vert[j], vert[k]);
											if (o2 != null) {
												code |= CreateKoefMass.arr_idx[4 * j + k];
												pairsList.add(new Pair<Integer>(j, k));

											}
										}
									}
									int iso = CreateKoefMass.arrcode[code];
									if (pans[iso] == null) {
										pans[iso] = new JMy4Panel(pairsList);
										// pans[iso].dr

									}
								}
							}
						}
					}
				}
			}

			return pans;
		}

	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////

//=================================================================================================================

	class TotalTask extends SwingWorker<Void, Void> implements PropertyChangeListener {
		private AbstractExecutor activeCalc;
		MySparseGraph graph;
		MySparseGraph g_i;
		// int exchangesPerEdges;

		class TaskRandomization extends SwingWorker<Void, Void> implements PropertyChangeListener {
			public TaskRandomization(int num) {
				super();
				exchangesPerEdges = num;
			}

			@Override
			public Void doInBackground() {
				int progress = 0;
				setProgress(0);
				System.out.println("progress::"+progress);
				while (progress < 100) {
					// Sleep for up to one second.
					try {
						Thread.sleep(500);
						//System.out.println("progress::"+progress);
					} catch (InterruptedException ignore) {
						System.out.println("Bad");
					}

					progress = g_i.getRandomizedStatus() / exchangesPerEdges;
					System.out.println("progress="+progress);
					setProgress(Math.min(progress, 100));
				}
				return null;
			}

			/*
			 * Executed in event dispatching thread
			 */
			@Override
			public void done() {
				// Toolkit.getDefaultToolkit().beep();
				System.out.println("Done");
				setCursor(null);
			}

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress" == evt.getPropertyName()) {
					int progress = (Integer) evt.getNewValue();
					RandomizationBar.setValue(progress);
				}
			}

		}

		class Task extends SwingWorker<Void, Void> implements PropertyChangeListener {
			/*
			 * Main task. Executed in background thread.
			 */
			@Override
			public Void doInBackground() {
				int progress = 0;
				setProgress(0);
				while (progress < 100) {
					// Sleep for up to one second.
					try {
						Thread.sleep(500);
					} catch (InterruptedException ignore) {
						System.out.println("ss");
					}
					progress = (int) (100. * activeCalc.iter / 2. / activeCalc.numberOfRuns);
					setProgress(Math.min(progress, 100));
				}
				return null;
			}

			/*
			 * Executed in event dispatching thread
			 */
			@Override
			public void done() {
				Toolkit.getDefaultToolkit().beep();
				// startButton.setEnabled(true);
				setCursor(null); // turn off the wait cursor
			}

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress" == evt.getPropertyName()) {
					int progress = (Integer) evt.getNewValue();
					taskProgressBar.setValue(progress);
				}
			}

		}

		Factory<Integer> createIntegerFactory() {
			return new Factory<Integer>() {
				private int n = 0;

				@Override
				public Integer create() {
					return n++;
				}
			};
		}

		@Override
		public Void doInBackground() {
			outTextPane.setText("");
			setProgress(0);

			runButton.setEnabled(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			String path = fileNameTextField.getText();

			graph = null;
			try {
				graph = (MySparseGraph) new PajekNetReader<>(createIntegerFactory(), createIntegerFactory()).load(path,
						new MySparseGraph<>());
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(me, "Problem while opening file");
				return null;
			}

			long tStart = System.nanoTime();

			outTextPane.append("Network name: " + path + "\n");
			outTextPane.append("" + graph + "\n");
			if (rdbtnSamplingCount.isEnabled()) {
				outTextPane.append("\nAlgorithm: sampling");
				outTextPane.append("\nSampling parameter: " + numSamples);
			} else {
				outTextPane.append("\nAlgorithm: enumeration");

			}
			outTextPane.append("\nSubgraph Size: " + size + "\n");

			if (numNet > 0) {
				outTextPane.append("\nGenerated: " + numNet + " random networks");
				outTextPane.append("\n   with locally constant number of bidirectional edges,");
				outTextPane.append("\n   " + exchangesPerEdges + " exchanges per edge and " + exchangesAttempts
						+ " tries per edge.");
			} else
				outTextPane.append("\nNo random graphs were generated");

			setProgress(1);

			Task task = new Task();
			task.addPropertyChangeListener(task);
			task.execute();

			if (rdbtnSamplingCount.isSelected()) {

				if (size == 3) {
					if (checkboxParallel.isSelected())
						activeCalc = new RandSF3p(graph, numSamples);
					else
						activeCalc = new RandSF3(graph, numSamples);

				} else {
					if (checkboxParallel.isSelected())
						activeCalc = new RandSF4p(graph, numSamples);
					else
						activeCalc = new RandSF4(graph, numSamples);
				}
			} else {

				if (size == 3) {
					if (checkboxParallel.isSelected())
						activeCalc = new EnumFrames3p(graph);
					else
						activeCalc = new EnumFrames3(graph);

				} else {
					if (checkboxParallel.isSelected())
						activeCalc = new EnumFrames4p(graph);
					else
						activeCalc = new EnumFrames4(graph);

				}

			}
			long t = System.nanoTime();
			activeCalc.execute();
			COUNT_G = activeCalc.counts;
			// System.out.println("dt=" + FormatUtils.durationToHMS(System.nanoTime() - t));

			outTextPane.append("\n\nCalculating in the original network took: "
					+ FormatUtils.durationToHMS(System.nanoTime() - t) + "\n");

			N_G = activeCalc.motifs;
			SKO_G = activeCalc.sigmas;

			for (int j = 0; j < activeCalc.motifs.length; j++) {
				if (N_G[j] != null) {
					R_[j] = 0.;
					R[j] = 0.;
				} else {
					N_G[j] = 0.;
					SKO_G[j] = 0.;
				}

			}

			int progress = 100 / (numNet + 1);
			setProgress(progress);
			double countR[];
			if (size == 3)
				countR = new double[16];
			else
				countR = new double[218];

			for (int i = 0; i < numNet; i++) {
				t = System.nanoTime();
				TaskRandomization RandomizationTask = new TaskRandomization(exchangesPerEdges);
				RandomizationTask.addPropertyChangeListener(RandomizationTask);

				g_i = new MySparseGraph(graph);
				RandomizationTask.execute();


				g_i.getRandomized(exchangesPerEdges, exchangesAttempts);
				// FormatUtils.durationToHMS(System.nanoTime() - t));

				outTextPane
						.append("\nRandomization " + i + " took: " + FormatUtils.durationToHMS(System.nanoTime() - t));

				task = new Task();
				task.addPropertyChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if ("progress" == evt.getPropertyName()) {
							int progress = (Integer) evt.getNewValue();
							taskProgressBar.setValue(progress);

						}

					}
				});

				task.execute();
				if (rdbtnSamplingCount.isSelected()) {

					if (size == 3)
						activeCalc = new RandSF3(g_i, numSamples);
					else
						activeCalc = new RandSF4<>(g_i, numSamples);

				} else {

					if (size == 3)
						activeCalc = new EnumFrames3(g_i);
					else
						activeCalc = new EnumFrames4p<>(g_i);
				}
				activeCalc.execute();

				progress += 100 / (numNet + 1);
				setProgress(Math.min(progress, 98));

				for (int j = 0; j < activeCalc.motifs.length; j++) {
					if (N_G[j] > 0.1) {

						Double x;
						if (activeCalc.motifs[j] != null)
							x = activeCalc.motifs[j];
						else
							x = 0.;
						N_GR[i][j] = x;

					}
					if (N_G[j] > 0.1 && N_GR[i][j] > 0.1) {
						countR[j] = countR[j] + 1.;
						R_[j] = R_[j] + (N_G[j] - 3. * SKO_G[j]) / (N_GR[i][j] + 3. * activeCalc.sigmas[j]);
						R[j] = R[j] + (N_G[j]) / (N_GR[i][j]);

					}
				}
			}
			outTextPane
					.append("\nTotal time of a calculation: " + FormatUtils.durationToHMS(System.nanoTime() - tStart));

			for (int j = 0; j < R.length; j++) {
				if (N_G[j] > 0.1 && countR[j] > 0) {
					R[j] = R[j] / countR[j];
					R_[j] = R_[j] / countR[j];

				} else {
					R[j] = Double.MIN_VALUE;
					R_[j] = Double.MIN_VALUE;

				}
			}
			for (int j = 0; j < N_G.length; j++) {
				double mx = 0.;
				double sx = 0.;

				for (int i = 0; i < N_GR.length; i++) {
					if (N_G[j] > 0.1) {

						mx = mx + N_GR[i][j] / N_GR.length;
						sx = sx + N_GR[i][j] * (N_GR[i][j] / (double) N_GR.length);
					}
				}
				if (mx > 0) {
					Z_SCORE[j] = (N_G[j] - mx) / Math.sqrt((sx - mx * mx));
				} else
					Z_SCORE[j] = -1. / 0.;
			}

			// System.out.println("����� ������� " + i + " :" +
			// FormatUtils.durationToHMS(System.nanoTime() - t));

			setProgress(100);
			precessEnded();
			return null;
		}

		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			runButton.setEnabled(true);
			setCursor(null);
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if ("progress" == evt.getPropertyName()) {
				int progress = (Integer) evt.getNewValue();
				totalPogressBar.setValue(progress);

			}
		}
	}

//========================================================================================================
	private final JPanel contentPanel = new JPanel();
	private final JProgressBar totalPogressBar;
	private final JProgressBar taskProgressBar;
	private final JProgressBar RandomizationBar;
	private final JTextField fileNameTextField = new JTextField();
	private final MotifsDialog me;
	private final JButton runButton;
	private final JButton btnSave;
	private final JButton btnLoad;
	private final JButton btnView;

	private final JSpinner spin_numNetwork;
	private final JSpinner numSampleSpinner;
	private final JSpinner spin_attempts;
	private final JSpinner spin_l_rtyies;
	private final JSpinner MotifSizeSpinner;
	private final JTextArea outTextPane;
	JRadioButton rdbtnEnumeration;
	JRadioButton rdbtnSamplingCount;
	JCheckBox checkboxParallel;

	void precessStarted() {
		btnLoad.setEnabled(false);
		btnSave.setEnabled(false);
		btnView.setEnabled(false);

	}

	void precessEnded() {
		btnLoad.setEnabled(true);
		btnSave.setEnabled(true);
		btnView.setEnabled(true);

	}

	private ActionListener runMotifs = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			precessStarted();
			numNet = (Integer) spin_numNetwork.getValue();
			numSamples = (Integer) numSampleSpinner.getValue();
			exchangesPerEdges = (Integer) spin_l_rtyies.getValue();
			exchangesAttempts = (Integer) spin_attempts.getValue();

			size = (Integer) MotifSizeSpinner.getValue();
			if (size == 4) {
				N_GR = new Double[numNet][218];
				N_G = new Double[218];
				SKO_G = new Double[218];
				// SKO_GR = new Double[218];
				R = new Double[218];
				R_ = new Double[218];
				Z_SCORE = new Double[218];
				COUNT_G = new Double[218];

			} else if (size == 3) {
				N_GR = new Double[numNet][16];
				N_G = new Double[16];
				SKO_G = new Double[16];
				// SKO_GR = new Double[16];
				R = new Double[16];
				R_ = new Double[16];
				Z_SCORE = new Double[16];
				COUNT_G = new Double[16];

			}

			TotalTask task = new TotalTask();

			task.addPropertyChangeListener(task);
			task.execute();

		}

	};
//=============================================================================================================

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MotifsDialog dialog = new MotifsDialog();
				dialog.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
				dialog.setVisible(true);
				System.out.println(dialog.getSize());

			}
		});

	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Create the dialog.
	 */

	public MotifsDialog() {
		me = this;
		setResizable(false);
		setTitle("MFSViewer");
		setBounds(100, 100, 720, 480);
		getContentPane().setLayout(new BorderLayout());

		// create text feild for file name, and add button to choose it
		JPanel panelFN = new JPanel();
		panelFN.setBorder(
				new TitledBorder(UIManager.getBorder("TitledBorder.border"), "File name of a network in Pajek format",
						TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		fileNameTextField.setPreferredSize(new Dimension(550, 25));
		fileNameTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		fileNameTextField.setText("graphs/VKOmgtu.net");
		JButton bntOpenFile = new JButton("Open");
		bntOpenFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// try to open file
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Pajek files", "net");
				chooser.setCurrentDirectory(new File("."));
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileNameTextField.setText(chooser.getSelectedFile().getAbsolutePath());
				}

			}
		});
		panelFN.add(fileNameTextField);
		panelFN.add(bntOpenFile);
		add(panelFN, BorderLayout.NORTH);

		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Algorithm options",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(29, 10, 658, 120);
		contentPanel.add(panel);
		panel.setLayout(null);

		rdbtnEnumeration = new JRadioButton("Full Enumiration", false);
		rdbtnEnumeration.setBounds(230, 20, 156, 23);

		rdbtnSamplingCount = new JRadioButton("Sampling frames", true);
		rdbtnSamplingCount.setBounds(230, 45, 156, 23);

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnEnumeration);
		group.add(rdbtnSamplingCount);

		panel.add(rdbtnEnumeration);
		panel.add(rdbtnSamplingCount);

		checkboxParallel = new JCheckBox("Use Parallelism");
		checkboxParallel.setBounds(50, 80, 156, 23);
		panel.add(checkboxParallel);

		JLabel lblSubgraphSize = new JLabel("Subgraph Size");
		lblSubgraphSize.setBounds(20, 20, 120, 20);
		panel.add(lblSubgraphSize);
		MotifSizeSpinner = new JSpinner();
		MotifSizeSpinner.setModel(new SpinnerNumberModel(4, 3, 4, 1));
		MotifSizeSpinner.setBounds(140, 20, 70, 20);
		panel.add(MotifSizeSpinner);

		JLabel lblNumIter = new JLabel("Number of samples");
		lblNumIter.setBounds(20, 45, 120, 20);
		panel.add(lblNumIter);
		numSampleSpinner = new JSpinner();
		numSampleSpinner.setModel(new SpinnerNumberModel(numSamples, 10000, 100000000, 10000));
		numSampleSpinner.setBounds(140, 45, 70, 20);
		panel.add(numSampleSpinner);

		JPanel RandPanel = new JPanel();
		RandPanel.setLayout(null);
		RandPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Randomization options",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		RandPanel.setBounds(400, 10, 240, 103);
		JLabel l_rtyies = new JLabel("Exchanges per edges");
		l_rtyies.setBounds(35, 25, 125, 20);
		RandPanel.add(l_rtyies);
		spin_l_rtyies = new JSpinner();
		spin_l_rtyies.setModel(new SpinnerNumberModel(exchangesPerEdges, 1, 5, 1));
		spin_l_rtyies.setBounds(170, 25, 45, 20);
		RandPanel.add(spin_l_rtyies);

		JLabel l_attempts = new JLabel("Exchange Attempts");
		l_attempts.setBounds(35, 50, 125, 20);
		RandPanel.add(l_attempts);
		spin_attempts = new JSpinner();
		spin_attempts.setModel(new SpinnerNumberModel(exchangesAttempts, 1, 5, 1));
		spin_attempts.setBounds(170, 50, 45, 20);
		RandPanel.add(spin_attempts);

		JLabel l_numNetwork = new JLabel("Randomized graphs");
		l_numNetwork.setBounds(35, 75, 125, 20);
		RandPanel.add(l_numNetwork);
		spin_numNetwork = new JSpinner();
		spin_numNetwork.setModel(new SpinnerNumberModel(numNet, 0, 100000, 1));
		spin_numNetwork.setBounds(170, 75, 45, 20);
		RandPanel.add(spin_numNetwork);

		panel.add(RandPanel);

		rdbtnEnumeration.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				lblNumIter.setEnabled(false);
				numSampleSpinner.setEnabled(false);
			}
		});

		rdbtnSamplingCount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				lblNumIter.setEnabled(true);
				numSampleSpinner.setEnabled(true);

			}
		});

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(
				new TitledBorder(null, "Algorithm running", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(29, 133, 660, 91);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);

		totalPogressBar = new JProgressBar(0, 100);
		totalPogressBar.setValue(0);
		totalPogressBar.setStringPainted(true);
		totalPogressBar.setBounds(172, 25, 199, 14);
		panel_1.add(totalPogressBar);
		JLabel lblTotalProgress = new JLabel("Total Progress");
		lblTotalProgress.setBounds(40, 25, 87, 15);
		panel_1.add(lblTotalProgress);

		taskProgressBar = new JProgressBar(0, 100);
		taskProgressBar.setValue(0);
		taskProgressBar.setStringPainted(true);
		taskProgressBar.setBounds(172, 46, 199, 14);
		panel_1.add(taskProgressBar);
		JLabel lblTotalProgress2 = new JLabel("Graph processing");
		lblTotalProgress2.setBounds(40, 46, 140, 15);
		panel_1.add(lblTotalProgress2);

		RandomizationBar = new JProgressBar(0, 100);
		RandomizationBar.setValue(0);
		RandomizationBar.setStringPainted(true);
		RandomizationBar.setBounds(172, 65, 199, 16);
		panel_1.add(RandomizationBar);
		JLabel lblRandomizationBar = new JLabel("Randomization graph");
		lblRandomizationBar.setBounds(40, 65, 140, 16);
		panel_1.add(lblRandomizationBar);

		runButton = new JButton("Start");
		runButton.setBounds(527, 57, 89, 23);
		panel_1.add(runButton);
		runButton.addActionListener(runMotifs);
		runButton.setActionCommand("Start");

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Results", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(29, 221, 658, 154);
		contentPanel.add(panel_2);
		panel_2.setLayout(null);

		btnView = new JButton("View");
		btnView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JDialog dialog = new MyDialog();
				dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				dialog.setVisible(true);

			}
		});
		btnView.setEnabled(false);
		btnView.setBounds(10, 30, 89, 23);
		panel_2.add(btnView);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("."));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showSaveDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					exportToCSV(chooser.getSelectedFile().getAbsolutePath());
				} else {
					JOptionPane.showMessageDialog(me, "File not saved");

				}
			}
		});
		btnSave.setBounds(10, 60, 89, 23);
		btnSave.setEnabled(false);
		panel_2.add(btnSave);

		btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("."));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					boolean isRead = false;
					try {
						isRead = importFromCSV(chooser.getSelectedFile().getAbsolutePath());
					} catch (Exception e) {
					}
					if (!isRead) {
						JOptionPane.showMessageDialog(me, "File not readed");
					}
					btnView.setEnabled(true);
					btnSave.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(me, "File not opened");
				}
			}
		});
		btnLoad.setBounds(10, 90, 89, 23);
		panel_2.add(btnLoad);

		outTextPane = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(outTextPane);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		JPanel psp = new JPanel();
		psp.setLayout(new BorderLayout());
		psp.add(scrollPane, BorderLayout.CENTER);
		psp.setBounds(131, 10, 520, 140);
		outTextPane.setEditable(false);

		panel_2.add(psp);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
	}

	/**
	 * @author Gleepa
	 * @param pathToExportTo
	 * @return
	 */
	public boolean exportToCSV(String pathToExportTo) {

		try {
			FileWriter csv = new FileWriter(new File(pathToExportTo));
			for (String line : outTextPane.getText().split("\\n"))
				csv.append(line + "\n");

			csv.append("N_G;SKO_G;R;R_;Z_SCORE;COUNT_G\n");

			for (int index = 0; index < N_G.length; index++) {
				// csv.append(index + ";");

				if (index == N_G.length - 1) {
					csv.append(N_G[index].toString() + ";");
					csv.append(SKO_G[index].toString() + ";");
					csv.append(R[index].toString() + ";");
					csv.append(R_[index].toString() + ";");
					csv.append(Z_SCORE[index].toString() + ";");
					csv.append(COUNT_G[index].toString());

				} else {
					csv.append(N_G[index].toString() + ";");
					csv.append(SKO_G[index].toString() + ";");
					csv.append(R[index].toString() + ";");
					csv.append(R_[index].toString() + ";");
					csv.append(Z_SCORE[index].toString() + ";");
					csv.append(COUNT_G[index].toString());
					csv.append("\n");
				}
			}
			csv.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * 
	 * @author Gleepa
	 * @param pathFrom
	 * @return
	 */
	public boolean importFromCSV(String pathFrom) {
		try {
			File file = new File(pathFrom);
			Scanner sc = new Scanner(file);
			int n = 0;

			String firstLine = sc.nextLine();
			if (firstLine.startsWith("Network name")) {
				outTextPane.setText("");
				outTextPane.append(firstLine + "\n");
			} else
				return false;

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] words = line.split(": ");
				if (words[0].equals("Subgraph Size")) {
					int x = Integer.parseInt(words[1]);
					if (x == 4)
						n = 218;
					else
						n = 16;
				}
				if (line.equals("N_G;SKO_G;R;R_;Z_SCORE;COUNT_G"))
					break;
				outTextPane.append(line + "\n");
			}
			if (n != 218 && n != 16) {
				JOptionPane.showMessageDialog(me, "Wrong File");
				return false;
			}
			N_G = new Double[n];
			SKO_G = new Double[n];
			Z_SCORE = new Double[n];
			R = new Double[n];
			R_ = new Double[n];
			Z_SCORE = new Double[n];
			COUNT_G = new Double[n];

			int i = 0;

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] str = line.split(";");
				N_G[i] = Double.parseDouble(str[0]);
				SKO_G[i] = Double.parseDouble(str[1]);
				R[i] = Double.parseDouble(str[2]);
				R_[i] = Double.parseDouble(str[3]);
				Z_SCORE[i] = Double.parseDouble(str[4]);
				COUNT_G[i] = Double.parseDouble(str[5]);
				i++;
			}

			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
