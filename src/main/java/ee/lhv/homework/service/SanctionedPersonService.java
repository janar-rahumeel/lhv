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

    private final SanctionedPersonRepository sanctionedPersonRepository;

    @Override
    public SanctionedPerson get(Long id) {
        return sanctionedPersonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sanctioned person not found: " + id));
    }

    @Override
    public SanctionedPerson insert(SanctionedPersonData newSanctionedPersonData) {
        SanctionedPerson sanctionedPerson = SanctionedPerson.builder().fullName(newSanctionedPersonData.getFullName()).build();
        return sanctionedPersonRepository.save(sanctionedPerson);
    }

    @Override
    public SanctionedPerson update(Long id, SanctionedPersonData updatedSanctionedPersonData) {
        SanctionedPerson sanctionedPerson = get(updatedSanctionedPersonData.getId());
        sanctionedPerson.setFullName(updatedSanctionedPersonData.getFullName());
        return sanctionedPersonRepository.save(sanctionedPerson);
    }

    @Override
    public void delete(Long id) {
        sanctionedPersonRepository.deleteById(id);
    }

}
