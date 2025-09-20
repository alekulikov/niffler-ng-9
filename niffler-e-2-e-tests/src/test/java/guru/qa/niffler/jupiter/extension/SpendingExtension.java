package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.service.SpendClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static guru.qa.niffler.jupiter.extension.TestMethodContextExtension.context;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

  private final SpendClient spendClient = SpendClient.getInstance();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(anno -> {
          if (anno.spendings().length > 0) {
            UserDataJson createdUser = UserExtension.createdUser();
            String username = createdUser != null
                ? createdUser.username()
                : anno.username();
            List<SpendJson> createdSpends = new ArrayList<>();

            for (Spending spend : anno.spendings()) {
              SpendJson spendJson = new SpendJson(
                  null,
                  new Date(),
                  new CategoryJson(
                      null,
                      spend.category(),
                      username,
                      false
                  ),
                  spend.currency(),
                  spend.amount(),
                  spend.description(),
                  username
              );
              SpendJson created = spendClient.createSpend(spendJson);
              createdSpends.add(created);
            }

            if (createdUser != null) {
              createdUser.testData().spendings().addAll(createdSpends);
            } else {
              context.getStore(NAMESPACE).put(context.getUniqueId(), createdSpends);
            }
          }
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(SpendJson[].class);
  }

  @Override
  public SpendJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return createdSpending().toArray(SpendJson[]::new);
  }

  @SuppressWarnings("unchecked")
  public static List<SpendJson> createdSpending() {
    final ExtensionContext methodContext = context();
    return methodContext.getStore(NAMESPACE)
        .get(methodContext.getUniqueId(), List.class);
  }
}
