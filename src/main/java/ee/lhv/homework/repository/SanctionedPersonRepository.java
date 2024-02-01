package ee.lhv.homework.repository;

import ee.lhv.homework.entity.SanctionedPerson;
import org.springframework.data.repository.CrudRepository;

public interface SanctionedPersonRepository extends CrudRepository<SanctionedPerson, Long> {
}
