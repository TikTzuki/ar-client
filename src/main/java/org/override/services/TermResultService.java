package org.override.services;

import lombok.extern.log4j.Log4j2;
import org.override.core.SocketService;
import org.override.core.models.HyperEntity;
import org.override.core.models.HyperException;
import org.override.core.models.HyperRoute;
import org.override.core.models.HyperStatus;
import org.override.models.TermResult;
import org.override.utils.Constants;
import org.override.utils.StringResources;
import org.override.utils.Utils;

import java.io.IOException;
import java.util.HashMap;

@Log4j2
public class TermResultService {
    SocketService socketService = SocketService.getInstance();
    StringResources stringResources = StringResources.getInstance();

    public TermResult getTermResult(String studenId) {
        HyperEntity response = null;
        try {
            response = socketService.sendRequest(
                    HyperRoute.GET_TERM_RESULT,
                    new HashMap<>() {{
                        put(Constants.STUDENT_ID, studenId);
                    }},
                    null
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response == null)
            return null;
        if (!response.status.equals(HyperStatus.OK)) {
            HyperException ex = HyperException.fromJson(response.body);
            Utils.showAlertInPlatform(stringResources.requestFailed(), ex.getLoc(), ex.getDetail());
            return null;
        }
        return TermResult.fromJson(response.body);
    }

    private static TermResultService INSTANCE;

    public static TermResultService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TermResultService();
        }
        return INSTANCE;
    }
}
