package ee.lhv.homework.service;

import ee.lhv.homework.repository.SanctionedPersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SanctionedPersonMatcherService implements InitializingBean {

    static final Map<Long, Pattern> SANCTIONED_PERSON_PATTERNS = new HashMap<>();

    private final SanctionedPersonRepository sanctionedPersonRepository;

    @Override
    public void afterPropertiesSet() {
        StreamSupport.stream(sanctionedPersonRepository.findAll().spliterator(), false)
                .forEach(sanctionedPerson -> SANCTIONED_PERSON_PATTERNS.put(sanctionedPerson.getId(), map(sanctionedPerson.getFullName())));
    }

    Pattern map(String fullName) {
        String pattern = String.format("^.*%s.*$", Arrays.stream(fullName.split(" ")).map(nameFragment -> "(\\s?(" + nameFragment + ")\\s?){1}").collect(Collectors.joining()));
        log.info("Pattern: " + pattern);
        return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    }

    public boolean isSanctioned(String fullName) {
        return SANCTIONED_PERSON_PATTERNS.values().stream().anyMatch(pattern -> pattern.matcher(fullName).matches());
    }

}
