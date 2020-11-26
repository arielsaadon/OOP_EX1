package Ex1.src;

import java.io.Serializable;
import java.util.*;

public class WGraph_DS implements weighted_graph , Serializable {
    private HashMap<Integer,node_info> vertex;
    private HashMap<Integer,HashMap<Integer, Double>> edges;
    private int countVertex;
    private int countEdges;
    private int activity;

    public WGraph_DS(){
        vertex = new HashMap<>();
        edges = new HashMap<>();
        countVertex = 0;
        countEdges = 0;
        activity = 0;
    }

    public WGraph_DS(weighted_graph g){
        vertex = new HashMap<>();
        edges = new HashMap<>();
        countVertex = 0;
        countEdges = 0;
        activity = 0;
        Iterator<node_info> itr = g.getV().iterator();
        while (itr.hasNext()){
            addNode(itr.next().getKey());
        }
        itr = g.getV().iterator();
        while (itr.hasNext()){
            node_info n = itr.next();
            for (node_info v : g.getV(n.getKey())) {
                connect(n.getKey(), v.getKey(), g.getEdge(n.getKey(), v.getKey()));
            }
        }
    }

    @Override
    public node_info getNode(int key) {
        if(vertex.containsKey(key) && vertex.get(key) != null)
            return vertex.get(key);
        return null;
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        if(node1 == node2) return true;
        if(vertex.containsKey(node1) && vertex.containsKey(node2)) {
            return edges.get(node1).containsKey(node2) && edges.get(node2).containsKey(node1);
        }
        return false;
    }

    @Override
    public double getEdge(int node1, int node2) {
        if(node1 == node2) return 0;
        if(!hasEdge(node1,node2)) return -1;
        return edges.get(node1).get(node2);
    }

    @Override
    public void addNode(int key) {
        if(vertex.containsKey(key)) return;
        vertex.put(key,new NodeInfo(key));
        edges.put(key,new HashMap<>());
        countVertex++;
        activity++;
    }

    @Override
    public void connect(int node1, int node2, double w) {
        if(w<0) throw new IllegalArgumentException("Weight should be bigger or equal to 0");
        if(vertex.containsKey(node1) && vertex.containsKey(node2) && node1 !=node2) {
            if(hasEdge(node1,node2)) {
                edges.get(node1).replace(node2,w);
                edges.get(node2).replace(node1,w);
            }
            else {
                edges.get(node1).put(node2, w);
                edges.get(node2).put(node1, w);
                countEdges++;
            }
            activity++;
        }
    }

    @Override
    public Collection<node_info> getV() {
        return this.vertex.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        if(!vertex.containsKey(node_id))
            return null;
        Collection<node_info> ListOfNeighbors = new ArrayList<>();
        for(Integer node : edges.get(node_id).keySet()) {
            ListOfNeighbors.add(vertex.get(node));
        }
        return ListOfNeighbors;
    }

    @Override
    public node_info removeNode(int key) {
        node_info vertexDelete = getNode(key);
        if(vertexDelete == null) return null;

        for(node_info neighbor : getV(key)) {
            removeEdge(key, neighbor.getKey());
        }
        vertex.remove(key);
        edges.remove(key);
        countVertex--;
        activity++;
        return vertexDelete;
    }

    @Override
    public void removeEdge(int node1, int node2) {
        if(vertex.containsKey(node1) && vertex.containsKey(node2) && node1 != node2) {
            if(edges.get(node1).containsKey(node2) && edges.get(node2).containsKey(node1)) {
                edges.get(node1).remove(node2);
                edges.get(node2).remove(node1);
                countEdges--;
                activity++;
            }
        }
    }

    @Override
    public int nodeSize() {
        return this.countVertex;
    }

    @Override
    public int edgeSize() {
        return this.countEdges;
    }

    @Override
    public int getMC() {
        return this.activity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return countVertex == wGraph_ds.countVertex &&
                countEdges == wGraph_ds.countEdges &&
                activity == wGraph_ds.activity &&
                Objects.equals(vertex, wGraph_ds.vertex) &&
                Objects.equals(edges, wGraph_ds.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertex, edges, countVertex, countEdges, activity);
    }

    @Override
    public String toString() {
        for(Map.Entry m : edges.entrySet()){
            System.out.print(m.getKey());
            System.out.print("-->");
            System.out.println(m.getValue());
        }
        return "";
    }

    private class NodeInfo implements node_info , Serializable {
        private int key;
        private double tag;
        private String info;

        public NodeInfo(int key){
            this.key = key;
            this.tag = Double.MAX_VALUE;
            this.info = "f";
        }
        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NodeInfo nodeInfo = (NodeInfo) o;
            return key == nodeInfo.key &&
                    Double.compare(nodeInfo.tag, tag) == 0 &&
                    Objects.equals(info, nodeInfo.info);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, tag, info);
        }
    }
}
