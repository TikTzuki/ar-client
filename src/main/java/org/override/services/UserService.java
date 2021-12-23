package org.override.services;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Pair;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.override.AcademicResultsApplication;
import org.override.core.configs.Appconfig;
import org.override.core.models.HyperEntity;
import org.override.core.models.HyperRoute;
import org.override.core.models.HyperStatus;
import org.override.models.AuthenticationModel;
import org.override.models.UserModel;
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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


@Log4j2
@Data
@NoArgsConstructor
public class UserService {
    private UserModel currentUser;

    public void requiredLogin() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.getDialogPane().getStylesheets().add(AcademicResultsApplication.class.getResource("css/dialog.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("login-dialog");
        dialog.getDialogPane().setBackground(new Background(new BackgroundImage(
                new Image(AcademicResultsApplication.class.getResource("images/login-bg.jpg").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)
        ));
        dialog.setTitle("Login, sweetie!!!");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType);

        VBox vBox = new VBox();
        vBox.setSpacing(40);
        vBox.setAlignment(Pos.BOTTOM_CENTER);

        TextField emailField = new TextField();
        emailField.setPrefHeight(40);
        emailField.setPromptText("Email");
        TextField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        passwordField.setPromptText("Password");

        vBox.getChildren().addAll(emailField, passwordField);

        dialog.getDialogPane().setContent(vBox);

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
            if (rawRes != null && rawRes.status.equals(HyperStatus.OK))
                currentUser = UserModel.fromJson(rawRes.body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        currentUser = null;
    }


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

            out.close();
            in.close();
            socket.close();
            return response;
        } catch (ClassNotFoundException e) {
            Utils.showAlert(stringResources.requestFailed(), "", e.getMessage());
        } catch (ConnectException e) {
            Utils.showAlert(stringResources.requestFailed(), "", stringResources.cannotConnectToServer());
        }
        return null;
    }

    private static final UserService INSTANCE = new UserService();

    public static UserService getInstance() {
        return INSTANCE;
    }
}
