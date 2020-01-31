package sw.springboot.console.jsonbuilder.jsoup.values;

import java.util.Optional;

import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import sw.springboot.console.jsonbuilder.jsoup.JsoupNamedValueProvider;

@Component
public class TitleValueProvider implements JsoupNamedValueProvider<String> {

    private static final String PRODUCT_DETAILS_LINK_SELECTOR = "a";

	@Override
	public String getName() {
		return "title";
	}

	@Override
	public Optional<String> getValue(Element source, String sourceUrl) {
		final Element productDetailsLink = source.selectFirst(PRODUCT_DETAILS_LINK_SELECTOR);
		return Optional.of(productDetailsLink.text());
	}

}
