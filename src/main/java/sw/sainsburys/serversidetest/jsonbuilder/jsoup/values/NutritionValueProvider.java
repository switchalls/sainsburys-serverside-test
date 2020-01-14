package sw.sainsburys.serversidetest.jsonbuilder.jsoup.values;

import java.util.Optional;

import javax.annotation.Nullable;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sw.sainsburys.serversidetest.jsonbuilder.jsoup.JsoupNamedValueProvider;
import sw.sainsburys.serversidetest.jsonbuilder.jsoup.ProductDetailsProvider;
import sw.sainsburys.serversidetest.utils.DisplayedValueParser;

@Component
public class NutritionValueProvider implements JsoupNamedValueProvider<Double> {

    private static final String NUTRITION_LEVEL_SELECTOR = ".nutritionLevel1";

    private static final String NUTRITION_TABLE_SELECTOR = "table.nutritionTable tbody tr:eq(1) td:eq(0)";

    private static final String ENERGY_KCAL_ROW_SELECTOR = "table.nutritionTable tbody th:contains(Energy kcal) + td";

    private final ProductDetailsProvider detailsProvider;
    
    private final DisplayedValueParser numberParser;

    @Autowired
    public NutritionValueProvider(
    		ProductDetailsProvider detailsProvider,
    		DisplayedValueParser numberParser) {

    	this.detailsProvider = detailsProvider;
    	this.numberParser = numberParser;
    }

	@Override
	public String getName() {
		return "kcal_per_100g";
	}

	@Override
	public Optional<Double> getValue(Element source, String sourceUrl) {
		final Document detailsHtml = detailsProvider.loadProductDetails(source, sourceUrl);
		final Element kcal = this.getNutritionLevel(detailsHtml);

		if (kcal == null) {
			return Optional.empty();
		}
		
		return Optional.of(numberParser.getFirstNumericFieldFrom(kcal.text()));
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

}
