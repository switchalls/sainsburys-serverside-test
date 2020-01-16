package sw.sainsburys.serversidetest.jsonbuilder.jsoup;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.Matcher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static sw.sainsburys.serversidetest.jsonbuilder.jsoup.TestProducts.aProductsPage;
import static sw.sainsburys.serversidetest.matchers.JsonProductMatcher.isJsonProduct;

import java.io.IOException;

import static sw.sainsburys.serversidetest.jsonbuilder.jsoup.TestProducts.aProductDetailsFor;

import sw.sainsburys.serversidetest.TestApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class JsoupBerriesCherriesCurrantsJsonBuilderTest {

	private static final String EXPECTED_URL = "http://sainsburys-test/sainsburys-berries.html";

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@MockBean
	private JsoupConnectionProvider mockJsoupConnectionProvider;
	
	@Mock
	private Connection mockConnection;

	@Autowired
	private JsoupBerriesCherriesCurrantsJsonBuilder testSubject;

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
		testSubject
			.withHtmlPage(EXPECTED_URL)
			.build();
	}

	@Test
	public void shouldConnectToStatedUrl() throws Exception {
		// Given
		when(mockConnection.get())
			.thenReturn(aProductsPage())
			.thenReturn(aProductDetailsFor("british-strawberries-400g"));

		// When
		final JSONObject result = testSubject
			.withHtmlPage(EXPECTED_URL)
			.build();

		// Then
		assertThat(result.get("result"), not(nullValue()));
		
		verify(mockJsoupConnectionProvider).createConnectionFor(eq(EXPECTED_URL));
	}

	@Test
	public void shouldListProducts() throws Exception {
		// Given
		when(mockConnection.get())
			.thenReturn(aProductsPage())
			.thenReturn(aProductDetailsFor("british-strawberries-400g"))
			.thenReturn(aProductDetailsFor("blackberries-sweet-150g"));

		// When
		final JSONObject result = testSubject
				.withHtmlPage("http://test-site")
				.build();
		
		// Then
		final JSONArray products = result.getJSONArray("result");
		assertThat(products.length(), equalTo(17));

		assertThat(products, (Matcher) hasItems(
				isJsonProduct()
					.withTitle("Sainsbury's Strawberries 400g")
					.withUnitPrice(1.75)
					.withDescription("by Sainsbury's strawberries")
					.withNutritionLevel(33),
				isJsonProduct()
					.withTitle("Sainsbury's Blackberries, Sweet 150g")
					.withUnitPrice(1.5)
					.withDescription("by Sainsbury's blackberries")
					.withNutritionLevel(32),
				isJsonProduct()
					.withTitle("Sainsbury's British Cherry & Strawberry Pack 600g")
					.withUnitPrice(4.0)));
	}

	@Test
	public void shouldAddTotals() throws Exception {
		// Given
		when(mockConnection.get())
			.thenReturn(aProductsPage())
			.thenReturn(aProductDetailsFor("british-strawberries-400g"));

		// When
		final JSONObject result = testSubject
				.withHtmlPage(EXPECTED_URL)
				.build();

		// Then
		final JSONObject totals = result.getJSONObject("total");
		assertThat(totals.getDouble("gross"), equalTo(39.5));
		assertThat(totals.getDouble("vat"), equalTo(7.9));
	}


}
