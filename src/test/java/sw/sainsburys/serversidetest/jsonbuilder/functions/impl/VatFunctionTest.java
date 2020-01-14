package sw.sainsburys.serversidetest.jsonbuilder.functions.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.same;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertThat;

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
