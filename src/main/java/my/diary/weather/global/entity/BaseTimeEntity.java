package my.diary.weather.global.entity;

// build.gradle에 spring-boot-starter-data-jpa 포함되어 있으면 OK
// @EnableJpaAuditing 은 설정 클래스에 추가해야 함

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz")
    protected OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamptz")
    protected OffsetDateTime updatedAt;
}

