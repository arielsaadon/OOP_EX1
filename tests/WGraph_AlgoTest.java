package Ex1.tests;

import Ex1.src.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {
    @Test
    void isConnected() {
        weighted_graph g0 = new WGraph_DS();
        weighted_graph_algorithms g = new WGraph_Algo();
        g.init(g0);
        assertTrue(g.isConnected());

        g0.addNode(0);
        g.init(g0);
        assertTrue(g.isConnected());

        g0.addNode(5);
        g0.connect(0,5,3);
        g.init(g0);
        assertTrue(g.isConnected());

        g0.addNode(7);
        g.init(g0);
        assertFalse(g.isConnected());

        g0 = WGraph_DSTest.small_graph();
        g.init(g0);
        assertTrue(g.isConnected());

        g0.removeEdge(9,3);
        g.init(g0);
        assertFalse(g.isConnected());
    }

    @Test
    void shortestPathDist() {
        weighted_graph g0 = WGraph_DSTest.small_graph();
        weighted_graph_algorithms g = new WGraph_Algo();
        g.init(g0);
        assertTrue(g.isConnected());

        double w = g.shortestPathDist(0,12);
        assertEquals(w, -1,"12 not in graph");
        double w1 = g.shortestPathDist(14,12);
        assertEquals(w1, -1,"14 and 12 not in graph");

        double w2 = g.shortestPathDist(0,2);
        assertEquals(w2, 2);
        double w3 = g.shortestPathDist(0,7);
        assertEquals(w3, 3.1);
        double w4 = g.shortestPathDist(0,10);
        assertEquals(w4, 5.1);
        double w5 = g.shortestPathDist(2,9);
        assertEquals(w5, 15);

        g0.removeEdge(9,3);
        g.init(g0);
        double w6 = g.shortestPathDist(2,9);
        assertEquals(w6, -1,"dont have a edge");

        g0.removeNode(7);
        g.init(g0);
        double w8 = g.shortestPathDist(0,10);
        assertEquals(w8, 33);
    }

    @Test
    void shortestPath() {
        weighted_graph g0 = WGraph_DSTest.small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        List<node_info> sp = ag0.shortestPath(0,10);
        double[] checkTag = {0.0, 1.0, 2.0, 3.1, 5.1};
        int[] checkKey = {0, 1, 5, 7, 10};
        int i = 0;
        for(node_info n: sp) {
            assertEquals(n.getTag(), checkTag[i]);
            assertEquals(n.getKey(), checkKey[i]);
            // System.out.print(n.getKey() +"-->");
            //System.out.print(n.getTag() +", ");
            //System.out.println();
            i++;
        }
        g0.removeEdge(7,10);
        ag0.init(g0);
        List<node_info> sp1 = ag0.shortestPath(0,10);
        double[] checkTag1 = {0.0, 2.0, 3.0, 33.0};
        int[] checkKey1 = {0, 2, 4, 10};
        i = 0;
        for(node_info n: sp1) {
            assertEquals(n.getTag(), checkTag1[i]);
            assertEquals(n.getKey(), checkKey1[i]);
            //System.out.print(n.getKey() +"-->");
            //System.out.print(n.getTag() +", ");
            i++;
        }
        assertNull(ag0.shortestPath(0,15));
        assertNull(ag0.shortestPath(12,15));
        assertNull(ag0.shortestPath(12,0));
    }

    @Test
    void save_load() {
        weighted_graph g0 = WGraph_DSTest.small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        String str = "g0.obj";
        ag0.save(str);
        weighted_graph g1 = WGraph_DSTest.small_graph();
        ag0.load(str);
        assertEquals(g0,g1);
        g0.removeNode(0);
        assertNotEquals(g0,g1);
    }
}