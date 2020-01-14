package sw.sainsburys.serversidetest.matchers;

import static org.hamcrest.Matchers.equalTo;

import javax.annotation.Nullable;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.json.JSONObject;

public class JsonProductMatcher extends TypeSafeDiagnosingMatcher<JSONObject>{

	public static JsonProductMatcher isJsonProduct() {
		return new JsonProductMatcher();
	}

	private Matcher<String> description;

	private Matcher<Double> nutritionLevel;

	private Matcher<String> title;
	
	private Matcher<Double> unitPrice;

	public JsonProductMatcher withDescription(String description) {
		return this.withDescription(equalTo(description));
	}

	public JsonProductMatcher withDescription(Matcher<String> description) {
		this.description = description;
		return this;
	}

	public JsonProductMatcher withNutritionLevel(double nutritionLevel) {
		return this.withNutritionLevel(equalTo(nutritionLevel));
	}

	public JsonProductMatcher withNutritionLevel(Matcher<Double> nutritionLevel) {
		this.nutritionLevel = nutritionLevel;
		return this;
	}

	public JsonProductMatcher withTitle(String title) {
		return this.withTitle(equalTo(title));
	}

	public JsonProductMatcher withTitle(Matcher<String> title) {
		this.title = title;
		return this;
	}

	public JsonProductMatcher withUnitPrice(double unitPrice) {
		return this.withUnitPrice(equalTo(unitPrice));
	}

	public JsonProductMatcher withUnitPrice(Matcher<Double> unitPrice) {
		this.unitPrice = unitPrice;
		return this;
	}

	@Override
	public void describeTo(Description description) {
		int fieldCount = 0;

		description.appendText("JsonProduct with: ");
		
		fieldCount = this.addFieldDescription(description, fieldCount, "title", this.title);
		fieldCount = this.addFieldDescription(description, fieldCount, "unitPrice", this.unitPrice);
		fieldCount = this.addFieldDescription(description, fieldCount, "description", this.description);
		fieldCount = this.addFieldDescription(description, fieldCount, "nutritionLevel", this.nutritionLevel);
	}

	@Override
	protected boolean matchesSafely(JSONObject item, Description mismatchDescription) {
		return this.matchesField("description", this.description, item, mismatchDescription)
			&& this.matchesField("title", this.title, item, mismatchDescription)
			&& this.matchesField("unitPrice", this.unitPrice, item, mismatchDescription)
			&& this.matchesField("kcal_per_100g", this.nutritionLevel, item, mismatchDescription);
	}

	private int addFieldDescription(
			Description description,
			int fieldCount,
			String fieldName,
			@Nullable Matcher<?> matcher) {

		if (matcher != null) {
			if (fieldCount > 0) {
				description.appendText(" and ");
			}
			
			description.appendText(fieldName);
			description.appendText(" ");
			matcher.describeTo(description);
			
			return fieldCount + 1;
		}
		
		return fieldCount;
	}

	private boolean matchesField(
			String fieldName,
			@Nullable Matcher<?> matcher,
			JSONObject item,
			Description mismatchDescription) {

		if (matcher != null) {
			final Object value = item.get(fieldName);
			if (!matcher.matches(value)) {
				mismatchDescription.appendText(fieldName);
				mismatchDescription.appendText(" ");
				matcher.describeMismatch(value, mismatchDescription);
				return false;
			}
		}
		
		return true;
	}
}
