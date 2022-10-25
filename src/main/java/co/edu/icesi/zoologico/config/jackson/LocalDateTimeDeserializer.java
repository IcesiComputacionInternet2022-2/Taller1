package co.edu.icesi.zoologico.config.jackson;



import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    public LocalDateTimeDeserializer(){
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String text = p.getText();
        try{
            System.out.println(text);


            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime result = LocalDateTime.parse(text, format);

            System.out.println("DESERIALIZACIÓN EXITOSA");
            return result;

        }catch (DateTimeException dateTimeException){
            System.out.println("DESERIALIZACIÓN ERRONEA");

            System.out.println(dateTimeException.getMessage());
            throw new InvalidFormatException(p ,"", text, LocalDateTime.class);
        }
    }
}