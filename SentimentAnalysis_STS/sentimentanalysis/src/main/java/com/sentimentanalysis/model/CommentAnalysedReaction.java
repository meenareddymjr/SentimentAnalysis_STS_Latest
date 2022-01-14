package com.sentimentanalysis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "commentAnalysedReaction")
public class CommentAnalysedReaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long commentId;
	
	@Column(name = "comment_Reaction")
	private int commentReaction;
	
	public CommentAnalysedReaction() {
		super();
	}
	
	public CommentAnalysedReaction(long commentId, int commentReaction) {
		super();
		this.commentId = commentId;
		this.commentReaction = commentReaction;
	}

	public CommentAnalysedReaction(int commentReaction) {
		super();
		this.commentReaction = commentReaction;
	}

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public int getCommentReaction() {
		return commentReaction;
	}

	public void setCommentReaction(int commentReaction) {
		this.commentReaction = commentReaction;
	}
	
}
