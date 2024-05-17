package com.reservation.frontend;

import com.reservation.domain.User;
import com.reservation.domain.response.UserResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Vaadin view for login page
 * accessed <a href="http://localhost:8080/login">...</a>
 */
@Route("login")
public class LoginView extends VerticalLayout {

    private transient WebClient webClient;

    /**
     * Constructor of LoginView containing web elements initialization
     */
    public LoginView() {
        webClient = (WebClient) VaadinSession.getCurrent().getAttribute("webClient");
        if (webClient == null) webClient = WebClient.create();
        VaadinSession.getCurrent().setAttribute("webClient", webClient);

        if (VaadinSession.getCurrent().getAttribute("user") != null) {
            getUI().ifPresent(ui -> ui.navigate("main"));
            return;
        }

        TextField nameField = new TextField("Name");
        TextField personalIdField = new TextField("Personal Code");

        Button loginButton = new Button("Login", event -> {
            String name = nameField.getValue();
            String personalId = personalIdField.getValue();

            if (name.isEmpty() || personalId.isEmpty()) {
                Notification.show("Please enter both name and personal ID!");
                return;
            }

            ResponseEntity<UserResponse> responseEntity = webClient.post()
                    .uri("http://localhost:8080/users/login")
                    .bodyValue(new User(name, personalId))
                    .retrieve()
                    .toEntity(UserResponse.class).block();

            if (responseEntity != null && responseEntity.getBody() != null) {
                if (responseEntity.getBody().getStatus() == HttpStatus.OK) {
                    VaadinSession.getCurrent().setAttribute("user", responseEntity.getBody().getBody());
                    getUI().ifPresent(ui -> ui.navigate("main"));
                } else {
                    Notification.show("Failed to login!", 5000, Notification.Position.MIDDLE);
                }
            }
        });

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("justify-content", "center");

        add(new H2("Login"));
        add(nameField, personalIdField, loginButton);
    }
}
