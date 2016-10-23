package ru.innopolis.mputilov;


import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.In;

/**
 * Created by mputilov on 23/10/16.
 */
public class Main {
    /**
     * Алгоритм запускает дейкстру с двух сторон. Критерий остановки:
     * какой-то вертекс был удален из двух priorityQueue
     * <p>
     * Это означает что найден 100: короткий путь forward проходом до этого вертекса и 100% короткий путь backward проходом
     * до этого вертекса. Но короткий путь между началом и концом может лежать еще там, где релаксация прошла не до конца.
     * Надо найти specialVertex который обладает свойством path_length_forward(special_vertex) + path_length_backward(special_vertex) = минимум
     * <p>
     * Этот special vertex и определяет короткий путь между началом и концом
     * <p>
     * <p>
     * Реализован еще вариант для ненаправленного графа.
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("form 0 to 998:");
        In in = new In(Main.class.getClassLoader().getResource("1000ewg.txt").getPath());
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(in);
        new BidirectionalDijkstraDirected(graph, 0, 998);
    }
}
