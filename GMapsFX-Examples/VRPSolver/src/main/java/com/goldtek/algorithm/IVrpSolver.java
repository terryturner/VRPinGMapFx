package com.goldtek.algorithm;

import java.util.List;

public interface IVrpSolver {
	void reset();
	void inputFrom(String path);
	void costFrom(String path);
	List<Depot> getAllService();
	Depot getCenter();
	List<Route> solve(int iterations);
}
