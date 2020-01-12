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
	
	public JsonProductMatcher withTitle(String title) {
		return this.withTitle(equalTo(title));
	}

	public JsonProductMatcher withTitle(Matcher<String> title) {
		this.title = title;
		return this;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("JsonProduct with: ");

		if (title != null) {
			description.appendText("title");
			description.appendValue(this.title);
		}
	}

	@Override
	protected boolean matchesSafely(JSONObject item, Description mismatchDescription) {
		if (title != null && !title.matches(item.get("title"))) {
			title.describeMismatch(item.get("title"), mismatchDescription);
			return false;
		}

		return true;
	}

}
