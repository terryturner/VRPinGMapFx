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
    	Service service5 = Service.Builder.newInstance("5").addSizeDimension(WEIGHT_INDEX,1).setLocation(Location.newInstance(30, 10)).build();
    	Service service6 = Service.Builder.newInstance("6").addSizeDimension(WEIGHT_INDEX,1).setLocation(Location.newInstance(5,  30)).build();
    	
    	VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
    	vrpBuilder.addVehicle(vehicle);
    	vrpBuilder.addJob(service1).addJob(service2).addJob(service3).addJob(service4)
    	//.addJob(service5).addJob(service6)
    	;
    	VehicleRoutingTransportCostsMatrix costMatrix = createMatrix(vrpBuilder);
    	vrpBuilder.setRoutingCost(costMatrix);
    	
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
	
	private static VehicleRoutingTransportCostsMatrix createMatrix(VehicleRoutingProblem.Builder vrpBuilder) {
        VehicleRoutingTransportCostsMatrix.Builder matrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(true);
        for (String from : vrpBuilder.getLocationMap().keySet()) {
            for (String to : vrpBuilder.getLocationMap().keySet()) {
                Coordinate fromCoord = vrpBuilder.getLocationMap().get(from);
                Coordinate toCoord = vrpBuilder.getLocationMap().get(to);
                double distance = EuclideanDistanceCalculator.calculateDistance(fromCoord, toCoord);
                
                //matrixBuilder.addTransportDistance(from, to, distance);
                //matrixBuilder.addTransportTime(from, to, (distance / 2.));
                System.out.println(String.format("%s - %s: %f, %f", from, to, distance, (distance/2.)));
            }
        }
        
        String p0 = "[x=0.0][y=0.0]";
        String p1 = "[x=-10.0][y=-10.0]";
        String p2 = "[x=0.0][y=10.0]";
        String p3 = "[x=20.0][y=0.0]";
        String p4 = "[x=-10.0][y=10.0]";
        matrixBuilder.addTransportDistance(p0, p0, 0.);
        matrixBuilder.addTransportDistance(p0, p1, 14.142136);
        matrixBuilder.addTransportDistance(p0, p2, 10.);
        matrixBuilder.addTransportDistance(p0, p3, 20.);
        matrixBuilder.addTransportDistance(p0, p4, 14.142136);
        matrixBuilder.addTransportDistance(p1, p0, 14.142136);
        matrixBuilder.addTransportDistance(p1, p1, 0.);
        matrixBuilder.addTransportDistance(p1, p2, 22.360680);
        matrixBuilder.addTransportDistance(p1, p3, 31.622777);
        matrixBuilder.addTransportDistance(p1, p4, 20.);
        matrixBuilder.addTransportDistance(p2, p0, 10.);
        matrixBuilder.addTransportDistance(p2, p1, 22.360680);
        matrixBuilder.addTransportDistance(p2, p2, 0.);
        matrixBuilder.addTransportDistance(p2, p3, 22.360680);
        matrixBuilder.addTransportDistance(p2, p4, 10.);
		matrixBuilder.addTransportDistance(p3, p0, 20.);
		matrixBuilder.addTransportDistance(p3, p1, 31.622777);
		matrixBuilder.addTransportDistance(p3, p2, 22.360680);
		matrixBuilder.addTransportDistance(p3, p3, 0.);
		matrixBuilder.addTransportDistance(p3, p4, 5.);
		matrixBuilder.addTransportDistance(p4, p0, 14.142136);
		matrixBuilder.addTransportDistance(p4, p1, 20.);
		matrixBuilder.addTransportDistance(p4, p2, 10.);
		matrixBuilder.addTransportDistance(p4, p3, 5.);
		matrixBuilder.addTransportDistance(p4, p4, 0.);
		
//        matrixBuilder.addTransportTime(p0, p0, 0.);
//        matrixBuilder.addTransportTime(p0, p1, 14.142136);
//        matrixBuilder.addTransportTime(p0, p2, 10.);
//        matrixBuilder.addTransportTime(p0, p3, 20.);
//        matrixBuilder.addTransportTime(p0, p4, 14.142136);
//        matrixBuilder.addTransportTime(p1, p0, 14.142136);
//        matrixBuilder.addTransportTime(p1, p1, 0.);
//        matrixBuilder.addTransportTime(p1, p2, 22.360680);
//        matrixBuilder.addTransportTime(p1, p3, 31.622777);
//        matrixBuilder.addTransportTime(p1, p4, 20.);
//        matrixBuilder.addTransportTime(p2, p0, 10.);
//        matrixBuilder.addTransportTime(p2, p1, 22.360680);
//        matrixBuilder.addTransportTime(p2, p2, 0.);
//        matrixBuilder.addTransportTime(p2, p3, 22.360680);
//        matrixBuilder.addTransportTime(p2, p4, 10.);
//		matrixBuilder.addTransportTime(p3, p0, 20.);
//		matrixBuilder.addTransportTime(p3, p1, 31.622777);
//		matrixBuilder.addTransportTime(p3, p2, 22.360680);
//		matrixBuilder.addTransportTime(p3, p3, 0.);
//		matrixBuilder.addTransportTime(p3, p4, 2.);
//		matrixBuilder.addTransportTime(p4, p0, 14.142136);
//		matrixBuilder.addTransportTime(p4, p1, 20.);
//		matrixBuilder.addTransportTime(p4, p2, 10.);
//		matrixBuilder.addTransportTime(p4, p3, 2.);
//		matrixBuilder.addTransportTime(p4, p4, 0.);
//		
		
        return matrixBuilder.build();
    }
}
