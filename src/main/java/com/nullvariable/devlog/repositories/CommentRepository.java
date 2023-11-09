package com.nullvariable.devlog.repositories;

import com.nullvariable.devlog.entites.CommentEntity;
import com.nullvariable.devlog.entites.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String> {
    @Query("SELECT u FROM CommentEntity u WHERE u.uuid = :uuid")
    Optional<CommentEntity> findOneByUUID(@Param("uuid") String uuid);

    @Query("SELECT u FROM CommentEntity u ORDER BY u.createdAt DESC LIMIT :limit")
    List<CommentEntity> findAllLimitOrderByCreatedAtDesc(@Param("limit") int limit);
}
