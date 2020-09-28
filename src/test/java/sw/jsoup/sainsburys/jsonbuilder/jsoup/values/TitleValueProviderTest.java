package sw.jsoup.sainsburys.jsonbuilder.jsoup.values;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static sw.jsoup.sainsburys.jsonbuilder.jsoup.TestProducts.aStrawberriesProduct;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TitleValueProviderTest {

	private static final String TEST_URL = "http://test-site/sainsburys-berries.html";
	
	@InjectMocks
	private TitleValueProvider testSubject;

	@Test
	public void shouldSetValueName() {
		// Then
		assertThat(testSubject.getName(), equalTo("title"));
	}

	@Test
	public void shouldLoadTitle() throws Exception {		
		// When
		final Optional<String> result = testSubject.getValue(aStrawberriesProduct(), TEST_URL);
		
		// Then
		assertThat(result.isPresent(), equalTo(true));
		assertThat(result.get(), equalTo("Sainsbury's Strawberries 400g"));
	}

}
