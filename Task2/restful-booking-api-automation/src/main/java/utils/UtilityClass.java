package main.java.utils;

import io.restassured.response.Response;
import main.java.api.auth.AuthApi;
import main.java.api.booking.*;
import main.java.dto.BookingDates;
import main.java.dto.request.AuthRequest;
import main.java.dto.request.BookingRequest;
import main.java.dto.response.AuthResponse;
import main.java.dto.response.GetBookingIdResponse;
import main.java.model.BookingDataTable;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import static main.java.httpCore.Headers.COOKIE;
import static org.hamcrest.Matchers.notNullValue;

public class UtilityClass {

    /**
     * Helper method to update an existing booking
     *
     * @param firstname first name of the person
     * @param lastname  last name of the person
     * @return Response rest-assured response
     */
    public static List<GetBookingIdResponse> fetchBookingId(String firstname, String lastname) {
        GetBookingIdApi getBookingIdApi = new GetBookingIdApi(firstname, lastname);
        return Arrays.asList(getBookingIdApi.sendRequest().then().assertThat().statusCode(200).and().extract().response().as(GetBookingIdResponse[].class));
    }


    /**
     * Helper method to update an existing booking
     *
     * @param bookingRequest booking request to create a booking
     * @return Response rest-assured response
     */
    public static Response callUpdateBookingApi(BookingRequest bookingRequest, int bookingId, String authToken) {
        UpdateBookingApi updateBookingApi = new UpdateBookingApi(bookingId);
        updateBookingApi.setRequestBody(JsonUtil.toJson(bookingRequest));
        updateBookingApi.addHeader(COOKIE.getName(), String.format("token=%s", authToken));
        return updateBookingApi.sendRequest();
    }

    /**
     * Helper method to create booking
     *
     * @param bookingRequest booking request to create a booking
     * @return Response rest-assured response
     */
    public static Response callCreateBookingApi(BookingRequest bookingRequest) {
        CreateBookingApi createBookingApi = new CreateBookingApi();
        createBookingApi.setRequestBody(JsonUtil.toJson(bookingRequest));
        return createBookingApi.sendRequest();
    }

    /**
     * Helper method to retrieve an existing booking
     *
     * @param bookingId booking Id to retrieve the booking data
     * @param authToken valid Auth token
     * @return Response rest-assured response
     */
    public static Response callGetBookingApi(int bookingId, String authToken) {
        GetBookingApi getBookingApi = new GetBookingApi(bookingId);
        getBookingApi.addHeader(COOKIE.getName(), String.format("token=%s", authToken));
        return getBookingApi.sendRequest();
    }

    /**
     * Helper method to delete an existing booking
     *
     * @param bookingId booking Id to retrieve the booking data
     * @param authToken valid Auth token
     * @return Response rest-assured response
     */
    public static Response callDeleteBookingApi(int bookingId, String authToken) {
        DeleteApi deleteApi = new DeleteApi(bookingId);
        deleteApi.addHeader(COOKIE.getName(), String.format("token=%s", authToken));
        return deleteApi.sendRequest();
    }

    /**
     * Helper method to create an Auth token
     *
     * @param authRequest request with user credentials
     * @return Response rest-assured response
     */
    public static AuthResponse callAuthApi(AuthRequest authRequest) {
        //Preparing request object for Auth Api with valid user details
        AuthApi authApi = new AuthApi();
        //Setting the request Json body in api request.
        authApi.setRequestBody(JsonUtil.toJson(authRequest));

        //Sending POST request with valid params. We are then validating HTTP response status code and token field in response.
        return authApi.sendRequest().then().assertThat()
                //Assert HTTP status code
                .statusCode(200)
                //Assert token string not null in response
                .and().body("token", notNullValue())
                //extract validated response as class
                .extract().response().as(AuthResponse.class);
    }

    /**
     * Helper method to update an existing booking
     *
     * @param dataTable Cucumber DataTable with request data
     * @return BookingRequest
     */
    public static BookingRequest prepareBookingRequestFromDataTableModel(BookingDataTable dataTable) {
        BookingRequest bookingRequest = new BookingRequest();

        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckIn(dataTable.getCheckInDate());
        bookingDates.setCheckOut(dataTable.getCheckOut());
        bookingRequest.setBookingDates(bookingDates);

        bookingRequest.setAdditionalNeeds(dataTable.getAdditionalNeeds());
        bookingRequest.setDepositPaid(dataTable.isDepositPaid());
        bookingRequest.setFirstName(dataTable.getFirstName());
        bookingRequest.setLastName(dataTable.getLastName());
        bookingRequest.setTotalPrice(dataTable.getTotalPrice());

        return bookingRequest;
    }

    public static BookingRequest prepareBookingRequestFromExcelData(String firstName, String lastName, String checkIn, String checkOut,
                                                                    String additionalNeed, String depositPaid, String totalPrice) {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setFirstName(firstName);
        bookingRequest.setLastName(lastName);
        BookingDates bookingDates = new BookingDates();

        bookingDates.setCheckIn(checkIn);
        bookingDates.setCheckOut(checkOut);
        bookingRequest.setBookingDates(bookingDates);

        bookingRequest.setTotalPrice(Integer.parseInt(totalPrice));
        bookingRequest.setDepositPaid(Boolean.parseBoolean(depositPaid));

        if (StringUtils.isNotBlank(additionalNeed))
            bookingRequest.setAdditionalNeeds(additionalNeed);
        else bookingRequest.setAdditionalNeeds("");
        return bookingRequest;
    }

}
