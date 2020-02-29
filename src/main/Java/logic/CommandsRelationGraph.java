package logic;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import model.Command;

import java.util.*;

public class CommandsRelationGraph {
    MutableValueGraph<Command, Integer> graph;
    Set<Command> commandSet;
    Command previousCmd;

    private int counter; // Number of commands users asked

    public CommandsRelationGraph() {
        // Creating mutable， self look, undirected graph
         this.graph = ValueGraphBuilder.undirected().allowsSelfLoops(true).build();
         this.commandSet = new HashSet<>();
         previousCmd = null;
         counter = 0;
    }

    /**
     * @param cmd to be added or updated in the relation graph
     *            if the cmd is new, simply add an edge to the previousCmd
     *            if the cmd exists, increment the relation count by 1
     * Update the previousCmd and currCmd status
     */
    public List<Command> addCommand(Command cmd) {
        List<Command> prediction = new ArrayList<>();
        if (cmd == null) {
            return prediction;
        }
        counter++;
        if (previousCmd == null) {
            previousCmd = cmd;
            prediction.add(cmd);
            commandSet.add(cmd);
            graph.addNode(cmd);
            assert graph.nodes().size() == commandSet.size();
            return prediction;
        }
        if (commandSet.contains(cmd)) {
            int count = graph.edgeValueOrDefault(cmd, previousCmd, 0);
            graph.putEdgeValue(cmd, previousCmd, count + 1);
        } else {
            commandSet.add(cmd);
            graph.addNode(cmd);
            graph.putEdgeValue(cmd, previousCmd, 1);
        }
        assert graph.nodes().size() == commandSet.size();
        previousCmd = cmd;
        return predict(cmd);

    }

    public int getTraverseSteps() {
        int E = graph.edges().size();
        return Math.ceil(Math.sqrt(E)) > Integer.MAX_VALUE ? graph.nodes().size() :
                (int) Math.ceil(Math.sqrt(E));
    }

    /**
     * @return predict a list of Commands
     * the list size should be the ceiling of sqrt(number of edges)
     * base case: 1. no commands, return empty list
     *            2. only 1 command, return it only
     */
    private List<Command> predict(Command root) {
        int steps = getTraverseSteps();
        return bfs(root, steps);
    }

    private List<Command> bfs(Command root, int steps) {
        List<Command> res = new ArrayList<>();
        if (steps < 0 || graph.degree(root) == 0) {
            return res;
        }
        if (graph.incidentEdges(root).size() == 0) {
            res.add(root);
            return res;
        }
        Set<Command> visited = new HashSet<>();
        PriorityQueue<Connection> maxHeap = new PriorityQueue<>(new Comparator<Connection>() {
            @Override
            public int compare(Connection o1, Connection o2) {
                return o2.count.compareTo(o1.count);
            }
        });

        while (steps > 0) {
            int neighborSize = graph.degree(root);
            for (Command nei: graph.adjacentNodes(root)) {
                if (!visited.contains((nei))) {
                    maxHeap.offer(new Connection(nei, graph.edgeValueOrDefault(root, nei, 1)));
                    visited.add(nei);
                }

            }
            if (graph.hasEdgeConnecting(root, root)) {
                if (!visited.contains((root))) {
                    maxHeap.offer(new Connection(root, graph.edgeValueOrDefault(root, root, 1)));
                    visited.add(root);
                }
            }
            Command newRoot = maxHeap.peek().cmd;
            if (newRoot.equals(root)) {
                newRoot = maxHeap.peek().cmd;
                res.add(root);
            }

            while (!maxHeap.isEmpty()) {
                res.add(maxHeap.poll().cmd);
            }

            root = newRoot;
            steps -= neighborSize;
        }
        return res;
    }


    /**
     * Clear all commands relation in the graph
     * Delete all nodes and edges in the graph
     * Make the graph to be an empty graph;
     */
    public void reset() {
        assert graph.nodes().size() == commandSet.size();
        for (Command cmd: commandSet) {
            graph.removeNode(cmd);
        }
        assert graph.nodes().size() == 0;
        commandSet.clear();
        previousCmd = null;
        counter = 0;
    }

    public int numOfUniqueCommands() {
        return graph.nodes().size();
    }


    /**
     * @return all relations in the graph including self-loop
     */
    public int numOfRelations() {
        return graph.edges().size();
    }

    public int counter() {
        return counter;
    }

    static class Connection {
        Command cmd;
        Integer count;

        public Connection(Command cmd, Integer count) {
            this.cmd = cmd;
            this.count = count;
        }
    }
}
