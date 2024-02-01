package ee.lhv.homework.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@NoArgsConstructor
abstract class AbstractEntity<ID> implements Serializable {

    @Column(name = "CREATED_AT")
    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "MODIFIED_AT")
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    public abstract ID getId();

}
