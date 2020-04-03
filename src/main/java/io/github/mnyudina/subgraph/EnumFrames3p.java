package io.github.mnyudina.subgraph;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import edu.uci.ics.jung.graph.Graph;
import io.github.mnyudina.motifs.util.MySparseGraph;

/**
 * This is parallel version of 4-size directed subgraphs counter which uses
 * random frame sampling algorithm.
 *
 * @author Gleepa
 */

public class EnumFrames3p<V, E> extends AbstractExecutor {

	private MySparseGraph<V, E> graph;
	public final double motifsLapka[] = new double[16];
    
/////////////////////////////////////////////////////////////////////////////////////////
	public void searchLapka(V v1) {

		List<V> nei = new LinkedList<V>(graph.getNeighbors(v1));
		if (nei.size()<2) return;
            for(int b=0;b<nei.size()-1;b++)
                for(int c=b+1;c<nei.size();c++)
                {
                	 V[] vert = (V[]) new Object[3];
                     vert[0] = v1;
                     vert[1] = nei.get(b);
                     vert[2] = nei.get(c);
                     int code = 0;
                     for (int i = 0; i < vert.length - 1; i++) {
                         for (int j = i + 1; j < vert.length; j++) {
                             E o1 = graph.findEdge(vert[i], vert[j]);
                             if (o1 != null)
                                 code |= arr_idx3[3 * i + j];
                             E o2 = graph.findEdge(vert[j], vert[i]);
                             if (o2 != null)
                                 code |= arr_idx3[3 * j + i];
                         }
                     }     	
                     int x= arrcode3[code];
 					synchronized (this) { // object to synchronize motifsLapka
						motifsLapka[x] = motifsLapka[x] + 1;
					}
                }
			synchronized (this.getClass()) { //another object to synchronize iter
				iter=iter+2;
			}

    }

	/**
	 * Constructs and initializes the class.
	 *
	 * @param graph        the graph
	 * @author Gleepa
	 */
	public EnumFrames3p(Graph<V, E> g) {
		graph = (MySparseGraph) g;
		numberOfRuns = (graph.getVertexCount());
		iter = 0;
		motifs = new Double[16];
		sigmas = new Double[16];
		counts = new Double[16];
	}

	/**
	 *
	 * @throws UnsupportedEdgeTypeException
	 * @author Gleepa
	 */
	public void execute() {

		////////////////////  Experiments Making ////////////////////////////////////

		graph.getVertices().parallelStream().forEach((v)->this.searchLapka(v));
		
//////////////////// Results Processing ////////////////////////////////////
		for (int i = 0; i < motifs.length; i++) {
			if(motifsLapka[i]>0) {
				   motifs[i]= motifsLapka[i]/coef3[i];
				   counts[i] = motifs[i];

			} else counts[i] = 0.;
			sigmas[i]=0.;
		}
//////////////////////////////           END       /////////////////////////////////////////////
	}


}