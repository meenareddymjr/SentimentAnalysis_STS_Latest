package com.sentimentanalysis.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.sentimentanalysis.constants.Constants;
import com.sentimentanalysis.model.CommonDataTextReaction;
import com.sentimentanalysis.model.FinalReactionAndMostCommonComment;
import com.sentimentanalysis.model.TokenData;

public class SentenceTokenClass {

/*
 * From here you can find the reaction of the comment and noun with the adjective of the noun*/
public  FinalReactionAndMostCommonComment sentenceDetect(List<TokenData> tokenData) throws InvalidFormatException, IOException {
	    int size = tokenData.size();
	    String tokens[] = tokenData.get(size-1).getTokenText();
	    String tags[] = tokenData.get(size-1).getTokenTag();
	    List<CommonDataTextReaction> commonData = new ArrayList<CommonDataTextReaction>();
        int finalReaction=0;
        // iterate total array of tokens,tags
        	for(int i=0;i<tags.length;i++) {
        		// if the text is to,the,a,an it is ignored
        		if(!(tags[i].contains(Constants.PRP_CONSTANT) || tags[i].contains(Constants.TO_CONSTANT) || tags[i].contains(Constants.DT_CONSTANT)))
        		{
        			int reaction =0;
        			boolean containsNot = false;
        			//checks whether the sentence starts with noun  and finds the reaction
        			if(tags[i].equalsIgnoreCase(Constants.NN_CONSTANT)||tags[i].equalsIgnoreCase(Constants.NNP_CONSTANT)||tags[i].equalsIgnoreCase(Constants.NNS_CONSTANT)) {
        				String text = tokens[i];
        				i++;
        				do {
        					// If the text contains not then the reaction should be opposite of that
        					if(tokens[i].equalsIgnoreCase(Constants.NOT_CONSTANT)) {
        						containsNot = true;
        					}
        					String data = tokens[i] ; 
							reaction = reaction+(fileReaderClass(Constants.POSITIVE_KEYWORDS_LOCATION,data)?1:
								fileReaderClass(Constants.NEGATIVE_KEYWORDS_LOCATION, data)?-1:0);
							i++;
        				}while(i<tags.length &&(!(tags[i].equalsIgnoreCase(Constants.NN_CONSTANT))||tags[i].equalsIgnoreCase(Constants.NNP_CONSTANT) ||tags[i].equalsIgnoreCase(Constants.NNS_CONSTANT)));
        				i--;
        				if(containsNot) {
            				reaction = reaction>0?-1:reaction<0?1:0;
            			}
        				String storeReaction = reaction>0?Constants.GOOD_CONSTANT:reaction<0?Constants.BAD_CONSTANT:Constants.NEUTRAL_CONSTANT;
        				commonData.add(new CommonDataTextReaction(text,storeReaction,1));
        			}
        			// To check if noun is present at end and find the reaction
        			else {
        				do {
        					if(tokens[i].equalsIgnoreCase(Constants.NOT_CONSTANT)) {
        						containsNot = true;
        					}
        					String data = tokens[i] ; 
							reaction = reaction+(fileReaderClass(Constants.POSITIVE_KEYWORDS_LOCATION,data)?1:
								fileReaderClass(Constants.NEGATIVE_KEYWORDS_LOCATION, data)?-1:0);
							i++;
        				}while(i<tags.length &&(!(tags[i].equalsIgnoreCase(Constants.NN_CONSTANT))||tags[i].equalsIgnoreCase(Constants.NNP_CONSTANT)||tags[i].equalsIgnoreCase(Constants.NNS_CONSTANT)));
        				if(reaction==0) {
        					i--;
        				}
        				if(containsNot) {
            				reaction = reaction>0?-1:reaction<0?1:0;
            			}
        				if(i<tags.length&&(tags[i].equalsIgnoreCase(Constants.NN_CONSTANT)||tags[i].equalsIgnoreCase(Constants.NNP_CONSTANT)||tags[i].equalsIgnoreCase(Constants.NNS_CONSTANT))) {
        					String text = tokens[i];
            				String storeReaction = reaction>0?Constants.GOOD_CONSTANT:reaction<0?Constants.BAD_CONSTANT:Constants.NEUTRAL_CONSTANT;
            				commonData.add(new CommonDataTextReaction(text,storeReaction,1));
            				}
        			}
        			finalReaction = reaction+finalReaction;
        		}
        	}
        String[] commonUsedComment = new String[commonData.size()];
        for(int i=0;i<commonData.size();i++) {
        	commonUsedComment[i] = commonData.get(i).getMostRepeatedWord()+Constants.SPACE_CONSTANT+commonData.get(i).getRepeatedReaction();
        }
        
        finalReaction = finalReaction>0?1:finalReaction<0?-1:0;
        return new FinalReactionAndMostCommonComment(finalReaction,commonUsedComment);
    }

    public boolean fileReaderClass(String fileLocation,String token) {
    	return FileReaderClass.readDocxFile(fileLocation,token);
    }
}

