package sw.jsoup.sainsburys.jsonbuilder.jsoup.values;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

@RunWith(MockitoJUnitRunner.class)
public class DescriptionValueProviderTest {

	private static final String TEST_URL = "http://test-site/sainsburys-berries.html";

	@Mock
	private Document mockDocument;
	
	@Mock
	private ProductDetailsProvider mockProductDetailsProvider;
	
	@InjectMocks
	private DescriptionValueProvider testSubject;

	@Test
	public void shouldSetValueName() {
		// Then
		assertThat(testSubject.getName(), equalTo("description"));
	}

	@Test
	public void shouldLoadSingleLineDescription() throws Exception {		
		// Given
		when(mockProductDetailsProvider.loadProductDetails(any(Element.class), anyString()))
			.thenReturn(aProductDetailsFor("british-strawberries-400g"));
		
		// When
		final Optional<String> result = testSubject.getValue(mockDocument, TEST_URL);
		
		// Then
		assertThat(result.isPresent(), equalTo(true));
		assertThat(result.get(), equalTo("by Sainsbury's strawberries"));
	}

	@Test
	public void shouldLoadDescriptionFromLongTextItems() throws Exception {		
		// Given
		when(mockProductDetailsProvider.loadProductDetails(any(Element.class), anyString()))
			.thenReturn(aProductDetailsFor("cherry-punnet-200g"));
		
		// When
		final Optional<String> result = testSubject.getValue(mockDocument, TEST_URL);
		
		// Then
		assertThat(result.isPresent(), equalTo(true));
		assertThat(result.get(), equalTo("Succulent and sweet 1 of 5 a-day 10 cherries"));
	}

	@Test
	public void shouldLoadDescriptionFromItemTypeGroup() throws Exception {
		// Given
		when(mockProductDetailsProvider.loadProductDetails(any(Element.class), anyString()))
			.thenReturn(aProductDetailsFor("blackcurrents-150g"));

		// When
		final Optional<String> result = testSubject.getValue(mockDocument, TEST_URL);

		// Then		
		assertThat(result.isPresent(), equalTo(true));
		assertThat(result.get(), equalTo("Union Flag"));

	}

	@Test
	public void shouldLoadDescriptionForCherryPunnet200g() throws Exception {
		// Given
		when(mockProductDetailsProvider.loadProductDetails(any(Element.class), anyString()))
			.thenReturn(aProductDetailsFor("cherry-punnet-200g"));

		// When
		final Optional<String> result = testSubject.getValue(mockDocument, TEST_URL);

		// Then		
		assertThat(result.isPresent(), equalTo(true));
		assertThat(result.get(), equalTo("Succulent and sweet 1 of 5 a-day 10 cherries"));

	}

	@Test
	public void shouldLoadDescriptionForMixedBerryTwinPack200g() throws Exception {
		// Given
		when(mockProductDetailsProvider.loadProductDetails(any(Element.class), anyString()))
			.thenReturn(aProductDetailsFor("mixed-berry-twin-pack-200g"));

		// When
		final Optional<String> result = testSubject.getValue(mockDocument, TEST_URL);

		// Then		
		assertThat(result.isPresent(), equalTo(true));
		assertThat(result.get(), equalTo("1 of 5 a-day 80g serving"));

	}

}
