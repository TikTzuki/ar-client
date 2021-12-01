package org.override.services;

import lombok.extern.log4j.Log4j2;
import org.override.core.SocketService;

@Log4j2
public class TermResultService {
    SocketService socketService = SocketService.getInstance();

    private static TermResultService INSTANCE;
    public static TermResultService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TermResultService();
        }
        return INSTANCE;
    }
}
