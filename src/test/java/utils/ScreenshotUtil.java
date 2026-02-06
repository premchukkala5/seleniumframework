package utils;

import base.DriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;

public class ScreenshotUtil {

    public static String takeScreenshot(String testName) throws IOException {

        TakesScreenshot takesScreenshot =(TakesScreenshot) DriverManager.getDriver();
        File src = takesScreenshot.getScreenshotAs(OutputType.FILE);
        String screenshotPath = System.getProperty("user.dir") + File.separator + "test-output"+File.separator +"screenshots" + File.separator + testName + "_" + System.currentTimeMillis() + ".png";
        File dest = new File(screenshotPath);

        // Create screenshots directory if it doesn't exist
        File screenshotDir = dest.getParentFile();
        if (screenshotDir != null && !screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }

        FileUtils.copyFile(src, dest);
        return screenshotPath;

    }
}



