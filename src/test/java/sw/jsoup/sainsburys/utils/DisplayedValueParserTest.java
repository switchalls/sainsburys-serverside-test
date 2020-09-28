package sw.jsoup.sainsburys.utils;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;

@RunWith(DataProviderRunner.class)
public class DisplayedValueParserTest {

	@InjectMocks
	private DisplayedValueParser testSubject;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@DataProvider({ "33", "£33", "33/unit", "£33/unit" })
    @Test
    public void shouldIgnoreTextAroundNumber(String text) {
        assertThat(testSubject.getFirstNumericFieldFrom(text), equalTo(33.0));
    }

}
