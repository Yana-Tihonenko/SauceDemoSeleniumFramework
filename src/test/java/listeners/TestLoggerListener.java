package listeners;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestLoggerListener extends TestListenerAdapter {
  private static final Logger logger = LogManager.getLogger(TestLoggerListener.class);


  @Override
  public void onTestStart(ITestResult result) {
    logger.info("------------------------------------------------------------------------");
    logger.info("Starting test: {}", result.getMethod().getMethodName());
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    logger.info("Test passed: {}", result.getMethod().getMethodName());
    logger.info("------------------------------------------------------------------------");
  }

  @Override
  public void onTestFailure(ITestResult result) {
    logger.error("Test failed: {}", result.getMethod().getMethodName());
    Throwable throwable = result.getThrowable();
    if (throwable != null) {
      logger.error("Failure reason: ", throwable);
    }
    logger.info("------------------------------------------------------------------------");
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    logger.warn("Test skipped: {}", result.getMethod().getMethodName());
    logger.info("------------------------------------------------------------------------");
  }
}
