package io.github.mnyudina.subgraph;

import java.util.ArrayList;
import java.util.List;

public class LayerOfVertecies<V> {
	public double probability;
	
	public List<V> vertices = new ArrayList<>();
	
	public LayerOfVertecies() {
		vertices = new ArrayList<>();
	}
	
	public double getProbability() {
		return probability;
	}

	public List<V> getVerticies() {
		return vertices;
	}
}
