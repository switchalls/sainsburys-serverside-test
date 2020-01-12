package sw.sainsburys.serversidetest;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BerriesHtmlParser {

    private static final String PRODUCT_SELECTOR = "div.productNameAndPromotions";

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
		final JSONObject newProduct = new JSONObject();
		newProduct.put("title", htmlProduct.selectFirst("a").text());
		
		return newProduct;
	}
}
