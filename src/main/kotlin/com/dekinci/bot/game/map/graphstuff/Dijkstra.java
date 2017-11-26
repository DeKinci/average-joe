package com.dekinci.bot.game.map.graphstuff;

import java.io.IOException;
import java.util.*;

public class Dijkstra {
    private static int INF = Integer.MAX_VALUE / 2;

    private static ArrayList<ArrayList<Integer>> adjacencyList;
    private static int vertexAmount;

    private int dist[] = new int[vertexAmount];
    private Set<Integer> queue = new TreeSet<>();

    public static void init(int vertexAmount, ArrayList<ArrayList<Integer>> adjacencyList) {
        Dijkstra.adjacencyList = adjacencyList;
        Dijkstra.vertexAmount = vertexAmount;
    }

    public Dijkstra() {
        Arrays.fill(dist, INF);
    }

    public int[] sparse(int start) {
        dist[start] = 0;
        queue.add(start);

        while (!queue.isEmpty()) {
            int from = queue.iterator().next();
            queue.remove(from);

            for (int to : adjacencyList.get(from))
                if (dist[from] + 1 < dist[to]) {
                    queue.remove(to);
                    queue.add(to);
                    dist[to] = dist[from] + 1;
                }
        }

        return dist;
    }

    public static void main(String[] args) throws IOException {
        test();
    }

    private static void test() {//TODO: REMOVE
        int vertexAmount = 7;
        int start = 0;

        ArrayList<ArrayList<Integer>> adjacencyList = new ArrayList<>(vertexAmount);
        for (int i = 0; i < vertexAmount; ++i)
            adjacencyList.add(new ArrayList<>());

        String[] data = "6 7 2 4 1 2 1 3 2 5 3 2 3 5 4 5 5 3 5 4 7 6 1 3".split(" ");
        int edgeAmount = 11;

        for (int i = 0; i < edgeAmount; i += 2)
            adjacencyList
                    .get(Integer.parseInt(data[i]) - 1)
                    .add(Integer.parseInt(data[i + 1]) - 1);

        Dijkstra.init(vertexAmount, adjacencyList);

        Dijkstra dijkstra = new Dijkstra();
        printData(dijkstra.sparse(start));
    }

    private static void printData(int dist[]) {
        for (int v : dist)
            if (v != INF)
                System.out.print((v + " "));
            else
                System.out.print("-1 ");
    }
}