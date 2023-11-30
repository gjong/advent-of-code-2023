package com.github.gjong.advent2023.common.algo;

import java.util.*;

public class DepthFirstSearch<T> {
    private int vertices;
    private Map<T, Queue<T>> adjacentList;

    DepthFirstSearch(int vertices) {
        this.vertices = vertices;
        this.adjacentList = new HashMap<>();
    }

    public void addEdge(T v, T w) {
        adjacentList.computeIfAbsent(v, key -> new LinkedList<>())
                .add(w);
    }

    void search(T start) {
        var visited = new HashSet<T>();
        var stack = new Stack<T>();

        stack.push(start);
        while (!stack.empty()) {
            var current = stack.pop();

            visited.add(current);
            for (var connected : adjacentList.get(current)) {
                if (!visited.contains(connected)) {
                    stack.add(connected);
                }
            }
        }
    }
}
