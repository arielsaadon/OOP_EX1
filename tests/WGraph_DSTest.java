package Ex1.tests;

import Ex1.src.WGraph_DS;
import Ex1.src.node_info;
import Ex1.src.weighted_graph;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {

    @Test
    void getNode() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(3);

        g.removeNode(2);
        g.removeNode(1);
        assertNull(g.getNode(1), "check if node 1 in graph Expected null");
        assertNotNull(g.getNode(3), "check if node 3 in graph");
    }

    @Test
    void hasEdge() {
        weighted_graph g = small_graph();
        assertTrue(g.hasEdge(0,0));
        assertTrue(g.hasEdge(0,1));
        assertTrue(g.hasEdge(3,9));
        assertTrue(g.hasEdge(7,5));

        g.removeEdge(0,0);
        g.removeEdge(0,1);
        g.removeEdge(2,4);
        g.removeNode(7);
        assertFalse(g.hasEdge(0,1));
        assertFalse(g.hasEdge(4,2));
        assertFalse(g.hasEdge(7,5));
    }

    @Test
    void getEdge() {
        weighted_graph g = small_graph();

        assertEquals(g.getEdge(1,0),1);
        assertEquals(g.getEdge(7,5),1.1);
        assertEquals(g.getEdge(6,8),30);

        assertEquals(g.getEdge(5,5),0,"node1 == node2");
        assertEquals(g.getEdge(8,9),-1,"dont have edge");
        assertEquals(g.getEdge(2,10),-1,"dont have edge");

        Collection<node_info> v = g.getV();
        for (node_info n : v) {
            Collection<node_info> nei = g.getV(n.getKey());
            for (node_info ne : nei) {
                double w = g.getEdge(n.getKey(), ne.getKey());
                assertTrue(w >= 0, "check if the weight bigger or equal to 0");
            }
        }
    }

    @Test
    void addNode() {
        weighted_graph g = new WGraph_DS();
        int[] nodes = new int[]{0,1,1,2,4,2};
        for (int node : nodes) {
            g.addNode(node);
        }
        assertNotNull(g.getNode(4), "check if node 4 in graph");
        assertNotNull(g.getNode(0), "check if node 0 in graph");
    }

    @Test
    void connect() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(4);
        g.addNode(6);

        g.connect(4,4,5);
        double w = g.getEdge(4,4);
        assertEquals(w,0);

        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(1,2,4);

        g.connect(2,4,5);
        g.connect(2,4,8);
        double w1 = g.getEdge(4,2);
        assertEquals(w1,8);

        double w2 = g.getEdge(6,6);
        assertEquals(w2,0);

        assertFalse(g.hasEdge(0,6));

        g.removeEdge(0,1);
        assertFalse(g.hasEdge(1,0));

        g.connect(0,1,1);
        assertTrue(g.hasEdge(1,0));
    }

    @Test
    void getV() {
        weighted_graph g = new WGraph_DS();
        int[] nodes = new int[]{0,1,2,4};
        for (int node : nodes) {
            g.addNode(node);
        }
        g.addNode(0);
        g.addNode(4);
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);
        g.connect(0,1,1);
        Collection<node_info> v = g.getV();
        Iterator<node_info> iter = v.iterator();
        int it = 0;
        while (iter.hasNext() || it < nodes.length) {
            node_info n = iter.next();
            assertEquals(n.getKey(),nodes[it]);
            assertNotNull(n);
            it++;
        }
    }

    @Test
    void getVNeighbors() {
        weighted_graph g = new WGraph_DS();
        int[] nodes = new int[]{0,1,2,7};
        for (int node : nodes) {
            g.addNode(node);
        }
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(2,4,3);
        g.connect(4,9,1);

        assertNull(g.getV(11));

        g.connect(0,7,1);
        g.connect(1,2,2);
        g.connect(1,7,3);
        g.connect(2,7,1);

        assertNotNull(g.getV(7));

        Collection<node_info> v = g.getV(7);
        Iterator<node_info> it = v.iterator();
        int i = 0;
        while (it.hasNext() || i < nodes.length-1){
            node_info n = it.next();
            assertEquals(n.getKey(),nodes[i]);
            i++;
        }
    }

    @Test
    void removeNode() {
        weighted_graph g = small_graph();
        assertNull(g.removeNode(12));

        g.removeNode(4);
        g.removeNode(0);

        assertFalse(g.hasEdge(1,0));
        assertEquals(9,g.edgeSize());
        assertEquals(9,g.nodeSize());
        assertNotNull(g.getV(7));
    }

    @Test
    void removeEdge() {
        weighted_graph g = small_graph();

        g.removeEdge(0,3);
        assertEquals(g.getEdge(0,3),-1);

        assertEquals(g.getEdge(10,8),10);

        g.removeNode(10);
        assertEquals(g.getEdge(10,8),-1);
    }

    public static weighted_graph small_graph() {
        weighted_graph g0 = new WGraph_DS();
        for(int i = 0 ; i < 11 ; i++) {
            g0.addNode(i);
        }
        g0.connect(0,1,1);
        g0.connect(0,2,2);
        g0.connect(0,3,3);
        g0.connect(1,4,17);
        g0.connect(1,5,1);
        g0.connect(2,4,1);
        g0.connect(3, 5,10);
        g0.connect(3,6,100);
        g0.connect(5,7,1.1);
        g0.connect(6,7,10);
        g0.connect(7,10,2);
        g0.connect(6,8,30);
        g0.connect(8,10,10);
        g0.connect(4,10,30);
        g0.connect(3,9,10);
        g0.connect(8,10,10);

        return g0;
    }

}