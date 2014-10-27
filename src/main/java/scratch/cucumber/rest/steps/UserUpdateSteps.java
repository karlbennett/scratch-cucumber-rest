package scratch.cucumber.rest.steps;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.glassfish.jersey.client.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Map;

import static javax.ws.rs.client.Entity.json;
import static scratch.cucumber.rest.steps.UserFields.ID;

@ContextConfiguration(classes = CucumberScratchConfiguration.class)
public class UserUpdateSteps {

    @Autowired
    private PropertyObject user;

    @Autowired
    private WebTarget client;

    @Autowired
    private Requests requests;

    @Autowired
    private Responses responses;

    @Autowired
    private UserSteps steps;

    @When("^I update the user$")
    public void I_update_the_user() {

        final ClientResponse createResponse = responses.created().latest();

        @SuppressWarnings("unchecked")
        final Map<String, Object> body = createResponse.readEntity(Map.class);

        final String id = body.get(ID).toString();

        client.path(id).request(MediaType.APPLICATION_JSON_TYPE).put(json(user.toMap()));
    }

    @Then("^the user should be updated$")
    @SuppressWarnings("unchecked")
    public void the_user_should_be_updated() {

        final String id = requests.updated().latest().getUri().getPath().replaceAll("(?:/[a-zA-Z]*)*/", "");

        steps.the_user_should_be_persisted_with_id(Integer.valueOf(id));
    }
}
