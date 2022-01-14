package com.sentimentanalysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sentimentanalysis.model.CommonDataTextReaction;

@Repository
public interface CommonDataTextReactionRepository extends JpaRepository<CommonDataTextReaction,Long>{

}
