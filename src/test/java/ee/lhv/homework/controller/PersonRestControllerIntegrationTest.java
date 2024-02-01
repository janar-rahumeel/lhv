package ee.lhv.homework.controller;

import ee.lhv.homework.AbstractRestControllerIntegrationTest;
import ee.lhv.homework.controller.model.SanctionedData;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class PersonRestControllerIntegrationTest extends AbstractRestControllerIntegrationTest {

    @Test
    void testThatIsSanctionedIsSuccessful() {
        // given
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>("{\"fullName\": \" Mr. sanctioned Personny\"}", httpHeaders);

        // when
        ResponseEntity<SanctionedData> responseEntity = testRestTemplate
                .exchange("/persons/is-sanctioned", HttpMethod.POST, httpEntity, SanctionedData.class);

        // then
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));

        SanctionedData sanctionedData = responseEntity.getBody();
        assertThat(sanctionedData, is(notNullValue()));
        assertThat(sanctionedData.isSanctioned(), is(true));
    }

}