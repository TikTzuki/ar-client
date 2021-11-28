package org.override.services;

import lombok.extern.log4j.Log4j2;
import org.override.core.SocketService;
import org.override.models.CreditModel;

import java.util.List;

@Log4j2
public class LearningProcessService {
    SocketService socketService = SocketService.getInstance();

    public String getProcess(String studentId) {
        return "90";
    }

    public List<CreditModel> getCredit(String studenId) {
        return getCredit(studenId, false, true);
    }

    public List<CreditModel> getCredit(String studenId, boolean includeAchieved, boolean includeNotAchieved) {
        return List.of(
                new CreditModel("841059", "Quản trị mạng", 3),
                new CreditModel("841113", "Phát triển phần mềm mã nguồn mở", 3)
                );
    }

    private static LearningProcessService INSTANCE;

    public static LearningProcessService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LearningProcessService();
        }
        return INSTANCE;
    }
}
