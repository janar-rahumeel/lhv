package ee.lhv.homework.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Immutable;

@Table(name = "SANCTIONED_PERSON")
@Getter
@Entity
@Builder
@ToString
@Immutable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SanctionedPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SANCTIONED_PERSON")
    @SequenceGenerator(name = "SEQ_SANCTIONED_PERSON", initialValue = 1000, allocationSize = 1)
    private Long id;

    @Setter
    @Column(name = "FULL_NAME")
    @NotBlank
    private String fullName;

}
