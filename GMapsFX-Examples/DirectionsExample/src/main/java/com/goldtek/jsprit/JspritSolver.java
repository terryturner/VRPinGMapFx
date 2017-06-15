package com.goldtek.jsprit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.goldtek.algorithm.Depot;
import com.goldtek.algorithm.IVrpSolver;
import com.goldtek.algorithm.Route;
import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Delivery;
import com.graphhopper.jsprit.core.problem.job.Job;
import com.graphhopper.jsprit.core.problem.job.Pickup;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.VehicleRoutingTransportCostsMatrix;
import com.graphhopper.jsprit.io.problem.VrpXMLReader;

public class JspritSolver implements IVrpSolver {
	VehicleRoutingProblem.Builder mVrpBuilder = VehicleRoutingProblem.Builder.newInstance();
	VehicleRoutingProblem mVRP = null;

	private static JspritSolver sInstance = new JspritSolver();
	
	private JspritSolver() {}
	
	public static JspritSolver getInstance() {
		if (sInstance == null) sInstance = new JspritSolver();
		return sInstance;
	}
	
	
	public String debug() {
		return "debug for ";
	}
	
	@Override
	public void reset() {
		mVrpBuilder = VehicleRoutingProblem.Builder.newInstance();
		mVRP = null;
	}

	@Override
	public void inputFrom(String path) {
		new VrpXMLReader(mVrpBuilder).read(path);
	}

	@Override
	public void costFrom(String path) {
    	VehicleRoutingTransportCostsMatrix.Builder matrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(false);

    	try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] lineTokens = line.split(",");
                matrixBuilder.addTransportDistance(lineTokens[0], lineTokens[1], Integer.parseInt(lineTokens[2]));
            }
            reader.close();
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	}
    	
    	VehicleRoutingTransportCostsMatrix matrix = matrixBuilder.build();
    	
    	//TODO: mapping all matrix with mVrpBuilder service stop
    	mVrpBuilder.setRoutingCost(matrix);
	}

	
	@Override
	public List<Depot> getAllService() {
		List<Depot> list = new ArrayList<>();

		for (Job job : mVrpBuilder.getAddedJobs()) {
			if (job instanceof Service) {
				Service serv = (Service) job;
				
				boolean isNewDepot = true;
				for (Depot depot : list) {
					if (serv.getLocation().getId().equalsIgnoreCase(depot.getLocationID())) {
						isNewDepot = false;
						
						if (serv instanceof Pickup) depot.setPickupCapacity(serv.getSize().get(0));
						else if (serv instanceof Delivery) depot.setDeliverCapacity(serv.getSize().get(0));

						break;
					}
				}
				if (isNewDepot) {
					Depot depot = new Depot(serv.getLocation().getId(), serv.getName(),
							serv.getLocation().getCoordinate().getX(), serv.getLocation().getCoordinate().getY());
					
					if (serv instanceof Pickup) depot.setPickupCapacity(serv.getSize().get(0));
					else if (serv instanceof Delivery) depot.setDeliverCapacity(serv.getSize().get(0));
						
					list.add(depot);
				}				
			}
		}
		return list;
	}

	@Override
	public Depot getCenter() {
		Depot center = null;
		if (mVrpBuilder.getAddedVehicles().size() > 0) {
			Vehicle car = mVrpBuilder.getAddedVehicles().iterator().next();
			center = new Depot(car.getStartLocation().getId(), car.getId(),
					car.getStartLocation().getCoordinate().getX(), car.getStartLocation().getCoordinate().getY());
		}
		return center;
	}

	@Override
	public List<Route> solve(int iterations) {
		mVRP = mVrpBuilder.build();
		VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(mVRP);
    	algorithm.setMaxIterations(iterations);

    	Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();
    	VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

    	List<Route> showRoutes = new ArrayList<>();
    	for (VehicleRoute vehicleRoute : bestSolution.getRoutes()) {    		
    		Route showRoute = new Route();
    		
    		for (TourActivity act : vehicleRoute.getActivities()) {
    			Depot depot = null;
    			
    			if (showRoute.getLastDepot() != null && showRoute.getLastDepot().getLocationID().equals(act.getLocation().getId())) {
    				depot = showRoute.getLastDepot();
    			} else {
    				if (act instanceof TourActivity.JobActivity) {                	
                    	Job job = ((TourActivity.JobActivity) act).getJob();
    					depot = new Depot(act.getLocation().getId(), job.getName(),
    							act.getLocation().getCoordinate().getX(), act.getLocation().getCoordinate().getY());
                    } else {
    					depot = new Depot(act.getLocation().getId(), "-", act.getLocation().getCoordinate().getX(),
    							act.getLocation().getCoordinate().getY());
                    }
                    showRoute.addDepot(depot);
    			}
    			
    			if (act instanceof TourActivity.JobActivity) {
    				Job job = ((TourActivity.JobActivity) act).getJob();
    				if (job instanceof Pickup) depot.setPickupCapacity(job.getSize().get(0));
					else if (job instanceof Delivery) depot.setDeliverCapacity(job.getSize().get(0));
    			}
    		}
    		showRoutes.add(showRoute);
    	}
    	return showRoutes;
	}
}
