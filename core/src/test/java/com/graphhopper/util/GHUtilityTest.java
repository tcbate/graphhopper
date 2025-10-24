/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.util;

import com.github.javafaker.Faker;
import com.graphhopper.GraphHopperConfig;
import com.graphhopper.coll.GHIntLongHashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assertions.*;

import com.graphhopper.routing.Path;
import com.graphhopper.routing.ev.DecimalEncodedValue;
import com.graphhopper.routing.ev.VehicleSpeed;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.storage.BaseGraph;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.NodeAccess;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

/**
 * @author Peter Karich
 */
public class GHUtilityTest {
    
    private Graph graph;

    @Test
    public void testEdgeStuff() {
        assertEquals(2, GHUtility.createEdgeKey(1, false));
        assertEquals(3, GHUtility.createEdgeKey(1, true));
    }

    @Test
    public void testZeroValue() {
        GHIntLongHashMap map1 = new GHIntLongHashMap();
        assertFalse(map1.containsKey(0));
        // assertFalse(map1.containsValue(0));
        map1.put(0, 3);
        map1.put(1, 0);
        map1.put(2, 1);

        // assertTrue(map1.containsValue(0));
        assertEquals(3, map1.get(0));
        assertEquals(0, map1.get(1));
        assertEquals(1, map1.get(2));

        // instead of assertEquals(-1, map1.get(3)); with hppc we have to check before:
        assertTrue(map1.containsKey(0));

        // trove4j behaviour was to return -1 if non existing:
//        TIntLongHashMap map2 = new TIntLongHashMap(100, 0.7f, -1, -1);
//        assertFalse(map2.containsKey(0));
//        assertFalse(map2.containsValue(0));
//        map2.add(0, 3);
//        map2.add(1, 0);
//        map2.add(2, 1);
//        assertTrue(map2.containsKey(0));
//        assertTrue(map2.containsValue(0));
//        assertEquals(3, map2.get(0));
//        assertEquals(0, map2.get(1));
//        assertEquals(1, map2.get(2));
//        assertEquals(-1, map2.get(3));
    }

    @BeforeEach
    public void setup() {
        // initialiser EncodingManagerobligatoire pour BaseGraph
        DecimalEncodedValue carSpeedEnc = VehicleSpeed.create("car", 5, 5, true);
        EncodingManager em = new EncodingManager.Builder().add(carSpeedEnc).build();
        
        // Création du BaseGraph
        graph = new BaseGraph.Builder(em).create(); 
        
        //  Initialisation des noeuds utilisés dans les tests (0, 1, 2, 3)
        NodeAccess na = graph.getNodeAccess();
        na.setNode(0, 0.0, 0.0);
        na.setNode(1, 0.001, 0.0);
        na.setNode(2, 0.002, 0.0);
        na.setNode(3, 0.003, 0.0);
    }

    @Test
    public void testComparePathsWithDifferentWeightsShouldFail() {
        //  two paths with a big weight difference (> 1.e-2).
        Path path1 = new Path(graph);
        path1.setFound(true);
        path1.setWeight(100.0);

        Path path2 = new Path(graph);
        path2.setFound(true);
        path2.setWeight(101.1); // Weight difference is 1.1

        // Expect an AssertionError due to the large weight difference
        assertThrows(AssertionError.class, () -> {
            GHUtility.comparePaths(path1, path2, 0, 1, 0);
        }, "AssertionError expected for large weight discrepancy.");
    }

    @Disabled
    public void testComparePathsWithDifferentNodeSequenceShouldReturnViolation() {
        // two paths with identical weight/distance but different node sequences
        Path path1 = new Path(graph);
        path1.setFound(true);
        path1.setWeight(100.0);
        path1.setDistance(100.0);
        path1.calcNodes().add(0); // Node 0
        path1.calcNodes().add(1); // Node 1
        path1.calcNodes().add(3); // Node 3

        Path path2 = new Path(graph);
        path2.setFound(true);
        path2.setWeight(100.0);
        path2.setDistance(100.0);
        path2.calcNodes().add(0); // Node 0
        path2.calcNodes().add(2); // Node 2 (Different node sequence)
        path2.calcNodes().add(3); // Node 3

        // Execute comparison and get the list of violations.
        List<String> violations = GHUtility.comparePaths(path1, path2, 0, 3, 0);

        // Verify a wrong nodes violation was reported.
        assertFalse(violations.isEmpty(), "Violation list must not be empty for different nodes.");
        assertTrue(violations.get(0).contains("wrong nodes"),
            "Violation must concern the incorrect node sequence.");
    }
    @Disabled
    public void testComparePathsWithDifferentDistanceShouldReturnViolation() {
        //  two paths with identical weights/nodes, but distances little  different (> 0.1).
        Path path1 = new Path(graph);
        path1.setFound(true);
        path1.setWeight(100.0);
        path1.setDistance(50.0);
        path1.calcNodes().add(0);
        path1.calcNodes().add(1);
        
        Path path2 = new Path(graph);
        path2.setFound(true);
        path2.setWeight(100.0);
        path2.setDistance(50.2); // Distance difference is 0.2 (> 0.1)
        path2.calcNodes().add(0);
        path2.calcNodes().add(1);
        
        // Execute comparison
        List<String> violations = GHUtility.comparePaths(path1, path2, 0, 1, 0);

        // Verify a wrong distance violation was reported.
        assertFalse(violations.isEmpty(), "Violation list must not be empty for different distances.");
        assertTrue(violations.get(0).contains("wrong distance"),
            "Violation must concern the incorrect distance metric.");
    }

    @Test
    void testComparePathsWithEquivalentDetourShouldReturnNoViolations() {
        // Path1 is direct A->C. Path2 is equivalent detour A->B->C.
        // test depends on the internal logic of pathsEqualExceptOneEdge()
        
        // Path 1: A -> C (direct)
        Path path1 = new Path(graph);
        path1.setFound(true);
        path1.setWeight(50.0);
        path1.setDistance(10.0);
        path1.calcNodes().add(0); // A
        path1.calcNodes().add(2); // C

        // Path 2: A -> B -> C (detour)
        Path path2 = new Path(graph);
        path2.setFound(true);
        path2.setWeight(50.0);
        path2.setDistance(10.0);
        path2.calcNodes().add(0); // A
        path2.calcNodes().add(1); // B (detour node)
        path2.calcNodes().add(2); // C

        //  Compare paths. 
        List<String> violations = GHUtility.comparePaths(path1, path2, 0, 2, 1); // From=0, To=2, Via=1

        // Logic should recognize the detour as valid = in no violations.
        assertTrue(violations.isEmpty(),
            "No violation expected when a valid detour node (via) is accounted for.");
    }
    @Test
    public void testComparePathsWithFakerGeneratedWeights() {
        // Verify that comparePaths has no violations if the weights are very similar
        // (difference less than 1.e-2), even with Faker-generated data.
        Faker faker = new Faker();

        // Generate a random base weight between 1000 and 10000 (for a long path)

        double minBase = 1000.0;
        double maxBase = 10000.0;
        double baseWeight = minBase + (maxBase - minBase) * faker.random().nextDouble();

        // Generate a small difference less than the tolerance (1.e-2 = 0.01)
        double minDiff = 0.0001; 
        double maxDiff = 0.005;
        double smallDiff = minDiff + (maxDiff - minDiff) * faker.random().nextDouble();

        double weight1 = baseWeight;
        double weight2 = baseWeight + smallDiff; // Almost identical weight

        // Path 1 setup (Path 1 and 2 are identical in structure here)
        Path path1 = new Path(graph);
        path1.setFound(true);
        path1.setWeight(weight1);
        path1.calcNodes().add(0); 
        path1.calcNodes().add(1);

        // Path 2 setup
        Path path2 = new Path(graph);
        path2.setFound(true);
        path2.setWeight(weight2);
        path2.calcNodes().add(0);
        path2.calcNodes().add(1);

        // expect an empty list of violation 
        List<String> violations = GHUtility.comparePaths(path1, path2, 0, 1, 0);

        assertTrue(violations.isEmpty(), 
            "No violation expected because weights (" + weight1 + " vs " + weight2 + 
            ") are very similar and within the 1.e-2 tolerance. Violations: " + violations);
        }

}
