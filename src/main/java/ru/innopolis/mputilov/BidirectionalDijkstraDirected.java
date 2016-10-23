package ru.innopolis.mputilov;

import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mputilov on 23/10/16.
 */
@SuppressWarnings("Duplicates")
public class BidirectionalDijkstraDirected {

    private final double[] forwardDistTo;
    private final double[] backwardDistTo;
    private DirectedEdge[] forwardEdgeTo;
    private DirectedEdge[] backwardEdgeTo;

    private final IndexMinPQ<Double> forwardPq;
    private final IndexMinPQ<Double> backwardPq;
    private final Set<Integer> forwardAlreadyProcessedNodes;
    private final Set<Integer> backwardAlreadyProcessedNodes;


    public BidirectionalDijkstraDirected(EdgeWeightedDigraph G, int from, int to) {
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        forwardDistTo = new double[G.V()];
        backwardDistTo = new double[G.V()];
        forwardAlreadyProcessedNodes = new HashSet<>();
        backwardAlreadyProcessedNodes = new HashSet<>();
        forwardEdgeTo = new DirectedEdge[G.V()];
        backwardEdgeTo = new DirectedEdge[G.V()];
        Arrays.fill(forwardDistTo, Double.POSITIVE_INFINITY);
        Arrays.fill(backwardDistTo, Double.POSITIVE_INFINITY);
        forwardDistTo[from] = 0.0;
        backwardDistTo[to] = 0.0;

        // forwardRelax vertices in order of distance from s
        forwardPq = new IndexMinPQ<>(G.V());
        backwardPq = new IndexMinPQ<>(G.V());
        forwardPq.insert(from, forwardDistTo[from]);
        backwardPq.insert(to, backwardDistTo[to]);

        while (!forwardPq.isEmpty() || !backwardPq.isEmpty()) {
            int forwardV = forwardPq.delMin();
            for (DirectedEdge forwardE : G.adj(forwardV)) {
                forwardRelax(forwardE);
            }
            int backwardV = backwardPq.delMin();
            for (DirectedEdge backwardE : G.revadj(backwardV)) {
                backwardRelax(backwardE);
            }

            forwardAlreadyProcessedNodes.add(forwardV);
            backwardAlreadyProcessedNodes.add(backwardV);
            if (isIntersects()) {
                break;
            }
        }
        Set<Integer> processedNodesWithMinimumSize = forwardAlreadyProcessedNodes.size() < backwardAlreadyProcessedNodes.size() ? forwardAlreadyProcessedNodes : backwardAlreadyProcessedNodes;
        double minPath = Double.POSITIVE_INFINITY;
        int x = Integer.MAX_VALUE;
        for (Integer processedNode : processedNodesWithMinimumSize) {
            double pathCost = forwardDistTo[processedNode] + backwardDistTo[processedNode];
            if (pathCost < minPath) {
                minPath = pathCost;
                x = processedNode;
            }
        }

        if (x != Integer.MAX_VALUE) {
            print(restorePath(to, x));
        } else {
            System.out.println("Search didnt find the path!");
        }

    }

    private void print(Iterable<DirectedEdge> edges) {
        double sum = 0;
        for (DirectedEdge e : edges) {
            sum += e.weight();
            System.out.format("%d-%d %f\n", e.from(), e.to(), e.weight());
        }
        System.out.println("sum of path: " + sum);
    }

    private boolean isIntersects() {
        Set<Integer> intersection = new HashSet<>(forwardAlreadyProcessedNodes); // use the copy constructor
        intersection.retainAll(backwardAlreadyProcessedNodes);
        return !intersection.isEmpty();
    }

    private void forwardRelax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (forwardDistTo[w] > forwardDistTo[v] + e.weight()) {
            forwardDistTo[w] = forwardDistTo[v] + e.weight();
            forwardEdgeTo[w] = e;
            if (forwardPq.contains(w)) forwardPq.decreaseKey(w, forwardDistTo[w]);
            else forwardPq.insert(w, forwardDistTo[w]);
        }
    }

    private void backwardRelax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (backwardDistTo[w] > backwardDistTo[v] + e.weight()) {
            backwardDistTo[w] = backwardDistTo[v] + e.weight();
            backwardEdgeTo[w] = e;
            if (backwardPq.contains(w)) backwardPq.decreaseKey(w, backwardDistTo[w]);
            else backwardPq.insert(w, backwardDistTo[w]);
        }
    }

    public Iterable<DirectedEdge> restorePath(int v, int x) {
        Stack<DirectedEdge> path = new Stack<>();
        for (DirectedEdge e = forwardEdgeTo[x]; e != null; e = forwardEdgeTo[e.from()]) {
            path.push(e);
        }
        ArrayList<DirectedEdge> edges = new ArrayList<>();
        for (DirectedEdge e : path) {
            edges.add(e);
        }
        for (DirectedEdge e = backwardEdgeTo[x]; e != null; e = backwardEdgeTo[e.from()]) {
            edges.add(new DirectedEdge(e.to(), e.from(), e.weight()));
        }
        return edges;
    }

}
