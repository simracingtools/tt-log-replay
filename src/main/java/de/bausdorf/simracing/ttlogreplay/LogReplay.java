package de.bausdorf.simracing.ttlogreplay;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalTime;

@Slf4j
@Component
public class LogReplay {

    RestTemplate restTemplate;

    @Value("${postUrl}")
    @Setter
    @Getter
    private String postUrl;

    @Setter
    private boolean stopOnError;

    public LogReplay() {
        restTemplate = new RestTemplate();
    }
    public void replayLogfile(InputStream logStream, boolean timed) {
        try (BufferedLogReader logReader = new BufferedLogReader(new InputStreamReader(logStream))) {
            LocalTime replayStart = null;
            while( logReader.ready() ) {
                TimedMessage timedMessage = logReader.readMessage();
                if(timed) {
                    if( replayStart == null ) {
                        replayStart = timedMessage.getTimestamp();
                    } else {
                        Duration waitTime = Duration.between(replayStart, timedMessage.getTimestamp());
                        log.info("Sleeping {} sec to replay next message", ((double)waitTime.toMillis()) / 1000);
                        Thread.sleep(waitTime.toMillis());
                        replayStart = timedMessage.getTimestamp();
                    }
                }
                try {
                    ResponseEntity<String> response = restTemplate.postForEntity(postUrl, timedMessage.getMessage(), String.class);
                    log.info("Message sent: " + response.getBody());
                } catch (Exception e) {
                    log.warn(timedMessage.getMessage().getType().name() + ": " + e.getMessage());
                    if (stopOnError) {
                        break;
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }

    }

}
