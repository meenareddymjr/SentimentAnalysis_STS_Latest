package com.sentimentanalysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sentimentanalysis.model.Comments;

@Repository
public interface CommentRepository extends JpaRepository<Comments,Long>{

}


