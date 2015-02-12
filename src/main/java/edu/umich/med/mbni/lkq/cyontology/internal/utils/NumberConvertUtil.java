package edu.umich.med.mbni.lkq.cyontology.internal.utils;

import java.awt.Color;

public class NumberConvertUtil {

	private double min;
	private double max;
	private double factor;
	
	public NumberConvertUtil(double min, double max) {
		this.min = min;
		this.max = max;
		this.factor = 255 / (max - min);
	}
	
	public double convertValue(double origin) {
		return origin * factor;
	}
	
	public double getMin() {
		return min;
	}
	
	public double getMax() {
		return max;
	}
	
	public Color convertToColor(double value) {
		Double convertedValue = convertValue(value - min);
		Color color = new Color(convertedValue.intValue(), 0, 0);
		return color;
	}
}