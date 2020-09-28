package sw.jsoup.sainsburys.jsonbuilder.jsoup.values;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static sw.jsoup.sainsburys.jsonbuilder.jsoup.TestProducts.aProductDetailsFor;

import java.util.Optional;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import sw.jsoup.sainsburys.jsonbuilder.jsoup.ProductDetailsProvider;
import sw.jsoup.sainsburys.utils.DisplayedValueParser;

@RunWith(MockitoJUnitRunner.class)
public class NutritionValueProviderTest {

	private static final String TEST_URL = "http://test-site/sainsburys-berries.html";

	@Mock
	private Document mockDocument;
	
	@Mock
	private DisplayedValueParser mockDisplayedValueParser;

	@Mock
	private ProductDetailsProvider mockProductDetailsProvider;
	
	@InjectMocks
	private NutritionValueProvider testSubject;

	@Test
	public void shouldSetValueName() {
		// Then
		assertThat(testSubject.getName(), equalTo("kcal_per_100g"));
	}

	@Test
	public void shouldLoadFromNutritionLevelField() throws Exception {		
		// Given
		when(mockProductDetailsProvider.loadProductDetails(any(Element.class), anyString()))
			.thenReturn(aProductDetailsFor("british-strawberries-400g"));
		
		when(mockDisplayedValueParser.getFirstNumericFieldFrom("33kcal"))
			.thenReturn(33.0);
		
		// When
		final Optional<Double> result = testSubject.getValue(mockDocument, TEST_URL);
		
		// Then
		assertThat(result.isPresent(), equalTo(true));
		assertThat(result.get(), equalTo(33.0));
	}

	@Test
	public void shouldLoadFromEnergyKcalRow() throws Exception {		
		// Given
		when(mockProductDetailsProvider.loadProductDetails(any(Element.class), anyString()))
			.thenReturn(aProductDetailsFor("cherry-punnet-200g"));
		
		when(mockDisplayedValueParser.getFirstNumericFieldFrom("52"))
			.thenReturn(52.0);
		
		// When
		final Optional<Double> result = testSubject.getValue(mockDocument, TEST_URL);
		
		// Then
		assertThat(result.isPresent(), equalTo(true));
		assertThat(result.get(), equalTo(52.0));
	}

	@Test
	public void shouldLoadFromNutritionTable() throws Exception {		
		// Given
		when(mockProductDetailsProvider.loadProductDetails(any(Element.class), anyString()))
			.thenReturn(aProductDetailsFor("blueberries-200g"));
		
		when(mockDisplayedValueParser.getFirstNumericFieldFrom("45kcal"))
			.thenReturn(45.0);
		
		// When
		final Optional<Double> result = testSubject.getValue(mockDocument, TEST_URL);
		
		// Then
		assertThat(result.isPresent(), equalTo(true));
		assertThat(result.get(), equalTo(45.0));
	}

	@Test
	public void shouldNotLoadValueWhenMissing() throws Exception {		
		// Given
		when(mockProductDetailsProvider.loadProductDetails(any(Element.class), anyString()))
			.thenReturn(aProductDetailsFor("blackcurrents-150g"));
				
		// When
		final Optional<Double> result = testSubject.getValue(mockDocument, TEST_URL);
		
		// Then
		verify(mockDisplayedValueParser, never()).getFirstNumericFieldFrom(anyString());
		assertThat(result.isPresent(), equalTo(false));
	}

}
