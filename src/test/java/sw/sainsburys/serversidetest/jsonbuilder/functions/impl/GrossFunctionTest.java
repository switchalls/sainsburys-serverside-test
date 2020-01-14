package sw.sainsburys.serversidetest.jsonbuilder.functions.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GrossFunctionTest {

	@InjectMocks
	private GrossFunction testSubject;

	@Test
	public void shouldTotalUnitPriceValues() {
		// Given
		final JSONArray source = new JSONArray();
		source.put(new JSONObject().put("unitPrice", 1.1d));
		source.put(new JSONObject().put("unitPrice", 2.2d));
		source.put(new JSONObject().put("unitPrice", 3.3d));

		// When
		final double result = testSubject.apply(source);
		
		// Then
		assertThat(result, equalTo(6.6));
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowExceptinWhenUnitPriceValueMissing() {
		// Given
		final JSONArray source = new JSONArray();
		source.put(new JSONObject().put("unitPrice", 1.1d));
		source.put(new JSONObject());

		// When
		testSubject.apply(source);
	}

}
