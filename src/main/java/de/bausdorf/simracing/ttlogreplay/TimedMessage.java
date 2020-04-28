package de.bausdorf.simracing.ttlogreplay;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Builder
public class TimedMessage {
    private final LocalTime timestamp;
    private final ClientMessage message;
}
