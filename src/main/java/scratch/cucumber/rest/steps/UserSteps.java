package scratch.cucumber.rest.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Map;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static scratch.cucumber.rest.steps.StatusMatcher.statusEquals;
import static scratch.cucumber.rest.steps.UserFields.ADDRESS_ID;
import static scratch.cucumber.rest.steps.UserFields.ID;

@ContextConfiguration(classes = CucumberScratchConfiguration.class)
@Component
public class UserSteps {

    @Autowired
    private PropertyObject user;

    @Autowired
    private WebTarget client;

    @Autowired
    private Responses responses;

    @Given("^there is a(?:nother)? new user$")
    public void there_is_a_new_user() {

        user.clear();
    }

    @Given("^the user has a[n]? \"([^\"]*)\" of \"([^\"]*)\"$")
    public void the_user_has_an_of(final String propertyPath, final String value) {

        the_user_has_an_of(user, propertyPath, value, new Runnable() {
            @Override
            public void run() {

                user.set(propertyPath, value);
            }
        });
    }

    @Given("^the user has a[n]? \"([^\"]*)\" of ?(\\w*)$")
    public void the_user_has_a_of(final String propertyPath, final String value) {

        the_user_has_an_of(user, propertyPath, value, new Runnable() {
            @Override
            public void run() {

                if ("".equals(value)) {
                    user.set(propertyPath, null);
                    return;
                }

                user.set(propertyPath, Integer.valueOf(value));
            }
        });
    }

    @Then("^I should receive a status code of (\\d+)$")
    public void I_should_receive_a_status_code_of(int status) {

        assertThat("the response HTTP status code should be correct.", new VerboseResponse(responses.latest()),
                statusEquals(status));
    }

    @Then("^the response body should be empty$")
    public void the_response_body_should_be_empty() {

        final String body = responses.latest().readEntity(String.class);

        assertThat("the response body should be empty.", body, isEmptyOrNullString());
    }

    @When("^the user has no \"([^\"]*)\" field$")
    public void the_user_has_no_field(String fieldName) {

        user.remove(fieldName);
    }

    private static void the_user_has_an_of(PropertyObject user, String propertyPath, String value, Runnable setter) {

        if ("".equals(propertyPath)) {
            // We ignore empty property names so that it is possible to write Scenario Examples that don't set some
            // properties.
            return;
        }

        if ("NULL".equals(value) || "null".equals(value)) {
            user.set(propertyPath, null);
            return;
        }

        setter.run();
    }

    public void the_user_should_be_persisted_with_id(Integer id) {

        @SuppressWarnings("unchecked")
        final PropertyObject actual = new PropertyObject(
                client.path(id.toString())
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .get()
                        .readEntity(Map.class)
        );

        user.set(ID, id);

        final Object addressId = actual.get(ADDRESS_ID);

        if (null != addressId) {
            user.set(ADDRESS_ID, addressId);
        }

        assertEquals("the user should have been persisted.", user.toMap(), actual.toMap());
    }
}
