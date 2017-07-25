package com.goldtek.algorithm;

import java.util.List;

public interface IVrpSolver {
	void reset();
	void inputFrom(String path);
	void costFrom(String path);
	void costFrom(List<Cost> list);
	List<Depot> getAllService();
	Depot getCenter(int route);
	List<Route> solve(int iterations);
}
