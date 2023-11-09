package com.nullvariable.devlog.repositories;

import com.nullvariable.devlog.entites.PostEntity;
import com.nullvariable.devlog.entites.TagsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagsRepository extends JpaRepository<TagsEntity, String> {
    @Query("SELECT u FROM TagsEntity u WHERE u.uuid = :uuid")
    Optional<TagsEntity> findOneByUUID(@Param("uuid") String uuid);

    @Query("SELECT u FROM TagsEntity u WHERE u.name = :name")
    Optional<TagsEntity> findOneByName(@Param("name") String name);

    @Query("SELECT u FROM TagsEntity u ORDER BY u.createdAt DESC LIMIT :limit")
    List<TagsEntity> findAllLimitOrderByCreatedAtDesc(@Param("limit") int limit);
}
