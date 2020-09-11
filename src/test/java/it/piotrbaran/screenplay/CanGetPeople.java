package it.piotrbaran.screenplay;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.thucydides.core.util.EnvironmentVariables;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;

@RunWith(SerenityRunner.class)
public class CanGetPeople {

    private String theRestApiBaseUrl;
    private EnvironmentVariables environmentVariables;
    private Actor sam;

    @Before
    public void configureBaseUrl() {
        theRestApiBaseUrl = environmentVariables.optionalProperty("restapi.baseurl")
                .orElse("https://reqres.in/api");

        sam = Actor.named("Sam the supervisor").whoCan(CallAnApi.at(theRestApiBaseUrl));
    }

    @Test
    public void should_get_valid_response() {
        sam.attemptsTo(
                Get.resource("people/1")
        );

        sam.should(
                seeThatResponse("Response is correct",
                        response -> response.statusCode(200)
                            .body("name", equalTo("Luke Skywalker"))
                )
        );
    }

    @Test
    public void should_get_all_the_planets() {
        sam.attemptsTo(
                Get.resource("planets")
        );

        sam.should(
                seeThatResponse("Response shows all the planets",
                        response -> response.statusCode(200)
                            .body("results.name", hasItems("Tatooine", "Hoth", "Dagobah")))
        );
    }
}
