package ee.lhv.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class SanctionedPersonMatcherServiceTest {

    @InjectMocks
    private SanctionedPersonMatcherService sanctionedPersonMatcherService;

    @BeforeEach
    void beforeEach() {
        SanctionedPersonMatcherService.SANCTIONED_PERSON_LOWER_CASE_NAMES.clear();
        SanctionedPersonMatcherService.SANCTIONED_PERSON_LOWER_CASE_NAMES.put(1L, Set.of("osama", "bin", "laden"));
    }

    private static Stream<Arguments> provideSanctionedPersonNameCombinations() {
        return Stream.of(
                Arguments.of("Osama Laden", true),
                Arguments.of("Osama Bin Laden", true),
                Arguments.of("Bin Laden, Osama", true),
                Arguments.of("Laden Osama Bin", true),
                Arguments.of("to the osama bin laden", true),
                Arguments.of("osama and bin laden", true),
                Arguments.of("Mr. Osama Laden", true),
                Arguments.of("Ossaka Can Biden", false),
                Arguments.of("Osaka Can Biden", false)
        );
    }

    @MethodSource("provideSanctionedPersonNameCombinations")
    @ParameterizedTest
    void testThatIsSanctionedIsSuccessful(String sanctionedPersonFullName, boolean expectedIsSanctioned) {
        // given

        // when
        boolean actualIsSanctioned = sanctionedPersonMatcherService.isSanctioned(sanctionedPersonFullName);

        // then
        assertThat(actualIsSanctioned, is(expectedIsSanctioned));
    }

}