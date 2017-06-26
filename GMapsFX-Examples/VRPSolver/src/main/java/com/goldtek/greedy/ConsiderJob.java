package com.goldtek.greedy;

import com.graphhopper.jsprit.core.problem.job.Job;

public class ConsiderJob {
	private final Job mJob;
	private boolean isConsiderable = true;
	
	public ConsiderJob(Job job) {
		mJob = job;
	}
	
	public Job get() { return mJob; }
	
	public void isConsiderable(boolean consider) { isConsiderable = consider; }

    public boolean isConsiderable() { return isConsiderable; }
}
