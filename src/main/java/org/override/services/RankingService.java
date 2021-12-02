package org.override.services;

import lombok.extern.log4j.Log4j2;
import org.override.core.SocketService;
import org.override.core.models.HyperEntity;
import org.override.core.models.HyperException;
import org.override.core.models.HyperRoute;
import org.override.core.models.HyperStatus;
import org.override.models.PagingModel;
import org.override.models.StudentModel;
import org.override.utils.Constants;
import org.override.utils.StringResources;
import org.override.utils.Utils;

import java.io.IOException;
import java.util.HashMap;

@Log4j2
public class RankingService {
    SocketService socketService = SocketService.getInstance();
    StringResources stringResources = StringResources.getInstance();

    public PagingModel<StudentModel> getRanking(String studentId, boolean includeCourse, boolean includeSubject, boolean includeSpeciality) {
        try {
            HyperEntity response = socketService.sendRequest(
                    HyperRoute.GET_RANKING,
                    new HashMap<>() {{
                        put(Constants.STUDENT_ID, studentId);
                        put(Constants.INCLUDE_COURSE, String.valueOf(includeCourse));
                        put(Constants.INCLUDE_SUBJECT, String.valueOf(includeSubject));
                        put(Constants.INCLUDE_SPECIALITY, String.valueOf(includeSpeciality));
                    }},
                    null
            );
            if (!response.status.equals(HyperStatus.OK)) {
                HyperException ex = HyperException.fromJson(response.body);
                Utils.showAlert(stringResources.requestFailed(), ex.getLoc(), ex.getDetail());
                return null;
            }
            return PagingModel.fromGson(response.body, StudentModel.class);
        } catch (IOException e) {
            e.printStackTrace();
            Utils.showAlert(stringResources.requestFailed(), null, e.getMessage());
        }
        return null;
    }

    private static RankingService INSTANCE;

    public static RankingService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RankingService();
        }
        return INSTANCE;
    }
}
