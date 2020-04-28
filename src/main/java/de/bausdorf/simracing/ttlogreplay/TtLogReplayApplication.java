package de.bausdorf.simracing.ttlogreplay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
@Slf4j
public class TtLogReplayApplication implements ApplicationRunner {

    final LogReplay logReplay;

    public TtLogReplayApplication(@Autowired LogReplay logReplay) {
        this.logReplay = logReplay;
    }

    public static void main(String[] args) {
        SpringApplication springApplication =
                new SpringApplicationBuilder()
                        .web(WebApplicationType.NONE)
                        .build();
        springApplication.run(TtLogReplayApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!args.getNonOptionArgs().isEmpty()) {
            boolean timed = false;
            String logname = args.getNonOptionArgs().get(0);
            if (args.containsOption("timed")){
                timed = true;
                log.info("using log timestamps in replay");
            } else {
                log.info("doing fast replay");
            }
            if (args.containsOption("url")) {
                if (!args.getOptionValues("url").isEmpty()) {
                    logReplay.setPostUrl(args.getOptionValues("url").get(0));
                } else {
                    log.error("No url given on option --url");
                    System.exit(1);
                }
            }
            if (args.containsOption("stop")) {
                logReplay.setStopOnError(true);
            }
            log.info("try to replay {} to {}", logname, logReplay.getPostUrl());
            try(FileInputStream fis = new FileInputStream(logname)) {
                logReplay.replayLogfile(fis, timed);
            } catch (IOException e) {
                log.info(e.getMessage());
            }
            System.exit(0);
        } else {
            log.info("No logs to replay");
            System.exit(0);
        }
    }
}
