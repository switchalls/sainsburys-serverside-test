package sw.sainsburys.serversidetest;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.annotations.VisibleForTesting;

public class BerriesHtmlParser {

    private static final String PRODUCT_SELECTOR = "div.product";

    private static final String PRODUCT_DETAILS_LINK_SELECTOR = "a";

    private static final String PRICE_PER_UNIT_SELECTOR = ".pricePerUnit";

	private JsoupConnectionProvider connectionProvider;
	
	public BerriesHtmlParser(JsoupConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	public JSONObject parse(String url) throws IOException {
		final Document berriesHtml = this.connectionProvider.createConnectionFor(url).get();
		
		final JSONObject result = new JSONObject();
		result.put("result", this.createJsonForProducts(berriesHtml.select(PRODUCT_SELECTOR)));
		
		return result;
	}

	private JSONArray createJsonForProducts(Elements htmlProducts) {
		final JSONArray jsonProducts = new JSONArray();

		for (Element htmlProduct : htmlProducts) {
			jsonProducts.put(this.createJsonForProduct(htmlProduct));
		}
		
		return jsonProducts;
	}

	private JSONObject createJsonForProduct(Element htmlProduct) {
		final Element productDetailsLink = htmlProduct.selectFirst(PRODUCT_DETAILS_LINK_SELECTOR);
		final Element unitPrice = htmlProduct.selectFirst(PRICE_PER_UNIT_SELECTOR);
		
		final JSONObject newProduct = new JSONObject();
		newProduct.put("title", productDetailsLink.text());
		newProduct.put("unitPrice", this.getFirstNumericField(unitPrice.text()));
		
		return newProduct;
	}

	// TODO - Use mocked Document(s) to avoid exposing getFirstNumericField() method?

	@VisibleForTesting
    double getFirstNumericField(String text) {
        final StringBuffer sb = new StringBuffer();

        int i = 0;
        while (i < text.length() && !isCharForNumber(text.charAt(i))) {
            i++;
        }

        while (i < text.length() && isCharForNumber(text.charAt(i))) {
            sb.append(text.charAt(i++));
        }

        return Double.parseDouble(sb.toString());
    }

    private boolean isCharForNumber(char c) {
        return Character.isDigit(c) || c == '.';
    }

}