package ee.lhv.homework.controller;

import ee.lhv.homework.controller.mapper.SanctionedPersonMapper;
import ee.lhv.homework.controller.model.SanctionedPersonData;
import ee.lhv.homework.entity.SanctionedPerson;
import ee.lhv.homework.repository.SanctionedPersonRepository;
import ee.lhv.homework.service.SanctionedPersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Transactional
@RestController
@RequestMapping(path = "/sanctioned-persons")
@RequiredArgsConstructor
public class SanctionedPersonRestController {

    private static final SanctionedPersonMapper MAPPER = Mappers.getMapper(SanctionedPersonMapper.class);

    private final SanctionedPersonService sanctionedPersonService;
    private final SanctionedPersonRepository sanctionedPersonRepository;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SanctionedPersonData> insert(@Valid @RequestBody SanctionedPersonData newSanctionedPersonData) {
        SanctionedPerson sanctionedPerson = sanctionedPersonService.insert(newSanctionedPersonData);
        SanctionedPersonData sanctionedPersonData = MAPPER.map(sanctionedPerson);
        return ResponseEntity.ok(sanctionedPersonData);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SanctionedPersonData> update(@PathVariable("id") Long id, @Valid @RequestBody SanctionedPersonData updatedSanctionedPersonData) {
        if (!sanctionedPersonRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sanctioned person not found: " + id);
        }
        SanctionedPerson sanctionedPerson = sanctionedPersonService.update(id, updatedSanctionedPersonData);
        SanctionedPersonData sanctionedPersonData = MAPPER.map(sanctionedPerson);
        return ResponseEntity.ok(sanctionedPersonData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        sanctionedPersonService.delete(id);
        return ResponseEntity.ok().build();
    }

}
