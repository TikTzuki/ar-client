package org.override.services;

import lombok.extern.log4j.Log4j2;
import org.override.core.SocketService;
import org.override.models.TermResult;
import org.override.utils.FakeData;

import java.util.List;

@Log4j2
public class RankingService {
    SocketService socketService = SocketService.getInstance();

    public List<TermResult> getRanking(String studentId) {
        return List.of(
                FakeData.getTermResult("123"),
                FakeData.getTermResult("456")
        );
    }

    private static RankingService INSTANCE;
    public static RankingService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RankingService();
        }
        return INSTANCE;
    }
}
