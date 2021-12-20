package org.override.services;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.override.AcademicResultsApplication;
import org.override.core.SocketService;
import org.override.core.configs.Appconfig;
import org.override.core.models.HyperEntity;
import org.override.core.models.HyperException;
import org.override.core.models.HyperRoute;
import org.override.core.models.HyperStatus;
import org.override.models.AuthenticationModel;
import org.override.models.UserModel;
import org.override.utils.RSAUtil;
import org.override.utils.SecurityUtil;
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
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;
import java.util.Optional;


@Log4j2
@Data
@NoArgsConstructor
public class UserService {
    private UserModel currentUser;

    public void requiredLogin() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login, sweetie!!!");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        gridPane.add(emailField, 0, 0);
        gridPane.add(passwordField, 0, 1);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        Platform.runLater(emailField::requestFocus);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(emailField.getText(), passwordField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresentOrElse(pair -> {
            login(pair.getKey(), pair.getValue());
            if (currentUser == null)
                requiredLogin();
        }, () -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void login(String email, String password) {
        try {
            HyperEntity rawRes = sendRequest(
                    HyperEntity.request(
                            HyperRoute.LOGIN, null, new AuthenticationModel(email, password)
                    )
            );
            if (rawRes != null && rawRes.status.equals(HyperStatus.OK)) {
                currentUser = UserModel.fromJson(rawRes.body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        currentUser = null;
    }

//    public HyperEntity handleLogin(HyperEntity entity) {
//        try {
//            AuthenticationModel authenticationModel = AuthenticationModel.fromJson(entity.body);
//
//            String hasedPassword = DigestUtils.sha256Hex(authenticationModel.password + salt);
//            Example<UserModel> example = Example.of(new UserModel(authenticationModel.email, hasedPassword));
//            Optional<UserModel> userOpt = userRepository.findOne(example);
//            if (userOpt.isPresent()) {
//                UserModel user = userOpt.get();
//                return HyperEntity.ok(
//                        new AuthenticationModel.AuthenticationSuccess(user.getPublicKey())
//                );
//            } else
//                return HyperEntity.notFound(
//                        new HyperException(HyperException.NOT_FOUND, "body -> email", "email or password invalid")
//                );
//        } catch (JsonSyntaxException e) {
//            e.printStackTrace();
//        }
//        return HyperEntity.unprocessableEntity(
//                new HyperException(HyperException.BAD_REQUEST, "", "internal error")
//        );
//    }
//
//    public HyperEntity auth(HyperEntity hyperEntity) {
//        if (hyperEntity.route.equals(HyperRoute.LOGIN))
//            return hyperEntity;
//        String authorization = hyperEntity.headers.get("Authorization");
//
//        if (!authorization.matches(".+:.+")) {
//            return HyperEntity.unauthorized(
//                    new HyperException(HyperException.UNAUTHORIZED)
//            );
//        }
//
//        String[] authorizations = authorization.split(":");
//        String userIdString = authorizations[0];
//        String ivString = authorizations[1];
//
//        if (!NumberUtils.isParsable(userIdString))
//            return HyperEntity.unauthorized(new HyperException(HyperException.UNAUTHORIZED));
//
//        Integer userId = Integer.parseInt(userIdString);
//        Optional<UserModel> userOpt = userRepository.findById(userId);
//        if (userOpt.isPresent()) {
//            try {
//                UserModel user = userOpt.get();
//                String key = SecurityUtil.generateKey(user.getPublicKey(), user.getEmail());
//                hyperEntity.body = SecurityUtil.decrypt(
//                        hyperEntity.body,
//                        key,
//                        ivString
//                );
//
//                log.info(hyperEntity.body);
//
//                return hyperEntity;
//            } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException ignore) {
//            }
//        }
//        return HyperEntity.unauthorized(
//                new HyperException(HyperException.UNAUTHORIZED)
//        );
//
//    }

    public HyperEntity encryptRequest(
            HyperEntity request
    ) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        log.info("into encrypt");
        if (request.route.equals(HyperRoute.LOGIN)) {
            log.info("request logn");
            return request;
        }
        String ivString = SecurityUtil.generateIv();
        request.headers.put("Authorization", "%s:%s".formatted(currentUser.id, ivString));
        String key = SecurityUtil.generateKey(currentUser.getPublicKey(), currentUser.getEmail());
        log.info("encrypt requets. key: %s iv: %s".formatted(key, ivString));
        if (request.body != null)
            request.body = SecurityUtil.encrypt(
                    request.body,
                    key,
                    ivString
            );
        log.info(request);
        return request;
    }

    public HyperEntity descryptResponse(
            HyperEntity response, HyperEntity request
    ) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String ivString = request.headers.get("Authorization").split(":")[1];
        if (request.route.equals(HyperRoute.LOGIN) || !response.status.equals(HyperStatus.OK))
            return response;
        String key = SecurityUtil.generateKey(currentUser.getPublicKey(), currentUser.getEmail());
        response.body = SecurityUtil.decrypt(
                response.body,
                key,
                ivString
        );
        return response;
    }

    Appconfig configs = Appconfig.getInstance();
    private static final Gson gson = new Gson();
    final StringResources stringResources = StringResources.getInstance();

    public HyperEntity sendRequest(HyperEntity request) throws IOException {
        try {
            log.info(request);
            Socket socket = new Socket(configs.getHost(), configs.getPort());
            log.info(request.headers);
            socket.setSoTimeout(30_000);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            log.info("before write");
            out.writeObject(gson.toJson(request));
            log.info("after write");
            HyperEntity response = gson.fromJson((String) in.readObject(), HyperEntity.class);
            log.info(response.body);

            out.close();
            in.close();
            socket.close();
            return response;
        } catch (ClassNotFoundException e) {
            Utils.showAlert(stringResources.requestFailed(), "", e.getMessage());
        } catch (ConnectException e) {
            Utils.showAlert(stringResources.requestFailed(), "", "cant connect to server");
        }
        return null;
    }

    private static final UserService INSTANCE = new UserService();

    public static UserService getInstance() {
        return INSTANCE;
    }
}
