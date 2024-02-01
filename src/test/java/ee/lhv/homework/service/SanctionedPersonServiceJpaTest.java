package ee.lhv.homework.service;

import ee.lhv.homework.AbstractJpaTest;
import ee.lhv.homework.entity.SanctionedPerson;
import ee.lhv.homework.repository.SanctionedPersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class SanctionedPersonServiceJpaTest extends AbstractJpaTest {

    @Autowired
    private SanctionedPersonRepository sanctionedPersonRepository;

    private SanctionedPersonService sanctionedPersonService;

    @BeforeEach
    void beforeEach() {
        sanctionedPersonService = new SanctionedPersonService(sanctionedPersonRepository);
    }

    @Sql("/sql/SanctionedPersonServiceJpaTest/testThatGetIsSuccessful.sql")
    @Test
    void testThatGetIsSuccessful() {
        // given
        Long id = 12345L;

        // when
        SanctionedPerson sanctionedPerson = sanctionedPersonService.get(id);

        // then
        assertThat(sanctionedPerson, is(notNullValue()));
    }

}