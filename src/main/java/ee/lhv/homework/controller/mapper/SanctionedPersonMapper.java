package ee.lhv.homework.controller.mapper;

import ee.lhv.homework.controller.model.SanctionedPersonData;
import ee.lhv.homework.entity.SanctionedPerson;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SanctionedPersonMapper {

    SanctionedPersonData map(SanctionedPerson sanctionedPerson);

}
