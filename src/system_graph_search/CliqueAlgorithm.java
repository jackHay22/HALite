package system_graph_search;

import java.util.*;
import java.util.Map.Entry;
import java.lang.Math;


public class CliqueAlgorithm {

    public static Set<Set<WeightedVertex>> kPlexPLS(UndirectedGraph graph, int seed) {
        Set<Set<WeightedVertex>> allCliques = new HashSet<>();
        Set<WeightedVertex> graphsVertices = new HashSet<>(graph.getVertices());

//        List<String> sglist = Arrays.asList("Ash","AlcalinityOfAsh","Proanthocyanins","ColorIntensity","OD280","Proline");
//        Set<WeightedVertex> sg = new HashSet<>();
//        for (String node : sglist) {
//            System.out.println(graph.getVertex(node));
//            sg.add(graph.getVertex(node));
//        }
//        Integer k = 0;
//        while (!isKPlex(sg, k)) {
//            k += 1;
//        }
//        System.out.println("Subgraph kplex k:" + Integer.toString(k));

        runKplexPLS(graph, graphsVertices, allCliques, seed);

        return allCliques;
    }

    public static boolean isKPlex(Set<WeightedVertex> graph, int k) {
        List<WeightedVertex> vertexList = new ArrayList<>();
        vertexList.addAll(graph);
        int[] edgeCounts = new int[vertexList.size()];

        for (int i = 0; i < vertexList.size(); i++) {
            for (int j = i+1; j < vertexList.size(); j++) {
                if (!vertexList.get(i).getEdges().containsKey(vertexList.get(j))) {
                    edgeCounts[i] = edgeCounts[i] + 1;
                    edgeCounts[j] = edgeCounts[j] + 1;
                    if (edgeCounts[i] > k || edgeCounts[j] > k) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean find2SwapKPlex(Set<WeightedVertex> subgraph, WeightedVertex nodeToSwap, int n, int k,
                                      double weight, Set<WeightedVertex> graph, UndirectedGraph fullGraph) {
        List<WeightedVertex> vertexList = new ArrayList<WeightedVertex>();
        vertexList.addAll(graph);
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                if (vertexList.get(i).equals(nodeToSwap) || vertexList.get(i).equals(nodeToSwap)) {
                    subgraph.remove(nodeToSwap);
                    subgraph.add(vertexList.get(i));
                    subgraph.add(vertexList.get(j));
                    if (isKPlex(subgraph, k) && weight < getCurrentSGValue(fullGraph, subgraph)) {
                        return true;
                    } else {
                        subgraph.add(nodeToSwap);
                        subgraph.remove(vertexList.get(i));
                        subgraph.remove(vertexList.get(j));
                    }
                }
            }
        }
        return false;
    }

    public static void removeNonNeighbors(WeightedVertex node, Set<WeightedVertex> subgraph) {
        Set<WeightedVertex> toRemove = new HashSet<>();
        for (WeightedVertex wv : subgraph) {
            if (!node.getEdges().containsKey(wv))
                toRemove.add(wv);
        }
        for (WeightedVertex wv : toRemove) {
            subgraph.remove(wv);
        }
    }

    public static void runKplexPLS(UndirectedGraph fullGraph, Set<WeightedVertex> graph, Set<Set<WeightedVertex>> allCliques, int seed) {

        // We're going to use this a couple of times
        Random rand = new Random();
        rand.setSeed(seed);

        // Number of nodes
        int n = graph.size();
        ArrayList<String> nodes = new ArrayList<>();
        nodes.addAll(fullGraph.getVerticesMap().keySet());

        // Measures appearances of nodes in graphs to prevent solution revisiting
        HashMap<String, Integer> appearances = new HashMap<>();
        for (String node : nodes) {
            appearances.put(node, 0);
        }

        Set<WeightedVertex> subgraph = new HashSet<>();
        subgraph.add(graph.iterator().next());
        
        double value = 0;

        int iterations = 0;
        int k = 0;
        int lastCount = 0;
        int graphCount;
        int sgSize = subgraph.size();

        int swaps = 0;
        int swapLimit = (int) (1000 * (Math.log(n)/ Math.log(10)));

        // Include below
        //  && swaps < swapLimit
        while (allCliques.size() < (n*n ) && iterations < 3000) {
            iterations += 1;
            boolean u = true;
            boolean p = true;
            boolean d = true;

            // Uniform Random Selection for 50 iterations
            if (u)
            for (int i = 0; i < 50; i++) {
                while (sgSize != subgraph.size()) {
                    for (WeightedVertex wv : graph) {
                        if (subgraph.contains(wv)) {
                            sgSize = subgraph.size();
                            value = getCurrentSGValue(fullGraph, subgraph);
                            // Find a 2swap for our current k of k-plex
                            if (find2SwapKPlex(subgraph, wv, n, k, value, graph, fullGraph))
                                break;
                        }
                    }
                    break;
                }
                Set<WeightedVertex> sg = new HashSet<>(subgraph);
                allCliques.add(sg);
                swaps++;
                for (WeightedVertex wv : subgraph) {
                    appearances.put(wv.getName(), appearances.get(wv.getName()) + 1);
                }

                // Grab random node
                String nodeName = nodes.get(rand.nextInt(nodes.size()));
                WeightedVertex node = fullGraph.getVertex(nodeName);

                // Remove nodes that don't share an edge with our random
                removeNonNeighbors(node, subgraph);
                subgraph.add(node);

            }


            // Penalty based random
            if (p)
            for (int i = 0; i < 50; i++) {
                while (sgSize != subgraph.size()) {
                    for (WeightedVertex wv : graph) {
                        if (subgraph.contains(wv)) {
                            sgSize = subgraph.size();
                            value = getCurrentSGValue(fullGraph, subgraph);
                            // Find a 2swap for our current k of k-plex
                            if (find2SwapKPlex(subgraph, wv, n, k, value, graph, fullGraph))
                                break;
                        }
                    }
                    break;
                }
                Set<WeightedVertex> sg = new HashSet<>(subgraph);
                allCliques.add(sg);
                swaps++;
                for (WeightedVertex wv : subgraph) {
                    appearances.put(wv.getName(), appearances.get(wv.getName()) + 1);
                }

                int maxFreq = 0;
                int minFreq = 2147483647;
                for (String key : appearances.keySet()) {
                    int freq = appearances.get(key);
                    if (freq > maxFreq)
                        maxFreq = freq;
                    if (freq < minFreq)
                        minFreq = freq;
                }

                WeightedVertex node;
                int id;
                Double probability = Math.abs(rand.nextGaussian() * (maxFreq - minFreq)) + minFreq;

                if (subgraph.size() != n) {
                    ArrayList<WeightedVertex> matches = new ArrayList<>();
                    ArrayList<WeightedVertex> matches_out = new ArrayList<>();
                    for (int add = 0; add < n; add++) {
                        node = fullGraph.getVertex(nodes.get(add));
                        if (appearances.get(nodes.get(add)) <= probability) {
                            matches.add(node);
                            if (!subgraph.contains(node)) {
                                matches_out.add(node);
                            }
                        }
                    }
                    if (matches_out.size() > 0) {
                        id = rand.nextInt(matches_out.size());
                    } else {
                        id = rand.nextInt(matches.size());
                    }
                } else {
                    id = rand.nextInt(graph.size());
                }
                node = fullGraph.getVertex(nodes.get(id));


                // Remove nodes that don't share an edge with our random
                removeNonNeighbors(node, subgraph);
                subgraph.add(node);

            }

            // Degree based approach
            if (d)
            for (int i = 0; i < 100; i++) {
                while (sgSize != subgraph.size()) {
                    for (WeightedVertex wv : graph) {
                        if (subgraph.contains(wv)) {
                            sgSize = subgraph.size();
                            value = getCurrentSGValue(fullGraph, subgraph);
                            // Find a 2swap for our current k of k-plex
                            if (find2SwapKPlex(subgraph, wv, n, k, value, graph, fullGraph))
                                break;
                        }
                    }
                    break;
                }
                Set<WeightedVertex> sg = new HashSet<>(subgraph);
                allCliques.add(sg);
                swaps++;
                for (WeightedVertex wv : subgraph) {
                    appearances.put(wv.getName(), appearances.get(wv.getName()) + 1);
                }

                WeightedVertex node = graph.iterator().next();
                for (WeightedVertex pv : graph) {
                    if (pv.getEdges().size() > node.getEdges().size())
                        node = pv;
                }

                // Remove nodes that don't share an edge with our random
                removeNonNeighbors(node, subgraph);
                subgraph.add(node);

            }

            graphCount = allCliques.size();
            if (lastCount == graphCount) {

                k += 2;
//                System.out.println(k);
            }
            lastCount = graphCount;
            if (k < 0) {
            	for (Entry<String, Integer> e : appearances.entrySet()) {
            		System.out.print("{" + e.getKey() + ": " + e.getValue() + "}, ");
            	}
            	System.out.println();
            } else if (k < 7 && k > 1) {
            	System.out.println("k " + Integer.toString(k));
            }

        }


    }

    public static Double getCurrentSGValue(UndirectedGraph graph, Set<WeightedVertex> subgraph) {
    	
    	Double total = 0.0;
    	
    	for (WeightedVertex wv: subgraph) {
    		if (wv != null && wv.getProperty("weight") != null) {
    			total += (Double)(wv.getProperty("weight"));
    		}
    	}
//    	for (WeightedVertex wv: subgraph) {
//    		for (WeightedVertex wvi: subgraph) {
//    			if (graph.getEdge(wv, wvi) != null && graph.getEdge(wv, wvi).getProperty("weight") != null) {
//            		total += (Double)(graph.getEdge(wv, wvi).getProperty("weight"));
//    			}
//        	}
//    	}
    	return total/Math.log(subgraph.size());
    }
    
    /*
     *    BronKerbosch1(R, P, X):
       if P and X are both empty:
           report R as a maximal clique
       for each vertex v in P:
           BronKerbosch1(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
           P := P \ {v}
           X := X ⋃ {v}
     */
    public static Set<Set<WeightedVertex>> bronKerbosch(UndirectedGraph graph) {
        Set<Set<WeightedVertex>> allCliques = new HashSet<>();
        Set<WeightedVertex> graphsVertices = new HashSet<>(graph.getVertices());
        runBronKerbosch(new HashSet<>(), graphsVertices, new HashSet<>(), allCliques);
        return allCliques;
    }

    /*
     *    BronKerbosch2(R,P,X):
       if P and X are both empty:
           report R as a maximal clique
       choose a pivot vertex u in P ⋃ X
       for each vertex v in P \ N(u):
           BronKerbosch2(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
           P := P \ {v}
           X := X ⋃ {v}
     */
    public static Set<Set<WeightedVertex>> bronKerboschPivoting(UndirectedGraph graph) {
        Set<Set<WeightedVertex>> allCliques = new HashSet<>();
        Set<WeightedVertex> graphsVertices = new HashSet<>(graph.getVertices());
        runBronKerboschPivoting(new HashSet<>(), graphsVertices, new HashSet<>(), allCliques);
        return allCliques;
    }

    /*
        R is current growing clique, p all possible vertices, x also
     */
    private static void runBronKerbosch(Set<WeightedVertex> r, Set<WeightedVertex> p, Set<WeightedVertex> x,
                                        Set<Set<WeightedVertex>> allCliques) {
        if (notContainsSet(r, allCliques) && !r.isEmpty()) {
            Set<WeightedVertex> newClique = new HashSet<>(r);
            allCliques.add(newClique);
        }
        if (p.isEmpty() && x.isEmpty()) {
            return; //as max clique
        }
        Iterator<WeightedVertex> pIter = p.iterator();
        while (pIter.hasNext()) {
            WeightedVertex v = pIter.next();
            Set<WeightedVertex> newR = new HashSet<>(r);
            newR.add(v);
            Set<WeightedVertex> newP = new HashSet<>(p);
            newP.retainAll(v.getNeighbors());
            Set<WeightedVertex> newX = new HashSet<>(x);
            newX.retainAll(v.getNeighbors());
            runBronKerbosch(newR, newP, newX, allCliques);
            pIter.remove();
            x.add(v);
        }
    }

    private static void runBronKerboschPivoting(Set<WeightedVertex> r, Set<WeightedVertex> p, Set<WeightedVertex> x,
                                                Set<Set<WeightedVertex>> allCliques) {
        if (notContainsSet(r, allCliques)) {
            Set<WeightedVertex> newClique = new HashSet<>(r);
            allCliques.add(newClique);
        }
        if (p.isEmpty() && x.isEmpty()) {
            return; //as max clique
        }
        Set<WeightedVertex> possiblePivotSet = new HashSet<>(p);
        possiblePivotSet.addAll(x);
        WeightedVertex pivorU = findHighestNeighbors(possiblePivotSet);
        Set<WeightedVertex> pivotSet = new HashSet<>(p);
        pivotSet.removeAll(pivorU.getNeighbors());
        for (WeightedVertex v : pivotSet) {
            Set<WeightedVertex> newR = new HashSet<>(r);
            newR.add(v);
            Set<WeightedVertex> newP = new HashSet<>(p);
            newP.retainAll(v.getNeighbors());
            Set<WeightedVertex> newX = new HashSet<>(x);
            newX.retainAll(v.getNeighbors());
            runBronKerbosch(newR, newP, newX, allCliques);
            p.remove(v);
            x.add(v);
        }
    }

    private static WeightedVertex findHighestNeighbors(Set<WeightedVertex> set) {
        WeightedVertex v = null;
        for (WeightedVertex s : set) {
            if (v == null || s.getNeighbors().size() > v.getNeighbors().size()) {
                v = s;
            }
        }
        return v;
    }

    private static boolean notContainsSet(Set<WeightedVertex> currentClique, Set<Set<WeightedVertex>> allCliques) {
        boolean notContained = true;
        for (Set<WeightedVertex> c : allCliques) {
            if (c.equals(currentClique)) {
                notContained = false;
                break;
            }
        }
        return notContained;
    }

    /*
     *    BronKerbosch2(R,P,X):
       if P and X are both empty:
           report R as a maximal clique
       choose a pivot vertex u in P ⋃ X
       for each vertex v in P \ N(u):
           BronKerbosch2(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
           P := P \ {v}
           X := X ⋃ {v}
     */
    public static Set<Set<WeightedVertex>> bronKerboschPivotingOnlyMax(UndirectedGraph graph) {
        Set<Set<WeightedVertex>> allCliques = new HashSet<>();
        Set<WeightedVertex> graphsVertices = new HashSet<>(graph.getVertices());
        runBronKerboschPivotingOnlyMax(new HashSet<>(), graphsVertices, new HashSet<>(), allCliques);
        return allCliques;
    }

    private static void runBronKerboschPivotingOnlyMax(Set<WeightedVertex> r,
                                                       Set<WeightedVertex> p,
                                                       Set<WeightedVertex> x,
                                                       Set<Set<WeightedVertex>> allCliques) {
        if (p.isEmpty() && x.isEmpty()) {
            allCliques.add(r);
            return; //as max clique
        }
        Set<WeightedVertex> possiblePivotSet = new HashSet<>(p);
        possiblePivotSet.addAll(x);
        WeightedVertex pivorU = findHighestNeighbors(possiblePivotSet);
        Set<WeightedVertex> pivotSet = new HashSet<>(p);
        pivotSet.removeAll(pivorU.getNeighbors());
        for (WeightedVertex v : pivotSet) {
            Set<WeightedVertex> newR = new HashSet<>(r);
            newR.add(v);
            Set<WeightedVertex> newP = new HashSet<>(p);
            newP.retainAll(v.getNeighbors());
            Set<WeightedVertex> newX = new HashSet<>(x);
            newX.retainAll(v.getNeighbors());
            runBronKerbosch(newR, newP, newX, allCliques);
            p.remove(v);
            x.add(v);
        }
    }
}
