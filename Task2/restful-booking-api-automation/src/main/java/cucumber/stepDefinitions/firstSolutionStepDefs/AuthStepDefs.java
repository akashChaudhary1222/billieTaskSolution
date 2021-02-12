package main.java.cucumber.stepDefinitions.firstSolutionStepDefs;

import io.cucumber.java.en.Given;
import main.java.dto.request.AuthRequest;
import main.java.dto.response.AuthResponse;
import main.java.model.StepTestContext;
import main.java.utils.UtilityClass;

import static main.java.utils.PropertyUtil.getPropertyValue;

public class AuthStepDefs {

    private StepTestContext stepTestContext;

    public AuthStepDefs(StepTestContext stepTestContext) {
        this.stepTestContext = stepTestContext;
    }

    @Given("Valid Auth token is created successfully")
    public void validAuthTokenIsCreatedSuccessfully() {
        initAndValidateAuthToken();
    }

    private void initAndValidateAuthToken() {
        //Preparing request object for Auth Api with valid user details
        AuthRequest authRequest = new AuthRequest(getPropertyValue("user.name"), getPropertyValue("password"));
        AuthResponse authResponse = UtilityClass.callAuthApi(authRequest);
        //Store the token for global access
        stepTestContext.authToken = authResponse.getToken();
    }

}
