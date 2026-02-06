package reports;

import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {

    private static ThreadLocal<ExtentTest> tlTest = new ThreadLocal<>();

    public static synchronized void startTest(String testName) {
        ExtentTest test = ExtentManager.getExtent().createTest(testName);
        tlTest.set(test);
    }

    public static synchronized ExtentTest getTest() {
        return tlTest.get();
    }

    public static synchronized void endTest() {
        tlTest.remove();
    }
}
