package com.nullvariable.devlog.repositories;

import com.nullvariable.devlog.entites.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, String> {
    @Query("SELECT u FROM PostEntity u WHERE u.uuid = :uuid")
    Optional<PostEntity> findOneByUUID(@Param("uuid") String uuid);

    @Query("SELECT u FROM PostEntity u ORDER BY u.createdAt DESC LIMIT :limit")
    List<PostEntity> findAllLimitOrderByCreatedAtDesc(@Param("limit") int limit);
}
