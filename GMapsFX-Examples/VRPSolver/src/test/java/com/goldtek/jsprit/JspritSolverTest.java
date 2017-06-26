package com.goldtek.jsprit;

import org.junit.After;
import org.junit.AfterClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import java.util.List;

import org.mockito.runners.MockitoJUnitRunner;

import com.goldtek.algorithm.IVrpSolver;
import com.goldtek.algorithm.Route;

public class JspritSolverTest {
    protected IVrpSolver solver = JspritSolver.getInstance();
    
    public JspritSolverTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testInfiniteFleet() {
        solver.reset();
        solver.inputFrom("input/zhonghe_test.xml");
        List<Route> routes = solver.solve(1);
        assertTrue("Thers's no infinite solutions", routes.size() > 0);
    }
    
    @Test
    public void testFiniteFleet() {
        solver.reset();
        solver.inputFrom("input/zhonghe_test_vehicle.xml");
        List<Route> routes = solver.solve(1);
        assertTrue("Thers's no finite solutions", routes.size() > 0);
    }
}
