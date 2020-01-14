package sw.sainsburys.serversidetest.jsonbuilder.jsoup;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TestProducts {

    private static final String PRODUCT_SELECTOR = "div.product";

	public static Document aProductsPage() throws Exception {
		return loadHtmlDocument("../../sainsburys-berries.html");
	}

	public static Element aBlueberriesProduct() throws Exception {
		return aProductsPage().select(PRODUCT_SELECTOR).get(1);
	}

	public static Element aStrawberriesProduct() throws Exception {
		return aProductsPage().selectFirst(PRODUCT_SELECTOR);
	}

	public static Document aProductDetailsFor(String productName) throws Exception {
		return loadHtmlDocument("../../sainsburys-" + productName + ".html");
	}

	public static Document loadHtmlDocument(String path) throws Exception {
        final URL htmlUrl = TestProducts.class.getResource(path);
        assertThat("Cannot find file on classpath: " + path, htmlUrl, not(nullValue(URL.class)));

        try (InputStream fin = htmlUrl.openStream()) {
            return Jsoup.parse(fin, "UTF-8", "http://sw-test.com");
        }
    }

}
