package sw.jsoup.sainsburys.jsonbuilder.jsoup;

import java.util.Collection;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sw.jsoup.sainsburys.jsonbuilder.BerriesCherriesCurrantsJsonBuilder;
import sw.jsoup.sainsburys.jsonbuilder.functions.TotalsFunction;

@Component
public class JsoupBerriesCherriesCurrantsJsonBuilder implements BerriesCherriesCurrantsJsonBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsoupBerriesCherriesCurrantsJsonBuilder.class);

    private static final String PRODUCT_SELECTOR = "div.product";

	private final JsoupConnectionProvider connectionProvider;
	
	private final Collection<TotalsFunction<?>> totalsFunctions;

	private final Collection<JsoupNamedValueProvider<?>> fieldValueProviders;

	private String url;
	
	@Autowired
	public JsoupBerriesCherriesCurrantsJsonBuilder(
			JsoupConnectionProvider connectionProvider,
			Collection<JsoupNamedValueProvider<?>> fieldValueProviders,
			Collection<TotalsFunction<?>> totalsFunctions) {
		
		this.connectionProvider = connectionProvider;
		this.fieldValueProviders = fieldValueProviders;
		this.totalsFunctions = totalsFunctions;
	}

	public BerriesCherriesCurrantsJsonBuilder withProductField(JsoupNamedValueProvider<?> provider) {
		this.fieldValueProviders.add(provider);
		return this;
	}

	public BerriesCherriesCurrantsJsonBuilder withTotalFunction(TotalsFunction<?> func) {
		this.totalsFunctions.add(func);
		return this;
	}

	@Override
	public BerriesCherriesCurrantsJsonBuilder withHtmlPage(String url) {
		this.url = url;
		return this;
	}

	@Override
	public JSONObject build() throws Exception {
		LOGGER.info("Parsing main page " + url);

		final Document berriesHtml = this.connectionProvider.createConnectionFor(url).get();

		final JSONArray jsonProducts = new JSONArray();
		berriesHtml.select(PRODUCT_SELECTOR).forEach(
				(p) -> jsonProducts.put(this.createJsonForProduct(url, p))
		);

		return new JSONObject()
				.put("result", jsonProducts)
				.put("total", this.createTotals(jsonProducts));
	}

	private JSONObject createJsonForProduct(String baseUrl, Element htmlProduct) {
		final JSONObject jsonProduct = new JSONObject();
		this.fieldValueProviders.forEach((p) -> {
			final Optional<?> value = p.getValue(htmlProduct, url);
			if (value.isPresent()) {
				jsonProduct.put(p.getName(), value.get());
			}
		});

		return jsonProduct;
	}
	
	private JSONObject createTotals(JSONArray jsonProducts) {
		final JSONObject jsonProduct = new JSONObject();
		this.totalsFunctions.forEach((f) ->
			jsonProduct.put(f.getName(), f.apply(jsonProducts))
		);

		return jsonProduct;
	}

}
