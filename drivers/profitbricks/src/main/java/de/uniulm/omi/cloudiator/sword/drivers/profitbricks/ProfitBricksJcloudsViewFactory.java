package de.uniulm.omi.cloudiator.sword.drivers.profitbricks;

import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.drivers.jclouds.BaseJCloudsViewFactory;
import de.uniulm.omi.cloudiator.sword.logging.LoggerFactory;
import java.util.Properties;
import javax.inject.Inject;
import org.jclouds.Constants;

public class ProfitBricksJcloudsViewFactory extends BaseJCloudsViewFactory {

  @Inject
  public ProfitBricksJcloudsViewFactory(Cloud cloud,
      LoggerFactory loggerFactory) {
    super(cloud, loggerFactory);
  }

  @Override
  protected Properties overrideProperties(Properties properties) {
    Properties superProperties = super.overrideProperties(properties);
    superProperties.setProperty(Constants.PROPERTY_USER_AGENT, "jclouds/1.0 urlfetch/1.4.3");
    return superProperties;
  }
}
