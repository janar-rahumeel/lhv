package ee.lhv.homework.service;

import ee.lhv.homework.controller.model.SanctionedPersonData;
import ee.lhv.homework.entity.SanctionedPerson;
import ee.lhv.homework.repository.SanctionedPersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SanctionedPersonService implements CrudSupport<SanctionedPerson, SanctionedPersonData, Long> {

    private final SanctionedPersonMatcherService sanctionedPersonMatcherService;
    private final SanctionedPersonRepository sanctionedPersonRepository;

    @Override
    public SanctionedPerson get(Long id) {
        return sanctionedPersonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sanctioned person not found: " + id));
    }

    @Override
    public SanctionedPerson insert(SanctionedPersonData newSanctionedPersonData) {
        SanctionedPerson newSanctionedPerson = SanctionedPerson.builder().fullName(newSanctionedPersonData.getFullName()).build();
        SanctionedPerson sanctionedPerson = sanctionedPersonRepository.save(newSanctionedPerson);
        updatePattern(sanctionedPerson);
        return sanctionedPerson;
    }

    @Override
    public SanctionedPerson update(Long id, SanctionedPersonData updatedSanctionedPersonData) {
        SanctionedPerson updatedSanctionedPerson = get(updatedSanctionedPersonData.getId());
        updatedSanctionedPerson.setFullName(updatedSanctionedPersonData.getFullName());
        SanctionedPerson sanctionedPerson = sanctionedPersonRepository.save(updatedSanctionedPerson);
        updatePattern(sanctionedPerson);
        return sanctionedPerson;
    }

    private void updatePattern(SanctionedPerson sanctionedPerson) {
        SanctionedPersonMatcherService.SANCTIONED_PERSON_PATTERNS.put(sanctionedPerson.getId(), sanctionedPersonMatcherService.map(sanctionedPerson.getFullName()));
    }

    @Override
    public void delete(Long id) {
        sanctionedPersonRepository.deleteById(id);
        SanctionedPersonMatcherService.SANCTIONED_PERSON_PATTERNS.remove(id);
    }

}
