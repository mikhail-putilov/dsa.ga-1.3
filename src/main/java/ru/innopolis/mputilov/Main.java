package ru.innopolis.mputilov;


import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;

/**
 * Created by mputilov on 23/10/16.
 */
public class Main {
    public static void main(String[] args) {
        In in = new In(Main.class.getClassLoader().getResource("10ewg.txt").getPath());
        EdgeWeightedGraph graph = new EdgeWeightedGraph(in);
        new BidirectionalDijkstraUndirected(graph, 7, 2);
    }
}
