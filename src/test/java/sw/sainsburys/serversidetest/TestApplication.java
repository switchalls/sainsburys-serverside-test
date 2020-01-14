package sw.sainsburys.serversidetest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Spring entry point for running test suites without triggering {@link CommandLineRunner} entry points.
 * 
 * @author stewartw
 */
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		value = CommandLineRunner.class))
@EnableAutoConfiguration
public class TestApplication {
}
