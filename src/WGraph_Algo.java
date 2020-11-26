package Ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms , Serializable {
    private weighted_graph graph;

    public WGraph_Algo(){
        this.graph = new WGraph_DS();
    }

    @Override
    public void init(weighted_graph g) {
        this.graph = g;
    }

    @Override
    public weighted_graph getGraph() {
        return this.graph;
    }

    @Override
    public weighted_graph copy() {
        return new WGraph_DS(this.graph);
    }

    /**
     * over of all vertex in graph by the weights.
     * @param g - graph given
     * @param src - start vertex
     */
    private void Dijksta(weighted_graph g, int src){
        g.getNode(src).setTag(0);

        PriorityQueue<node_info> queue = new PriorityQueue<node_info>(new NodeComparator());
        queue.addAll(g.getV());

        while(!queue.isEmpty()){
            node_info node = queue.poll();
            for(node_info neighbors : g.getV(node.getKey())){
                if(neighbors.getInfo().charAt(0) == 'f'){
                    double dist = node.getTag()+g.getEdge(node.getKey(), neighbors.getKey());
                    if(dist < neighbors.getTag()) {
                        neighbors.setTag(dist);
                        neighbors.setInfo(setFather(neighbors.getInfo(),node.getKey()));
                        queue.remove(neighbors);
                        queue.add(neighbors);
                    }
                }
            }
            String visited = node.getInfo();
            node.setInfo(changeToVisit(visited));
        }
}

    @Override
    public boolean isConnected() {
        weighted_graph wGraphDsCopy = this.copy();
        if(wGraphDsCopy.nodeSize() == 0)
            return true;
        Iterator<node_info> it = wGraphDsCopy.getV().iterator();
        Dijksta(wGraphDsCopy,it.next().getKey());
        List<node_info> list = new ArrayList<>(wGraphDsCopy.getV());
        int numOfVertex = wGraphDsCopy.nodeSize();
        for(node_info node : list)
            if(node.getTag() == Double.MAX_VALUE)
                numOfVertex--;
        return numOfVertex == wGraphDsCopy.nodeSize();
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        if(graph.getNode(src) == null || graph.getNode(dest) == null) return -1;
        weighted_graph wGraphDsCopy = this.copy();
        Dijksta(wGraphDsCopy, src);
        double dist = wGraphDsCopy.getNode(dest).getTag();
        if (dist == Double.MAX_VALUE) return -1;
        return dist;
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        weighted_graph wGraphDsCopy = this.copy();
        if(wGraphDsCopy.getNode(dest) != null && wGraphDsCopy.getNode(src) != null) {
            Dijksta(wGraphDsCopy, src);
            List<node_info> list = new ArrayList<>();

            node_info node = wGraphDsCopy.getNode(dest);
            list.add(node);
            int _father = getFather(node.getInfo());
                node_info father = wGraphDsCopy.getNode(_father);
                while (father.getKey() != src) {
                    list.add(father);
                    father = wGraphDsCopy.getNode(getFather(father.getInfo()));
                }
                list.add(father);
                Collections.reverse(list);
                return list;
            }
        return null;
    }

    @Override
    public boolean save(String file) {
        try{
            FileOutputStream myFile = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(myFile);
            oos.writeObject(this.graph);
            oos.close();
            myFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean load(String file) {
        try {
            FileInputStream myFile = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(myFile);
            weighted_graph loadGraph = (weighted_graph) ois.readObject();
            init(loadGraph);
            ois.close();
            myFile.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String changeToVisit(String visited){
        visited = visited.substring(0,0) + 't' + visited.substring(1);
        return visited;
    }
    private int getFather(String info){
        String father = info.substring(1);
        return Integer.parseInt(father);
    }
    private String setFather(String info,int father){
        return info.substring(0,1) + father;
    }

    public static class NodeComparator implements Comparator<node_info> {
        @Override
        public int compare(node_info node1, node_info node2) {
            return Double.compare(node1.getTag(), node2.getTag());
        }
    }
}
