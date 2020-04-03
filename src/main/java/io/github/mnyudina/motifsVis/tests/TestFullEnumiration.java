package io.github.mnyudina.motifsVis.tests;

import java.io.IOException;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.io.PajekNetReader;
import io.github.mnyudina.motifs.util.MySparseGraph;
import io.github.mnyudina.subgraph.EnumFrames4p;
import io.github.mnyudina.subgraph.RandSF4;
/**
 * 
 * @author Gleepa
 * _DEBUG
 *
 */
public class TestFullEnumiration {
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
		long tStart = System.currentTimeMillis();
		try {
			graph = (MySparseGraph) new PajekNetReader<>(createIntegerFactory(), createIntegerFactory())
					// .load("graphs/PathwayCommons.net", new
					// MySparseGraph<>());
					// .load("graphs/GenReg.net", new MySparseGraph<>());
					.load("graphs/VKOmgtu.net", new MySparseGraph<>());
			// .load("graphs/Pahom2.net", new MySparseGraph<>());
			// .load("graphs/PPI_HS_U.net", new MySparseGraph<>());
			// .load("graphs/Twitter.net", new MySparseGraph<>());

		} catch (IOException e1) {
			return;
		}

		System.out.println(graph);
		
		EnumFrames4p activeCalc2 = new EnumFrames4p<>(graph);
		activeCalc2.execute();
		double sum=0.;
		for (int j = 0; j < activeCalc2.motifs.length; j++)
			{
			if(activeCalc2.motifs[j]!=null) {
				System.out.println("id:"+j+" num="+activeCalc2.motifs[j]);
				sum= sum+activeCalc2.motifs[j]; 
			}
			}
		System.out.println("Summa ="+sum);
		


	}
}
