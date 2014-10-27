package scratch.cucumber.rest.steps;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.glassfish.jersey.client.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.junit.Assert.assertEquals;
import static scratch.cucumber.rest.steps.UserFields.ID;

@ContextConfiguration(classes = CucumberScratchConfiguration.class)
public class UserDeleteSteps {

    @Autowired
    private WebTarget client;

    @Autowired
    private Responses responses;

    @When("^I delete an existing user$")
    public void I_delete_an_existing_user() {

        final String id = getLastCreatedId();

        I_delete_a_user_with_an_ID_of(id);
    }

    @When("^I delete a user with an ID of \"([^\"]*)\"$")
    public void I_delete_a_user_with_an_ID_of(String id) {

        client.path(id).request(MediaType.APPLICATION_JSON_TYPE).delete();
    }

    @Then("^the user should be deleted$")
    public void the_user_should_be_deleted() {

        final String id = getLastCreatedId();

        final Response response = client.path(id).request(MediaType.APPLICATION_JSON_TYPE).get();

        assertEquals("the user should no longer exist.", NOT_FOUND.getStatusCode(), response.getStatus());
    }

    private String getLastCreatedId() {

        final ClientResponse createResponse = responses.created().latest();

        @SuppressWarnings("unchecked")
        final Map<String, Object> body = createResponse.readEntity(Map.class);

        return body.get(ID).toString();
    }
}
