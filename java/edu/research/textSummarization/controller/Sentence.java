package edu.research.textSummarization.controller;

public class Sentence {
    private int m_SentenceNumber;
    private String m_Sentence;
    private double m_SentenceWeight;
    private String m_SentenceSeperator;
    public Sentence(int sentenceNumber,String sentence,String sentenceSeparator)
    {
        m_SentenceNumber=sentenceNumber;
    m_Sentence=sentence;
    m_SentenceWeight=0;
    m_SentenceSeperator=sentenceSeparator;
    }
    public String getSentenceSeperator() 
    {
    return m_SentenceSeperator;
    }
    public void setSentenceSeperator(String m_SentenceSeperator) 
    {
    this.m_SentenceSeperator=m_SentenceSeperator;
    }
    public void setWeight(double weight)
    {
    m_SentenceWeight=weight;
    }
    public double getWeight()
    {
    return m_SentenceWeight;
    }
    public String getSentenceText()
    {
    return m_Sentence;
    }
    public int getSentenceNo()
    {
    return m_SentenceNumber;
    }
}
