package nz.bignell.bede.camel;

import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@RunWith(CamelSpringRunner.class)
@ContextConfiguration(classes = {ReproduceUnableToLoadProperties.ExampleRouteBuilder.class, ReproduceUnableToLoadProperties.ContextConfig.class}, loader = CamelSpringDelegatingTestContextLoader.class)
@TestPropertySource(
    properties = {
        "rankOfCamel=1"
    }
)
public class ReproduceUnableToLoadProperties extends AbstractJUnit4SpringContextTests {
  @Autowired
  ProducerTemplate genericProducer;

  @Test
  public void checkProducer() {
    Assert.assertTrue(true);
    Assert.assertNotNull(genericProducer);
    Object resp = genericProducer.sendBody("direct:testA", ExchangePattern.InOut, " Claus");
    Assert.assertEquals("hi Claus", resp);
  }

  @Component
  public static class ExampleRouteBuilder extends RouteBuilder {
    @Value("${rankOfCamel}")
    int rankOfCamel;

    @Override
    public void configure() throws Exception {
      from("direct:testA")
          .log("Camel is number: " + rankOfCamel)
          .setBody(constant("hi ").append(body().convertToString()))
      ;
    }
  }

  @Configuration
  public static class ContextConfig extends SingleRouteCamelConfiguration {
    ReproduceUnableToLoadProperties.ExampleRouteBuilder exampleRouteBuilder;

    // Constructor Injection of Spring bean 'ExampleRouteBuilder'
    ContextConfig(ReproduceUnableToLoadProperties.ExampleRouteBuilder exampleRouteBuilder) {
      this.exampleRouteBuilder = exampleRouteBuilder;
    }

    @Override
    @Bean
    public RouteBuilder route() {
      return exampleRouteBuilder;
    }
  }
}
