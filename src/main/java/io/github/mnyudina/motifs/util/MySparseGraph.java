package io.github.mnyudina.motifs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * An implementation of <code>edu.uci.ics.jung.graph.Graph</code> which
 * represents the graph as an adjacency list and list of the edges. The
 * implementation permits directed or undirected edges only.
 *
 * @author Gleepa
 */
public class MySparseGraph<V, E> extends SparseGraph<V, E> {
	private Random mRand = new Random();
	public int rNum=0;

	public MySparseGraph() {
		vertex_maps = new HashMap<V, Map<V, E>[]>();
		directed_edges = new HashMap<E, Pair<V>>();
		undirected_edges = new HashMap<E, Pair<V>>();
	}

	public MySparseGraph(final MySparseGraph<V, E> mySparseGraph) {
		/*
		 * vertex_maps = new HashMap<V, Map<V,
		 * E>[]>(Collections.unmodifiableMap(mySparseGraph.vertex_maps)); directed_edges
		 * = new HashMap<E,
		 * Pair<V>>(Collections.unmodifiableMap(mySparseGraph.directed_edges));
		 * undirected_edges = new HashMap<E,
		 * Pair<V>>(Collections.unmodifiableMap(mySparseGraph.undirected_edges));
		 */

		//vertex_maps = new HashMap<V, Map<V, E>[]>(mySparseGraph.vertex_maps);
		//directed_edges = new HashMap<E, Pair<V>>(mySparseGraph.directed_edges);
		//undirected_edges = new HashMap<E, Pair<V>>(mySparseGraph.undirected_edges);

		for (E e : mySparseGraph.getEdges()) {
			EdgeType type = mySparseGraph.getEdgeType(e);
			if (type.equals(EdgeType.DIRECTED)) {
				V v1 = mySparseGraph.getSource(e);
				V v2 = mySparseGraph.getDest(e);
				addEdge(e, v1, v2, EdgeType.DIRECTED);

			} else {
				Pair<V> p = mySparseGraph.getEndpoints(e);
				addEdge(e, p.getFirst(), p.getSecond(), EdgeType.UNDIRECTED);
			}
		}

	}
	private  int ignored=0, loops=0;
	@Override
	public boolean addEdge(E edge, Pair<? extends V> endpoints, EdgeType edgeType) {
		Pair<V> new_endpoints = getValidatedEndpoints(edge, endpoints);
		if (new_endpoints == null)
			return false;

		V v1 = new_endpoints.getFirst();
		V v2 = new_endpoints.getSecond();

		if(v1.equals(v2)) {
			loops++;
			return false;
		}
		
		// undirected and directed edges are not considered to be parallel to each other,
		// so as long as anything that's returned by findEdge is not of the same type as
		// edge, we're fine
		E connection = findEdge(v1, v2);
		if (connection != null && getEdgeType(connection) == edgeType)
			{
			ignored++;
			return false;
			}

		if (!containsVertex(v1))
			this.addVertex(v1);

		if (!containsVertex(v2))
			this.addVertex(v2);

		// map v1 to <v2, edge> and vice versa
		if (edgeType == EdgeType.DIRECTED) {
			// E retEdge = findEdge(v2, v1);
			E retEdge = vertex_maps.get(v2)[OUTGOING].get(v1);

			if (retEdge == null) {
				vertex_maps.get(v1)[OUTGOING].put(v2, edge);
				vertex_maps.get(v2)[INCOMING].put(v1, edge);
				directed_edges.put(edge, new_endpoints);
			} else {
				removeEdge(retEdge);
				vertex_maps.get(v1)[INCIDENT].put(v2, edge);
				vertex_maps.get(v2)[INCIDENT].put(v1, edge);
				undirected_edges.put(edge, new_endpoints);
			}
		} else {
			vertex_maps.get(v1)[INCIDENT].put(v2, edge);
			vertex_maps.get(v2)[INCIDENT].put(v1, edge);
			undirected_edges.put(edge, new_endpoints);
		}

		return true;
	}

	public List<V> getNeighbors(V vertex) {
		if (!containsVertex(vertex))
			return null;
		// consider directed edges and undirected edges
		List<V> neighbors = new ArrayList<V>(vertex_maps.get(vertex)[INCOMING].keySet());
		neighbors.addAll(vertex_maps.get(vertex)[OUTGOING].keySet());
		neighbors.addAll(vertex_maps.get(vertex)[INCIDENT].keySet());
		return Collections.unmodifiableList(neighbors);
	}

	public EdgeType getDefaultEdgeType() {
		return EdgeType.DIRECTED;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Number of nodes: " + getVertexCount());
		sb.setLength(sb.length());
		sb.append("\nNumber of edges: " + (undirected_edges.keySet().size()+ directed_edges.keySet().size()));
		sb.append("\nNumber of single edges: " + directed_edges.keySet().size());
		sb.append("\nNumber of mutual edges: " + undirected_edges.keySet().size());
		if(loops>0)	sb.append("\nNumber of ignored loops: " + loops);
		if(ignored>0)sb.append("\nNumber of parallel ignored edges: " + ignored);

		
		return sb.toString();
	}

	public List<E> getIncidentEdges(V vertex) {
		if (!containsVertex(vertex))
			return null;
		List<E> incident = new ArrayList<E>(vertex_maps.get(vertex)[INCOMING].values());
		incident.addAll(vertex_maps.get(vertex)[OUTGOING].values());
		incident.addAll(vertex_maps.get(vertex)[INCIDENT].values());
		return Collections.unmodifiableList(incident);
	}
	public int iter=0;
	
	public MySparseGraph getRandomized(int per_edges, int atempts) {
		iter=0;
		List<E> edges = new ArrayList(undirected_edges.keySet());
		for (int c1 = 0; (c1 < per_edges); c1++) {
			for (E unE1 : edges) {
				iter++;
				boolean success = false;
				for (int c2 = 0; (!success) && (c2 < atempts); c2++) {

					E unE2 = edges.get(mRand.nextInt(edges.size()));
					Pair<V> firstE = undirected_edges.get(unE1);
					V v1 = firstE.getFirst();
					V v2 = firstE.getSecond();

					Pair<V> secondE = undirected_edges.get(unE2);
					V v3 = secondE.getFirst();
					V v4 = secondE.getSecond();

					this.getNeighbors(v1).contains(v4);
					
					//(vertex_maps.get(v1)[INCOMING].containsKey(v3))&&(vertex_maps.get(v1)[INCOMING].containsKey(v3))&&(vertex_maps.get(v1)[INCOMING].containsKey(v3))
					if (!v1.equals(v3) && !v1.equals(v4) && !v2.equals(v3) && !v2.equals(v4)&&!v2.equals(v1)&&!v3.equals(v4)&&
					//	(!getNeighbors(v1).contains(v4))&&(!getNeighbors(v2).contains(v3)))
			!(vertex_maps.get(v1)[INCOMING].containsKey(v4))&&!(vertex_maps.get(v1)[OUTGOING].containsKey(v4))&&!(vertex_maps.get(v1)[INCIDENT].containsKey(v4))&&
			!(vertex_maps.get(v2)[INCOMING].containsKey(v3))&&!(vertex_maps.get(v2)[OUTGOING].containsKey(v3))&&!(vertex_maps.get(v2)[INCIDENT].containsKey(v3)))
					{
						//removeEdge(unE1);
						vertex_maps.get(v1)[INCIDENT].remove(v2);
						vertex_maps.get(v2)[INCIDENT].remove(v1);
						undirected_edges.remove(unE1);
						
						//removeEdge(unE2);
						vertex_maps.get(v3)[INCIDENT].remove(v4);
						vertex_maps.get(v4)[INCIDENT].remove(v3);
						undirected_edges.remove(unE2);
						
						//addEdge(unE1, v1, v4, EdgeType.UNDIRECTED);
						vertex_maps.get(v1)[INCIDENT].put(v4, unE1);
						vertex_maps.get(v4)[INCIDENT].put(v1, unE1);
						undirected_edges.put(unE1, new Pair<>(v1, v4));
						
						//addEdge(unE2, v3, v2, EdgeType.UNDIRECTED);
						vertex_maps.get(v3)[INCIDENT].put(v2, unE2);
						vertex_maps.get(v2)[INCIDENT].put(v3, unE2);
						undirected_edges.put(unE2, new Pair<>(v2, v3));
						success = true;
						rNum++;
					}

				}
			}
			List<E> links = new ArrayList(directed_edges.keySet());
			for (E dirE1 : links) {
				boolean success = false;
				iter++;
				for (int c2 = 0; (!success) && (c2 < atempts); c2++) {

					E dirE2 = links.get(mRand.nextInt(links.size()));

					V v1 = getSource(dirE1);
					V v2 = getDest(dirE1);

					V v3 = getSource(dirE2);
					V v4 = getDest(dirE2);

					if (!v1.equals(v3) && !v1.equals(v4) && !v2.equals(v3) && !v2.equals(v4)&&
						(!getNeighbors(v1).contains(v4))&&(!getNeighbors(v2).contains(v3))&&
						!(vertex_maps.get(v1)[INCOMING].containsKey(v4))&&!(vertex_maps.get(v1)[OUTGOING].containsKey(v4))&&!(vertex_maps.get(v1)[INCIDENT].containsKey(v4))&&
						!(vertex_maps.get(v2)[INCOMING].containsKey(v3))&&!(vertex_maps.get(v2)[OUTGOING].containsKey(v3))&&!(vertex_maps.get(v2)[INCIDENT].containsKey(v3)))
					 {
						// removeEdge(dirE1);
						vertex_maps.get(v1)[OUTGOING].remove(v2);
						vertex_maps.get(v2)[INCOMING].remove(v1);
						directed_edges.remove(dirE1);
						// removeEdge(dirE2);
						vertex_maps.get(v3)[OUTGOING].remove(v4);
						vertex_maps.get(v4)[INCOMING].remove(v3);
						directed_edges.remove(dirE2);

						// addEdge(dirE1, v1, v4, EdgeType.DIRECTED)
						vertex_maps.get(v1)[OUTGOING].put(v4, dirE1);
						vertex_maps.get(v4)[INCOMING].put(v1, dirE1);
						directed_edges.put(dirE1, new Pair<>(v1, v4));
						// addEdge(dirE2, v3, v2, EdgeType.DIRECTED)
						vertex_maps.get(v3)[OUTGOING].put(v2, dirE2);
						vertex_maps.get(v2)[INCOMING].put(v3, dirE2);
						directed_edges.put(dirE2, new Pair<>(v3, v2));

						success = true;
						rNum++;
					}

				}
			}
		}

		return this;
	}
	public int getRandomizedStatus() {
		//System.out.println("dd="+iter*100/getEdgeCount());

		return iter*100/getEdgeCount();

		
	}
}