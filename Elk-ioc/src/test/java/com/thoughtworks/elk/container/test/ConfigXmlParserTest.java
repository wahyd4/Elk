package com.thoughtworks.elk.container.test;

import com.thoughtworks.elk.container.ConfigXmlParser;
import com.thoughtworks.elk.container.Property;
import com.thoughtworks.elk.container.exception.ElkParseException;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigXmlParserTest {

    private ConfigXmlParser configConstructorXmlParser;
    private ConfigXmlParser configSetterXmlParser;

    @Before
    public void setUp() throws Exception, ElkParseException {
        configConstructorXmlParser = new ConfigXmlParser("testConstructorInjection.xml");
        configSetterXmlParser = new ConfigXmlParser("testSetterInjection.xml");
    }

    @Test(expected = ElkParseException.class)
    public void shouldSThrowElkParseException() throws ElkParseException {
        new ConfigXmlParser("fake_file.xml");
    }

    @Test
    public void should_return_a_node_given_bean_id() {
        Node company = configConstructorXmlParser.getNode("company");

        assertThat(company, notNullValue());
        assertThat(company.getNodeName(), is("bean"));
    }

    @Test
    public void should_return_a_node_given_parent_bean_id_and_child_node_name() {
        ArrayList<Node> nodeArrayList = configConstructorXmlParser.getChildNodes("company", "description");

        assertThat(nodeArrayList.size(), is(1));
        assertThat(nodeArrayList.get(0).getChildNodes().item(0).getNodeValue(), is("test"));
    }

    @Test
    public void should_get_class_name_given_a_bean_id() {
        String attribute = configConstructorXmlParser.getBeanClass("company");

        assertThat(attribute, is("com.thoughtworks.elk.movie.Hollywood"));
    }

    @Test
    public void should_get_constructor_dependencies_class_given_a_bean_id() {
        List<String> constructorDependencies = configConstructorXmlParser.getConstructorDependenciesClass("director");

        assertThat(constructorDependencies.get(0), is("com.thoughtworks.elk.movie.Movie"));
        assertThat(constructorDependencies.size(), is(2));
    }

    @Test
    public void should_get_constructor_dependencies_name_given_a_bean_id() {
        List<String> constructorDependencies = configConstructorXmlParser.getConstructorDependenciesName("director");

        assertThat(constructorDependencies.get(0), is("movie"));
        assertThat(constructorDependencies.size(), is(2));
    }

    @Test
    public void should_get_properties_name_given_a_bean_id() {
        List<Property> properties = configSetterXmlParser.getProperties("director");

        assertThat(properties.get(0).getName(), is("movie"));
        assertThat(properties.get(0).getRef(), is("movie"));
        assertThat(properties.get(0).getType(), is("com.thoughtworks.elk.movie.Movie"));
        assertThat(properties.size(), is(1));
    }
}