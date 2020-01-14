package sw.sainsburys.serversidetest.jsonbuilder.jsoup.values;

import java.util.Optional;

import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sw.sainsburys.serversidetest.jsonbuilder.jsoup.JsoupNamedValueProvider;
import sw.sainsburys.serversidetest.utils.DisplayedValueParser;

@Component
public class UnitPriceValueProvider implements JsoupNamedValueProvider<Double> {

    private static final String PRICE_PER_UNIT_SELECTOR = ".pricePerUnit";

    private final DisplayedValueParser numberParser;
    
    @Autowired
    public UnitPriceValueProvider(DisplayedValueParser numberParser) {
    	this.numberParser = numberParser;
    }

	@Override
	public String getName() {
		return "unitPrice";
	}

	@Override
	public Optional<Double> getValue(Element source, String sourceUrl) {
		final Element unitPrice = source.selectFirst(PRICE_PER_UNIT_SELECTOR);
		return Optional.of(this.numberParser.getFirstNumericFieldFrom(unitPrice.text()));
	}

}
