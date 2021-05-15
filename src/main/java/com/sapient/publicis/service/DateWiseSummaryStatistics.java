/**
 * 
 */
package com.sapient.publicis.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sapient.publicis.model.in.DateSpecifcAggregateData;
import com.sapient.publicis.model.out.ListData;

import lombok.ToString;

/**
 * @author LabB
 *
 */
@ToString
public class DateWiseSummaryStatistics implements Consumer<ListData> {

	@JsonIgnore
	private final Map<String, DateSpecifcAggregateData> dateWiseMap = new TreeMap<>();

	public Set<DateSpecifcAggregateData> getDateWiseWeatherReport() {
		final Set<DateSpecifcAggregateData> treeSet = new TreeSet<>(DateSpecifcAggregateData.DATE_BASE_COMPARATOR);
		treeSet.addAll(dateWiseMap.values());
		return treeSet;
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
