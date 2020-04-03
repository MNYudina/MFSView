package io.github.mnyudina.motifsVis.tests;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.io.PajekNetReader;
import io.github.mnyudina.motifs.util.FormatUtils;
import io.github.mnyudina.motifs.util.MySparseGraph;
import io.github.mnyudina.subgraph.RandSF4;
/**
 * 
 * @author Gleepa
 *
 */
public class SKO_M_Test {
	static Factory<Integer> createIntegerFactory() {
		return new Factory<Integer>() {
			private int n = 0;

			@Override
			public Integer create() {
				return n++;
			}
		};
	}

	public static void main(String[] args) {
		MySparseGraph graph = null;
		try {
			graph = (MySparseGraph) new PajekNetReader<>(createIntegerFactory(), createIntegerFactory())
					.load("graphs/VKOmgtu.net", new MySparseGraph<>());
			// .load("graphs/Pahomov.net", new MySparseGraph<>());
		} catch (IOException e1) {
			return;
		}

		// System.out.println("V=" + graph.getEdgeCount());
		// System.out.println("E=" + graph.getVertexCount());
		System.out.println(graph);

		int N_plus = 100000;
		int numNet = 1;

		long t = System.currentTimeMillis();
		Set<Integer> idSet = new HashSet();

		RandSF4 activeCalc1 = new RandSF4<>(graph, N_plus);
		activeCalc1.execute();
		/*
		 * for (int j = 0; j < activeCalc1.motifs.length; j++) if
		 * (activeCalc1.motifs[j] > 0) { System.out.print(j + ";");
		 * idSet.add(j); } for (int j = 0; j < activeCalc1.motifs.length; j++)
		 * if (idSet.contains(j)) System.out.print(";" + j);
		 * System.out.println();
		 * 
		 * for (int j = 0; j < activeCalc1.motifs.length; j++) if
		 * (idSet.contains(j)) System.out.print(activeCalc1.motifs[j] + ";");
		 * for (int j = 0; j < activeCalc1.motifs.length; j++) if
		 * (idSet.contains(j)) System.out.print(";" + activeCalc1.sigmas[j]);
		 * 
		 * System.out.println();
		 */
		// System.out.println("dt=" + (System.currentTimeMillis() - t));

		double av_motifs[][] = new double[numNet][218];
		double av_SKO[][] = new double[numNet][218];

		RandSF4 activeCalc = new RandSF4<>(graph, N_plus);
		t = System.currentTimeMillis();
		MySparseGraph g_i = new MySparseGraph(graph).getRandomized(3, 3);
		t = System.currentTimeMillis();
		activeCalc = new RandSF4<>(g_i, N_plus);
		activeCalc.execute();


	}

	private static Double average(long[] randomization) {
		long sum = 0l;
		for (int i = 0; i < randomization.length; i++) {
			sum = randomization[i] + sum;
		}

		return sum / (double) randomization.length;
	}
}
