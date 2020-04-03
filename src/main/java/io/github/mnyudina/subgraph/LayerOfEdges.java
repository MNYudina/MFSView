package io.github.mnyudina.subgraph;


import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Gleepa
 *
 * @param <E>
 */
public class LayerOfEdges<E>{
	
	public double probability;
	
	public List<E> edges = new ArrayList<>();
	
	public LayerOfEdges() {
		edges = new ArrayList<>();
	}
	
	public double getProbability() {
		return probability;
	}

	public List<E> getEdges() {
		return edges;
	}
	
}
