package sw.sainsburys.serversidetest.jsonbuilder.jsoup.values;

import java.util.Optional;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sw.sainsburys.serversidetest.jsonbuilder.jsoup.JsoupNamedValueProvider;
import sw.sainsburys.serversidetest.jsonbuilder.jsoup.ProductDetailsProvider;

@Component
public class DescriptionValueProvider implements JsoupNamedValueProvider<String> {

    private static final String DESCRIPTION_LONG_TEXT_ITEMS_SELECTOR = "div#information :contains(Description) div.longTextItems";

    private static final String DESCRIPTION_SINGLE_LINE_SELECTOR = "div#information div.productText";

    private final ProductDetailsProvider detailsProvider;
    
    @Autowired
    public DescriptionValueProvider(ProductDetailsProvider detailsProvider) {
    	this.detailsProvider = detailsProvider;
    }

	@Override
	public String getName() {
		return "description";
	}

	@Override
	public Optional<String> getValue(Element source, String sourceUrl) {
		final Document detailsHtml = detailsProvider.loadProductDetails(source, sourceUrl);
		return Optional.of(this.getProductDescription(detailsHtml).text());
	}

	private Element getProductDescription(Document detailsHtml) {
		final Element longTextItems = detailsHtml.selectFirst(DESCRIPTION_LONG_TEXT_ITEMS_SELECTOR);
		if (longTextItems != null) {
			return longTextItems;
		}
		
		return detailsHtml.selectFirst(DESCRIPTION_SINGLE_LINE_SELECTOR);
	}

}
