package com.graphhopper.util;

import com.graphhopper.routing.ev.EdgeIntAccess;
import com.graphhopper.storage.BaseGraph;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.storage.RAMDirectory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GHUtilityThomasTest {

    RAMDirectory dir;
    BaseGraph bg;
    NodeAccess na;


    @BeforeEach
    public void setup(){
        dir = new RAMDirectory();
        bg  = new BaseGraph(dir,false,false,128,4);
        na =  bg.getNodeAccess();

        na.setNode(0,1,1);
        na.setNode(1,0,0);
        na.setNode(2,1,0);
        na.setNode(3,0,1);

        //Formation d'une circle
        na.setNode(4,2,2);
        na.setNode(5,2,3);

        //Chain
        na.setNode(6,4,4);
        na.setNode(7,5,5);
        na.setNode(8,6,6);

        EdgeIntAccess ea = bg.getEdgeAccess();
        //Star
        bg.edge(0,1).setDistance(5);
        bg.edge(0,2).setDistance(5);
        bg.edge(0,3).setDistance(5);
        //Circle
        bg.edge(4,5).setDistance(5);
        bg.edge(5,4).setDistance(5);

        //Chain
        bg.edge(6,7).setDistance(5);
        bg.edge(7,8).setDistance(5);

    }

    @AfterEach
    public void clearAll(){
        dir = null;
        bg = null;
        na = null;
    }

    @Test
    public void FindCommonNodeNormal() {
        assertEquals(0, GHUtility.getCommonNode(bg, 1, 0));
    }

    @Test
    public void FindCommonNodeDisconnected() {
        assertThrows(IllegalArgumentException.class, () -> GHUtility.getCommonNode(bg,2,3));
    }

    @Disabled
    public void FindCommonNodeCycle() {
        assertThrows(IllegalArgumentException.class, () -> GHUtility.getCommonNode(bg,4,3));
    }

    @Disabled
    public void findCommonNodeSameEdge(){
        assertEquals(7,GHUtility.getCommonNode(bg,5,6));
    }

    @Test
    public void mockedLoopEdge1(){
        int edge1 = 1;
        int edge2 = 2;
        BaseGraph bgMock = mock(BaseGraph.class);
        EdgeIteratorState eisMock = mock(EdgeIteratorState.class);
        EdgeIteratorState eisMock2 = mock(EdgeIteratorState.class);
        when(bgMock.getEdgeIteratorState(eq(edge1), anyInt())).thenReturn(eisMock);
        when(bgMock.getEdgeIteratorState(eq(edge2), anyInt())).thenReturn(eisMock2);

        when(eisMock.getBaseNode()).thenReturn(2);
        when(eisMock.getAdjNode()).thenReturn(2);

        assertThrows(IllegalArgumentException.class, () -> GHUtility.getCommonNode(bgMock,edge1,edge2));

    }

    @Test
    public void mockedLoopEdge2(){
        int edge1 = 1;
        int edge2 = 2;
        BaseGraph bgMock = mock(BaseGraph.class);
        EdgeIteratorState eisMock = mock(EdgeIteratorState.class);
        EdgeIteratorState eisMock2 = mock(EdgeIteratorState.class);
        when(bgMock.getEdgeIteratorState(eq(edge1), anyInt())).thenReturn(eisMock);
        when(bgMock.getEdgeIteratorState(eq(edge2), anyInt())).thenReturn(eisMock2);

        when(eisMock.getBaseNode()).thenReturn(1);
        when(eisMock.getBaseNode()).thenReturn(2);

        when(eisMock2.getBaseNode()).thenReturn(2);
        when(eisMock2.getAdjNode()).thenReturn(2);

        assertThrows(IllegalArgumentException.class, () -> GHUtility.getCommonNode(bgMock,edge1,edge2));

    }
}
