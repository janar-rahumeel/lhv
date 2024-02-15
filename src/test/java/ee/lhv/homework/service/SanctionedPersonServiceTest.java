package ee.lhv.homework.service;

import ee.lhv.homework.controller.model.SanctionedPersonData;
import ee.lhv.homework.entity.SanctionedPerson;
import ee.lhv.homework.repository.SanctionedPersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class SanctionedPersonServiceTest {

    @Mock
    private SanctionedPersonMatcherService sanctionedPersonMatcherService;

    @Mock
    private SanctionedPersonRepository sanctionedPersonRepository;

    @InjectMocks
    private SanctionedPersonService sanctionedPersonService;

    @Test
    void testThatGetIsSuccessful() {
        // given
        Long id = 1L;

        SanctionedPerson sanctionedPerson = SanctionedPerson.builder().id(id).build();
        given(sanctionedPersonRepository.findById(id)).willReturn(Optional.of(sanctionedPerson));

        // when
        SanctionedPerson actualSanctionedPerson = sanctionedPersonService.get(id);

        // then
        assertThat(actualSanctionedPerson, is(sanctionedPerson));
    }

    @Test
    void testThatInsertIsSuccessful(@Mock Set<String> mockedSetOfPersonNames) {
        // given
        Long id = 4L;
        String fullName = "Inserted Name";

        doAnswer(invocationOnMock -> {
            SanctionedPerson sanctionedPerson = invocationOnMock.getArgument(0);
            sanctionedPerson.setId(id);
            return sanctionedPerson;
        }).when(sanctionedPersonRepository).save(any(SanctionedPerson.class));

        given(sanctionedPersonMatcherService.map(fullName)).willReturn(mockedSetOfPersonNames);

        SanctionedPersonData sanctionedPersonData = SanctionedPersonData.builder().fullName(fullName).build();

        // when
        SanctionedPerson sanctionedPerson = sanctionedPersonService.insert(sanctionedPersonData);

        // then
        assertThat(sanctionedPerson.getFullName(), is(fullName));
        assertThat(SanctionedPersonMatcherService.SANCTIONED_PERSON_LOWER_CASE_NAMES.containsKey(id), is(true));
    }

    @Test
    void testThatUpdateIsNotSuccessful() {
        // given
        Long id = 1L;

        given(sanctionedPersonRepository.findById(id)).willReturn(Optional.empty());

        SanctionedPersonData updatedSanctionedPersonData = SanctionedPersonData.builder().id(id).fullName("Updated Name").build();

        // when
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> sanctionedPersonService.update(id, updatedSanctionedPersonData));

        // then
        assertThat(entityNotFoundException.getMessage(), is("Sanctioned person not found: 1"));
    }

    @Test
    void testThatDeleteIsSuccessful(@Mock Set<String> mockedSetOfPersonNames) {
        // given
        Long id = 1L;

        SanctionedPersonMatcherService.SANCTIONED_PERSON_LOWER_CASE_NAMES.put(id, mockedSetOfPersonNames);

        doAnswer(invocationOnMock -> null).when(sanctionedPersonRepository).deleteById(id);

        // when
        sanctionedPersonService.delete(id);

        // then
        assertThat(SanctionedPersonMatcherService.SANCTIONED_PERSON_LOWER_CASE_NAMES.containsKey(id), is(false));
    }

}