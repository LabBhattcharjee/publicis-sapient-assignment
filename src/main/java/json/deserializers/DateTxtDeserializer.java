package json.deserializers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateTxtDeserializer extends JsonDeserializer<Date> {

	public DateTxtDeserializer() {
		System.out.println("DateTxtDeserializer.DateTxtDeserializer()");
	}

	private static final ThreadLocal<DateFormat> FORMAT = ThreadLocal
			.withInitial(() -> new SimpleDateFormat("yyyy-MM-DD HH:mm:ss"));

	@Override
	public Date deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
		try {
			return FORMAT.get().parse(jsonParser.getText());
		} catch (final Exception e) {
			log.error("Error while Getting value for {} value ::  {}", jsonParser.getCurrentName(),
					jsonParser.getText(), e);
		}
		return null;
	}
}
