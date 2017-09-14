package com.goldtek.main.routeguide;

public class RouteColor {
	private static RouteColor sInstance = new RouteColor();

	public static RouteColor getInstance() {
		return sInstance;
	}

	public RouteColor() {

	}

	private String[] colors = { "#006600", "#ff0000", "#6600ff", "#ff9900", "#800000", "#ff00ff", "#0099cc", "#ff4000",
			"#ff8000", "#00ff40", "#00bfff", "#0080ff", "#8000ff", "#00ff80", "#339933", "#009900", "#996633",
			"#cc6600", "#660066", "#003300"};

	public String get(int index) {
		if (index < 0)
			index = 0;
		else if (index >= colors.length)
			index = index % colors.length;
		return colors[index];
	}

}
