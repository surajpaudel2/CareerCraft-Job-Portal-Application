package com.suraj.careercraft.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EpochMillisToLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        try {
            // Check if the input is a number (epoch milliseconds)
            if (parser.currentToken().isNumeric()) {
                long epochMillis = parser.getLongValue();
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault());
            } else if (parser.currentToken().isScalarValue()) {
                // Handle ISO-8601 string with potential 'Z'
                String isoDate = parser.getText();
                if (isoDate.endsWith("Z")) {
                    // Remove the 'Z' and parse as LocalDateTime
                    isoDate = isoDate.substring(0, isoDate.length() - 1);
                    return LocalDateTime.parse(isoDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
                }
                // If no 'Z', parse directly
                return LocalDateTime.parse(isoDate);
            } else {
                throw new IOException("Unsupported format for LocalDateTime: " + parser.getCurrentToken());
            }
        } catch (DateTimeParseException e) {
            throw new IOException("Failed to parse date: " + parser.getText(), e);
        }
    }
}
