/**
 * 
 */
package com.sapient.publicis.service;

import java.util.Date;
import java.util.function.Consumer;

import com.sapient.publicis.model.out.ListData;

import lombok.ToString;

/**
 * @author LabB
 *
 */
@ToString
public class DateSummaryStatistics implements Consumer<ListData> {

	private long count;
	private Date minDate = new Date(System.currentTimeMillis() + 1000);
	private Date maxDate = new Date(0);

	/**
	 * Records a new value into the summary information
	 *
	 * @param value the input value
	 */
	@Override
	public void accept(final ListData value) {
		++count;
		if (value.getDtTxt().compareTo(minDate) < 0) {
			minDate = value.getDtTxt();
		}
		if (value.getDtTxt().compareTo(maxDate) > 0) {
			maxDate = value.getDtTxt();
		}
	}

	/**
	 * Combines the state of another {@code IntSummaryStatistics} into this one.
	 *
	 * @param other another {@code IntSummaryStatistics}
	 * @throws NullPointerException if {@code other} is null
	 */
	public void combine(final DateSummaryStatistics other) {
		count += other.count;
		if (other.minDate.compareTo(minDate) < 0) {
			minDate = other.minDate;
		}
		if (other.maxDate.compareTo(maxDate) > 0) {
			maxDate = other.maxDate;
		}
	}

}
