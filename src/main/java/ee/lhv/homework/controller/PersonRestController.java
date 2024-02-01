package ee.lhv.homework.controller;

import ee.lhv.homework.controller.model.PersonData;
import ee.lhv.homework.controller.model.SanctionedData;
import ee.lhv.homework.service.SanctionedPersonMatcherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional(readOnly = true)
@RestController
@RequestMapping(path = "/persons")
@RequiredArgsConstructor
public class PersonRestController {

    private final SanctionedPersonMatcherService sanctionedPersonMatcherService;

    @PostMapping("/is-sanctioned")
    public ResponseEntity<SanctionedData> isSanctioned(@Valid @RequestBody PersonData personData) {
        boolean sanctioned = sanctionedPersonMatcherService.isSanctioned(personData.getFullName());
        SanctionedData sanctionedData = SanctionedData.builder().sanctioned(sanctioned).build();
        return ResponseEntity.ok(sanctionedData);
    }

}
