package main.java.cucumber.stepDefinitions.secondSolutionStepDefs;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import main.java.api.auth.AuthApi;
import main.java.dto.request.AuthRequest;
import main.java.dto.request.BookingRequest;
import main.java.dto.response.AuthResponse;
import main.java.dto.response.Booking;
import main.java.dto.response.BookingResponse;
import main.java.model.BookingDataTable;
import main.java.model.TestContext;
import main.java.utils.UtilityClass;
import org.testng.Assert;
import test.missionMarsTests.cucumber.CucumberTask2TestRunner;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static main.java.utils.JsonUtil.fromJson;
import static main.java.utils.JsonUtil.toJson;
import static main.java.utils.PropertyUtil.getPropertyValue;
import static org.hamcrest.Matchers.notNullValue;

public class TaskSolutionSingleStepDefs extends CucumberTask2TestRunner {

    private String authToken;
    private final Map<String, Integer> astronautBookingIdMap = new HashMap<>();
    private Map<String, TestContext> multipleBookingsResponseMap;

    @Given("our astronaut is an authorised user and has following request details available to proceed")
    public void ourAstronautIsAnAuthorisedUserAndHasFollowingRequestDetailsAvailableToProceed() {
        initAndValidateAuthToken();
    }

    @When("we perform {string} booking request to the restful-booker App with following data")
    public void wePerformBookingRequestToTheRestfulBookerApp(String arg0, DataTable requestDataTable) {
        multipleBookingsResponseMap = new HashMap<>();
        //Loading Request Data from feature file to Class object for all the astronauts
        requestDataTable.asMaps().forEach(stringStringMap -> {

            //map data with BookingDataTable model
            BookingDataTable bookingDataTable = fromJson(toJson(stringStringMap), BookingDataTable.class);
            Assert.assertNotNull(bookingDataTable, "Please initialise test data table.");
            //Prepared booking request
            BookingRequest bookingRequest = UtilityClass.prepareBookingRequestFromDataTableModel(bookingDataTable);
            int bookingId = 0;

            //If Booking id is already present in this astronautBookingIdMap, that means this step is an update to an existing booking.
            //then we get the booking id from astronautBookingIdMap and make an update.
            if (astronautBookingIdMap.containsKey(bookingDataTable.getFirstName() + bookingDataTable.getLastName())
                    && arg0.equalsIgnoreCase("update")) {
                bookingId = astronautBookingIdMap.get(bookingDataTable.getFirstName() + bookingDataTable.getLastName());
            }

            //if the step is create booking then simply create one booking. Booking id is not used in this step.
            performRequestToTheRestfulBookerApp(arg0, bookingRequest, bookingId);
        });
    }

    @Then("response should have HTTP status code {int}")
    public void responseShouldHaveHTTPStatusCode(int arg0) {
        //Assert HTTP status code for all the saved responses
        multipleBookingsResponseMap.forEach((k, testContext) -> testContext.getCurrentEventRestAssuredResponse().then().assertThat().statusCode(arg0));
    }

    @And("response should have {string} and it should not be null")
    public void responseShouldHaveAndItShouldNotBeNull(String arg0) {
        //Assert bookingId in response and it should not be null for all the booking responses
        multipleBookingsResponseMap.forEach((k, testContext) -> {
            Response response = testContext.getCreateUpdateRestAssuredResponse();
            response.then().assertThat().body(arg0, notNullValue());
            //If booking id is not null then we can load the data in our class
            BookingResponse bookingResponse = response.then().extract().response().as(BookingResponse.class);
            testContext.setBookingResponse(bookingResponse);
            //Storing this bookingId against astronaut name so that we will then use it in Update request.
            astronautBookingIdMap.put(k, bookingResponse.getBookingId());
        });

    }

    @And("data given in response body should be equal to the data in {string} request")
    public void dataGivenInResponseBodyShouldBeEqualToTheDataInRequest(String arg0) {
        //Assert request data with response data based on the HTTP request type
        multipleBookingsResponseMap.forEach((k, testContext) -> {
            String bookingDataInResponse;

            //If this is where we just created the booking,  then we can use already stored BookingResponse object from test context
            if (arg0.equalsIgnoreCase("create")) {
                bookingDataInResponse = toJson(testContext.getBookingResponse().getBooking());
            }
            //Otherwise if this is update or get request where we can store this new response direct with Booking class and use it for assertions
            else if (arg0.equalsIgnoreCase("update")) {
                Booking updatedBooking = testContext.getCreateUpdateRestAssuredResponse().then().extract().response().as(Booking.class);
                bookingDataInResponse = toJson(updatedBooking);
                //If this is a Getbooking step then assert updated booking data from last step with the data fetched in Getbooking response
            } else {
                Booking updatedBooking = testContext.getGetApiRestAssuredResponse().then().extract().response().as(Booking.class);
                bookingDataInResponse = toJson(updatedBooking);
            }
            //Assert the response data and request data
            Assert.assertEquals(testContext.getBookingRequestJson(), bookingDataInResponse, "Booking data in request is not same as in response. Step: " + arg0);
        });
    }

    @When("we {string} the created booking by booking Id")
    public void weTheCreatedBookingByBookingId(String arg0) {
        //This is for Delete and Get steps. We perform the action received from feature file based on if the local booking id map has that data or not
        astronautBookingIdMap.forEach((astronaut, bookingId) -> {
            if (multipleBookingsResponseMap.containsKey(astronaut)) {
                performRequestToTheRestfulBookerApp(arg0, multipleBookingsResponseMap.get(astronaut).getBookingRequestClassObject(), bookingId);
            }
        });
    }


    //Helper methods

    private void initAndValidateAuthToken() {
        //Preparing request object for Auth Api with valid user details
        AuthRequest authRequest = new AuthRequest(getPropertyValue("user.name"), getPropertyValue("password"));
        //Initialising RequestSpecification for Auth API
        AuthApi authApi = new AuthApi();
        //Extract response as AuthResponse class
        AuthResponse authResponse = UtilityClass.callAuthApi(authRequest);
        //Store the token for global access
        authToken = authResponse.getToken();
    }

    private void performRequestToTheRestfulBookerApp(String arg0, BookingRequest bookingRequest, int bookingId) {
        String bookingJsonRequest;
        Response response;
        switch (arg0.toUpperCase(Locale.ROOT)) {
            case "CREATE" -> {
                //perform POST request to create a booking
                response = UtilityClass.callCreateBookingApi(bookingRequest);
                //save latest test data from this step and this will be used in further assertions and validations
                TestContext testContext = new TestContext();
                testContext.setCreateUpdateRestAssuredResponse(response);
                testContext.setCurrentEventRestAssuredResponse(response);
                testContext.setBookingRequestJson(toJson(bookingRequest));

                //store this test data against firstname and lastnames for each booking. This map is updated at each API call
                multipleBookingsResponseMap.put(bookingRequest.getFirstName() + bookingRequest.getLastName(), testContext);
            }
            case "UPDATE" -> {
                //perform PUT request to update an existing the booking
                response = UtilityClass.callUpdateBookingApi(bookingRequest, bookingId, authToken);
                //save latest test data from this step and this will be used in further assertions and validations
                TestContext testContext = new TestContext();
                testContext.setCurrentEventRestAssuredResponse(response);
                testContext.setCreateUpdateRestAssuredResponse(response);
                testContext.setBookingRequestJson(toJson(bookingRequest));
                testContext.setBookingRequestClassObject(bookingRequest);

                //This map is updated at each API call
                multipleBookingsResponseMap.put(bookingRequest.getFirstName() + bookingRequest.getLastName(), testContext);
            }
            case "GET" -> {
                //fetch already saved test data for this booking and update with new
                // so that this can be validated with the response received from Get Step
                TestContext testContext = multipleBookingsResponseMap.get(bookingRequest.getFirstName() + bookingRequest.getLastName());
                //perform GET request to update an existing the booking
                response = UtilityClass.callGetBookingApi(bookingId, authToken);
                //Store data in test context which will be used in next Steps
                testContext.setCurrentEventRestAssuredResponse(response);
                testContext.setGetApiRestAssuredResponse(response);
                testContext.setAssertWithUpdate(true);

                //This map is updated at each API call
                multipleBookingsResponseMap.put(bookingRequest.getFirstName() + bookingRequest.getLastName(), testContext);
            }
            case "DELETE" -> {
                //fetch already saved test data for this booking. Delete will be performed on existing bookings
                TestContext testContext = multipleBookingsResponseMap.get(bookingRequest.getFirstName() + bookingRequest.getLastName());
                //perform DELETE request to update an existing the booking

                response = UtilityClass.callDeleteBookingApi(bookingId, authToken);

                //Store data in test context which will be used in next Steps
                testContext.setCurrentEventRestAssuredResponse(response);
                testContext.setDeleteApiRestAssuredResponse(response);

                //This map is updated at each API call
                multipleBookingsResponseMap.put(bookingRequest.getFirstName() + bookingRequest.getLastName(), testContext);
            }
        }
    }
}
