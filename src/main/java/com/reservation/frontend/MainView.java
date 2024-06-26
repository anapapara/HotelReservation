package com.reservation.frontend;

import com.reservation.domain.Hotel;
import com.reservation.domain.Reservation;
import com.reservation.domain.Room;
import com.reservation.domain.User;
import com.reservation.domain.dto.ReservationDTO;
import com.reservation.domain.response.ReservationResponse;
import com.reservation.domain.response.StringResponse;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import elemental.json.JsonArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Vaadin view for login page
 * accessed <a href="http://localhost:8080/main">...</a>
 */
@Route("main")
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    private Grid<Hotel> hotelGrid;
    private Grid<Room> roomGrid;
    private Grid<Reservation> reservationGrid;
    private transient WebClient webClient;
    private User loggedUser;

    private TextField rangeField;

    private Double currentLatitude = null;
    private Double currentLongitude = null;

    /**
     * Constructor containing web elements initialization
     */
    public MainView() {
        webClient = (WebClient) VaadinSession.getCurrent().getAttribute("webClient");
        if (webClient == null) webClient = WebClient.create();
        if (VaadinSession.getCurrent().getAttribute("user") == null) {
            getUI().ifPresent(ui -> ui.navigate("login"));
        } else {
            loggedUser = (User) VaadinSession.getCurrent().getAttribute("user");

            createHotelGrid();
            createRoomGrid();
            createReservationGrid();

            Button logoutButton = new Button("Logout", click -> {
                VaadinSession.getCurrent().setAttribute("user", null);
                getUI().ifPresent(ui -> ui.navigate("login"));
            });
            H1 title = new H1("Welcome to the room reservation  system");
            HorizontalLayout header = new HorizontalLayout(title, logoutButton);
            header.setWidthFull();
            header.setAlignItems(Alignment.CENTER);
            header.expand(title);
            header.setJustifyContentMode(JustifyContentMode.BETWEEN);

            add(header);
            add(arrangeLayouts());
            setSizeFull();

            loadHotels();
            loadReservations();
        }
    }

    /**
     * Arrange elements in layouts
     *
     * @return the main layout for entire page
     */
    private HorizontalLayout arrangeLayouts() {
        rangeField = new TextField();
        rangeField.setPlaceholder("Enter range in kilometers");
        rangeField.setWidth("250px");

        Button applyFilterButton = new Button("Search hotels in range", click -> loadHotelsInRange());
        Button removeFilterButton = new Button("Remove filtering", click -> {
            loadHotels();
            rangeField.clear();
        });

        HorizontalLayout rangeLayout = new HorizontalLayout(rangeField, applyFilterButton, removeFilterButton);
        rangeLayout.setAlignItems(Alignment.BASELINE);

        VerticalLayout hotelRoomLayout = new VerticalLayout(new H3("Hotels"), rangeLayout,
                hotelGrid, new H3("Available rooms"), roomGrid);
        hotelRoomLayout.setSizeFull();

        VerticalLayout reservationLayout = new VerticalLayout(new H3("Your reservations"), reservationGrid);
        reservationLayout.setSizeFull();

        HorizontalLayout mainLayout = new HorizontalLayout(hotelRoomLayout, reservationLayout);
        mainLayout.setSizeFull();
        mainLayout.setFlexGrow(1, hotelRoomLayout);
        mainLayout.setFlexGrow(1, reservationLayout);


        return mainLayout;
    }

    /**
     * Create the hotels grid
     */
    private void createHotelGrid() {
        hotelGrid = new Grid<>(Hotel.class);
        hotelGrid.setColumns("name");

        hotelGrid.addSelectionListener(event -> event.getFirstSelectedItem().ifPresent(hotel -> {
            List<Room> rooms = getRooms(hotel.getId());
            roomGrid.setItems(rooms);
        }));
    }

    /**
     * Create the rooms grid
     */
    private void createRoomGrid() {
        roomGrid = new Grid<>(Room.class);
        roomGrid.removeAllColumns();
        roomGrid.addColumn(Room::getRoomNumber).setHeader("Room Number");
        roomGrid.addColumn(room -> mapRoomType(room.getType())).setHeader("Type");
        roomGrid.addColumn(Room::getPrice).setHeader("Price");

        roomGrid.addItemClickListener(event -> openReservationDialog());
    }

    /**
     * Maps room types from integer to string
     *
     * @param type Integer type to be mapped to string
     * @return String referring mapped value
     */

    private String mapRoomType(int type) {
        return switch (type) {
            case 1 -> "Single room";
            case 2 -> "Double room";
            case 3 -> "Suite room";
            case 4 -> "Matrimonial room";
            default -> "Unknown";
        };
    }

    /**
     * Create the reservations grid
     */
    private void createReservationGrid() {
        reservationGrid = new Grid<>(Reservation.class);
        reservationGrid.removeAllColumns();
        reservationGrid.addColumn(reservation -> reservation.getHotel().getName()).setHeader("Hotel name");
        reservationGrid.addColumn(reservation -> reservation.getRoom().getRoomNumber()).setHeader("Room number");
        reservationGrid.addColumn(reservation -> formatDate(reservation.getStartDate())).setHeader("Start date");
        reservationGrid.addColumn(reservation -> formatDate(reservation.getEndDate())).setHeader("End date");
        reservationGrid.addColumn(Reservation::getFeedback).setHeader("Feedback");


        reservationGrid.addComponentColumn(reservation -> new Button("Cancel", click -> cancelReservation(reservation)));
        reservationGrid.addComponentColumn(reservation -> new Button("Add feedback", click -> openUpdateDialog(reservation)));

        reservationGrid.addItemClickListener(event -> {
            Reservation reservation = event.getItem();
            openReservationDetailsDialog(reservation);
        });
    }

    /**
     * Format a date using 'dd MMMM yyyy' formatter
     *
     * @param date The date to be formatted
     * @return Formatted date
     */
    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return dateFormat.format(date);
    }

    /**
     * Populate the hotels grid
     */
    private void loadHotels() {
        List<Hotel> hotels = webClient.get()
                .uri("http://localhost:8080/hotels")
                .retrieve()
                .bodyToFlux(Hotel.class)
                .collectList()
                .block();
        hotelGrid.setItems(hotels);
    }

    /**
     * Populate the rooms grid
     */
    private List<Room> getRooms(Integer hotelId) {
        return webClient.get()
                .uri("http://localhost:8080/rooms/" + hotelId)
                .retrieve()
                .bodyToFlux(Room.class)
                .collectList()
                .block();
    }

    /**
     * Populate the reservations grid
     */
    private void loadReservations() {
        List<Reservation> reservations = webClient.get()
                .uri("http://localhost:8080/reservations/user/" + loggedUser.getId())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Reservation.class)
                .collectList()
                .block();
        reservationGrid.setItems(reservations);
    }

    /**
     * Populate the filtered grid by range and current user location
     */
    private void loadHotelsInRange() {
        String rangeString = rangeField.getValue();
        if (rangeString.isEmpty()) {
            Notification.show("Please enter a value for range", 5000, Notification.Position.MIDDLE);
            return;
        }
        try {
            double range = Double.parseDouble(rangeString);
            double rangeInMeters = range * 1000;

            UI.getCurrent().getPage().executeJs("""
                        return new Promise((resolve, reject) => {
                            navigator.geolocation.getCurrentPosition(
                                position => {
                                    const latitude = position.coords.latitude;
                                    const longitude = position.coords.longitude;
                                    resolve([latitude, longitude]);
                                },
                                error => {
                                    reject(error);
                                }
                            );
                        });
                    """).then(JsonArray.class, coordinates -> {
                currentLatitude = coordinates.getNumber(0);
                currentLongitude = coordinates.getNumber(1);
                if (currentLatitude != null) {
                    List<Hotel> hotels = webClient.get()
                            .uri("http://localhost:8080/hotels/" + currentLatitude + "/" + currentLongitude + "/" + rangeInMeters)
                            .retrieve()
                            .bodyToFlux(Hotel.class)
                            .collectList()
                            .block();
                    hotelGrid.setItems(hotels);
                } else {
                    Notification.show("Current location cannot be accessed!", 5000, Notification.Position.MIDDLE);
                }
            });
        } catch (NumberFormatException ex) {
            Notification.show("Invalid range value", 5000, Notification.Position.MIDDLE);
        }
    }

    /**
     * Update reservation by adding feedback
     *
     * @param reservation The reservation to be updated
     */
    private void updateReservation(Reservation reservation) {
        ResponseEntity<StringResponse> responseEntity = webClient.put()
                .uri("http://localhost:8080/reservations/feedback/" + reservation.getId() + "/" + reservation.getFeedback())
                .retrieve()
                .toEntity(StringResponse.class).block();

        if (responseEntity != null) {
            StringResponse response = responseEntity.getBody();
            if (response != null) {
                if (response.getStatus() == HttpStatus.OK) {
                    Notification.show(response.getBody(), 5000, Notification.Position.MIDDLE);
                    loadReservations();
                } else {
                    Notification.show("Failed to update reservation\n" + response.getError(), 5000, Notification.Position.MIDDLE);
                }
            } else {
                Notification.show("No response!", 5000, Notification.Position.MIDDLE);
            }
        }
    }

    /**
     * Delete reservation
     *
     * @param reservation The reservation to be deleted
     */
    private void cancelReservation(Reservation reservation) {
        ResponseEntity<ReservationResponse> responseEntity = webClient.delete()
                .uri("http://localhost:8080/reservations/" + reservation.getId())
                .retrieve()
                .toEntity(ReservationResponse.class).block();

        if (responseEntity != null && responseEntity.getBody() != null) {
            ReservationResponse response = responseEntity.getBody();
            if (response.getStatus() == HttpStatus.OK) {
                Notification.show("Reservation was canceled!", 5000, Notification.Position.MIDDLE);
                loadReservations();
            } else {
                Notification.show("Failed to cancel reservation\n" + response.getError(), 5000, Notification.Position.MIDDLE);
            }
        } else {
            Notification.show("No response!", 5000, Notification.Position.MIDDLE);
        }

    }

    /**
     * Open new dialog for requesting feedback
     *
     * @param reservation The reservation to be updated
     */
    private void openUpdateDialog(Reservation reservation) {
        Dialog dialog = new Dialog();
        TextArea feedbackArea = new TextArea("Feedback", "...");

        Button updateButton = new Button("Update", e -> {
            String feedback = feedbackArea.getValue();
            reservation.setFeedback(feedback);

            updateReservation(reservation);
            dialog.close();
        });

        dialog.add(feedbackArea, updateButton);
        dialog.open();
    }

    /**
     * Open new dialog for requesting information about new reservation
     */
    private void openReservationDialog() {
        Dialog dialog = new Dialog();

        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        startDatePicker.setLabel("Start Date");
        endDatePicker.setLabel("End Date");

        dialog.add(startDatePicker, endDatePicker);

        Button reserveButton = new Button("Reserve", e -> {
            Date startDate = Date.from(startDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(endDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());


            Hotel selectedHotel = hotelGrid.asSingleSelect().getValue();
            Room selectedRoom = roomGrid.asSingleSelect().getValue();

            if (selectedHotel == null) {
                Notification.show("Please select a hotel!", 5000, Notification.Position.MIDDLE);
                return;
            }
            if (selectedRoom == null) {
                Notification.show("Please select a room!", 5000, Notification.Position.MIDDLE);
                return;
            }
            ReservationDTO reservation = new ReservationDTO(loggedUser.getId(), selectedHotel.getId(), selectedRoom.getId(), startDate, endDate);


            ResponseEntity<ReservationResponse> responseEntity = webClient.post()
                    .uri("http://localhost:8080/reservations")
                    .bodyValue(reservation)
                    .retrieve()
                    .toEntity(ReservationResponse.class).block();

            if (responseEntity != null && responseEntity.getBody() != null) {
                ReservationResponse response = responseEntity.getBody();
                if (response.getStatus() == HttpStatus.OK) {
                    Notification.show("Room reserved!\nReservation id:" + response.getBody().getId(), 5000, Notification.Position.MIDDLE);
                    dialog.close();
                    loadReservations();
                } else {
                    Notification.show("Failed to reserve room!\n" + response.getError(), 5000, Notification.Position.MIDDLE);
                }
            } else {
                Notification.show("No response!", 5000, Notification.Position.MIDDLE);
            }

        });
        dialog.add(reserveButton);
        dialog.open();
    }

    /**
     * Open new read only dialog containing details for a reservation
     *
     * @param reservation Reservation which details to be shown
     */
    private void openReservationDetailsDialog(Reservation reservation) {
        Dialog dialog = new Dialog();

        VerticalLayout layout = new VerticalLayout();

        TextField hotelNameField = new TextField("Hotel name");
        hotelNameField.setValue(reservation.getHotel().getName());
        hotelNameField.setReadOnly(true);

        TextField roomNumberField = new TextField("Room number");
        roomNumberField.setValue(String.valueOf(reservation.getRoom().getRoomNumber()));
        roomNumberField.setReadOnly(true);

        TextField startDateField = new TextField("Start date");
        startDateField.setValue(formatDate(reservation.getStartDate()));
        startDateField.setReadOnly(true);

        TextField endDateField = new TextField("End date");
        endDateField.setValue(formatDate(reservation.getEndDate()));
        endDateField.setReadOnly(true);

        TextField feedbackField = new TextField("Feedback");
        feedbackField.setValue(reservation.getFeedback() != null ? reservation.getFeedback() : "");
        feedbackField.setReadOnly(true);

        H2 title = new H2("Reservation details");
        HorizontalLayout titleLayout = new HorizontalLayout(title);
        titleLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        titleLayout.setWidthFull();

        Button okButton = new Button("OK", click -> dialog.close());
        HorizontalLayout buttonLayout = new HorizontalLayout(okButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttonLayout.setWidthFull();

        layout.add(titleLayout);
        layout.add(hotelNameField, roomNumberField, startDateField, endDateField, feedbackField);
        layout.add(buttonLayout);


        dialog.add(layout);
        dialog.setWidth("450px");
        dialog.setHeight("650px");
        dialog.open();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (VaadinSession.getCurrent().getAttribute("user") == null) {
            event.rerouteTo("login");
        } else {
            loggedUser = (User) VaadinSession.getCurrent().getAttribute("user");
        }


    }
}