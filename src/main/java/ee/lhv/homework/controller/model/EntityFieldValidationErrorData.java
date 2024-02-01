package ee.lhv.homework.controller.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityFieldValidationErrorData {

    private String fieldName;

    private String validationErrorMessage;

}
