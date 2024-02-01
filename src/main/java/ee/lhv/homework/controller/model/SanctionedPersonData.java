package ee.lhv.homework.controller.model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
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
public class SanctionedPersonData {

    @Nullable
    private Long id;

    @NotNull
    private String fullName;

}
