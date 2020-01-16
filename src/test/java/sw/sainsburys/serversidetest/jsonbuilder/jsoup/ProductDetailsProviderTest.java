package sw.sainsburys.serversidetest.jsonbuilder.jsoup;

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static sw.sainsburys.serversidetest.jsonbuilder.jsoup.TestProducts.aProductDetailsFor;
import static sw.sainsburys.serversidetest.jsonbuilder.jsoup.TestProducts.aBlueberriesProduct;
import static sw.sainsburys.serversidetest.jsonbuilder.jsoup.TestProducts.aStrawberriesProduct;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsProviderTest {

	private static final String EXPECTED_BASE_URL = "http://test-site";

	private static final String TEST_URL = EXPECTED_BASE_URL + "/index.html";

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
	@Mock
	private JsoupConnectionProvider mockJsoupConnectionProvider;

	@Mock
	private Connection mockConnection;

	@InjectMocks
	private ProductDetailsProvider testSubject;
	
	@Before
	public void setUp() throws Exception {
		when(mockJsoupConnectionProvider.createConnectionFor(anyString()))
			.thenReturn(mockConnection);
	}

	@Test
	public void shouldLoadProductDetails() throws Exception {
		// Given
		final Document expectedDocument = aProductDetailsFor("british-strawberries-400g");
		
		when(mockConnection.get())
			.thenReturn(expectedDocument);

		// When
		final Document result = testSubject.loadProductDetails(aStrawberriesProduct(), TEST_URL);

		// Then
		assertThat(result, sameInstance(expectedDocument));
	}

	@Test
	public void shouldLoadProductDetailsUsingAbsoluteHref() throws Exception {
		// Given
		when(mockConnection.get())
			.thenReturn(aProductDetailsFor("british-strawberries-400g"));

		// When
		testSubject.loadProductDetails(aStrawberriesProduct(), TEST_URL);

		// Then		
		verify(mockJsoupConnectionProvider).createConnectionFor(eq(
				"https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/shop/gb/groceries/berries-cherries-currants/sainsburys-british-strawberries-400g.html"));
	}

	@Test
	public void shouldLoadProductDetailsUsingRelativeHref() throws Exception {
		// Given
		when(mockConnection.get())
			.thenReturn(aProductDetailsFor("british-strawberries-400g"));

		// When
		testSubject.loadProductDetails(aBlueberriesProduct(), TEST_URL);

		// Then		
		verify(mockJsoupConnectionProvider).createConnectionFor(eq(
				EXPECTED_BASE_URL + "/../../../../../../shop/gb/groceries/berries-cherries-currants/sainsburys-blueberries-200g.html"));
	}

	@Test
	public void shouldThrowExceptionWhenCannotLoadProductDetails() throws Exception {
		// Expects
		exceptionRule.expect(IllegalStateException.class);
		exceptionRule.expectMessage("Cannot load product details page: https://jsainsburyplc.github.io");

		// Given
		when(mockConnection.get())
			.thenThrow(new IOException("Bang!"));

		// When
		testSubject.loadProductDetails(aStrawberriesProduct(), TEST_URL);
	}

	@Test
	public void shouldCacheLastUrl() throws Exception {
		// Given
		final Document expectedDocument = aProductDetailsFor("british-strawberries-400g");
		
		when(mockConnection.get())
			.thenReturn(expectedDocument);

		testSubject.loadProductDetails(aBlueberriesProduct(), TEST_URL);

		// When
		final Document result = testSubject.loadProductDetails(aBlueberriesProduct(), TEST_URL);

		// When
		assertThat(result, sameInstance(expectedDocument));
	}

	@Test
	public void shouldNotCacheDifferentUrl() throws Exception {
		// Given
		final Document firstDocument = aProductDetailsFor("british-strawberries-400g");
		final Document secondDocument = aProductDetailsFor("blueberries-200g");
		
		when(mockConnection.get())
			.thenReturn(firstDocument)
			.thenReturn(secondDocument);

		testSubject.loadProductDetails(aStrawberriesProduct(), TEST_URL);

		// When
		final Document result = testSubject.loadProductDetails(aBlueberriesProduct(), TEST_URL);

		// When
		assertThat(result, sameInstance(secondDocument));
	}

}
