package ee.lhv.homework.service;

import ee.lhv.homework.repository.SanctionedPersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SanctionedPersonMatcherService implements InitializingBean {

    static final Map<Long, Set<String>> SANCTIONED_PERSON_LOWER_CASE_NAMES = new ConcurrentHashMap<>();
    private static final int REQUIRED_MINIMUM_MATCH_COUNT = 2;
    private static final LevenshteinDistance LEVENSHTEIN_DISTANCE = new LevenshteinDistance(1);

    private final SanctionedPersonRepository sanctionedPersonRepository;

    @Override
    public void afterPropertiesSet() {
        StreamSupport.stream(sanctionedPersonRepository.findAll().spliterator(), false)
                .forEach(sanctionedPerson -> SANCTIONED_PERSON_LOWER_CASE_NAMES.put(sanctionedPerson.getId(), map(sanctionedPerson.getFullName())));
    }

    Set<String> map(String fullName) {
        return Arrays.stream(fullName.toLowerCase().split(" ")).collect(Collectors.toUnmodifiableSet());
    }

    public boolean isSanctioned(String fullName) {
        String[] personNames = Arrays.stream(fullName.toLowerCase().split(" ")).sorted().toArray(String[]::new);
        for (Set<String> sanctionedPersonNames : SANCTIONED_PERSON_LOWER_CASE_NAMES.values()) {
            log.info("Comparing person names {} by sanctioned person names {}", personNames, sanctionedPersonNames);
            if (sanctioned(personNames, new HashSet<>(sanctionedPersonNames))) {
                return true;
            }
        }
        return false;
    }

    private boolean sanctioned(String[] personNames, Set<String> sanctionedPersonNames) {
        int matchesCount = 0;
        for (int p = 1; p <= personNames.length; p++) {
            String personName = personNames[p - 1];
            if (matchesCount == REQUIRED_MINIMUM_MATCH_COUNT) {
                return true;
            }
            if (sanctionedPersonNames.contains(personName)) {
                sanctionedPersonNames.remove(personName);
                log.info("Matches count is {}", ++matchesCount);
                continue;
            }
            for (String sanctionedPersonName : sanctionedPersonNames) {
                int levenshteinDistance = LEVENSHTEIN_DISTANCE.apply(personName, sanctionedPersonName);
                log.info("Levenshtein distance for [{}, {}] is {}", personName, sanctionedPersonName, levenshteinDistance);
                if (levenshteinDistance != -1) {
                    sanctionedPersonNames.remove(sanctionedPersonName);
                    log.info("Matches count is {}", ++matchesCount);
                    break;
                }
            }
            int possibleMatchesCount = matchesCount + personNames.length - p;
            if (possibleMatchesCount < REQUIRED_MINIMUM_MATCH_COUNT) {
                log.info("Unable to fulfil required minimum match count ({} < {}). Skipping ...", possibleMatchesCount, REQUIRED_MINIMUM_MATCH_COUNT);
                return false;
            }
        }
        return matchesCount >= REQUIRED_MINIMUM_MATCH_COUNT;
    }

}
