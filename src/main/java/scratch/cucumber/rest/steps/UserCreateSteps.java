package scratch.cucumber.rest.steps;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Map;

import static java.util.Collections.singleton;
import static javax.ws.rs.client.Entity.json;
import static org.junit.Assert.assertEquals;
import static scratch.cucumber.rest.steps.UserFields.ID;

@ContextConfiguration(classes = CucumberScratchConfiguration.class)
public class UserCreateSteps {

    @Autowired
    private PropertyObject user;

    @Autowired
    private WebTarget client;

    @Autowired
    private Responses responses;

    @When("^I create the user(?: again)?$")
    public void I_create_the_user() {

        client.request(MediaType.APPLICATION_JSON_TYPE).post(json(user.toMap()));
    }

    @Then("^the(?: new)? user should be (?:persisted|updated)$")
    public void the_new_user_should_be() {

        @SuppressWarnings("unchecked")
        final Map<String, Object> body = responses.latest().readEntity(Map.class);

        assertEquals("the user should have been persisted.", body, get(body.get(ID).toString()));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> get(String id) {

        return client.path(id).request(MediaType.APPLICATION_JSON_TYPE).get().readEntity(Map.class);
    }

    @Then("^the response body should contain an id$")
    public void the_response_body_should_contain_an_id() {

        final Map body = responses.latest().readEntity(Map.class);

        assertEquals("the response should only contain the id.", singleton(ID), body.keySet());
    }
}
