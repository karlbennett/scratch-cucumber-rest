package scratch.cucumber.rest.steps;

import org.glassfish.jersey.client.ClientResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserStepsTest {

    @Mock
    private PropertyObject user;

    @Mock
    private Responses responses;

    @InjectMocks
    private UserSteps steps;

    @Test
    public void I_can_prepare_a_new_user() {

        steps.there_is_a_new_user();

        verify(user).clear();
    }

    @Test
    public void I_can_set_the_property_of_a_user() {

        final String name = "name";
        final String value = "value";

        steps.the_user_has_an_of(name, value);

        verify(user).set(name, value);
        verifyNoMoreInteractions(user);
    }

    @Test
    public void I_can_set_the_property_of_a_user_to_an_integer() {

        final String name = "name";
        final int value = 1;

        steps.the_user_has_a_of(name, String.valueOf(value));

        verify(user).set(name, value);
        verifyNoMoreInteractions(user);
    }

    @Test(expected = NumberFormatException.class)
    public void I_cannot_set_the_int_property_of_a_user_to_a_string() {

        steps.the_user_has_a_of("name", "not an int");
    }

    @Test
    public void I_can_set_the_property_name_to_nothing_and_no_property_is_set() {

        steps.the_user_has_an_of("", "value");

        verifyZeroInteractions(user);
    }

    @Test
    public void I_can_set_the_property_value_to_the_string_null_and_the_property_is_set_to_null() {

        final String name = "name";

        steps.the_user_has_an_of(name, "NULL");
        steps.the_user_has_an_of(name, "null");

        verify(user, times(2)).set(name, null);
        verifyNoMoreInteractions(user);
    }

    @Test
    public void I_can_set_the_property_value_to_an_empty_string_for_an_integer_and_the_property_is_set_to_null() {

        steps.the_user_has_a_of("name", "");

        verify(user).set("name", null);
        verifyNoMoreInteractions(user);
    }

    @Test
    public void I_can_check_the_status_code_of_the_last_response() {

        final int status = 999;

        final ClientResponse response = mock(ClientResponse.class);
        when(response.getStatus()).thenReturn(status);

        when(responses.latest()).thenReturn(response);

        steps.I_should_receive_a_status_code_of(status);
    }

    @Test(expected = AssertionError.class)
    public void I_can_check_that_the_status_code_of_the_last_response_is_wrong() {

        final int status = 999;

        final ClientResponse response = mock(ClientResponse.class);
        when(response.getStatus()).thenReturn(status);

        when(responses.latest()).thenReturn(response);

        steps.I_should_receive_a_status_code_of(998);
    }

    @Test
    public void I_can_check_that_the_response_body_is_empty() {

        final ClientResponse response = mock(ClientResponse.class);
        when(response.readEntity(String.class)).thenReturn("");

        when(responses.latest()).thenReturn(response);

        steps.the_response_body_should_be_empty();
    }

    @Test
    public void I_can_check_that_the_response_body_is_null() {

        when(responses.latest()).thenReturn(mock(ClientResponse.class));

        steps.the_response_body_should_be_empty();
    }

    @Test(expected = AssertionError.class)
    public void I_can_check_that_the_response_body_is_not_empty() {

        final ClientResponse response = mock(ClientResponse.class);
        when(response.readEntity(String.class)).thenReturn("not empty");

        when(responses.latest()).thenReturn(response);

        steps.the_response_body_should_be_empty();
    }

    @Test
    public void I_can_remove_a_field_from_the_user() {

        final String fieldName = "test field";

        steps.the_user_has_no_field(fieldName);

        verify(user).remove(fieldName);
        verifyNoMoreInteractions(user);
    }
}
