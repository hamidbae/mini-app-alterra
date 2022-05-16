package com.alterra.miniapp.repository;

import com.alterra.miniapp.domain.dao.Comment;
import com.alterra.miniapp.domain.dao.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.plant.id = ?1"
            + " ORDER BY c.createdAt DESC")
    public List<Comment> searchByPlantId(Long id);
}
