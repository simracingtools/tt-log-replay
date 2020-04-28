package de.bausdorf.simracing.ttlogreplay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientMessage {

    private MessageType type;
    private String version;
    private String sessionId;
    private String teamId;
    private String clientId;
    private Map<String, Object> payload;
}
