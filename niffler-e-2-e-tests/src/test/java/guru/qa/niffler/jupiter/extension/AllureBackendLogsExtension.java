package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class AllureBackendLogsExtension implements SuiteExtension {

  public static final String caseName = "Niffler backend logs";

  @Override
  public void afterSuite() {
    final AllureLifecycle allureLifecycle = Allure.getLifecycle();
    final String caseId = UUID.randomUUID().toString();
    allureLifecycle.scheduleTestCase(new TestResult().setUuid(caseId).setName(caseName));
    allureLifecycle.startTestCase(caseId);

    addLogAttachment("niffler-auth");
    addLogAttachment("niffler-spend");
    addLogAttachment("niffler-userdata");
    addLogAttachment("niffler-currency");
    addLogAttachment("niffler-gateway");

    allureLifecycle.stopTestCase(caseId);
    allureLifecycle.writeTestCase(caseId);
  }

  private void addLogAttachment(String serviceName) {
    try (var fis = Files.newInputStream(
        Path.of(String.format("./logs/%s/app.log", serviceName))
    )) {
      Allure.getLifecycle().addAttachment(
          String.format("%s-log", serviceName),
          "text/html",
          ".log",
          fis
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}