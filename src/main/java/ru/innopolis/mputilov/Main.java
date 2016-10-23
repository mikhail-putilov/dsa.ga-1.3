package ru.innopolis.mputilov;


import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.In;

/**
 * Created by mputilov on 23/10/16.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("form 0 to 998:");
        In in = new In(Main.class.getClassLoader().getResource("1000ewg.txt").getPath());
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(in);
        new BidirectionalDijkstraDirected(graph, 0, 999);
    }
}
