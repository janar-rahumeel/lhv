package ee.lhv.homework.controller;

import ee.lhv.homework.AbstractRestControllerIntegrationTest;
import ee.lhv.homework.controller.model.EntityFieldValidationErrorData;
import ee.lhv.homework.controller.model.ErrorData;
import ee.lhv.homework.controller.model.SanctionedPersonData;
import ee.lhv.homework.entity.SanctionedPerson;
import ee.lhv.homework.repository.SanctionedPersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class SanctionedPersonRestControllerIntegrationTest extends AbstractRestControllerIntegrationTest {

    @Autowired
    private SanctionedPersonRepository sanctionedPersonRepository;

    @Test
    void testThatInsertIsNotSuccessful() {
        // given
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>("{}", httpHeaders);

        // when
        ResponseEntity<ErrorData> responseEntity = testRestTemplate
                .exchange("/sanctioned-persons", HttpMethod.POST, httpEntity, ErrorData.class);

        // then
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.UNPROCESSABLE_ENTITY));

        ErrorData errorData = responseEntity.getBody();
        assertThat(errorData, is(notNullValue()));
        assertThat(errorData.getEntityFieldValidationErrors().size(), is(1));

        EntityFieldValidationErrorData entityFieldValidationErrorData = errorData.getEntityFieldValidationErrors().get(0);
        assertThat(entityFieldValidationErrorData.getFieldName(), is("fullName"));
        assertThat(entityFieldValidationErrorData.getValidationErrorMessage(), is("Required"));
    }

    @Test
    void testThatInsertIsSuccessful() {
        // given
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>("{\"fullName\": \"Sanction Personny\"}", httpHeaders);

        // when
        ResponseEntity<SanctionedPersonData> responseEntity = testRestTemplate
                .exchange("/sanctioned-persons", HttpMethod.POST, httpEntity, SanctionedPersonData.class);

        // then
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));

        SanctionedPersonData sanctionedPersonData = responseEntity.getBody();
        assertThat(sanctionedPersonData, is(notNullValue()));
        assertThat(sanctionedPersonData.getId(), is(notNullValue()));
        assertThat(sanctionedPersonData.getFullName(), is("Sanction Personny"));

        SanctionedPerson sanctionedPerson = sanctionedPersonRepository.findAll().iterator().next();
        assertThat(sanctionedPerson.getId(), is(sanctionedPersonData.getId()));
    }

    @Test
    void testThatUpdateIsNotSuccessful() {
        // given
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>("{\"id\": 123456789, \"fullName\": \"New Name\"}", httpHeaders);

        // when
        ResponseEntity<ErrorData> responseEntity = testRestTemplate
                .exchange("/sanctioned-persons/123456789", HttpMethod.PUT, httpEntity, ErrorData.class);

        // then
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        ErrorData errorData = responseEntity.getBody();
        assertThat(errorData, is(notNullValue()));
        assertThat(errorData.getUuid(), is(notNullValue()));
        assertThat(errorData.getMessage(), is("Bad Request"));
    }

    @Sql("/sql/SanctionedPersonRestControllerIntegrationTest/testThatUpdateIsSuccessful.sql")
    @Test
    void testThatUpdateIsSuccessful() {
        // given
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>("{\"id\": 100, \"fullName\": \"New Name\"}", httpHeaders);

        // when
        ResponseEntity<SanctionedPersonData> responseEntity = testRestTemplate
                .exchange("/sanctioned-persons/100", HttpMethod.PUT, httpEntity, SanctionedPersonData.class);

        // then
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), is(notNullValue()));
        assertThat(responseEntity.getBody().getFullName(), is("New Name"));

        SanctionedPerson sanctionedPerson = sanctionedPersonRepository.findById(100L).get();
        assertThat(sanctionedPerson.getFullName(), is("New Name"));
    }

    @Sql("/sql/SanctionedPersonRestControllerIntegrationTest/testThatDeleteIsSuccessful.sql")
    @Test
    void testThatDeleteIsSuccessful() {
        // given
        HttpEntity<String> httpEntity = new HttpEntity<>(null);

        // when
        ResponseEntity<ErrorData> responseEntity = testRestTemplate
                .exchange("/sanctioned-persons/123", HttpMethod.DELETE, httpEntity, ErrorData.class);

        // then
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));

        boolean exists = sanctionedPersonRepository.existsById(123L);
        assertThat(exists, is(false));
    }

}