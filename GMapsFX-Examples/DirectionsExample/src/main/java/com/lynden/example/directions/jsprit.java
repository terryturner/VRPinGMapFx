package com.lynden.example.directions;

import java.util.Collection;

import com.graphhopper.jsprit.analysis.toolbox.GraphStreamViewer;
import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.util.Coordinate;
import com.graphhopper.jsprit.core.util.EuclideanDistanceCalculator;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.VehicleRoutingTransportCostsMatrix;

public class jsprit {
	VehicleRoutingProblem problem;
	public jsprit() {
		final int WEIGHT_INDEX = 0;
    	VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance("vehicleType")
    			.addCapacityDimension(WEIGHT_INDEX,50);
    	VehicleType vehicleType = vehicleTypeBuilder.build();

    	VehicleImpl.Builder vehicleBuilder = VehicleImpl.Builder.newInstance("vehicle");
    	vehicleBuilder.setStartLocation(Location.newInstance(0, 0));
    	vehicleBuilder.setType(vehicleType);
    	VehicleImpl vehicle = vehicleBuilder.build();
    	
    	Service service1 = Service.Builder.newInstance("1").addSizeDimension(WEIGHT_INDEX,1).setLocation(Location.newInstance(-10, -10)).build();
    	Service service2 = Service.Builder.newInstance("2").addSizeDimension(WEIGHT_INDEX,1).setLocation(Location.newInstance(0, 10)).build();
    	Service service3 = Service.Builder.newInstance("3").addSizeDimension(WEIGHT_INDEX,1).setLocation(Location.newInstance(20, 0)).build();
    	Service service4 = Service.Builder.newInstance("4").addSizeDimension(WEIGHT_INDEX,1).setLocation(Location.newInstance(-10, 10)).build();
    	
    	VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
    	vrpBuilder.addVehicle(vehicle);
    	vrpBuilder.addJob(service1).addJob(service2).addJob(service3).addJob(service4);
    	
    	problem = vrpBuilder.build();


	}
	
	public void start() {

    	VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);//key point
    	algorithm.setMaxIterations(200);
    	Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();

    	VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

    	//SolutionPrinter.print(problem, bestSolution, Print.CONCISE);
    	new GraphStreamViewer(problem, bestSolution).setRenderDelay(200).display();
	}
	
}
