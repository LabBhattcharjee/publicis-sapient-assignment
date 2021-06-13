package com.sapient.publicis.model.in;

public interface DateSpecifcAggregate {

	double getMaxTemp();

	void setWarning(String message);

	boolean isRainPredicted();

}
