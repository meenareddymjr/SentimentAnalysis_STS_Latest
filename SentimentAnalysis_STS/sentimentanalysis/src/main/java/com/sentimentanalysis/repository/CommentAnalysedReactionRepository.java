package com.sentimentanalysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sentimentanalysis.model.CommentAnalysedReaction;

@Repository
public interface CommentAnalysedReactionRepository extends JpaRepository<CommentAnalysedReaction,Long>{

}
