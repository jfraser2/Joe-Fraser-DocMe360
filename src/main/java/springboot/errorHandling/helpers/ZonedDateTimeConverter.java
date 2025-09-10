package springboot.errorHandling.helpers;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import springboot.enums.ZonedDateTimeEnum;

public class ZonedDateTimeConverter
	extends JsonSerializer<ZonedDateTime>
{

	@Override
	public void serialize(ZonedDateTime value, JsonGenerator jgen, SerializerProvider serializers)
		throws IOException
	{
		if (null != value)
		{
//            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern(ZonedDateTimeEnum.INSTANCE.DATE_FORMAT3);
//            String zonedDateTimeAsString = value.format(formatter1);
			String zonedDateTimeAsString = ZonedDateTimeEnum.INSTANCE.writeDateString(value, ZonedDateTimeEnum.INSTANCE.DATE_FORMAT3);
            
			jgen.writeString(zonedDateTimeAsString);
		}
	}

}
