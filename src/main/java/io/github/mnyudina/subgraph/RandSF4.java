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

public class RandSF4<V, E> extends AbstractExecutor {
	// private boolean isParallel;

	Map<Integer, LayerOfVertecies<V>> vertexLayers;
	Map<Long, LayerOfEdges<E>> edgeLayers;


	private MySparseGraph<V, E> graph;

	// public final double motifs1[] = new double[218];
	public final double motifs2[] = new double[218];

	// final double motifs_long[] = new double[218];

	public final double motifsScobka[] = new double[218];
	public final double motifsLapka[] = new double[218];

	// public double numberOfRuns;
	private double numberOfRunsLapka;
	private double numberOfRunsScoba;

	double numberOfCarcasScobka = 0;
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
		V v2, v3, v4;

		// Choose 3 successors of the vertex randomly
		List<V> v1List = new LinkedList<>(graph.getNeighbors(v1));
		int randomIntValue = randomGenerator.nextInt(v1List.size());
		v2 = v1List.remove(randomIntValue);
		randomIntValue = randomGenerator.nextInt(v1List.size());
		v3 = v1List.remove(randomIntValue);
		randomIntValue = randomGenerator.nextInt(v1List.size());
		v4 = v1List.remove(randomIntValue);

		V[] vert = (V[]) new Object[4];
		vert[0] = v1;
		vert[1] = v2;
		vert[2] = v3;
		vert[3] = v4;
		int code = 0;
		for (int i = 0; i < vert.length - 1; i++) {
			for (int j = i + 1; j < vert.length; j++) {
				E o1 = graph.findEdge(vert[i], vert[j]);
				if (o1 != null)
					code |= arr_idx4[4 * i + j];
				E o2 = graph.findEdge(vert[j], vert[i]);
				if (o2 != null)
					code |= arr_idx4[4 * j + i];
			}
		}
		return arrcode4[code];

	}

	public int searchScobka() {
		double randomDoubleValue = randomGenerator.nextDouble();
		double borderOfProbability = 0.0;
		LayerOfEdges<E> selectedEdgeLayer = null;

		// Choose a layer of edges taking into account the probabilities of
		// layers selection
		for (Entry<Long, LayerOfEdges<E>> edgeLayer : edgeLayers.entrySet()) {
			if (edgeLayer.getValue().getProbability() < 0) {
				System.out.println("ddd");
			}
			borderOfProbability += edgeLayer.getValue().getProbability();
			if (randomDoubleValue < borderOfProbability) {
				selectedEdgeLayer = edgeLayer.getValue();
				break;
			}
		}

		// Choose an edge from the layer of edges randomly
		E selectedEdge = selectedEdgeLayer.getEdges().get(randomGenerator.nextInt(selectedEdgeLayer.getEdges().size()));

		// Get endpoints of the edge
		V v1 = graph.getEndpoints(selectedEdge).getFirst();
		V v2 = graph.getEndpoints(selectedEdge).getSecond();

		// Generate a list of successors of the endpoints
		List<V> neigbours1 = new LinkedList<V>(graph.getNeighbors(v1));
		List<V> neigbours2 = new LinkedList<V>(graph.getNeighbors(v2));
		neigbours1.remove(v2);
		neigbours2.remove(v1);

		// Choose 2 successors of the endpoints randomly
		V v3 = neigbours1.get(randomGenerator.nextInt(neigbours1.size()));
		V v4 = neigbours2.get(randomGenerator.nextInt(neigbours2.size()));

		if (v3.equals(v4)) {
			return -1; // This is triangle
		}
		;

		V[] vert = (V[]) new Object[4];
		vert[0] = v1;
		vert[1] = v2;
		vert[2] = v3;
		vert[3] = v4;
		int code = 0;
		for (int i = 0; i < vert.length - 1; i++) {
			for (int j = i + 1; j < vert.length; j++) {
				E o1 = graph.findEdge(vert[i], vert[j]);
				if (o1 != null)
					code |= arr_idx4[4 * i + j];
				E o2 = graph.findEdge(vert[j], vert[i]);
				if (o2 != null)
					code |= arr_idx4[4 * j + i];
			}
		}
		return arrcode4[code];
	}

	/**
	 * Constructs and initializes the class.
	 *
	 * @param graph        the graph
	 * @param numberOfRuns number of runs of sampling algorithm
	 * @author Gleepa
	 */
	public RandSF4(Graph<V, E> g, int num) {
		graph = (MySparseGraph) g;
		numberOfRuns = num;
		iter = 0;
		motifs = new Double[218];
		sigmas = new Double[218];
		counts = new Double[218];
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
		for (Entry<Integer, LayerOfVertecies<V>> vertexLayer : vertexLayers.entrySet()) {
			numberOfCarcasLapka += vertexLayer.getValue().vertices.size() * vertexLayer.getKey()
					* (vertexLayer.getKey() - 1l) * (vertexLayer.getKey() - 2l) / 6l;
		}
		// Calculate probability of selection for each layer of the vertices
		for (Entry<Integer, LayerOfVertecies<V>> vertexLayer : vertexLayers.entrySet()) {
			vertexLayer.getValue().probability = (vertexLayer.getValue().vertices.size() * vertexLayer.getKey()
					* (vertexLayer.getKey() - 1l) * (vertexLayer.getKey() - 2l) / (6.0 * numberOfCarcasLapka));
		}
//////////////////// Prepare Path Frames  ////////////////////////////////////
		Collection<E> edges = graph.getEdges();
		Comparator<Long> comp = (Long x, Long y) -> Long.compare(y, x);
		edgeLayers = new TreeMap(comp);
		new HashMap<>();//
		long numberOfPathsOfLengthThree;

		for (E edge : edges) {
			V v1 = graph.getEndpoints(edge).getFirst();
			V v2 = graph.getEndpoints(edge).getSecond();
			numberOfPathsOfLengthThree = (graph.getNeighborCount(v1) - 1l) * (graph.getNeighborCount(v2) - 1l);
			if (edgeLayers.get(numberOfPathsOfLengthThree) == null) {
				edgeLayers.put(numberOfPathsOfLengthThree, new LayerOfEdges<>());
			}
			edgeLayers.get(numberOfPathsOfLengthThree).edges.add(edge);
		}

		// Calculate exact number of the graph's path of length three
		for (Entry<Long, LayerOfEdges<E>> edgeLayer : edgeLayers.entrySet()) {
			numberOfCarcasScobka += edgeLayer.getValue().edges.size() * edgeLayer.getKey();
		}

		// Calculate probability of selection for each layer of the edges
		for (Entry<Long, LayerOfEdges<E>> edgeLayer : edgeLayers.entrySet()) {
			edgeLayer.getValue().probability = (edgeLayer.getValue().edges.size() / (double) numberOfCarcasScobka
					* edgeLayer.getKey());
		}
//////////////////// Statistical Experiments Making ////////////////////////////////////

		numberOfRunsLapka = numberOfRuns;
		numberOfRunsScoba = numberOfRuns;
		long t = System.currentTimeMillis();

		for (int i = 0; i < numberOfRunsLapka; i++) {
			int x = searchLapka();
			motifsLapka[x] = motifsLapka[x] + 1;
			iter++;

		}
		for (int i = 0; i < numberOfRunsScoba; i++) {
			{
				int x = searchScobka();
				iter++; // for ProgressBar
				if (x > 0) {
					motifsScobka[x] = motifsScobka[x] + 1;
				} else {
					numberOfRunsScoba = numberOfRunsScoba + 1;
				}
			}
		}
//////////////////// Results Processing ////////////////////////////////////
		Double D1, D2, lyamda, D, Sigma;

		for (int i = 0; i < motifs.length; i++) {

			D1 = (numberOfCarcasScobka / numberOfRunsScoba) * (numberOfCarcasScobka / numberOfRunsScoba)
					* motifsScobka[i] / massKoefPath[i] * (1. - motifsScobka[i] / (massKoefPath[i]) / numberOfRunsScoba);
			D2 = (numberOfCarcasLapka / numberOfRunsLapka) * (numberOfCarcasLapka / numberOfRunsLapka)
					* (motifsLapka[i] / massKoefBranch[i]) * (1 - motifsLapka[i] / (massKoefBranch[i]) / numberOfRunsLapka);

			Double n1 = motifsScobka[i] * (numberOfCarcasScobka / massKoefPath[i] / numberOfRunsScoba);
			Double n2 = motifsLapka[i] * (numberOfCarcasLapka / massKoefBranch[i] / numberOfRunsLapka);

			lyamda = 0.;

			if (n2 >= 0.1 && n1 >= 0.1) {
				lyamda = D1 / (D1 + D2);
				D = (1 - lyamda) * (1 - lyamda) * D1 + lyamda * lyamda * D2;
				Sigma = Math.sqrt(D);
				sigmas[i] = Sigma;
				motifs[i] = (n1 + lyamda * (n2 - n1));
			}
			else if (n1 < 0.1 && n2 >= 0.1) {
				D = D2;
				Sigma = Math.sqrt(D);
				sigmas[i] = Sigma;
				motifs[i] = n2;
			}
			else if (n1 >= 0.1 && n2 <= 0.1) {
				D = D1;
				Sigma = Math.sqrt(D);
				sigmas[i] = Sigma;
				motifs[i] = n1;
			}
			counts[i] = motifsScobka[i] + motifsLapka[i];
		}

//////////////////////////////           END       /////////////////////////////////////////////

	}

}