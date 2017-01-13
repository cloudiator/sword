package de.uniulm.omi.cloudiator.sword.core.properties;

import de.uniulm.omi.cloudiator.sword.api.properties.Properties;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.fail;

/**
 * Created by daniel on 13.01.17.
 */
public class PropertiesImplTest {

    @Test public void getPropertyKeyTest() {
        Properties properties =
            PropertiesBuilder.newBuilder().putProperty("a", "b").putProperty("c", "a")
                .putProperty("c", "d").build();
        assertThat(properties.getProperty("a"), is(equalTo("b")));
        assertThat(properties.getProperty("c"), is(equalTo("d")));
        assertThat(properties.getProperty("f", "g"), is(equalTo("g")));
    }

    @Test public void getPropertiesTest() {
        Properties properties =
            PropertiesBuilder.newBuilder().putProperties(new HashMap<String, String>() {{
                put("a", "b");
                put("c", "a");

            }}).putProperties(new HashMap<String, String>() {{
                put("c", "d");
            }}).build();
        assertThat(properties.getProperty("a"), is(equalTo("b")));
        assertThat(properties.getProperty("c"), is(equalTo("d")));
    }

    @Test public void rejectsNullKey() {
        Properties properties = PropertiesBuilder.newBuilder().build();
        try {
            properties.getProperty(null);
            fail("Expected properties.getProperty(null) to throw NullPointerException.");
        } catch (NullPointerException ignored) {
        }

        try {
            properties.getProperty(null, "Hello, World!");
            fail("Expected properties.getProperty(null,String) to throw NullPointerException");
        } catch (NullPointerException ignored) {
        }

    }

}
