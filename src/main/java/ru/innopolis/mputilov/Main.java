package ru.innopolis.mputilov;

import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;

/**
 * Created by mputilov on 23/10/16.
 */
public class Main {
    public static void main(String[] args) {
        In in = new In(Main.class.getClassLoader().getResource("1000ewg.txt").getPath());
        EdgeWeightedGraph digraph = new EdgeWeightedGraph(in);
        new BidirectionalDijkstra(digraph, 541, 450);
        System.out.println();
    }
}
