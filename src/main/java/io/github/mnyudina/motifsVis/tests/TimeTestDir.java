package io.github.mnyudina.motifsVis.tests;

import java.beans.PropertyChangeListener;
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
public class TimeTestDir {
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
		long tStart =System.currentTimeMillis();
		try {
			graph = (MySparseGraph) new PajekNetReader<>(createIntegerFactory(), createIntegerFactory())
					// .load("graphs/PathwayCommons.net", new
					// MySparseGraph<>());
					// .load("graphs/GenReg.net", new MySparseGraph<>());
					 .load("graphs/VKOmgtu.net", new MySparseGraph<>());
					// .load("graphs/Pahom2.net", new MySparseGraph<>());
					//.load("graphs/PPI_HS_U.net", new MySparseGraph<>());
			// .load("graphs/Twitter.net", new MySparseGraph<>());

		} catch (IOException e1) {
			return;
		}

		// System.out.println("E=" + graph.getEdgeCount());
		// System.out.println("V=" + graph.getVertexCount());
		System.out.println(graph);

		int N_plus = 100000;

		long t = System.currentTimeMillis();
		Set<Integer> idSet = new HashSet();

		RandSF4 activeCalc = new RandSF4<>(graph, N_plus);
		activeCalc.execute();
		int count = 0;

		for (int j = 0; j < activeCalc.motifs.length; j++)
			if (activeCalc.motifs[j] !=null) {
				System.out.print(j + ";");
				idSet.add(j);
			}
		System.out.println();
		
		for (int j = 0; j < activeCalc.motifs.length; j++)
			if (activeCalc.motifs[j] !=null) {
				System.out.print(activeCalc.motifsScobka[j] + activeCalc.motifsLapka[j]  + ";");
			}
		System.out.println();
		for (int j = 0; j < activeCalc.motifs.length; j++)
			if (idSet.contains(j))
				System.out.print(activeCalc.motifs[j] + ";");

		System.out.println();

		for (int j = 0; j < activeCalc.motifs.length; j++)
			if (idSet.contains(j))
				System.out.print(activeCalc.sigmas[j] + ";");

		System.out.println();

		int numNet =10;
		long randomization[] = new long[numNet];
		long time_execution[] = new long[numNet];

		for (int i = 0; i < numNet; i++) {

			t = System.currentTimeMillis();
			MySparseGraph g_i = new MySparseGraph(graph).getRandomized(3, 3);
			randomization[i] = (System.currentTimeMillis() - t);
			t = System.currentTimeMillis();
			RandSF4 activeCalc2 = new RandSF4<>(g_i, N_plus);
			activeCalc2.execute();
			time_execution[i] = (System.currentTimeMillis() - t);
			double sum=0.;
			for (int j = 0; j < activeCalc2.motifs.length; j++)
				{
				if (idSet.contains(j))
					System.out.print(activeCalc2.motifs[j] + ";");
				if(activeCalc2.motifs[j]!=null)
					sum= sum+activeCalc2.motifs[j]; 
				}
			
			System.out.println(";;"+sum);

			/*for (int j = 0; j < activeCalc2.motifs.length; j++)
				if (idSet.contains(j))
					System.out.print(activeCalc2.sigmas[j] + ";");

			System.out.println();*/
		}
		
		System.out.println("Av. time of a randomization:;"+ average(randomization));
		System.out.println("Av. time of subgraph counting:;"+average(time_execution));
		System.out.println("Total time of calculation:;"+(System.currentTimeMillis()-tStart));

	}

	private static Double average(long[] randomization) {
		long sum = 0l;
		for (int i = 0; i < randomization.length; i++) {
			sum = randomization[i] + sum;
		}

		return sum / (double) randomization.length;
	}
}
