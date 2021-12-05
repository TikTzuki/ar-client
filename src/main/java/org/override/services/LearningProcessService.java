package org.override.services;

import javafx.application.Platform;
import lombok.extern.log4j.Log4j2;
import org.override.core.SocketService;
import org.override.core.models.*;
import org.override.models.CreditModel;
import org.override.models.ExampleModel;
import org.override.models.LearningProcessModel;
import org.override.utils.Constants;
import org.override.utils.StringResources;
import org.override.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Log4j2
public class LearningProcessService {
    SocketService socketService = SocketService.getInstance();
    StringResources stringResources = StringResources.getInstance();

    public LearningProcessModel getProcess(String studentId, boolean includeAchieved, boolean includeNotAchieved) {
        try {
            HyperEntity response = socketService.sendRequest(
                    HyperRoute.GET_LEARNING_PROCESS,
                    new HashMap<>() {{
                        put(Constants.STUDENT_ID, studentId);
                        put(Constants.INCLUDE_ACHIEVED, String.valueOf(includeAchieved));
                        put(Constants.INCLUDE_NOT_ACHIEVED, String.valueOf(includeNotAchieved));
                    }},
                    null
            );
            if (!response.status.equals(HyperStatus.OK)) {
                HyperException ex = HyperException.fromJson(response.body);
                Utils.showAlert(stringResources.requestFailed(), ex.getLoc(), ex.getDetail());
                return null;
            }
            return LearningProcessModel.fromJson(response.body);
        } catch (IOException e) {
            e.printStackTrace();
            Utils.showAlertInPlatform(stringResources.requestFailed(), null, e.getMessage());
            return null;
        }
    }

    private static LearningProcessService INSTANCE;

    public static LearningProcessService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LearningProcessService();
        }
        return INSTANCE;
    }
}
