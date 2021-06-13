/**
 * 
 */
package com.sapient.publicis.service;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;

import com.sapient.publicis.model.in.DateSpecifcAggregateData;
import com.sapient.publicis.model.out.ListData;

import lombok.ToString;

/**
 * @author LabB
 *
 */
@ToString
public class DateWiseSummaryStatistics implements Consumer<ListData> {

	private final Map<String, DateSpecifcAggregateData> dateWiseMap = new TreeMap<>();	
	
    public Collection<DateSpecifcAggregateData> getDateWiseWeatherReport() {
		return dateWiseMap.values();
    }
	

	/**
	 * Records a new value into the summary information
	 *
	 * @param value the input value
	 */
	@Override
	public void accept(final ListData value) {
		DateSpecifcAggregateData aggregateData = dateWiseMap.get(value.getDateOnly());
		if (aggregateData == null) {
			aggregateData = new DateSpecifcAggregateData(value.getDateOnly());
			dateWiseMap.put(value.getDateOnly(), aggregateData);
		}

		aggregateData.accept(value);
	}

	/**
	 * Combines the state of another {@code DateWiseSummaryStatistics} into this
	 * one.
	 *
	 * @param other another {@code DateWiseSummaryStatistics}
	 * @throws NullPointerException if {@code other} is null
	 */
	public void combine(final DateWiseSummaryStatistics other) {
		final Set<Entry<String, DateSpecifcAggregateData>> otherMap = other.dateWiseMap.entrySet();
		for (final Entry<String, DateSpecifcAggregateData> entry : otherMap) {
			final DateSpecifcAggregateData aggregateData = this.dateWiseMap.get(entry.getKey());
			if (aggregateData == null) {
				this.dateWiseMap.put(entry.getKey(), entry.getValue());
			} else {
				aggregateData.reconcile(entry.getValue());
			}
		}

	}

}
