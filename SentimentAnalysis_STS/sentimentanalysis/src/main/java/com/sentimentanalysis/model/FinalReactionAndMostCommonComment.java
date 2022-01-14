package com.sentimentanalysis.model;

public class FinalReactionAndMostCommonComment {
  
	public int reaction;
	public String[] commonUsedComment;
	
	public FinalReactionAndMostCommonComment() {
		super();
	}

	public FinalReactionAndMostCommonComment(int reaction, String[] commonUsedComment) {
		super();
		this.reaction = reaction;
		this.commonUsedComment = commonUsedComment;
	}

	public int getReaction() {
		return reaction;
	}

	public void setReaction(int reaction) {
		this.reaction = reaction;
	}

	public String[] getCommonUsedComment() {
		return commonUsedComment;
	}

	public void setCommonUsedComment(String[] commonUsedComment) {
		this.commonUsedComment = commonUsedComment;
	}
	
}
