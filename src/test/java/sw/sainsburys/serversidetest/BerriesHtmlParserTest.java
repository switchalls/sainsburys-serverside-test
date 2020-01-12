package sw.sainsburys.serversidetest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.hamcrest.Matcher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;

import static sw.sainsburys.serversidetest.JsonProductMatcher.isJsonProduct;

@RunWith(DataProviderRunner.class)
public class BerriesHtmlParserTest {

	private static final String EXPECTED_URL = "sainsburys-berries.html";

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Mock
	private JsoupConnectionProvider mockJsoupConnectionProvider;
	
	@Mock
	private Connection mockConnection;

	@InjectMocks
	private BerriesHtmlParser testSubject;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		when(mockJsoupConnectionProvider.createConnectionFor(anyString()))
			.thenReturn(mockConnection);
	}

	@Test
	public void shouldThrowExceptionWhenCannotConnect() throws Exception {
		// Expects
		exceptionRule.expect(IOException.class);
		exceptionRule.expectMessage("Bang!");

		// Given
		when(mockConnection.get())
			.thenThrow(new IOException("Bang!"));
	
		// When
		testSubject.parse(EXPECTED_URL);
	}

	@Test
	public void shouldConnectToStatedUrl() throws Exception {
		// Given
		when(mockConnection.get())
			.thenReturn(loadHtmlDocument("sainsburys-berries.html"))
			.thenReturn(loadHtmlDocument("sainsburys-strawberry.html"));

		// When
		final JSONObject result = testSubject.parse(EXPECTED_URL);

		// Then
		assertThat(result.get("result"), not(nullValue()));
		
		verify(mockJsoupConnectionProvider).createConnectionFor(eq(EXPECTED_URL));
	}

	@Test
	public void shouldAddProductTitle() throws Exception {
		// Given
		when(mockConnection.get())
			.thenReturn(loadHtmlDocument("sainsburys-berries.html"))
			.thenReturn(loadHtmlDocument("sainsburys-strawberry.html"));

		// When
		final JSONObject result = testSubject.parse(EXPECTED_URL);

		// Then
		final JSONArray products = result.getJSONArray("result");
		
		assertThat(products.length(), equalTo(17));
		
		assertThat(products, (Matcher) hasItems(
				isJsonProduct()
					.withTitle("Sainsbury's Strawberries 400g"),
				isJsonProduct()
					.withTitle("Sainsbury's Blackberries, Sweet 150g")));		
	}

	@Test
	public void shouldAddProductUnitPrice() throws Exception {
		// Given
		when(mockConnection.get())
			.thenReturn(loadHtmlDocument("sainsburys-berries.html"))
			.thenReturn(loadHtmlDocument("sainsburys-strawberry.html"));

		// When
		final JSONObject result = testSubject.parse(EXPECTED_URL);

		// Then		
		assertThat(result.getJSONArray("result"), (Matcher) hasItems(
				isJsonProduct()
					.withTitle("Sainsbury's Strawberries 400g")
					.withUnitPrice(1.75),
				isJsonProduct()
					.withTitle("Sainsbury's Blackberries, Sweet 150g")
					.withUnitPrice(1.5)));		
	}

	@Test
	public void shouldAddProductDescription() throws Exception {
		// Given
		when(mockConnection.get())
			.thenReturn(loadHtmlDocument("sainsburys-berries.html"))
			.thenReturn(loadHtmlDocument("sainsburys-strawberry.html"))
			.thenReturn(loadHtmlDocument("sainsburys-cherry.html"));

		// When
		final JSONObject result = testSubject.parse(EXPECTED_URL);

		// Then		
		assertThat(result.getJSONArray("result"), (Matcher) hasItems(
				isJsonProduct()
					.withTitle("Sainsbury's Strawberries 400g")
					.withDescription("by Sainsbury's strawberries"),
				isJsonProduct()
					.withTitle("Sainsbury's Blackberries, Sweet 150g")
					.withDescription("Cherries")));
	}

	@Test
	public void shouldAddProductNutritionLevel() throws Exception {
		// Given
		when(mockConnection.get())
			.thenReturn(loadHtmlDocument("sainsburys-berries.html"))
			.thenReturn(loadHtmlDocument("sainsburys-strawberry.html"))
			.thenReturn(loadHtmlDocument("sainsburys-cherry.html"));

		// When
		final JSONObject result = testSubject.parse(EXPECTED_URL);

		// Then
		assertThat(result.getJSONArray("result"), hasItem(
				isJsonProduct()
					.withTitle("Sainsbury's Strawberries 400g")
					.withNutritionLevel(33)));
	}

	@Test
	public void shouldAddTotals() throws Exception {
		// Given
		when(mockConnection.get())
			.thenReturn(loadHtmlDocument("sainsburys-berries.html"))
			.thenReturn(loadHtmlDocument("sainsburys-strawberry.html"));

		// When
		final JSONObject result = testSubject.parse(EXPECTED_URL);

		// Then
		final JSONObject totals = result.getJSONObject("total");
		assertThat(totals.getDouble("gross"), equalTo(39.5));
		assertThat(totals.getDouble("vat"), equalTo(7.9));
	}

	@DataProvider({ "33", "£33", "33/unit", "£33/unit" })
    @Test
    public void shouldIgnoreTextAroundNumber(String text) {
        assertThat(testSubject.getFirstNumericField(text), equalTo(33.0));
    }

	private Document loadHtmlDocument(String path) throws Exception {
        final URL htmlUrl = this.getClass().getResource(path);
        assertThat("Cannot find file on classpath: " + path, htmlUrl, not(nullValue(URL.class)));

        try (InputStream fin = htmlUrl.openStream()) {
            return Jsoup.parse(fin, "UTF-8", "http://sw-test.com");
        }
    }

}
