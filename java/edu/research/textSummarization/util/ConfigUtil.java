package edu.research.textSummarization.util;

public class ConfigUtil {
    private static boolean iSSteamingEnabled;
    private static boolean iSStopWordRemovalEnabled;
    private static int summaryPercentage = 0;
   public static boolean isStopWordEnabled() {
    return iSSteamingEnabled;   
    }
     public static boolean isStemingEnabled() {
           return iSSteamingEnabled; 
     }
    public static void setIsStopWordRemovalEnabled(boolean isStopWordRemovalEnabled) {
        iSStopWordRemovalEnabled = isStopWordRemovalEnabled;
    }
    public static void setSummarySize(int percentage) {
          summaryPercentage = percentage;
     }
   public static int getSummarySIze() {
         return summaryPercentage;
   }
}
