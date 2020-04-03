package io.github.mnyudina.motifsVis.tests;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.PajekNetReader;
import io.github.mnyudina.motifs.util.MySparseGraph;
/**
 * @author Gleepa
 */
public class RandomizationTest {
	static Factory<Integer> createIntegerFactory() {
		return new Factory<Integer>() {
			private int n = 0;

			@Override
			public Integer create() {
				return n++;
			}
		};
	}

public static void main(String[] args) throws IOException {
	
	MySparseGraph graph= (MySparseGraph) new PajekNetReader<>(createIntegerFactory(), createIntegerFactory()).
			//load("graphs/GenReg.net",
			//load("graphs/VKOmgtu.net",
		//	load("graphs/Pahom2.net",
		//	load("graphs/p2p-Gnutella31.net",
		   //load("graphs/Twitter.net",
			load("graphs/Regulon.net",
			new MySparseGraph<>());
	System.out.println(graph);
	
	//int[] degrees = getNodesInDegrees(graph, 10);
	/*int[] degrees = getNodesDegrees(graph, 10);

	System.out.println("Degree distribution");

	for (int i = 0; i < degrees.length; i++) {
		System.out.println(degrees[i] );
	}

	MySparseGraph g_i = new MySparseGraph(graph).getRandomized(3, 3);
	
	System.out.println(g_i);
	 //degrees = getNodesInDegrees(g_i, 10);
	 degrees = getNodesDegrees(g_i, 10);

	System.out.println("Degree distribution (rand)");

	for (int i = 0; i < degrees.length; i++) {
		System.out.println(degrees[i]);
	}
	System.out.println("rNum:"+g_i.rNum);*/



}
public static <V> int[] getNodesDegrees(Graph<V, ?> graph, int length) {
	Iterator<V> it = graph.getVertices().iterator();
	int[] distr = new int[length];
	while (it.hasNext()) {
		V node = it.next();
		int n = graph.degree(node);
		if (n < length)
			distr[n] = distr[n] + 1;
	}
	return distr;
}

public static <V> int[] getNodesInDegrees(Graph<V, ?> graph, int length) {
	Iterator<V> it = graph.getVertices().iterator();
	int[] distr = new int[length];
	while (it.hasNext()) {
		V node = it.next();
		int n = graph.inDegree(node);
		if (n < length)
			distr[n] = distr[n] + 1;
	}
	return distr;
}

}
