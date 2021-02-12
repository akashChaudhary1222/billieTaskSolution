package main.java.cucumber.stepDefinitions.secondSolutionStepDefs;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import main.java.api.auth.AuthApi;
import main.java.api.booking.CreateBookingApi;
import main.java.api.booking.DeleteApi;
import main.java.api.booking.GetBookingApi;
import main.java.api.booking.UpdateBookingApi;
import main.java.dto.request.AuthRequest;
import main.java.dto.request.BookingRequest;
import main.java.dto.response.AuthResponse;
import main.java.dto.response.Booking;
import main.java.dto.response.BookingResponse;
import main.java.model.BookingDataTable;
import main.java.model.TestContext;
import main.java.utils.JsonUtil;
import main.java.utils.UtilityClass;
import org.testng.Assert;
import test.missionMarsTests.cucumber.CucumberTask2TestRunner;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static main.java.httpCore.Headers.COOKIE;
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
            BookingDataTable bookingDataTable = fromJson(toJson(stringStringMap), BookingDataTable.class);
            Assert.assertNotNull(bookingDataTable, "Please initialise test data table.");
            //Prepared booking request
            BookingRequest bookingRequest = UtilityClass.prepareBookingRequestFromDataTableModel(bookingDataTable);
            int bookingId = 0;

            //Send it for processing. At the time of Create and Update, I have stored bookingId as 0
            if (astronautBookingIdMap.containsKey(bookingDataTable.getFirstName() + bookingDataTable.getLastName())
                    && arg0.equalsIgnoreCase("update")) {
                bookingId = astronautBookingIdMap.get(bookingDataTable.getFirstName() + bookingDataTable.getLastName());
            }
            performRequestToTheRestfulBookerApp(arg0, bookingRequest, bookingId);
        });
    }

    @Then("response should have HTTP status code {int}")
    public void responseShouldHaveHTTPStatusCode(int arg0) {
        //Assert HTTP status code for saved responses
        multipleBookingsResponseMap.forEach((k, testContext) -> testContext.getCommonRestAssuredResponse().then().assertThat().statusCode(arg0));
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

            } else {
                Booking updatedBooking = testContext.getGetApiRestAssuredResponse().then().extract().response().as(Booking.class);
                bookingDataInResponse = toJson(updatedBooking);
            }
            //Assert the response data and request data
            Assert.assertEquals(testContext.getBookingRequest(), bookingDataInResponse, "Booking data in request is not same as in response. Step: " + arg0);
        });
    }

    @When("we {string} the created booking by booking Id")
    public void weTheCreatedBookingByBookingId(String arg0) {

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
        authApi.setRequestBody(JsonUtil.toJson(authRequest));
        //Sending POST request with valid params. We are then validating HTTP response status code and token field in response.
        Response authRestResponse = authApi.sendRequest();
        authRestResponse.then().assertThat()
                //Assert HTTP status code
                .statusCode(200).and()
                //Assert Auth token string in response
                .body("token", notNullValue())
                //extract validated response
                .extract().response();
        //Extract response as AuthResponse class
        AuthResponse authResponse = authRestResponse.as(AuthResponse.class);
        //Store the token for global access
        authToken = authResponse.getToken();
    }

    private void performRequestToTheRestfulBookerApp(String arg0, BookingRequest bookingRequest, int bookingId) {
        String bookingJsonRequest;
        Response response;
        switch (arg0.toUpperCase(Locale.ROOT)) {
            case "CREATE" -> {
                TestContext testContext = new TestContext();
                bookingJsonRequest = toJson(bookingRequest);

                //Initialising RequestSpecification for Create Booking API
                CreateBookingApi createBookingApi = new CreateBookingApi();
                createBookingApi.setRequestBody(bookingJsonRequest);

                //perform POST request to create a booking
                response = createBookingApi.sendRequest();

                //save test data
                testContext.setCreateUpdateRestAssuredResponse(response);
                testContext.setCommonRestAssuredResponse(response);
                testContext.setBookingRequest(bookingJsonRequest);
                multipleBookingsResponseMap.put(bookingRequest.getFirstName() + bookingRequest.getLastName(), testContext);
            }
            case "UPDATE" -> {
                bookingJsonRequest = toJson(bookingRequest);

                //Initialising RequestSpecification for Update Booking API
                UpdateBookingApi updateBookingApi = new UpdateBookingApi(bookingId);
                updateBookingApi.setRequestBody(bookingJsonRequest);
                updateBookingApi.addHeader(COOKIE.getName(), String.format("token=%s", authToken));
                //perform PUT request to update an existing the booking
                response = updateBookingApi.sendRequest();

                //update test data
                TestContext testContext = new TestContext();
                testContext.setBookingRequest(bookingJsonRequest);
                testContext.setCommonRestAssuredResponse(response);
                testContext.setCreateUpdateRestAssuredResponse(response);
                testContext.setBookingRequest(bookingJsonRequest);
                testContext.setBookingRequestClassObject(bookingRequest);
                multipleBookingsResponseMap.put(bookingRequest.getFirstName() + bookingRequest.getLastName(), testContext);
            }
            case "GET" -> {
                TestContext testContext = multipleBookingsResponseMap.get(bookingRequest.getFirstName() + bookingRequest.getLastName());
                //Initialising RequestSpecification for Get Booking API
                GetBookingApi getBookingApi = new GetBookingApi(bookingId);
                getBookingApi.addHeader(COOKIE.getName(), String.format("token=%s", authToken));
                //perform GET request to update an existing the booking
                response = getBookingApi.sendRequest();
                testContext.setCommonRestAssuredResponse(response);
                testContext.setGetApiRestAssuredResponse(response);
                testContext.setAssertWithUpdate(true);
                multipleBookingsResponseMap.put(bookingRequest.getFirstName() + bookingRequest.getLastName(), testContext);
            }
            case "DELETE" -> {
                TestContext testContext = multipleBookingsResponseMap.get(bookingRequest.getFirstName() + bookingRequest.getLastName());
                //Initialising RequestSpecification for Delete Booking API
                DeleteApi deleteApi = new DeleteApi(bookingId);
                deleteApi.addHeader(COOKIE.getName(), String.format("token=%s", authToken));
                //perform DELETE request to update an existing the booking
                response = deleteApi.sendRequest();
                testContext.setCommonRestAssuredResponse(response);
                testContext.setDeleteApiRestAssuredResponse(response);
                multipleBookingsResponseMap.put(bookingRequest.getFirstName() + bookingRequest.getLastName(), testContext);
            }
        }
    }
}
