package com.nullvariable.devlog.entites;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import java.util.Optional;

@Builder
@Entity
@Getter
@Setter
@Table(name = "CommentEntity")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", length = 36, columnDefinition = "varchar(36)", nullable = false)
    private String uuid;

    @Column(name = "content", columnDefinition = "text", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target", referencedColumnName = "uuid")
    private PostEntity target;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "author", referencedColumnName = "uuid", nullable = true)
    private Optional<AuthEntity> author;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "parent", referencedColumnName = "uuid", nullable = true)
    private Optional<CommentEntity> parent;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", orphanRemoval = true)
    @JoinColumn(name = "children", referencedColumnName = "uuid")
    private List<CommentEntity> children;
}
