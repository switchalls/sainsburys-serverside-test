package sw.sainsburys.serversidetest;

import org.json.JSONObject;

public class Main {

	public static void main(String[] args) {
		final BerriesHtmlParser parser = new BerriesHtmlParser(new JsoupConnectionProvider());
		try {
			final JSONObject result = parser.parse("https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html");
			System.out.println(result);

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}
