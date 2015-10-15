package edu.research.textSummarization.controller;

import java.util.ArrayList;

import edu.research.textSummarization.gui.TextSummarizerGUI;
import edu.research.textSummarization.util.FileUtil;

public class Controller {
    private static Controller m_SelRef=null;
    private SentenceManager m_SentenceManager=null;
    private Tokenizer m_Tokenizer=null;
    private ArrayList<Sentence>m_TopWeightedSentenceList;
   private ArrayList<Sentence>m_postProcessedSentenceList;
    private Controller(){
   m_SentenceManager=SentenceManager.getInstance();
   }
   public static Controller getInstance(){
   if(m_SelRef==null){
    m_SelRef=new Controller();
    }
    return m_SelRef;
   }
   private void cleanup(){
    m_SentenceManager.cleanup();
    TokenManager.getInstance().cleanup();
    }
    public void onSummarizeButtonClick(String inputText){
    m_SentenceManager.extractSentences(inputText);
    m_Tokenizer=Tokenizer.getInstance();
    m_Tokenizer.Tokenizer(inputText);
    m_SentenceManager.calculateAllSentencesWeight();

    m_TopWeightedSentenceList=m_SentenceManager.extractTopWeightedSentanceAndArrange();
   //over47
    m_postProcessedSentenceList=PostProcesser.getInstance().postProcessSummary(m_TopWeightedSentenceList);
    System.out.println("............................summary.................");
     StringBuilder result=new StringBuilder();
     result.append(m_SentenceManager.getTitle());
     result.append("\n");
     for(int i=0;i<m_postProcessedSentenceList.size();i++){
      Sentence s=m_postProcessedSentenceList.get(i);
      result.append(s.getSentenceText()+s.getSentenceSeperator());
     }
    TextSummarizerGUI.getInstance().display.Summary(result.toString());
     cleanup();
   }
   private void printSummary(){
    for(int i=0;i<m_TopWeightedSentenceList.size();i++){
        Sentence s=m_TopWeightedSentenceList.get(i);
        System.out.println(""+s.getSentenceText());
    }
   }
   public static void main(String[] args){
    Controller c=Controller.getInstance();
    String text=FileUtil.getContentFromFile("C:\\exp.txt");
    c.onSummarizeButtonClick(text);
   }
}
