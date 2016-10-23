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
public class BidirectionalDijkstraUndirected {

    private final double[] forwardDistTo;
    private final double[] backwardDistTo;

    private Edge[] forwardEdgeTo;
    private Edge[] backwardEdgeTo;

    private final IndexMinPQ<Double> forwardPq;
    private final IndexMinPQ<Double> backwardPq;
    private final Set<Integer> forwardAlreadyProcessedNodes;
    private final Set<Integer> backwardAlreadyProcessedNodes;


    public BidirectionalDijkstraUndirected(EdgeWeightedGraph G, int from, int to) {
        if (from == to) {
            System.out.format("%d-%d 0.0", from, to);
        }
        for (Edge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        forwardDistTo = new double[G.V()];
        backwardDistTo = new double[G.V()];
        forwardAlreadyProcessedNodes = new HashSet<>();
        backwardAlreadyProcessedNodes = new HashSet<>();
        forwardEdgeTo = new Edge[G.V()];
        backwardEdgeTo = new Edge[G.V()];
        Arrays.fill(forwardDistTo, Double.POSITIVE_INFINITY);
        Arrays.fill(backwardDistTo, Double.POSITIVE_INFINITY);
        forwardDistTo[from] = 0.0;
        backwardDistTo[to] = 0.0;

        // forwardRelax vertices in order of distance from s
        forwardPq = new IndexMinPQ<>(G.V());
        backwardPq = new IndexMinPQ<>(G.V());
        forwardPq.insert(from, forwardDistTo[from]);
        backwardPq.insert(to, backwardDistTo[to]);

        while (!forwardPq.isEmpty() && !backwardPq.isEmpty()) {
            int forwardV = forwardPq.delMin();
            for (Edge forwardE : G.adj(forwardV)) {
                forwardRelax(forwardE, forwardV);
            }
            int backwardV = backwardPq.delMin();
            for (Edge backwardE : G.adj(backwardV)) {
                backwardRelax(backwardE, backwardV);
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
            print(restorePath(x), from);
        } else {
            System.out.println("Search didnt find the path!");
        }
    }

    private void print(Iterable<Edge> edges, int from) {
        double sum = 0;
        int previous = from;
        for (Edge e : edges) {
            sum += e.weight();
            System.out.format("%d-%d %f\n", previous, e.other(previous), e.weight());
            previous = e.other(previous);
        }
        System.out.println("sum of path: " + sum);
    }

    private boolean isIntersects() {
        Set<Integer> intersection = new HashSet<>(forwardAlreadyProcessedNodes); // use the copy constructor
        intersection.retainAll(backwardAlreadyProcessedNodes);
        return !intersection.isEmpty();
    }

    private void forwardRelax(Edge e, int v) {
        int w = e.other(v);
        if (forwardDistTo[w] > forwardDistTo[v] + e.weight()) {
            forwardDistTo[w] = forwardDistTo[v] + e.weight();
            forwardEdgeTo[w] = e;
            if (forwardPq.contains(w)) forwardPq.decreaseKey(w, forwardDistTo[w]);
            else forwardPq.insert(w, forwardDistTo[w]);
        }
    }

    private void backwardRelax(Edge e, int v) {
        int w = e.other(v);
        if (backwardDistTo[w] > backwardDistTo[v] + e.weight()) {
            backwardDistTo[w] = backwardDistTo[v] + e.weight();
            backwardEdgeTo[w] = e;
            if (backwardPq.contains(w)) backwardPq.decreaseKey(w, backwardDistTo[w]);
            else backwardPq.insert(w, backwardDistTo[w]);
        }
    }


    public Iterable<Edge> restorePath(int x) {
        Stack<Edge> path = new Stack<>();
        int y = x;
        for (Edge e = forwardEdgeTo[x]; e != null; e = forwardEdgeTo[y]) {
            path.push(e);
            y = e.other(y);
        }
        ArrayList<Edge> edges = new ArrayList<>();
        for (Edge e : path) {
            edges.add(e);
        }
        y = x;
        for (Edge e = backwardEdgeTo[x]; e != null; e = backwardEdgeTo[y]) {
            edges.add(e);
            y = e.other(y);
        }
        return edges;
    }
}
