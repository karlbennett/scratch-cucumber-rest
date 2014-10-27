package scratch.cucumber.rest.steps;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.glassfish.jersey.client.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static scratch.cucumber.rest.steps.UserFields.ADDRESS_ID;
import static scratch.cucumber.rest.steps.UserFields.ID;

@ContextConfiguration(classes = CucumberScratchConfiguration.class)
public class UserRetrieveSteps {

    @Autowired
    private PropertyObject user;

    @Autowired
    private WebTarget client;

    @Autowired
    private Responses responses;

    @When("^I request an existing user$")
    public void I_request_an_existing_user() {

        final ClientResponse createResponse = responses.created().latest();

        @SuppressWarnings("unchecked")
        final Map<String, Object> body = createResponse.readEntity(Map.class);

        I_request_a_user_with_an_ID_of(body.get(ID).toString());
    }

    @When("^I request all existing user$")
    public void I_request_all_existing_user() {

        I_request_a_user_with_an_ID_of("");
    }

    @When("^I request a user with an ID of \"([^\"]*)\"$")
    public void I_request_a_user_with_an_ID_of(String id) {

        client.path(id).request(MediaType.APPLICATION_JSON_TYPE).get();
    }


    @Then("^the response body should contain the (?:new|requested|updated) user$")
    @SuppressWarnings("unchecked")
    public void the_response_body_should_contain_the_user() {

        final PropertyObject expected = new PropertyObject(user);

        final PropertyObject actual = new PropertyObject(responses.latest().readEntity(Map.class));
        actual.remove(ID);
        actual.remove(ADDRESS_ID);

        assertEquals("the response body should contain the user.", expected, actual);
    }

    @Then("^the response body should contain all the requested users$")
    @SuppressWarnings("unchecked")
    public void the_response_body_should_contain_all_the_requested_users() {

        final Set<Map<String, Object>> retrievedUsers = responses.latest().readEntity(Set.class);

        final Set<Map<String, Object>> createdUsers = new HashSet<>();
        for (ClientResponse response : responses.created()) {

            createdUsers.add(response.readEntity(Map.class));
        }

        assertEquals("the number of retrieved users should equal the number of create users.", createdUsers.size(),
                retrievedUsers.size());

        assertEquals("the retrieved users equal create users.", createdUsers, retrievedUsers);
    }
}
