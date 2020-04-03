package io.github.mnyudina.motifsVis.tests;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.io.PajekNetReader;
import io.github.mnyudina.motifs.util.MySparseGraph;
import io.github.mnyudina.subgraph.RandSF4;
/**
 * 
 * @author Gleepa
 * _DEBUG
 *
 */
public class TimeTestUndir {
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
		} catch (IOException e1) {
			return;
		}

		// System.out.println("V=" + graph.getEdgeCount());
		// System.out.println("E=" + graph.getVertexCount());
		System.out.println(graph);

		int N_plus = 100000;
		int numNet = 10;
		Set<Integer> idSet = new HashSet(); 

		long t = System.currentTimeMillis();
		RandSF4 activeCalc = new RandSF4<>(graph, N_plus);
		activeCalc.execute();

		for (int j = 0; j < activeCalc.motifs.length; j++)
			if (activeCalc.motifs[j] > 0){
				System.out.print( j+ ";" );
				idSet.add(j);
			}
		for (int j = 0; j < activeCalc.motifs.length; j++)
			if (idSet.contains(j))
				System.out.print(";" +j );
		System.out.println();
		
		for (int j = 0; j < activeCalc.motifs.length; j++)
			if (idSet.contains(j))
				System.out.print(activeCalc.motifs[j] + ";" );
		for (int j = 0; j < activeCalc.motifs.length; j++)
			if (idSet.contains(j))
				System.out.print(";" + activeCalc.sigmas[j]);

		System.out.println();

		// System.out.println("dt=" + (System.currentTimeMillis() - t));

		double av_motifs[][] = new double[numNet][218];
		double av_SKO[][] = new double[numNet][218];

		long randomization[] = new long[numNet];
		long time_execution[] = new long[numNet];

		for (int i = 0; i < numNet; i++) {
			t = System.currentTimeMillis();
			MySparseGraph g_i = new MySparseGraph(graph).getRandomized(3, 3);
			randomization[i] = (System.currentTimeMillis() - t);
			t = System.currentTimeMillis();
			activeCalc = new RandSF4<>(g_i, N_plus);
			activeCalc.execute();
			time_execution[i] = (System.currentTimeMillis() - t);
			for (int j = 0; j < activeCalc.motifs.length; j++)
				if (idSet.contains(j))
					System.out.print(activeCalc.motifs[j]+";");
			for (int j = 0; j < activeCalc.motifs.length; j++)
				if (idSet.contains(j))
					System.out.print(";" + activeCalc.sigmas[j]);

			System.out.println();

		}
		System.out.println(average(randomization));
		System.out.println(average(time_execution));

	}

	private static Double average(long[] randomization) {
		long sum = 0l;
		for (int i = 0; i < randomization.length; i++) {
			sum = randomization[i] + sum;
		}

		return sum / (double) randomization.length;
	}
}
