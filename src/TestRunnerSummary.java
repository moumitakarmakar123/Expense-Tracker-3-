import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestRunnerSummary {
  public static void main(String[] args) {
    Result r = JUnitCore.runClasses(TestExample.class, ExportTests.class);
    int run = r.getRunCount();
    int failures = r.getFailureCount();
    int skipped = r.getIgnoreCount();
    int errors = 0;
    System.out.println("Tests run: " + run + ", Failures: " + failures + ", Errors: " + errors + ", Skipped: " + skipped);
    if (failures > 0) {
      r.getFailures().forEach(f -> System.out.println(f.toString()));
    }
  }
}

