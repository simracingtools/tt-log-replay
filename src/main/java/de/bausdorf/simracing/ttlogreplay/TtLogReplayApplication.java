package de.bausdorf.simracing.ttlogreplay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
@Slf4j
public class TtLogReplayApplication implements ApplicationListener<ApplicationReadyEvent> {

    final LogReplay logReplay;

    static String[] appArgs;

    public TtLogReplayApplication(@Autowired LogReplay logReplay) {
        this.logReplay = logReplay;
    }

    public static void main(String[] args) {
        appArgs = args;
        SpringApplication.run(TtLogReplayApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (appArgs.length > 0) {
            boolean timed = false;
            String logname = appArgs[0];
            log.info("try to replay {}", logname);
            if(appArgs.length > 1){
                timed = Boolean.parseBoolean(appArgs[1]);
            }
            try(FileInputStream fis = new FileInputStream(logname)) {
                logReplay.replayLogfile(fis, timed);
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        } else {
            log.info("No logs to replay");
            System.exit(0);
        }

    }
}
