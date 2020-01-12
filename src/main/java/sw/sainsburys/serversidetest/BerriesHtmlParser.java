package sw.sainsburys.serversidetest;

import java.io.IOException;
import java.util.Map;
import java.util.function.ToDoubleFunction;

import javax.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.annotations.VisibleForTesting;

public class BerriesHtmlParser {

    private static final String PRODUCT_SELECTOR = "div.product";

    private static final String PRODUCT_DETAILS_LINK_SELECTOR = "a";

    private static final String DESCRIPTION_FIRST_LINE_SELECTOR = "div#information :contains(Description) p:eq(0)";

    private static final String DESCRIPTION_SINGLE_LINE_SELECTOR = "div#information div.productText";

    private static final String NUTRITION_LEVEL_SELECTOR = ".nutritionLevel1";

    private static final String NUTRITION_TABLE_SELECTOR = "table.nutritionTable tbody tr:eq(1) td:eq(0)";

    private static final String ENERGY_KCAL_ROW_SELECTOR = "table.nutritionTable tbody th:contains(Energy kcal) + td";

    private static final String PRICE_PER_UNIT_SELECTOR = ".pricePerUnit";

	private static ToDoubleFunction<? super Object> JSONOBJECT_UNITPRICE = (p) ->
		((Map<String, Double>)p).get("unitPrice");

	private JsoupConnectionProvider connectionProvider;
	
	public BerriesHtmlParser(JsoupConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	public JSONObject parse(String url) throws IOException {
		final Document berriesHtml = this.connectionProvider.createConnectionFor(url).get();
		
		final String baseUrl = this.getBaseUrl(url);

		final JSONArray jsonProducts = new JSONArray();
		berriesHtml.select(PRODUCT_SELECTOR).forEach(
				(p) -> jsonProducts.put(this.createJsonForProduct(baseUrl, p))
		);
		
		final JSONObject result = new JSONObject()
				.put("result", jsonProducts)
				.put("total", this.createJsonForTotals(jsonProducts));
		
		return result;
	}

	private JSONObject createJsonForProduct(String baseUrl, Element htmlProduct) throws IllegalStateException {
		final Element productDetailsLink = htmlProduct.selectFirst(PRODUCT_DETAILS_LINK_SELECTOR);
		final Element unitPrice = htmlProduct.selectFirst(PRICE_PER_UNIT_SELECTOR);	
		
		String detailsUrl = productDetailsLink.attr("href");
		if (detailsUrl.startsWith("..")) {
			detailsUrl = baseUrl + "/" + detailsUrl;
		}

		final Document detailsHtml;
		try {
			detailsHtml = this.connectionProvider
				.createConnectionFor(detailsUrl)
				.get();

		} catch (IOException e) {
			// propagate as RunetimeException to allow use of method in lambda
			throw new IllegalStateException("Cannot load product details page: " + detailsUrl, e);
		}

		final Element description = this.getProductDescription(detailsHtml);
		final Element nutritionLevel = this.getNutritionLevel(detailsHtml);

		final JSONObject newProduct = new JSONObject()
				.put("title", productDetailsLink.text())
				.put("unitPrice", this.getFirstNumericField(unitPrice.text()))
				.put("description", description.text());
		
		if (nutritionLevel != null) {
			newProduct.put("kcal_per_100g", this.getFirstNumericField(nutritionLevel.text()));		
		}

		return newProduct;
	}

	private String getBaseUrl(String url) {
		final int lastSlash = url.lastIndexOf('/');
		return url.substring(0, lastSlash - 1);
	}
		
	private JSONObject createJsonForTotals(JSONArray products) {
		final double gross = products.toList().stream()
				.mapToDouble(JSONOBJECT_UNITPRICE)
				.sum();

		final JSONObject totals = new JSONObject()
				.put("gross", gross)
				.put("vat", (gross * 0.2));
		
		return totals;
	}

	@Nullable
	private Element getNutritionLevel(Document detailsHtml) {
		final Element nutritionLevel = detailsHtml.selectFirst(NUTRITION_LEVEL_SELECTOR);
		if (nutritionLevel != null) {
			return nutritionLevel;
		}
		
		final Element energyKcal = detailsHtml.selectFirst(ENERGY_KCAL_ROW_SELECTOR);
		if (energyKcal != null) {
			return energyKcal;
		}

		return detailsHtml.selectFirst(NUTRITION_TABLE_SELECTOR);
	}

	private Element getProductDescription(Document detailsHtml) {
		final Element description = detailsHtml.selectFirst(DESCRIPTION_FIRST_LINE_SELECTOR);
		if (description != null) {
			return description;
		}
		
		return detailsHtml.selectFirst(DESCRIPTION_SINGLE_LINE_SELECTOR);
	}

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
