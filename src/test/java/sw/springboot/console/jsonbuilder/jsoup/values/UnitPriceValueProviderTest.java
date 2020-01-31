package sw.springboot.console.jsonbuilder.jsoup.values;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static sw.springboot.console.jsonbuilder.jsoup.TestProducts.aStrawberriesProduct;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import sw.springboot.console.jsonbuilder.jsoup.values.UnitPriceValueProvider;
import sw.springboot.console.utils.DisplayedValueParser;

@RunWith(MockitoJUnitRunner.class)
public class UnitPriceValueProviderTest {

	private static final String TEST_URL = "http://test-site/sainsburys-berries.html";

	@Mock
	private DisplayedValueParser mockDisplayedValueParser;
	
	@InjectMocks
	private UnitPriceValueProvider testSubject;

	@Test
	public void shouldSetValueName() {
		// Then
		assertThat(testSubject.getName(), equalTo("unitPrice"));
	}

	@Test
	public void shouldLoadUnitPrice() throws Exception {		
		// Given
		when(mockDisplayedValueParser.getFirstNumericFieldFrom("£1.75/unit"))
			.thenReturn(1.75);
		
		// When
		final Optional<Double> result = testSubject.getValue(aStrawberriesProduct(), TEST_URL);
		
		// Then
		assertThat(result.isPresent(), equalTo(true));
		assertThat(result.get(), equalTo(1.75));
	}

}
