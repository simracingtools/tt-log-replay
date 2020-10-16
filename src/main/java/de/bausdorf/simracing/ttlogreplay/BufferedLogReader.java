package de.bausdorf.simracing.ttlogreplay;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class BufferedLogReader extends BufferedReader {

    ObjectMapper mapper;

    public BufferedLogReader(Reader in) {
        super(in);
        mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    @Override
    public String readLine() throws IOException {
        String line = super.readLine();
        //line = line.replaceAll("\\\\", "");
        if (line.endsWith("\"")) {
            return line.substring(1, line.length() - 1);
        }
        return line;
    }

    public TimedMessage readMessage() throws IOException {
        String line = this.readLine();
        String[] parts = line.split("\\$");
        ClientMessage msg = mapper.readValue(parts[1], ClientMessage.class);
        return TimedMessage.builder()
                //time format: 2020-04-04 15:34:09,530
                .timestamp(LocalTime.parse(parts[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS")))
                .message(msg)
                .build();
    }
}
