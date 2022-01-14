package com.sentimentanalysis.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.sentimentanalysis.constants.Constants;
import com.sentimentanalysis.model.CommentAnalysedReaction;
import com.sentimentanalysis.model.Comments;
import com.sentimentanalysis.model.CommonDataTextReaction;
import com.sentimentanalysis.model.FinalReactionAndMostCommonComment;
import com.sentimentanalysis.model.TokenData;
import com.sentimentanalysis.process.SentenceTokenClass;
import com.sentimentanalysis.repository.CommentAnalysedReactionRepository;
import com.sentimentanalysis.repository.CommentRepository;
import com.sentimentanalysis.repository.CommonDataTextReactionRepository;
import com.sentimentanalysis.repository.TokenDataRepository;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/")
public class CommentsController {
	
	// This repository stores the comments which user entered
	@Autowired
	private CommentRepository commentRepository;
	
	// This repository stores the individual reaction of each comment
	@Autowired
	private CommentAnalysedReactionRepository commentAnalysedReactionRepository;
	
	// Here the comment tokens and part of speech taggers are stored as they are used for finding most repeated comment
	@Autowired
	private  TokenDataRepository tokenDataRepository;
	
	// This is used for storing most repeated word with reaction,count as this is used for further finding repeated word
	@Autowired
	private CommonDataTextReactionRepository commonDataTextReactionRepository;
	
	SentenceTokenClass sentence = new SentenceTokenClass();

	/*To post most used Comment and Reaction from the user entered comments 
	 Developed by Meena Mekala*/
	@GetMapping("comments")
	public ResponseEntity<?> getMostUsedCommentAndReaction() throws InvalidFormatException, IOException{
		List<Comments> comments = this.commentRepository.findAll();
		int lastIndex = comments.size()-1;
		tokenSplitting(comments.get(lastIndex).getCommentText());
		List<TokenData> tokenData = this.tokenDataRepository.findAll();
		//it calls another method and gets overall comments reactions and most used comment
		FinalReactionAndMostCommonComment reactionandMostCommonComment = sentence.sentenceDetect(tokenData);
		this.commentAnalysedReactionRepository.save(new CommentAnalysedReaction(reactionandMostCommonComment.getReaction()));
		List<CommentAnalysedReaction> list = this.commentAnalysedReactionRepository.findAll();
		int reactionCount = 0;
		String[] mostCommonWord = reactionandMostCommonComment.getCommonUsedComment();
		List<CommonDataTextReaction> commonData = this.commonDataTextReactionRepository.findAll();
		boolean exists = false;
		/* we will iterate whole list and find whether the present comment word is present or not.
		If present the count will be incremented or it is added as a new one*/
		if(commonData.size()>0) {
			for(int k=0;k<mostCommonWord.length;k++) {
				String[] text = mostCommonWord[k].split(Constants.SPLIT_BY_SPACE_STRING);
				for(int j=0;j<commonData.size();j++)
				{
					String commonText = commonData.get(j).getMostRepeatedWord();
					String commonTextReaction = commonData.get(j).getRepeatedReaction();
					if(text[0].equalsIgnoreCase(commonText) && text[1].equalsIgnoreCase(commonTextReaction)) {
						int count = commonData.get(j).getCount();
						commonData.get(j).setCount(count+1);
						exists = true;
						break;
					}
				}
				if(!exists) {
					commonData.add(new CommonDataTextReaction(text[0],text[1],1));
					this.commonDataTextReactionRepository.save(new CommonDataTextReaction(text[0],text[1],1));
				}
			}
		}
		else {
			for(int k=0;k<mostCommonWord.length;k++) {
				String[] text = mostCommonWord[k].split(Constants.SPLIT_BY_SPACE_STRING);
				commonData.add(new CommonDataTextReaction(text[0],text[1],1));
				this.commonDataTextReactionRepository.save(new CommonDataTextReaction(text[0],text[1],1));
			}
		}
		// To find overall reaction of all comments, we need to add present comment reaction with previous comment reaction.
		for(int i=0;i<list.size();i++) {
			reactionCount = reactionCount+list.get(i).getCommentReaction();
		}
		Map<String, Object> response = new HashMap<>();
		/* check final reaction and if it is less than 0 it returns "Negative",
		  if it is greater than 0 it returns "Positive",
		  if it is 0 then it is "Neutral" */
        String finalReaction=reactionCount>0?Constants.POSITIVE_CONSTANT:reactionCount<0?Constants.NEGATIVE_CONSTANT:Constants.NEUTRAL_CONSTANT;
        /* If final reaction is less than 0 then all bad comment words are returned
           If final reaction is greater than 0 then all good comment words are returned
           If final reaction is neutral then null is returned
         */
		commonData = reactionCount<0?commonData.stream().filter(v->v.getRepeatedReaction().equalsIgnoreCase(Constants.BAD_CONSTANT)).toList():
			reactionCount>0?commonData.stream().filter(v->v.getRepeatedReaction().equalsIgnoreCase(Constants.GOOD_CONSTANT)).toList():null;
		// find the maximum of the commonData based on count
		CommonDataTextReaction maxValue = commonData!=null&&commonData.size()>0?commonData.stream().max(Comparator.comparing(v -> v.getCount())).get():null;
        String commonUsedComment = maxValue!=null?maxValue.getMostRepeatedWord()+Constants.SPACE_CONSTANT+maxValue.getRepeatedReaction():null;
		response.put(Constants.FINAL_REACTION_CONSTANT, finalReaction);
        response.put(Constants.COMMON_WORD_CONSTANT, commonUsedComment);
        return new ResponseEntity<>(response,HttpStatus.OK);
        
	}
	
	
	/*To get Comments from the user entered in frontend 
	 Developed by Ayanarmani Kalusulingam*/
	@PostMapping(value = "/postcomments")
	public  Comments createComments (HttpServletRequest request) throws IOException {
		//By using inputStream we will get the data from the request
		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;
	    this.tokenDataRepository.findAll();
	    // Using bufferedreader will read each and every char and finally the string is build
	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }
	    body = stringBuilder.toString();
	    return this.commentRepository.save(new Comments(body));		
	}
	
    /*The comment is splitted into tokens and parts of speech is given to each word in comment using OpenNLP */
	public void tokenSplitting(String tokenText) throws IOException,InvalidFormatException {
        tokenText=tokenText.replaceAll("\\p{Punct}", "");
        
        // refer to model file "en-token.bin" to split sentence tokens
        InputStream tokenIs = new FileInputStream(Constants.TOKENS_FILE_LOCATION);
        TokenizerModel tokenModel = new TokenizerModel(tokenIs);
        
        // refer to model file "en-pos-maxent.bin" to assign part of speech tagger each token 
        InputStream inputStream = new 
                FileInputStream(Constants.POS_FILE_LOCATION); 
        POSModel posModel = new POSModel(inputStream);
 
        TokenizerME tokenizer = new TokenizerME(tokenModel);
        
        POSTaggerME tagger = new POSTaggerME(posModel);
        
        String tokens[] = tokenizer.tokenize(tokenText);
        
        String[] tags = tagger.tag(tokens);

        //All tokens and tags i.e., text, part of speech tags for each tag are stored
        this.tokenDataRepository.save(new TokenData(tokens,tags));
        tokenIs.close();
	}
}	

