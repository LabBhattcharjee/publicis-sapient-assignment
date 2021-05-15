package json.deserializers;

import java.io.IOException;
import java.sql.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeStampDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
		try {
			return new Date(Long.parseLong(jsonParser.getText()));
		} catch (final Exception e) {
			log.error("Error while Getting value for {} value ::  {}", jsonParser.getCurrentName(),
					jsonParser.getText(), e);
		}
		return null;
	}
}
