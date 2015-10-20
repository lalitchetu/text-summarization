package edu.research.textSummarization.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class FileUtil {
    public static String getContentFromFile(String filePath) {
        FileInputStream fileInputStream = null;
        try {
            File file = new File(filePath);
            fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, Constants.CHAR_ENCODING_UTF8));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + Constants.NEWLINE);
            }
            return stringBuffer.toString();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("FileNotFoundException" + ex.toString());
            return null;
        } catch (IOException ie) {
            ie.printStackTrace();
            System.out.println("IO Exception in reading file" + ie.toString());
            return null;
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException ex) {
                System.out.println("exception in closing inputstream");
            }
        }

    }// end of function

    public static void saveIntoFile(String text, String filePath) {
        PrintWriter printWriter = null;
        try {
            File file = new File(filePath);
            printWriter = new PrintWriter(file);
            printWriter.print(text);
            printWriter.flush();
        } catch (FileNotFoundException ex) {
            System.out.println("FileUtilSaveIntoFile EXception.Message=" + ex.getMessage());
        } finally {
            printWriter.close();
        }
    }

    public static void main(String[] args) {
        // String s = getContentFromFile("C:/82_utf.txt");
        // ArrayList<String> filepath =
        // getAbsolutePathofAllFiles("D:\\TeluguRetrieval");
        // testmemory();
        // System.out.println("content="+s);
        // appendWordsIntoFile("\u096D",bvdf fgjdf jdf");
        // appendWordsIntoFile("\u096D","jayesh jethva"};
        FileUtil.saveIntoFile("dfgdhghhfdghfhghfh", "C:/a.txt");
    }
}
