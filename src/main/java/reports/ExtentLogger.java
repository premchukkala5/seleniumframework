package reports;

import com.aventstack.extentreports.Status;
import java.nio.file.Paths;

public class ExtentLogger {

    public static void pass(String message){
        ExtentTestManager.getTest().log(Status.PASS,message);
    }
    public static void fail(String message){
        ExtentTestManager.getTest().log(Status.FAIL,message);
    }
     public static void skip(String message){
        ExtentTestManager.getTest().log(Status.SKIP,message);
    }
    public static void info(String message){
        ExtentTestManager.getTest().log(Status.INFO,message);
    }
    public static void fail(String message, String screenshotPath){
        // Log the fail message FIRST
        ExtentTestManager.getTest().log(Status.FAIL, message);

        // THEN attach the screenshot so it appears under the failed step
        try {
            // Convert absolute path to file:// URI for proper attachment
            String reportScreenshotPath = Paths.get(screenshotPath).toUri().toString();
            ExtentTestManager.getTest().addScreenCaptureFromPath(reportScreenshotPath);
        } catch (Exception e) {
            System.out.println("Error attaching screenshot: " + e.getMessage());
        }
    }
}
