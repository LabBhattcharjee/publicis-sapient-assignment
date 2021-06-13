package com.sapient.publicis.model.in;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sapient.publicis.model.out.ListData;
import com.sapient.publicis.model.out.MainDetails;
import com.sapient.publicis.model.out.Weather;
import com.sapient.publicis.util.WeatherServiceUtility;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "dateValue", "minTemp", "maxTemp", "warning" })
public class DateSpecifcAggregateData2  implements  DateSpecifcAggregate{

	@JsonIgnore
//	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private double minTemp = 1000;

	@JsonIgnore
//	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private double maxTemp = 0;

	@JsonIgnore
	private boolean rainPredicted;

	private String minimumTemperature;

	private String maximumTemperature;
	
	private String warning;
	

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

	public DateSpecifcAggregateData2 reconcile(final DateSpecifcAggregateData2 other) {
		minTemp = Math.min(minTemp, other.minTemp);
		maxTemp = Math.max(maxTemp, other.maxTemp);
		return this;
	}


	// \u00B0 Unicode of degree

	public DateSpecifcAggregateData2 finisher() {
		this.minimumTemperature = String.format("%.3f (%.3f \u2103)", minTemp,
				WeatherServiceUtility.kelvinToDegreeCelcius(minTemp));
		this.maximumTemperature = String.format("%.3f (%.3f \u2103)", maxTemp,
				WeatherServiceUtility.kelvinToDegreeCelcius(maxTemp));
		return this;
	}


}