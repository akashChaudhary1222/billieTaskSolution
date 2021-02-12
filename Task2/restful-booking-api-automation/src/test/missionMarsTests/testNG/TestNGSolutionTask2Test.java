package test.missionMarsTests.testNG;

import main.java.constants.HttpStatusLines;
import main.java.dto.request.AuthRequest;
import main.java.dto.request.BookingRequest;
import main.java.dto.response.Booking;
import main.java.dto.response.BookingResponse;
import main.java.utils.ExcelUtil;
import main.java.utils.JsonUtil;
import main.java.utils.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test.missionMarsTests.base.BaseTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.java.utils.PropertyUtil.getPropertyValue;

public class TestNGSolutionTask2Test extends BaseTest {

    private String authToken;
    private final Map<BookingRequest, BookingResponse> createBookingRequestResponseMap = new HashMap<>();
    private final Map<BookingRequest, Booking> updateBookingRequestResponseMap = new HashMap<>();
    private final Map<BookingRequest, Integer> bookingRequestIdMap = new HashMap<>();


    @DataProvider
    @SuppressWarnings("unchecked")
    public Object[][] testDataProvider() {
        return ExcelUtil.fromHashMapToTwoDimensionalArray((Map<Integer, List>)
                ExcelUtil.loadExcelLines(ExcelUtil.getExcelFromPath("bookingTestData")).get("Sheet1"));
    }

    @BeforeClass(description = "We will first fetch the auth token in this first test for valid user details." +
            "We will then validate that the HTTP response status code should be 200 and the token value must not be null." +
            "Further execution for booking and delete api depends on this token. Hence, we will prioritise this test with priority=1 " +
            "so that it runs as the first test case during execution")
    public void fetchAuthToken() {
        AuthRequest authRequest = new AuthRequest(getPropertyValue("user.name"), getPropertyValue("password"));
        //Store the token locally for further use.
        authToken = UtilityClass.callAuthApi(authRequest).getToken();
        logger.info("Auth token fetched successfully");
    }

    @Test(description = "This test creates booking for all the astronauts and then validates if HTTP Status code is 200 OK or not.", dataProvider = "testDataProvider", priority = 1)
    public void createBookingAndValidateResponseHTTPStatusCode200(String firstName, String lastName, String checkIn, String checkOut,
                                                                  String additionalNeed, String depositPaid, String totalPrice) {
        BookingRequest bookingRequest = UtilityClass.prepareBookingRequestFromExcelData(firstName, lastName, checkIn, checkOut, additionalNeed, depositPaid, totalPrice);

        BookingResponse bookingResponse = UtilityClass.callCreateBookingApi(bookingRequest)
                .then().assertThat()
                .statusCode(200).assertThat()
                .statusLine(HttpStatusLines.HTTP_200.getStatusLine())
                .and().extract().response().as(BookingResponse.class);
        Assert.assertNotNull(bookingResponse.getBooking(), "Booking ID can not be null after booking is created successfully.");
        createBookingRequestResponseMap.put(bookingRequest, bookingResponse);
    }

    @Test(description = "This test validates that created booking data should be same as the data in request", priority = 2)
    public void verifyThatCreateResponseShouldBeEqualToRequest() {
        createBookingRequestResponseMap.forEach((bookingRequest, bookingResponse) -> Assert.assertEquals(JsonUtil.toJson(bookingRequest), JsonUtil.toJson(bookingResponse.getBooking()), "Response data for created booking is not same as the request data."));
    }

    @Test(description = "This test case updates bookings based on previous fields and with some random test data. " +
            "Here we have not used Data provider with Excel to reduce maintenance of multiple Excels. " +
            "The purpose is to only check that after PUT request, data should get updated for an existing resource." +
            "Then validate if HTTP Status code is 200 OK or not.", priority = 3)
    public void updateBookingsAndValidateResponseHTTPStatusCode200() {
        createBookingRequestResponseMap.forEach(((bookingRequest, bookingResponse)
                -> {
            if (!bookingRequest.isDepositPaid())
                bookingRequest.setDepositPaid(true);
            if (!bookingRequest.getAdditionalNeeds().equalsIgnoreCase("NA")) {
                bookingRequest.setTotalPrice(Integer.parseInt(bookingRequest.getTotalPrice() + RandomStringUtils.randomNumeric(3)));
                bookingRequest.setAdditionalNeeds(bookingRequest.getFirstName() + " needs some help with the booking.");
            }

            int bookingId = bookingResponse.getBookingId();
            Booking updateResponse = UtilityClass.callUpdateBookingApi(bookingRequest, bookingId, authToken)
                    .then().assertThat()
                    .statusCode(200).assertThat()
                    .statusLine(HttpStatusLines.HTTP_200.getStatusLine())
                    .and().extract().response().as(Booking.class);

            updateBookingRequestResponseMap.put(bookingRequest, updateResponse);
            bookingRequestIdMap.put(bookingRequest, bookingId);
        }));

    }

    @Test(description = "This test validates that updated booking data should be same as the data in request", priority = 4)
    public void verifyThatUpdatedResponseShouldBeEqualToRequest() {
        updateBookingRequestResponseMap.forEach((bookingRequest, booking) -> Assert.assertEquals(JsonUtil.toJson(bookingRequest), JsonUtil.toJson(booking), "Response data for updated booking is not same as the request data."));
    }


    @Test(description = "This test case retrieves booking data with for a bookingId by sending request to GetBooking Api." +
            " Then it validates that the response retrieved should be equal to the last response from update booking event.", priority = 5)
    public void retrieveUpdatedBookingsAndAssertResponseWithPreviousEventResponse() {
        updateBookingRequestResponseMap.forEach(((bookingRequest, booking)
                -> {
            int bookingId = bookingRequestIdMap.get(bookingRequest);
            Booking fetchedResponse = UtilityClass.callGetBookingApi(bookingId, authToken)
                    .then().assertThat()
                    .statusCode(200).and().assertThat()
                    .statusLine(HttpStatusLines.HTTP_200.getStatusLine())
                    .and().extract()
                    .response().as(Booking.class);

            Assert.assertEquals(JsonUtil.toJson(booking), JsonUtil.toJson(fetchedResponse), "Last updated actual response is not same as fetched response for bookingId: " + bookingId);
        }));

    }

    @Test(description = "This test case simply deletes all the created bookings. Then it validates that HTTP Status code should be 201 Created.", priority = 6)
    public void deleteBookingsAndValidateResponseHTTPStatusCode201() {
        createBookingRequestResponseMap.forEach(((bookingRequest, bookingResponse) -> {
            UtilityClass.callDeleteBookingApi(bookingResponse.getBookingId(), authToken)
                    .then().assertThat()
                    .statusCode(201).assertThat()
                    .statusLine(HttpStatusLines.HTTP_201.getStatusLine());
        }));

    }

    @Test(description = "This test case validates if deleted bookings are actually deleted or not. If yes, then Api should return 404 Not Found.", priority = 7)
    public void retrieveDeletedBookingsAndValidateResponseCodeHTTPStatusCode404() {
        createBookingRequestResponseMap.forEach(((bookingRequest, bookingResponse) -> {
            UtilityClass.callGetBookingApi(bookingResponse.getBookingId(), authToken)
                    .then().assertThat()
                    .statusCode(404).assertThat()
                    .statusLine(HttpStatusLines.HTTP_404.getStatusLine());
        }));

    }
}
