package scratch.cucumber.rest.steps;


import org.glassfish.jersey.client.ClientResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static scratch.cucumber.rest.steps.CucumberScratchConfiguration.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CucumberScratchConfiguration.class)
public class CucumberScratchConfigurationTest {

    @Autowired
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer;

    @Autowired
    public PropertyObject user;

    @Autowired
    public Responses responses;

    @Autowired
    public WebTarget client;

    @Test
    public void I_can_inject_the_properties_place_holder_configurer() {

        assertNotNull("the property placeholder configurer should be injected.", propertyPlaceholderConfigurer);
    }

    @Test
    public void I_can_inject_the_user() {

        assertNotNull("the user should be injected.", user);
    }

    @Test
    public void I_can_inject_the_responses() {

        assertNotNull("the responses should be injected.", responses);
    }

    @Test
    public void I_can_inject_the_client() {

        assertNotNull("the client should be injected.", client);
    }

    @Test
    public void I_can_record_a_response_made_through_the_client() {

        final Responses responses = mock(Responses.class);
        final ClientResponse response = mock(ClientResponse.class);

        new ResponsesFilter(responses).filter(null, response);

        verify(response).bufferEntity();
        verify(responses).add(response);
    }
}
