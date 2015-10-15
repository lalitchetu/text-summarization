package edu.research.textSummarization.controller;

import java.util.ArrayList;

import edu.research.textSummarization.util.Constants;

public class PostProcesser {
    private static PostProcesser m_SelfInstance = null;

  public static PostProcesser getInstance() {

  if(m_SelfInstance==null) {
  m_SelfInstance=new PostProcesser();
  }

  return m_SelfInstance;
  }

  public ArrayList postProcessSummary(ArrayList summary) {
  System.out.println("before postprocessing summary size = "+summary.size());
  ArrayList temp = removeRedundance(summary);
  ArrayList resultSummary = removeDiscontinuity(temp);
  System.out.println("After postproceesing summary size="+resultSummary.size());
  return resultSummary;
  }
   public ArrayList removeRedundance(ArrayList summary) {

      System.out.println("before removing redundancy summarysize="+summary.size());
       for(int i=0;i<summary.size()-1;i++) {
       int count=0;
          for(int iteration=1;iteration <=2 && (i<summary.size()-2);iteration++) {
              if(i+iteration>summary.size()||count==2)
              break;
               //System.out.println("i="+i+" and i+it="+(i+iteration)+"size="+summary.size());
                String preSentance= ((Sentence) summary.get(i)).getSentenceText();
                String nextSentance=((Sentence)summary.get(i+iteration)).getSentenceText();
                double cosineValue=getCosineValue(preSentance,nextSentance);
                 count++;
              if(cosineValue >=0.4) {
              summary.remove(i+iteration);
              iteration--;
              }
            
              //over 57
             // /67
                 weightCuePhare = Constants.WEIGHT_DUE_TO_CUE_PHARES;
                 break;
                }
      }
      // Logic for weight colaculation due to cue phrase...
      return weightCuePhare;
  }
  private double getWeightDueToLocation(Sentence sentence){
  return getLocWeight(Sentance.getSentenceNo(),m_SentencesList.size());
  }
  private double getLocWeight(int sentanceNo,inttotalNoOfSentence){
  int middle = totalNoOfSentence/2;
  double weight = 0.0;
  if(sentanceNo < middle){
  weight = (double)(middle - sentanceNo)/middle;
  }
  else{
  weight = (double)(sentaneNo - middle)/middle;
  }
  return weight;
  }
  private double getWeightDueToProperNoun(Sentence sentance) {
  double properNouneweight = 0.0;
  String sentanceText = sentance.getSentenceText();

  for(int i=1; i<sentanceWords.length;i++){
  System.out.println("sentecewrod="+sentancewords[i]);
  if(sentanceWords[i].length()<2)
  continue;
  char firstChar = sentanceWords[i].trim().charAt(0);
  //Character firstChar = new Character(sentanceWords[i].charAt(0));
  if(Character.isUpperCase(firstChar)){
  properNouneWeight = properNouneWeight + 
  Constants.WEIGHT_DUE_TO_PROPER_NOUN;
  }
  }
  //Logic for weight colaculation due to proper noun...
  return properNouneWeight;
  }
  public String getTitle(){
  return m_DocTitleSentence;
  }
  /*
  private double getWeightDueToDate(Sentence sentance){
   double dateWeight = 0.0;
  //Loogic for weight colaculation due to date format...
  return dateWeight;
  }
  */
  public static void main (String[]args) {
  SentenceManager s = new SentenceManager();
  //String orig = "this is jayesh jethva.i did? What d hell! nothing special dude.";
  //String sen = "what d hell".
  //String t = s.getSentenceSeperator(orig,sen);
  //System.outprintln("t++"+t+"==");
  String res = s.getSentenceSeperator("this is india? this works as a point. hello hows dat!","hello hows dat");
  //System.out.println("result="+res);
  //for(int i=1;i<=41;i++){
  //System.out.println(sentanceno="+i+" and weight ="+s.getLocWeight(i,41));
  //}
  }
}
