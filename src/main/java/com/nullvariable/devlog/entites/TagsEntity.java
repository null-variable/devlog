package com.nullvariable.devlog.entites;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@Table(name = "TagsEntity")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TagsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", length = 36, columnDefinition = "varchar(36)", nullable = false)
    private String uuid;

    @Column(name = "name", length = 20, columnDefinition = "varchar(20)", nullable = false, unique = true)
    private String name;

    @CreatedDate
    @Column(updatable = false, nullable = false, name = "createdAt")
    private LocalDateTime createdAt;
}
