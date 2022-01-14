package com.sentimentanalysis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tokens")
public class TokenData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long tokenId;
	
	@Column(name = "token_text")
	private String[] tokenText;
	
	@Column(name = "token_tags_POS")
	private String[] tokenTag;
	
	@Column(name = "token_count")
	private int count;
	
	

	public TokenData() {
		
	}
	
	public TokenData(String[] tokenText,String[] tokenTag) {
		super();
		this.tokenText = tokenText;
		this.tokenTag = tokenTag;
	}

	public TokenData(String[] tokenText, String[] tokenTag, int count) {
		super();
		this.tokenText = tokenText;
		this.tokenTag = tokenTag;
		this.count = count;
	}

	public long getTokenId() {
		return tokenId;
	}

	public void setTokenId(long tokenId) {
		this.tokenId = tokenId;
	}

	public String[] getTokenText() {
		return tokenText;
	}

	public void setTokenText(String[] tokenText) {
		this.tokenText = tokenText;
	}

	public String[] getTokenTag() {
		return tokenTag;
	}

	public void setTokenTag(String[] tokenTag) {
		this.tokenTag = tokenTag;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
