package com.sapient.publicis.model.in;

import java.util.Comparator;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sapient.publicis.model.out.ListData;
import com.sapient.publicis.model.out.MainDetails;
import com.sapient.publicis.model.out.Weather;
import com.sapient.publicis.util.WeatherServiceConstants;
import com.sapient.publicis.util.WeatherServiceUtility;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "dateValue", "minTemp", "maxTemp", "warning" })
public class DateSpecifcAggregateData {
	public static final Comparator<DateSpecifcAggregateData> DATE_BASE_COMPARATOR = (o1, o2) -> o1.dateValue
			.compareTo(o2.dateValue);

	@JsonIgnore
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private double minTemp = 1000;

	@JsonIgnore
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private double maxTemp = 0;

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

		final MainDetails mainInfo = value.getMain();
		minTemp = Math.min(minTemp, mainInfo.getTempMin());
		maxTemp = Math.max(maxTemp, mainInfo.getTempMax());
	}

	public void reconcile(final DateSpecifcAggregateData other) {
		minTemp = Math.min(minTemp, other.minTemp);
		maxTemp = Math.max(maxTemp, other.maxTemp);
	}

	public String getWarning() {
		if (WeatherServiceUtility.kelvinToDegreeCelcius(maxTemp) > 40) {
			return WeatherServiceConstants.getHeatWarningMessage();
		}

		if (rainPredicted) {
			return WeatherServiceConstants.getRainWarningMessage();
		}
		return "";
	}

	// \u00B0 Unicode of degree
	public String getMinimumTemperature() {
		return String.format("%.3f (%.3f \u2103)", minTemp, WeatherServiceUtility.kelvinToDegreeCelcius(minTemp));
	}

	public String getMaximumTemperature() {
		return String.format("%.3f (%.3f \u2103)", maxTemp, WeatherServiceUtility.kelvinToDegreeCelcius(maxTemp));
	}

}