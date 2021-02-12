package main.java.cucumber.stepDefinitions.firstSolutionStepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import main.java.constants.CrudEvents;
import main.java.dto.response.Booking;
import main.java.model.StepTestContext;
import org.testng.Assert;

import static main.java.utils.JsonUtil.toJson;

public class CommonStepDefs {

    private StepTestContext stepTestContext;

    public CommonStepDefs(StepTestContext stepTestContext) {
        this.stepTestContext = stepTestContext;
    }

    @Then("we should get HTTP response code {int}")
    public void weShouldGetHTTPResponseCode(int arg0) {
        //Assert response HTTP status code with method parameter received from feature file
        stepTestContext.currentEventRestAssuredResponse.then().assertThat().statusCode(arg0);
    }

    @And("response should be equal to {string}")
    public void responseShouldBeBlank(String arg0) {
        //Assert response stored from last event should be equal method parameter received from feature file
        Assert.assertEquals(stepTestContext.currentEventRestAssuredResponse
                        .then().extract().asString(), arg0,
                "Response assertion failed for Delete booking step");
    }

    @And("we shall get data in response equal to the request we made for {string}")
    public void theDataGivenInResponseBodyMustBeEqualToTheDataInActionRequest(String event) {
        String actualResponse = null;

        //if event is create then booking response will have extra fields such as booking id. This can be fetched with Json path 'booking'
        //and then the booking data is stored and asserted with booking request
        if (event.equalsIgnoreCase(CrudEvents.CREATE.name())) {
            Booking bookingData = stepTestContext.currentEventRestAssuredResponse.then().extract().jsonPath().getObject("booking", Booking.class);
            actualResponse = toJson(bookingData);
            stepTestContext.previousEventRestAssuredResponse = stepTestContext.currentEventRestAssuredResponse;
        }
        //if event is update then response will only have booking data directly accessible. This can be mapped with Booking class
        //and then the updated booking data is stored and asserted with update request
        else if (event.equalsIgnoreCase(CrudEvents.UPDATE.name())) {
            Booking booking = stepTestContext.currentEventRestAssuredResponse.then().extract().response().as(Booking.class);
            actualResponse = toJson(booking);
            stepTestContext.previousEventRestAssuredResponse = stepTestContext.currentEventRestAssuredResponse;
        }
        Assert.assertEquals(actualResponse, toJson(stepTestContext.bookingRequest), "Response data for booking is not same as request.");
    }

    @Then("data given in response body should be equal to the last actual data from {string} event")
    public void dataGivenInResponseBodyShouldBeEqualToTheLastActualDataFromActionEvent(String previousEvent) {
        String currentEventResponseFromGetApi, previousEventResponse = null;
        currentEventResponseFromGetApi = stepTestContext.currentEventRestAssuredResponse.then().extract().jsonPath().getString("");

        //if event is create then booking response will have extra fields such as booking id. This can be fetched with Json path 'booking'
        if (previousEvent.equalsIgnoreCase(CrudEvents.CREATE.name())) {
            previousEventResponse = stepTestContext.previousEventRestAssuredResponse.jsonPath().getString("booking");
        }
        //if event is update then response will only have booking data directly accessible. This can be fetched with empty Json path
        //Since actual and expected both responses are RestAssured responses, sequence of fields in string is not a problem while asserting.
        // Hence these are not mapped with Booking class.
        else if (previousEvent.equalsIgnoreCase(CrudEvents.UPDATE.name())) {
            previousEventResponse = stepTestContext.previousEventRestAssuredResponse.then().extract().jsonPath().getString("");
        }
        Assert.assertEquals(previousEventResponse, currentEventResponseFromGetApi, "Last actual data is not same as data fetched from Get api for the bookingId: " + stepTestContext.bookingId);
    }
}
