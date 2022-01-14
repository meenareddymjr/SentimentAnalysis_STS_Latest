package com.sentimentanalysis.process;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class FileReaderClass {
	public static boolean readDocxFile(String fileName,String adj) {
		try {
			File file = new File(fileName);
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            
			XWPFDocument document = new XWPFDocument(fis);

			List<XWPFParagraph> paragraphs = document.getParagraphs();
			
			for(XWPFTable tb1 : document.getTables()) {
				for(XWPFTableRow row: tb1.getRows()) {
					for(XWPFTableCell cell : row.getTableCells()) {
						for (XWPFParagraph para : cell.getParagraphs()) {
							for(XWPFRun r: para.getRuns()) {
								String text = r.getText(0);
								if(text!=null && text.toLowerCase().equals(adj.toLowerCase())) {
									return true;
								}
							}
						}
					}
				}
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
