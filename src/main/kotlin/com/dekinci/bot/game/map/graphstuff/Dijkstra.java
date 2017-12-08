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
}