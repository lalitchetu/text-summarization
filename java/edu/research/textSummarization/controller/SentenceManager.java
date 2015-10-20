package edu.research.textSummarization.controller;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import edu.research.textSummarization.gui.TextSummarizerGUI;
import edu.research.textSummarization.util.ConfigUtil;
import edu.research.textSummarization.util.Constants;
import edu.research.textSummarization.util.FileUtil;

public class SentenceManager {
    private static SentenceManager m_SelfRef=null;
    private Hashtable<Integer,Sentence>m_SentenceTable=new Hashtable<Integer,Sentence>();
    private ArrayList<Sentence>m_SentencesList=new ArrayList<Sentence>();
    private ArrayList<Sentence>m_TopWeightedSentenceList=new ArrayList<Sentence>();
    private TokenManager m_tokenManager=null;
    private String m_DocTitleSentence;

    private void init()
    {
    m_tokenManager=TokenManager.getInstance();
    }
    public void cleanup()
    {
    m_SentenceTable.clear();
    m_SentencesList.clear();
    m_TopWeightedSentenceList.clear();
    m_DocTitleSentence=null;
    }
    public static SentenceManager getInstance()
    {
    if(m_SelfRef==null)
    {
        m_SelfRef=new SentenceManager();
        m_SelfRef.init();
    }
    return m_SelfRef;
    }
    public Sentence getSentence(int sentenceNo)
    {
    Sentence sentence=m_SentenceTable.get(new Integer(sentenceNo));
    if(sentence==null)
    {
    throw new NullPointerException("some logical issue in getSentence()");
    }
    return sentence;
    }
    public String getSentenceSeperator(String origText,String sentence)
    {
    int index=origText.lastIndexOf(sentence);
    System.out.println("index=="+index);
    char c;
    try
    {
    c=origText.charAt(index+sentence.length());
    }
    catch(Exception e)
    {
    return ".";
    }
    return c+"";
    }
    public void extractSentences(String origText)
    {
    String[] lines=origText.trim().split("\n");
    String text="";
    m_DocTitleSentence=lines[0].trim();
    for(int i=1;i<lines.length;i++)
    {
    if(lines[i].length()<1)
    continue;
    text=text+"\n"+lines[i];
    }
    String[] sentences=text.split("[.!?]");
    //String[] sentences=StringUtils.split(text,'.');
    System.out.println("no.of sentences = "+sentences.length);
    int sentenceNumber=1;
    for(String sentence:sentences)
    {
    String sentenceSeperator=getSentenceSeperator(origText,sentence);
    Sentence sentenceObject=new Sentence(sentenceNumber,sentence,sentenceSeperator);
    m_SentenceTable.put(new Integer(sentenceNumber),sentenceObject);
    m_SentencesList.add(sentenceObject);
    sentenceNumber++;
    }
    }
    public void calculateAllSentencesWeight()
    {
    Iterator<Sentence>sentences=m_SentencesList.iterator();
    while(sentences.hasNext())
    {
    Sentence sentence=sentences.next();
    double weight=getSentenceWeight(sentence);
    sentence.setWeight(weight);
    }
    }
    public ArrayList<Sentence> extractTopWeightedSentanceAndArrange()
    {
    int summaryPercentage=ConfigUtil.getSummarySize();
    System.out.println("summarypercentage="+summaryPercentage);
    int summarySize=((m_SentencesList.size()*summaryPercentage)/100);
    System.out.println("summarysize="+summarySize);
    Collections.sort(m_SentencesList,new Comparator(){
    public int compare(Object o1,Object o2)
    {
    if(o1==null&&o2==null)
    {
    throw new RuntimeException("o1 or o2 can not be null");
    }
    if((o1 instanceof Sentence)&&(o2 instanceof Sentence))
    {
    Sentence sentence1=(Sentence)o1;
    Sentence sentence2=(Sentence)o2;
    if(sentence1.getWeight()>sentence2.getWeight())
    {
    return -1;
    }
    else if(sentence1.getWeight()==sentence2.getWeight())
    {
    return 0;
    }
    else
    {
    return 1;
    }

    }
    else
    {
    throw new ClassCastException("wrong object type");
    }
    }});
    for (int i=0;i<summarySize;i++)
    {
    m_TopWeightedSentenceList.add(m_SentencesList.get(i));
    }
    Collections.sort(m_TopWeightedSentenceList, new Comparator()
    {
    public int compare (Object o1, Object o2)
    {
    if(o1==null||o2==null)
    {
    throw new RuntimeException("o1 or o2 can not be null");
    }
    if((o1 instanceof Sentence)&&(o2 instanceof Sentence))
    {
    Sentence sentence1=(Sentence)o1;
    Sentence sentence2=(Sentence)o2;
    if(sentence1.getSentenceNo()>sentence2.getSentenceNo())
    {
    return 1;
    }
    else if(sentence1.getSentenceNo()==sentence2.getSentenceNo())
    {
    return 0;
    }
    else
    {
    return -1;
    }}
    else
    {
    throw new ClassCastException("wrong object type");
    }
    }
    });
    return m_TopWeightedSentenceList;
    }
    private double getSentenceWeight(Sentence sentence)
    {
    System.out.println("Entry SentenceManager:getSentenceWeight():Perameters:sentence=object of sentence");
    double weight =((Constants.ALPFA)*(getIndividualWordWeight(sentence))+
    (Constants.BETA)*(getThemeWordWeight(sentence))+
   // (Constants.GAMA)*(getTitleWordWeight(sentence))+
    (Constants.DELTA)*(getWeightDueToSRI(sentence))+
    (Constants.THITHA)*(getWeightDueToCuePhrase(sentence))+
    (Constants.PHAYI)*(getWeightDueToLocation(sentence))+
    (Constants.OMWEGA)*(getWeightDueToProperNoun(sentence)));
    System.out.println("Return SentenceManager:getSentenceWeight():Perameters:weight="+weight);
    return weight;
    }
    private double getIndividualWordWeight(Sentence sentence)
    {
    System.out.println("Entry SentenceManager:getIndividualWordWeight():perameters:sentence=object of sentence");
    Double sentenceWeight=0.0;
    if (sentence==null)
    {
    throw new NullPointerException("sentence can not be null");
    }
    String sentenceText=sentence.getSentenceText();
    System.out.println("sentenceText="+sentenceText);
    String[] tokens=sentenceText.split("[");
    for(String token:tokens)
    {
    sentenceWeight+=m_tokenManager.getTokenWeight(token);
    }
    System.out.println("sentenceWeight="+sentenceWeight);
    return sentenceWeight;
    }
    private double getThemeWordWeight(Sentence sentence)
    {
    System.out.println("Entry SentenceManager:getThemeWordWeight():perameters:sentence=object of sentence");
    Double sentenceWeight=0.0;
    if(sentence==null)
    {
    throw new NullPointerException("sentence can not be null");
    }
    String sentenceText=sentence.getSentenceText();
    System.out.println("sentenceText="+sentenceText);
    String[] tokens=sentenceText.split("[");
    for(String token:tokens)
    {
    sentenceWeight+=m_tokenManager.getThemewordweight(token);
    }
    //System.out,println("Return SentenceManager:getThemeWordWeight():Return="+sentenceWeight);
    return sentenceWeight;
    }
    private double fetTilteWordWeight(Sentence sentence)
    {
    System.out.println("Entry SentenceManager:getTitleWordWeight():permateres:sentence=objectof sentence");
    Double sentenceWeight=0.0;
    if(sentence==null)
    {
    throw new NullPointerException("sentence can not be null");
    }
    String sentenceText=sentence.getSentenceText();
    System.out.println("sentenceText="+sentenceText);
    String[] tokens=sentenceText.split("[]");
    for(String token:tokens)
    {
    if(m_DocTitleSentence.contains(token))
    {
    sentenceWeight+=m_tokenManager.getTokenWeight(token);
    }
    }
    System.out.println("Return SentenceManager:getTitleWordWeight()    Return="+sentenceWeight);
    return sentenceWeight;
    }
    private double getWeightDueToSRI(Sentence sentence){
    System.out.println("Entry SentenceManager:getWeightDuetoNextSentence():Perameters:sentence=object of sentence");
    int sentenceNo=sentence.getSentenceNo();
    String sentenceText=sentence.getSentenceText();
    System.out.println("sentenceNo="+sentenceNo+"sentenceText="+sentenceText);
    Sentence nextSentence=m_SentenceTable.get(new Integer(sentenceNo+1));
    if(nextSentence==null)
    {
    System.out.println("no next sentence");
    System.out.println("Return SentenceManager:getWeightDuetoNextSentence()    Return=0.0");
    return 0.0;
    }
    String nextSentenceText=nextSentence.getSentenceText();
    System.out.println("nextsentenceText="+nextSentenceText);
    if(nextSentenceText.length()<2)return 0.0;
    String nextSentenceFirstWord[] =sentenceText.split("[-]");
    System.out.println("nextSentenceFirstWord="+nextSentenceFirstWord);
    if(Constants.SENTENCE_REFERER_WORDS.contains(sentenceText))
    {
    System.out.println("Return SentenceManager:getWeightDuetoNextSentence()    Return="+Constants.WEIGHT_DUE_TO_HE);
    return Constants.WEIGHT_DUE_TO_HE;
    }
    System.out.println("Return SentenceManager:getWeightDuetoNextSentence()    Return=0.0");
    return 0.0;
    }
    private double getWeightDueToCuePhrase(Sentence sentance)
    {
    double weightCuePhrase=0.0;
    String sentanceText=sentance.getSentenceText();
    String[] cuePhares=Constants.CUE_PHRASE;
    for(String cuephras:cuePhares)
    {
    if(sentanceText.toLowerCase().contains(cuephras))
    {
        weightCuePhrase=Constants.WEIGHT_DUE_TO_CUE_PHARES;
    break;
    }
    }
    //Logic for weight calculation due to cue phrase...
    return weightCuePhrase;
    }
    private double getWeightDueToLocation(Sentence sentance)
    {
    return getLOcWeight(sentance.getSentenceNo(),m_SentencesList.size());
    }
    private double getLOcWeight(int sentanceNo,int totalNoOfSentence)
    {
    int middle=totalNoOfSentence/2;
    double weight=0.0;
    if(sentanceNo<middle)
    {
    weight=(double)(middle-sentanceNo)/middle;
    }
    else
    {
    weight=(double)(sentanceNo-middle)/middle;
    }
    return weight;
    }
    private double getWeightDueToProperNoun(Sentence sentance)
    {
    double properNounWeight=0.0;
    String sentanceText=sentance.getSentenceText();
    String[]  sentanceWords=sentanceText.split("[-");
    for(int i=1;i<sentanceWords.length;i++)
    {
    System.out.println("sentenceword="+sentanceWords[i]);
    if(sentanceWords[i].length()<2)
    continue;
    char firstChar=sentanceWords[i].trim().charAt(0);
    //Character firstChar=new Character(sentanceWords[i].charAt(0));
    if(Character.isUpperCase(firstChar))
    {
    properNounWeight=properNounWeight+Constants.WEIGHT_DUE_TO_PROPER_NOUN;
    }
    }
    //logic for weight calculation due to proper noun...
    return properNounWeight;
    }
    public String getTitle()
    {txtInput.setColumns(20);
    txtInput.setLineWrap(true);
    txtInput.setRows(5);
    txtInput.setAutoscrolls(false);
    //jScrollPane1.setViewportView(txtInput);


    jLabel2.setText("Input File(Original Text)");


    txtSummarizedText.setColumns(20);
    txtSummarizedText.setLineWrap(true);
    txtSummarizedText.setRows(5);
    txtSummarizedText.setAutoscrolls(false);
    txtSummarizedText.setName(txtSummarizedText.getText());
    //jScrollPane2.setViewportView(txtSummarizedText);


    jLabel3.setText("SummarizedText");


    btnSummarize.setText("Summarize");
    btnSummarize.addActionListener(new java.awt.event.ActionListener() {
       public void actionPerformed(java.awt.event.ActionEvent evt) {
          btnSummarizeActionPerformed(evt);
       }
    }); 

    cmbSummarySize.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Select","Large","Moderate","small"}));
              cmbSummarySize.addActionListener(new java.awt.event.ActionListener() {
                  public void actionPerformed(java.awt.event.ActionEvent evt) {
                         cmbSummarySizeActionPerformed(evt);
                  }
             });  


           jLabel4.setText("Summary Level");


           GroupLayout Layout = new  GroupLayout();
             jPanel1.setLayout(jPanel1Layout);
             jPanel1Layout.setHorizontalGroup(
                  jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                 .add(jPanel1Layout.createSequentialGroup()
                      .addContainerGap()
                      .add(jLabel4)
                       .add(18,18,18)
                       .add(cmbSummarySize,org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
    142,org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                       .add(32,32,32)
                       .add(btnSummarize,org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,114,
    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(18,Short.MAX_VALUE))
              );                                                                                                                                                                                                                                                        -77
       jPanel1Layout.setVerticalGroup(
         jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
         .add(jPanel1Layout.createSequentialGroup()   
               .add(28,28,28)
     
     
    .add( jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                             .add(jLabel4)
                             .add(cmbSummarySize,org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)      
                            .add(btnSummarize))
                         .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
    Short.MAX_VALUE))
              );


              jButton2.setText("Save");
              jButton2.addActionListener(new java.awt.event.ActionListener() {
                   public void actionPerformed(java.awt.event.ActionEvent evt) {
                       jButton2ActionPerformed(evt);
                   }
                });  


               jMenu1.setText("File");

    jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.
    VK_0,java.awt.event.InputEvent.CTRL_MASK));
             jMenuItem1.setText("Open");
             jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem1ActionPerformed(evt);
                }
             });  
             jMenu1.add( jMenuItem1);
             jMenu1.add( jSeparator1);


    jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.
    VK_S,java.awt.event.InputEvent.CTRL_MASK));
             jMenuItem3.setText("Save");
             jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem3ActionPerformed(evt);
               }
            });  
            jMenu1.add( jMenuItem3);


    jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.
    VK_E,java.awt.event.InputEvent.CTRL_MASK));
             jMenuItem2.setText("Exit");
             jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                     jMenuItem2ActionPerformed(evt);
               }
             });  
             jMenu1.add( jMenuItem2);


             jMenuBar1.add(jMenu1);


             setJMenuBar(jMenuBar1);


            org.jdesktop.layout.GroupLayout layout = new    
    org.jdesktop.layout.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                               .add(layout.createSequentialGroup()
                                       .add(jPanel1,org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                   .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                   .add(txtSaveFile,org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,223,
    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                 .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED,
    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
                                 .add(jButton2))
                               .add(org.jdesktop.layout.GroupLayout.TRAILING,
    layout.createSequentialGroup()
                                   .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jScrollPanel1,org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
    323,Short.MAX_VALUE)
                                 .add(jLabel2,org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,145,
    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                 .add(46,46,46)
                                 .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jLabel3,org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,103,
    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                     .add(jScrollPanel2,org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
    329,Short.MAX_VALUE))))               
                       .addContainerGap()
            );
            layout.setVerticalGROUP(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                  .add(layout.createSequentialGroup()
                           .addContainerGap()
                           .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)                                                                                                   -79
                            .add(jPanel1,org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                              .add(layout.createSequentialGroup()
                                     .add(25,25,25)
    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                         .add(txtSaveFile,org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                       .add(jButton2))))
                         .add(25,25,25)
                         .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                               .add(jLabel2)
                               .add(jLabel3))
                          .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                          .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                               .add(jScrollPane1,org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
    307,Short.MAX_VALUE)
                                .add(jScrollPane2,org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
    307,Short.MAX_VALUE)))
                       );

                      pack();
                 }// </editor-fold>//GEN-END:initComponents

                 private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-
            FIRST:event_jMenuItem1ActionPerformed  

                       javax.swing.JFileChooser fileChooser = new  javax.swing.JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    if(fileChooser.showDialog(this,"Select Text File") ==
    JFileChooser.APPROVE_OPTION) {
                 File file = fileChooser.getSelectedFile();
                 String inputText=FileUtil.getContentFromFile(file.getAbsolutePath());
                 txtInput.setText(inputText);

                 }
                 txtSummarizedText.setText("");
             }//GEN-LAST:event_jMenuItem1ActionPerformed


               private void btnSummarizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-
    FIRST:event_btnSummarizeActionPerformed
               String InputText = txtInput.getText();
               String summaryType=(String)cmbSummarySize.getSelectedItem();
               if(SummaryType.equalsIgnoreCase("Large")){
                    System.out.println("large summary");
                     ConfigUtil.setSummarySize(60);
              }else if(summaryType.equalsIgnoreCase("Moderate")){
                      System.out.println("moderate summary");                                                                                                                                                                            -80
                      ConfigUtil.setSummarySize(40); 


     }elseif(summaryType.equalsIgnoreCase("Small")){
                      System.out.println(" small summary");                                                                                                                                                                            -80
                      ConfigUtil.setSummarySize(20);

     }
    else{
        JOptionPane messageDialog = new JOptionPane();
         messageDialog.showMessageDialog(this,"Please select summary type","Error",JOptionPane.INFORMATION_MESSAGE);
          return;
          }
          Controller c=Controller.getInstance();
          c.onSummarizeButtonClick(inputText);
      }//GEN-LAST:event_btnSummarizeActionPerformed


       private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-
            FIRST:event_jMenuItem2ActionPerformed  
                      //TODO add your handling code here:
                      this.dispose();
                  }//GEN-LAST:event_jMenuItem2ActionPerformed


         private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-
            FIRST:event_jMenuItem3ActionPerformed  
                      //TODO add your handling code here:
                  }//GEN-LAST:event_jMenuItem3ActionPerformed


           private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-
            FIRST:event_jButton2ActionPerformed  
                      //TODO add your handling code here:
                      

                    JFileChooser fileChooser = new JFileChooser("");
                    fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                    if(fileChooser.showSaveDialog(this) ==JFileChooser.APPROVE_OPTION) {
                 File file = fileChooser.getSelectedFile();
                 mSaveFilePath = file.getAbsolutePath();
                       System.out.println("savepath="+mSaveFilePath);
                         txtSaveFile.setText(mSaveFilePath);
                         FileUtil.saveIntoFile(txtSummarizedText.getText(),mSaveFilPath);


                   }
              
               }//GEN-LAST:event_jButton2ActionPerformed


                private void cmbSummarySizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-
            FIRST:event_cmbSummarySizeActionPerformed  
                      //TODO add your handling code here:
                  }//GEN-LAST:event_cmbSummarySizeActionPerformed
                  public void displaySummary(StringSummarizedText) {
                 txtSumarizedText.SetText(summarizedText);
    }
    /**
     *@param args the command line arguments
    */
    public static void main(String args[]) {
          java.awt.EventQueue.invokeLater(new Runnable() {
               public void run() {
                     new TextSummarizerGUI().setVisible(true);
                 }
            });
    }


       //Variables declaration-do not modify//GEN-BEGIN:variables
       private javax.swing.JButton btnSummarize;
       private javax.swing.JComboBox cmbSummarySize;    
       private javax.swing.JButton jButton2;
       private javax.swing.JLabel jLabel2;
       private javax.swing.JLabel jLabel3;
       private javax.swing.JLabel jLabel4;
       private javax.swing.JMenu jMenu1;
       private javax.swing.JMenuBar jMenuBar1;
       private javax.swing.JMenuItem jMenuItem1; 
       private javax.swing.JMenuItem jMenuItem2; 
       private javax.swing.JMenuItem jMenuItem3; 
       private javax.swing.JPanel jPanel1;
       private javax.swing.JScrollPane JScrollPane1;
       private javax.swing.JScrollPane JScrollPane2;      
       private javax.swing.JSeparator jSeparator1; 
       private javax.swing.JTextArea txtInput;
       private javax.swing.JTextField txtSaveFile;
       private javax.swing.JTextArea txtSummarizedText;
       // End of variables declaration//GEN-END:variables
}
