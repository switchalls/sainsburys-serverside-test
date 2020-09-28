package sw.jsoup.sainsburys.jsonbuilder.functions.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VatFunctionTest {

	@Mock
	private GrossFunction mockGrossFunction;
	
	@InjectMocks
	private VatFunction testSubject;

	@Test
	public void shouldCalculateVatAtTwentyPercent() {
		// Given
		final JSONArray expectedSource = new JSONArray();
		
		when(mockGrossFunction.apply(any(JSONArray.class)))
				.thenReturn(100.1);

		// When
		final double result = testSubject.apply(expectedSource);
		
		// Then
		verify(mockGrossFunction).apply(same(expectedSource));
		assertThat(result, equalTo(20.02));
	}

}
