package main.java.cucumber.stepDefinitions.firstSolutionStepDefs;

import io.cucumber.java.en.When;
import main.java.model.StepTestContext;
import main.java.utils.UtilityClass;

public class GetBookingStepDefs {

    private StepTestContext stepTestContext;

    public GetBookingStepDefs(StepTestContext stepTestContext) {
        this.stepTestContext = stepTestContext;
    }

    @When("we make a call to GetBookings API with bookingId")
    public void weMakeACallToGetBookingsAPI() {
        //perform GET request to update an existing the booking
        stepTestContext.currentEventRestAssuredResponse = UtilityClass.callGetBookingApi(stepTestContext.bookingId, stepTestContext.authToken);
    }
}
