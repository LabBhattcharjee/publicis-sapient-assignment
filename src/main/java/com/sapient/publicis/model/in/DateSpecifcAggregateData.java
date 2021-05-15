package com.sapient.publicis.model.in;

import java.util.Comparator;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sapient.publicis.model.out.ListData;
import com.sapient.publicis.model.out.Weather;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "dateValue", "minTemp", "maxTemp", "warning" })
public class DateSpecifcAggregateData {
	public static final Comparator<DateSpecifcAggregateData> DATE_BASE_COMPARATOR = (o1, o2) -> o1.dateValue
			.compareTo(o2.dateValue);

	private double minTemp = -274;
	private double maxTemp = Double.MAX_VALUE;
	private final String dateValue;

	@JsonIgnore
	private boolean rainPredicted;

	public DateSpecifcAggregateData(final String dateOnly) {
		this.dateValue = dateOnly;
	}

	public void accept(final ListData value) {
		final List<Weather> weather = value.getWeather();
		if (!CollectionUtils.isEmpty(weather)) {
			this.rainPredicted = this.rainPredicted
					|| weather.stream().anyMatch(t -> "Rain".equalsIgnoreCase(t.getMain())
							|| (t.getDescription() != null && t.getDescription().contains("rain")));
		}

		minTemp = Math.min(minTemp, value.getMain().getTempMin());
		maxTemp = Math.min(maxTemp, value.getMain().getTempMax());
	}

	public void reconcile(final DateSpecifcAggregateData other) {
		minTemp = Math.min(minTemp, other.minTemp);
		maxTemp = Math.min(maxTemp, other.maxTemp);
	}

	public String getWarning() {
		if (maxTemp - 273.15 > 40) {
			return "Carry umbrella";
		}

		if (rainPredicted) {
			return "Use sunscreen lotion";
		}
		return "";
	}

}