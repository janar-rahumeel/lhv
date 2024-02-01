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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class SanctionedPersonServiceTest {

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
    void testThatInsertIsSuccessful() {
        // given
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0)).when(sanctionedPersonRepository).save(any(SanctionedPerson.class));

        SanctionedPersonData sanctionedPersonData = SanctionedPersonData.builder().fullName("Inserted Name").build();

        // when
        SanctionedPerson sanctionedPerson = sanctionedPersonService.insert(sanctionedPersonData);

        // then
        assertThat(sanctionedPerson.getFullName(), is("Inserted Name"));
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
    void testThatDeleteIsSuccessful() {
        // given
        Long id = 1L;

        doAnswer(invocationOnMock -> null).when(sanctionedPersonRepository).deleteById(id);

        // when
        sanctionedPersonService.delete(id);
    }

}