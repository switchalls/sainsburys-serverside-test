package sw.sainsburys.serversidetest;

import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.json.JSONObject;

public class JsonProductMatcher extends TypeSafeDiagnosingMatcher<JSONObject>{

	public static JsonProductMatcher isJsonProduct() {
		return new JsonProductMatcher();
	}

	private Matcher<String> title;
	
	private Matcher<Double> unitPrice;

	public JsonProductMatcher withTitle(String title) {
		return this.withTitle(equalTo(title));
	}

	public JsonProductMatcher withTitle(Matcher<String> title) {
		this.title = title;
		return this;
	}

	public JsonProductMatcher withUnitPrice(double price) {
		return this.withUnitPrice(equalTo(price));
	}

	public JsonProductMatcher withUnitPrice(Matcher<Double> price) {
		this.unitPrice = price;
		return this;
	}

	@Override
	public void describeTo(Description description) {
		int fieldCount = 0;

		description.appendText("JsonProduct with: ");
		
		fieldCount = this.addFieldDescription(description, fieldCount, "title", this.title);
		fieldCount = this.addFieldDescription(description, fieldCount, "unitPrice", this.unitPrice);
	}

	private int addFieldDescription(Description description, int fieldCount, String fieldName, Matcher<?> matcher) {
		if (fieldCount > 0) {
			description.appendText(" and ");
		}
		
		description.appendText(fieldName);
		description.appendText(" ");
		matcher.describeTo(description);
		
		return fieldCount + 1;	
	}

	@Override
	protected boolean matchesSafely(JSONObject item, Description mismatchDescription) {
		if (title != null && !title.matches(item.get("title"))) {
			title.describeMismatch(item.get("title"), mismatchDescription);
			return false;
		}

		if (unitPrice != null && !unitPrice.matches(item.get("unitPrice"))) {
			unitPrice.describeMismatch(item.get("unitPrice"), mismatchDescription);
			return false;
		}

		return true;
	}

}
