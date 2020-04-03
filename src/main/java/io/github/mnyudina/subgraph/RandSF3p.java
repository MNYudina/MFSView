package io.github.mnyudina.subgraph;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

public class RandSF3p<V, E> extends AbstractExecutor {

	Map<Integer, LayerOfVertecies<V>> vertexLayers;

	private MySparseGraph<V, E> graph;

	double numberOfCarcasLapka = 0;

	Random randomGenerator = new Random();

	public int searchLapka() {
		double randomDoubleValue = randomGenerator.nextDouble();
		while (randomDoubleValue == 0.0) {
			randomDoubleValue = randomGenerator.nextDouble();
		}
		double borderOfProbability = 0.0;
		LayerOfVertecies<V> selectedVertexLayer = new LayerOfVertecies<>();

		// Choose a layer of vertices taking into account the probabilities of
		// layers selection
		for (Entry<Integer, LayerOfVertecies<V>> vertexLayer : vertexLayers.entrySet()) {
			borderOfProbability += vertexLayer.getValue().getProbability();
			if (randomDoubleValue < borderOfProbability) {
				selectedVertexLayer = vertexLayer.getValue();
				break;
			}
		}

		V v1 = selectedVertexLayer.getVerticies()
				.get(randomGenerator.nextInt(selectedVertexLayer.getVerticies().size()));
		V v2, v3;
		// Choose 3 successors of the vertex randomly
		List<V> v1List = new LinkedList<>(graph.getNeighbors(v1));
		int randomIntValue = randomGenerator.nextInt(v1List.size());
		v2 = v1List.remove(randomIntValue);
		randomIntValue = randomGenerator.nextInt(v1List.size());
		v3 = v1List.remove(randomIntValue);

		V[] vert = (V[]) new Object[3];
		vert[0] = v1;
		vert[1] = v2;
		vert[2] = v3;
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
		synchronized (this) {
			iter=2+iter;
		}

		return arrcode3[code];

	}

	/**
	 * Constructs and initializes the class.
	 *
	 * @param graph        the graph
	 * @param numberOfRuns number of runs of sampling algorithm
	 * @author Gleepa
	 */
	public RandSF3p(Graph<V, E> g, int num) {
		this.graph = (MySparseGraph) g;
		numberOfRuns = num;
		motifs = new Double[16];
		sigmas = new Double[16];
		counts = new Double[16];
		for (int i = 0; i < counts.length; i++) {
			motifs[i] = 0.;
			sigmas[i] = 0.;
			counts[i] = 0.;
		}

	}

	/**
	 *
	 * @throws UnsupportedEdgeTypeException
	 * @author Gleepa
	 */
	public void execute() {
//////////////////// Prepare Branch frames  ////////////////////////////////////
		Collection<V> vertices = graph.getVertices();
		vertexLayers = new HashMap<>();
		int neibours;
		for (V vertex : vertices) {
			neibours = graph.getNeighborCount(vertex);
			if (vertexLayers.get(neibours) == null) {
				vertexLayers.put(neibours, new LayerOfVertecies<>());
			}
			vertexLayers.get(neibours).vertices.add(vertex);
		}
		
		// Calculate exact number of lapka
		numberOfCarcasLapka = (double) vertexLayers.entrySet().parallelStream().map((vertexLayer) -> {
			return vertexLayer.getValue().vertices.size() * vertexLayer.getKey() * (vertexLayer.getKey() - 1l) / 2l;
		}).reduce(0l, (x, y) -> x + y);

		// Calculate probability of selection for each layer of the vertices
		for (Entry<Integer, LayerOfVertecies<V>> vertexLayer : vertexLayers.entrySet()) {
			vertexLayer.getValue().probability = (vertexLayer.getValue().vertices.size() * vertexLayer.getKey()
					* (vertexLayer.getKey() - 1l) / (2.0 * numberOfCarcasLapka));
		}
		
		vertexLayers.entrySet().parallelStream().forEach(vertexLayer -> {
			vertexLayer.getValue().probability = (vertexLayer.getValue().vertices.size() * vertexLayer.getKey()
					* (vertexLayer.getKey() - 1l) / (2.0 * numberOfCarcasLapka));
		});


//////////////////// Statistical Experiments Making ////////////////////////////////////
		Map<Integer, Integer> countsLapka; // How to write to an array directly?
		countsLapka = Stream.iterate(0, i -> i).parallel().limit((int) numberOfRuns).map(x -> this.searchLapka())
				.collect(Collectors.toMap(w -> w, w -> 1, Integer::sum));

		
//////////////////// Results Processing ////////////////////////////////////
		for (int i = 0; i < motifs.length; i++) {
			double cLpk=0.;
			if(countsLapka.get(i)!=null)cLpk=countsLapka.get(i);

			double D = (numberOfCarcasLapka / numberOfRuns) * (numberOfCarcasLapka / numberOfRuns)
					* (cLpk / coef3[i]) * (1 - cLpk / (coef3[i]) / numberOfRuns);
			motifs[i] = cLpk * (numberOfCarcasLapka / coef3[i] / numberOfRuns);
			sigmas[i] = Math.sqrt(D);
			counts[i] = cLpk;


		}
//////////////////////////////           END       /////////////////////////////////////////////
	}

}