package edu.research.textSummarization.gui;
import edu.research.textSummarization.controller.Controller;
import edu.research.textSummarization.util.ConfigUtil;
import edu.research.textSummarization.util.FileUtil;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
public class TextSummarizerGUI extends java.swing.JFrame{
    private static TextSummarizerGUI me=null;
    private String mSaveFilePath="";
    /** Creates new form TextSummarizerGUI */
    public TextSummarizerGUI() {
    initComponent();
    me=this;
    }
    public static TextSummarizerGUI getInstance(){
    return me;
    }
    /**This method is called from within the constructor to
    *initialize the form.
    *WARNING: Do NOT modify this code.The content of this method is 
    *always regenerated by the Form Editor.
    */
    //<editor-forld defaultstate="collapsed" desc="Generated Code">//GEN-
    BEGIN:initComponents
    private void initComponents() {


    jScrollPane = new javax.swing.JScrollPane();
    txtInput = new javax.swing.JTextArea();
    jLabel2 = new javax.swing.JLabel();
    jScrollPane2 = new javax.swing.JScrollPane();
    txtSummarizedText = new javaxswing.JTextArea();
    jLabel3 = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JPanel();
    btnSummarize = new javax.swing.JButton();
    cmbSummarySize = new javax.swing.JComboBox();
    jLabel4 = new javax.swing.JLabel();
    txtSaveFile = new javax.swing.JTextField();
    jButton2 = new javax.swing.JButton();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    jMenuItem1 = new javax.swing.JMenuItem();
    jSeparator1 = new javax.swing.JSeparator();
    jMenuItem3 = new javax.swing.JMenuItem();
    jMenuItem2 = new javax.swing.JMenuItem();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle(Text Summarizer");
    setAlwaysOnTop(true);

    } 
    } 
    System.out.println("after removing redundancy summarysize="+summary.size());
    return summary;
    }
    private Sentence getExtraSentence(Sentence sentenceA,Sentence sentenceB)
    {
    int originalNoOfSentencea=sentenceA.getSentenceNo();
    int originalNoOfSentenceB=sentenceB.getSentenceNo();
    if(originalNoOfSentenceA>originalNoOfSentenceB)
    {
    throw new IllegalStateException("illegal state inside getExtrasentence");
    }
    for(int sentenceNo=originalNoOfSentenceA+1;sentenceNo<originalNoOfSentenceB;sentenceNo++)
    {
    Sentence middleSentence=SentenceManager.getInstance().getSentence(sentenceNo);
    double cosineValueA=getCosineValue(sentenceA.getSentenceText()),middleSentence.getSentenceText());
    double cosineValueB=getCosineValue(middleSentence.getSentenceText()),sentenceB.getSentenceText());
    if(cosineValueA>=0.3&&cosineValueB>=0.3)
    {
    System.out.println("SentenceA="+sentenceA.getSentenceText()+sentenceA.getSentenceNo());
    System.out.println("SentenceB="+sentenceB.getSentenceText()+sentenceB.getSentenceNo());
    System.out.println("EXTRA==="+middleSentence.getSentence.getSentenceText()+middleSentence.getSentenceNo());
    return  middleSentence;
    }
    }
    return null;
    }
    private ArrayList removeDiscontinuity(ArrayList summary)
    {
    System.out.println("before removing discontinuity summary size="+summary.size());
    ArrayListnewSummary=new ArrayList();
    for(int i=0;i<summary.size()-1;i++)
    {
    Sentence sentenceA=(Sentence)summary.get(i);
    Sentence sentenceB=(Sentence)summary.get(i+1);
    if(getCosineValue(sentenceA.getSentenceText(),sentenceB.getSentenceText())>0.3)
    {
    if(!newSummary.contains(sentenceA))
    newSummary.add(sentenceA);
    if(!newSummary.contains(sentenceB))
    newSummary.add(sentenceB);
    continue;
    }
    if(sentenceA==null || sentenceB==null)
    {
    throw new IllegalStateException("some illegal state inside removeDiscontinuity due to logic error");
    }
    if(!newSummary.contains(sentenceA))
    newSummary.add(sentenceA);
    Sentence extraSentence=getExtraSentence(sentenceA,sentenceB);
    if(extraSentence!=null)
    {
    if(!newSummary.contains(extraSentence))
    newSummary.add(extraSentence);
    }
    if(!newSummary.contains(sentenceB))
    newSummary.add(sentenceB);
    }
    System.out.println("after removing discontinuity summary size="+newSummary.size());
    return newSummary;
    }
    private double getCosineValue(String sentanceA,string sentanceB)
    {
    if(sentanceA==null || sentanceB==null)
    {
    throw new NullPointerException("inside getCosine() null parameter");
    ]
    if(sentanceA.equals("")||sentanceB.equals(""))
    return 0.0;
    double cosineValue=0.0;
    int count=0;
    String[] sentanceWordA=sentanceA.split("[-
    String[] sentanceWordB=sentanceB.split("[-

    for(String SWA:sentanceWordA)
    {
    for(String SWB:sentanceWordB)
    if(SWA.equals(SWB))
    {
    count++;
    break;
    }
    }
    cosineValue=(count)/(Math.sqrt(sentanceWordA,length)*Math.sqrt(sentanceWordB,length));
    return cosineValue;
    }
    public static void main(string[] args)
    {
    Postprocessorpp=new PostProcessor();
    String s1="Ram is a good boy";
    String s2="Shyam is a bad boy";
    System.out.println("cosineValue==="+pp.getCosineValue(s1,s2));
    }
}