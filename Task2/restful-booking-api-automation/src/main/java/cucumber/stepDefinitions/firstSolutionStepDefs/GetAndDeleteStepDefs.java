package main.java.cucumber.stepDefinitions.firstSolutionStepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import main.java.dto.response.GetBookingIdResponse;
import main.java.model.StepTestContext;
import main.java.utils.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.testng.Assert;

import java.util.List;

public class GetAndDeleteStepDefs {
    private StepTestContext stepTestContext;
    private int bookingId;

    public GetAndDeleteStepDefs(StepTestContext stepTestContext) {
        this.stepTestContext = stepTestContext;
    }

    @Given("we have {string} and {string} and bookingIds of our astronauts")
    public void weHaveFirstnameAndLastnameOfOurAstronauts(String firstName, String lastName) {

        List<GetBookingIdResponse> getBookingIdResponses = UtilityClass.fetchBookingId(firstName, lastName);

        if (!CollectionUtils.isEmpty(getBookingIdResponses)) {
            bookingId = getBookingIdResponses.get(0).getBookingId();
        } else
            //As remote source has no  data for this booking Id, yet we are fetching it then it is not created in previous step by us.
            Assert.fail("This astronaut has not yet made any booking. Add this name in test data and we shall take care of the rest for our Billie Martian");
    }

    @And("we delete these bookings by their ID")
    public void weDeleteTheseBookingsByTheirID() {
        //perform DELETE request to update an existing the booking
        stepTestContext.currentEventRestAssuredResponse = UtilityClass.callDeleteBookingApi(bookingId, stepTestContext.authToken);
        stepTestContext.bookingId = bookingId;
    }

}
