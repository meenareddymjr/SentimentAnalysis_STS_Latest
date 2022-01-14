package com.sentimentanalysis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "commonDataText")
public class CommonDataTextReaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long commonId;

	@Column(name = "most_repeated_word")
	private String mostRepeatedWord;
	
	@Column(name = "repeated_reaction")
	private String repeatedReaction;
	
	@Column(name = "count")
	private int count;
	
	public CommonDataTextReaction() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CommonDataTextReaction(String mostRepeatedWord, String repeatedReaction, int count) {
		super();
		this.mostRepeatedWord = mostRepeatedWord;
		this.repeatedReaction = repeatedReaction;
		this.count = count;
	}


	public String getMostRepeatedWord() {
		return mostRepeatedWord;
	}
	public void setMostRepeatedWord(String mostRepeatedWord) {
		this.mostRepeatedWord = mostRepeatedWord;
	}
	public String getRepeatedReaction() {
		return repeatedReaction;
	}
	public void setRepeatedReaction(String repeatedReaction) {
		this.repeatedReaction = repeatedReaction;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
