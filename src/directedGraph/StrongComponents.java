// O. Bittel;
// 05-09-2018

package directedGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Klasse für Bestimmung aller strengen Komponenten.
 * Kosaraju-Sharir Algorithmus.
 *
 * @param <V> Knotentyp.
 * @author Oliver Bittel
 * @since 22.02.2017
 */
public class StrongComponents<V> {
    // comp speichert fuer jede Komponente die zughörigen Knoten.
    // Die Komponenten sind numeriert: 0, 1, 2, ...
    // Fuer Beispielgraph in Aufgabenblatt 2, Abb3:
    // Component 0: 5, 6, 7,
    // Component 1: 8,
    // Component 2: 1, 2, 3,
    // Component 3: 4,

    private final Map<Integer, Set<V>> comp = new TreeMap<>();

    /**
     * Ermittelt alle strengen Komponenten mit
     * dem Kosaraju-Sharir Algorithmus.
     *
     * @param g gerichteter Graph.
     */
    public StrongComponents(DirectedGraph<V> g) {
        DepthFirstOrder<V> dfo = new DepthFirstOrder<>(g);
        List<V> postOrder = dfo.postOrder();
        List<V> inversedPostOrder = new LinkedList<>();
        inversedPostOrder.addAll(postOrder);
        Collections.reverse(inversedPostOrder);
        DirectedGraph<V> invertedGraph = g.invert();
        visitDepthFirst(invertedGraph, inversedPostOrder);


    }


    private void visitDepthFirst(DirectedGraph<V> g, List<V> sequence) {
        Set<V> visited = new TreeSet<>();
        int compNum = 0;
        for (V vertex : sequence) {
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                comp.put(compNum, new TreeSet<>());
                visitDepthFirstR(g, vertex, visited, compNum++);

            }
        }
    }

    private void visitDepthFirstR(DirectedGraph<V> g, V vertex, Set<V> visited, int compNum) {
        visited.add(vertex);
        comp.get(compNum).add(vertex);
        for (V adj : g.getSuccessorVertexSet(vertex)) {
            if (!visited.contains(adj)) {
                visitDepthFirstR(g, adj, visited, compNum);
            }
        }
    }

    /**
     * @return Anzahl der strengen Komponeneten.
     */
    public int numberOfComp() {
        return comp.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Set<V>> e : comp.entrySet()) {
            sb.append("Component ").append(e.getKey().toString()).append(": ");
            int i = 0;
            for (V v : e.getValue()) {
                sb.append(v.toString());
                if (i++ != e.getValue().size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(System.lineSeparator());

        }
        return sb.toString();
    }

    /**
     * Liest einen gerichteten Graphen von einer Datei ein.
     *
     * @param fn Dateiname.
     * @return gerichteter Graph.
     * @throws FileNotFoundException
     */
    public static DirectedGraph<Integer> readDirectedGraph(File fn) throws FileNotFoundException {
        DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
        Scanner sc = new Scanner(fn);
        sc.nextInt();
        sc.nextInt();
        while (sc.hasNextInt()) {
            int v = sc.nextInt();
            int w = sc.nextInt();
            g.addEdge(v, w);
        }
        return g;
    }

    private static void test1() {
        DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 1);
        g.addEdge(2, 3);
        g.addEdge(3, 1);

        g.addEdge(1, 4);
        g.addEdge(5, 4);

        g.addEdge(5, 7);
        g.addEdge(6, 5);
        g.addEdge(7, 6);

        g.addEdge(7, 8);
        g.addEdge(8, 2);

        StrongComponents<Integer> sc = new StrongComponents<>(g);

        System.out.println(sc.numberOfComp());  // 4

        System.out.println(sc);
        // Component 0: 5, 6, 7,
        // Component 1: 8,
        // Component 2: 1, 2, 3,
        // Component 3: 4,
    }

    private static void test2() throws FileNotFoundException {
        DirectedGraph<Integer> g = readDirectedGraph(new File("./res/mediumDG.txt"));
        System.out.println(g.getNumberOfVertexes());
        System.out.println(g.getNumberOfEdges());
        System.out.println(g);

        System.out.println("");

        StrongComponents<Integer> sc = new StrongComponents<>(g);
        System.out.println(sc.numberOfComp());  // 10
        System.out.println(sc);

    }

    public static void main(String[] args) throws FileNotFoundException {
        test1();
        test2();
    }
}
