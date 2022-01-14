package com.sentimentanalysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sentimentanalysis.model.TokenData;

@Repository
public interface TokenDataRepository extends JpaRepository<TokenData,Long>{

}
