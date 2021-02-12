package main.java.cucumber.stepDefinitions.firstSolutionStepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import main.java.dto.BookingDates;
import main.java.dto.request.BookingRequest;
import main.java.model.StepTestContext;
import main.java.utils.UtilityClass;

import java.util.Locale;

import static org.hamcrest.Matchers.notNullValue;

public class BookAndUpdateStepDefs {
    private final StepTestContext stepTestContext;
    private final BookingRequest bookingRequest = new BookingRequest();
    private boolean skipBookingIdValidation;

    public BookAndUpdateStepDefs(StepTestContext stepTestContext) {
        this.stepTestContext = stepTestContext;
    }

    @Given("we have {string} and {string} dates of astronaut {string} and {string}")
    public void weHaveCheckinAndCheckoutDatesOfAstronautFirstnameAndLastname(String checkIn, String checkout, String firstName, String lastName) {
        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckIn(checkIn);
        bookingDates.setCheckOut(checkout);
        bookingRequest.setFirstName(firstName);
        bookingRequest.setLastName(lastName);
        bookingRequest.setBookingDates(bookingDates);
    }

    @And("our astronaut has paid {string} and has additional choice for {string} and agreed to pay {string}")
    public void ourAstronautHasPaidDepositpaidAndHasAdditionalChoiceForAdditionalneedsAndAgreedToPayTotalprice(String depositPaid, String additionalChoices, String amount) {
        bookingRequest.setDepositPaid(Boolean.parseBoolean(depositPaid));
        bookingRequest.setAdditionalNeeds(additionalChoices);
        bookingRequest.setTotalPrice(Integer.parseInt(amount));
    }

    @When("we {string} bookings for our astronauts")
    public void weActionBookingsForOurAstronauts(String action) {
        if (action.equalsIgnoreCase("update")) {
            skipBookingIdValidation = true;

            //If step is update, then we will fetch the booking ID using the GetBookingId api
            String firstName, lastName;
            firstName = bookingRequest.getFirstName();
            lastName = bookingRequest.getLastName();

            //Fetch booking Id for this user to update
            int bookingId = UtilityClass.fetchBookingId(firstName, lastName).get(0).getBookingId();

            //store this booking id for further validation
            stepTestContext.bookingId = bookingId;

            //perform update request
            performRequestToTheRestfulBookerApp(action, bookingRequest, bookingId);

        } else
            //If this is create booking event, then simply perform create request
            performRequestToTheRestfulBookerApp(action, bookingRequest, 0);
    }

    @And("response should have {string} non null")
    public void responseShouldHaveNonNull(String arg0) {
        if (!skipBookingIdValidation) {
            stepTestContext.bookingId = stepTestContext.currentEventRestAssuredResponse.then().assertThat()
                    //assert that booking id should not null
                    .body(arg0, notNullValue())
                    //store this booking id for further use
                    .and().extract().jsonPath().getInt(arg0);
        }
    }

    /**
     * Allows you to specify URL Params to send along with URL.
     *
     * @param arg0           event or action name. e.g create
     * @param bookingId      booking id
     * @param bookingRequest request to perform action
     */
    private void performRequestToTheRestfulBookerApp(String arg0, BookingRequest bookingRequest, int bookingId) {
        Response response;
        switch (arg0.toUpperCase(Locale.ROOT)) {
            case "CREATE" -> {
                response = UtilityClass.callCreateBookingApi(bookingRequest);
                //store this response as latest and this will be used in response code, booking data, booking id assertions.
                stepTestContext.currentEventRestAssuredResponse = response;
                //store the booking request required for assertions with response
                stepTestContext.bookingRequest = bookingRequest;
            }
            case "UPDATE" -> {
                response = UtilityClass.callUpdateBookingApi(bookingRequest, bookingId, stepTestContext.authToken);
                //store this response as latest and this will be used in response code, booking data and will be compared with booking request..
                stepTestContext.currentEventRestAssuredResponse = response;
                //store the booking request required for assertions with response
                stepTestContext.bookingRequest = bookingRequest;
            }
        }
    }


}
