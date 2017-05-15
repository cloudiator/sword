package de.uniulm.omi.cloudiator.sword.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.domain.Configuration;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by daniel on 13.01.17.
 */
public class GroupEncodedIntoNameNamingStrategyTest {

  private GroupEncodedIntoNameNamingStrategy groupEncodedIntoNameNamingStrategy;

  @Before
  public void before() {
    Cloud cloudMock = Mockito.mock(Cloud.class);
    Configuration configuration = Mockito.mock(Configuration.class);
    when(cloudMock.configuration()).thenReturn(configuration);
    when(configuration.nodeGroup()).thenReturn("helloWorld");
    groupEncodedIntoNameNamingStrategy = new GroupEncodedIntoNameNamingStrategy(cloudMock);
  }

  @Test
  public void testUnique() {
    Set<String> names = new HashSet<>();
    for (int i = 0; i <= 100; i++) {
      String name = groupEncodedIntoNameNamingStrategy.generateUniqueNameBasedOnName("name");
      boolean add = names.add(name);
      if (!add) {
        fail("Generated duplicate name " + name);
      }
    }
  }

  @Test
  public void testUniqueGroupAndNameEncoded() {
    String name = groupEncodedIntoNameNamingStrategy.generateUniqueNameBasedOnName("name");
    assertThat(name, startsWith("helloWorld"));
    assertThat(name, endsWith("name"));
  }

  @Test
  public void testGroupAndNameEncoded() {
    String name = groupEncodedIntoNameNamingStrategy.generateNameBasedOnName("name");
    assertThat(name, startsWith("helloWorld"));
    assertThat(name, endsWith("name"));
  }

  @Test
  public void belongsToGroupTest() {
    String nameNonUnique = groupEncodedIntoNameNamingStrategy.generateNameBasedOnName("name");
    String nameUnique =
        groupEncodedIntoNameNamingStrategy.generateUniqueNameBasedOnName("name");
    assertTrue(groupEncodedIntoNameNamingStrategy.belongsToNamingGroup().test(nameNonUnique));
    assertTrue(groupEncodedIntoNameNamingStrategy.belongsToNamingGroup().test(nameUnique));
    assertFalse(groupEncodedIntoNameNamingStrategy.belongsToNamingGroup().test(null));
    assertFalse(groupEncodedIntoNameNamingStrategy.belongsToNamingGroup().test("name"));
    assertFalse(
        groupEncodedIntoNameNamingStrategy.belongsToNamingGroup().test("namehelloWorld"));
  }

}
