package edu.research.textSummarization.controller;
import java.util.ArrayList;

import edu.research.textSummarization.util.ConfigUtil;

public class Tokenizer {
    private static Tokenizer s_SelfRef=null;
    TokenManager m_TokenManager;
    private Tokenizer() {
    }
    private void init() {
    m_TokenManager = TokenManager.getInstance();
    }

    public static Tokenizer getInstance() {
    if(s_SelfRef == null ) {
    s_SelfRef=new Tokenizer();
    s_SelfRef.init();
    }
    return s_SelfRef;
    }
    public void Tokenizer(String text) {
    PorterStemmer porterStemmer = new PorterStemmer();
    boolean isStopWordEnabled = ConfigUtil.isStopWordEnabled();
    boolean isStemmingEnabled = ConfigUtil.isStemingEnabled();
    System.out.println("isstopwordenabled="+isStopWordEnabled);
    System.out.println("isstemmingenabled="+isStemmingEnabled);

    StopWordRemoval stopWordRemoval = StopWordRemoval.getReference();
    String[] tokens=null;
    ArrayList<String>tokensList=new ArrayList<String>();

    int tokenLength = tokens.length;

    for (int j=0;j<tokenLength;j++) {
    tokens[j]=tokens[j].trim();
    if(tokens[j].trim().equals("")||tokens[j].length()<=2||tokens[j].contains("[")||tokens[j].contains("]")||tokens[j].contains("{")||tokens[j].contains("}")||tokens[j].contains("\\")){
    continue;
    }
    if(isStopWordEnabled) {
    continue;
    }
    if(isStopWordEnabled){
    if(!stopWordRemoval.isStopWord(tokens[j])){
    tokensList.add(tokens[j]);
    }
    }
    }
    if(isStopWordEnabled) {
    tokens=tokensList.toArray(new String[tokensList.size()]);
    }
    if(isStemmingEnabled) {
    for( int k=0;k<tokens.length;k++) {
    tokens[k]=porterStemmer.stem(tokens[k]);
    }
    }
    m_TokenManager.StoreTokens(tokens);
    }
}
