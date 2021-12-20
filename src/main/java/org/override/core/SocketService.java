package org.override.core;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.override.core.configs.Appconfig;
import org.override.core.models.HyperBody;
import org.override.core.models.HyperEntity;
import org.override.models.UserModel;
import org.override.services.UserService;
import org.override.utils.StringResources;
import org.override.utils.Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@Log4j2
@AllArgsConstructor
@NoArgsConstructor
public class SocketService {
    Appconfig configs = Appconfig.getInstance();
    StringResources stringResources = StringResources.getInstance();
    UserService userService = UserService.getInstance();
    private static final Gson gson = new Gson();

    public HyperEntity sendRequest(HyperEntity request) throws IOException {
        try {
            while (userService.getCurrentUser() == null)
                userService.requiredLogin();

            request = userService.encryptRequest(request);
            log.info("after encrypt request");
            Socket socket = new Socket(configs.getHost(), configs.getPort());
            log.info(request.headers);
            socket.setSoTimeout(30_000);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            log.info("before write");
            out.writeObject(gson.toJson(request));
            log.info("after write");
            HyperEntity response = gson.fromJson((String) in.readObject(), HyperEntity.class);
            log.info(response);
            response = userService.descryptResponse(response, request);
            log.info(response);

            out.close();
            in.close();
            socket.close();
            return response;
        } catch (ClassNotFoundException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeySpecException | InvalidKeyException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            Utils.showAlertInPlatform(stringResources.requestTimeOut(), null, stringResources.requestTimeOut());
        } catch (ConnectException e) {
            Utils.showAlert(stringResources.requestFailed(), null, stringResources.cannotConnectToServer());
        } catch (Exception ignore) {
        }
        return null;
    }

    public HyperEntity sendRequest(String route, Map<String, String> headers, HyperBody body) throws IOException {
        HyperEntity request = HyperEntity.request(route, headers, body);
        return sendRequest(request);
    }

    private static final SocketService INSTANCE = new SocketService();

    public static SocketService getInstance() {
        return INSTANCE;
    }
}
