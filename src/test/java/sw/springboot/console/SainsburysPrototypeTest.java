package sw.springboot.console;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;

/**
 * Sainsbury's <a href="https://jsainsburyplc.github.io/serverside-test/">serverside-test</a>
 * 
 * <p>
 * Strategy overview from analysing HTML downloaded using Chrome File / Save Page As ...
 * <ol>
 * <li>[Requirement] Do not include cross sell items, such as the Sainsbury’s Klip Lock Storage Set ;
 * Cross sell items (eg. Klip Lock Storage Set) tagged with class ".crossSellInfo"
 * 
 * <li>[Requirement] Omit the kcal_per_100g field, if calories are unavailable ;
 * Field tagged with ".nutritionLevel1"
 * 
 * <li>[Requirement] If the description is spread over multiple lines, scrape only the first line ;
 * {@link #getFirstLine(String)}
 * 
 * <li>[Requirement] Show unit price and total up to 2 decimal places, representing pounds and whole pence ;
 * Units prices listed to 2 dp
 * </ol>
 * 
 * <p>
 * See <a href="https://jsoup.org/cookbook/extracting-data/selector-syntax">Jsoup selector syntax</a> for help.
 * 
 * @author Stewart Witchalls
 */
@RunWith(DataProviderRunner.class)
public class SainsburysPrototypeTest {

    private static final String FIRST_LINE_DESCRIPTION_SELECTOR = "#information div:contains(Description) p:eq(0)";

    private static final String SINGLE_LINE_DESCRIPTION_SELECTOR = "div#information div.productText";

    private static final String PRODUCT_DETAILS_LINK_SELECTOR = ".productNameAndPromotions a";

    private static final String NUTRITION_LEVEL_SELECTOR = ".nutritionLevel1";

    private static final String PRICE_PER_UNIT_SELECTOR = ".pricePerUnit";

    @Test
    public void shouldListProductTitlesAndLinks() throws Exception {
        // Given
        final Document html = this.loadHtmlDocument("sainsburys-berries.html");

        // When
        final Elements result = html.select(".product");

        // Then
        assertThat(result.size(), equalTo(17));

        assertThat(
                result.first().select(PRODUCT_DETAILS_LINK_SELECTOR).first().text(),
                startsWith("Sainsbury's Strawberries 400g"));

        assertThat(
                result.last().select(PRODUCT_DETAILS_LINK_SELECTOR).first().text(),
                startsWith("Sainsbury's British Cherry & Strawberry Pack 600g"));

        // result.forEach((p) -> {
        // final Element link = p.selectFirst(PRODUCT_LINK_SELECTION);
        // System.out.println("title = " + link.text());
        // System.out.println("url = " + link.attr("href"));
        // });
    }

    @Test
    public void shouldListProductPrice() throws Exception {
        // Given
        final Document html = this.loadHtmlDocument("sainsburys-berries.html");

        // When
        final Elements result = html.select(".product");

        // Then
        assertThat(result.size(), equalTo(17));

        assertThat(
                result.first().select(PRICE_PER_UNIT_SELECTOR).text(),
                startsWith("£1.75/unit"));

        assertThat(
                result.last().select(PRICE_PER_UNIT_SELECTOR).text(),
                startsWith("£4.00/unit"));

        // result.forEach((p) -> {
        // final String pricePerUnit = p.selectFirst(PRICE_PER_UNIT_SELECTOR).text();
        // System.out.println("pricePerUnit = " + pricePerUnit);
        // });
    }

    @Test
    public void shouldFindSingleLineDescription() throws Exception {
        // Given
        final Document html = this.loadHtmlDocument("sainsburys-british-strawberries-400g.html");

        // When
        final Element result = html.selectFirst(SINGLE_LINE_DESCRIPTION_SELECTOR);

        // Then
        assertThat(result, not(nullValue(Element.class)));
        assertThat(result.text(), equalTo("by Sainsbury's strawberries"));
    }

    @Test
    public void shouldFindFirstLineOfDescription() throws Exception {
        // Given
        final Document html = this.loadHtmlDocument("sainsburys-cherry-punnet-200g.html");

        // When
        final Element result = html.selectFirst(FIRST_LINE_DESCRIPTION_SELECTOR);

        // Then
        assertThat(result, not(nullValue(Element.class)));
        assertThat(result.text(), equalTo("Cherries"));
    }

    @Test
    public void shouldFindProductNutritionalLevelWhenAvailable() throws Exception {
        // Given
        final Document html = this.loadHtmlDocument("sainsburys-british-strawberries-400g.html");

        // When
        final Element result = html.selectFirst(NUTRITION_LEVEL_SELECTOR);

        // Then
        assertThat(result, not(nullValue(Element.class)));
        assertThat(result.text(), equalTo("33kcal"));
    }

    @Test
    public void shouldNotFindProductNutritionalLevelWhenMissing() throws Exception {
        // Given
        final Document html = this.loadHtmlDocument("sainsburys-cherry-punnet-200g.html");

        // When
        final Element result = html.selectFirst(NUTRITION_LEVEL_SELECTOR);

        // Then
        assertThat(result, nullValue(Element.class));
    }

    @DataProvider({ "33", "£33", "33/unit", "£33/unit" })
    @Test
    public void shouldIgnoreTextAroundNumber(String text) {
        assertThat(this.getFirstNumericField(text), equalTo("33"));
    }

    private String getFirstNumericField(String text) {
        final StringBuffer sb = new StringBuffer();

        int i = 0;
        while (i < text.length() && !isCharForNumber(text.charAt(i))) {
            i++;
        }

        while (i < text.length() && isCharForNumber(text.charAt(i))) {
            sb.append(text.charAt(i++));
        }

        return sb.toString();
    }

    private boolean isCharForNumber(char c) {
        return Character.isDigit(c) || c == '.';
    }

    private Document loadHtmlDocument(String path) throws Exception {
        final URL htmlUrl = this.getClass().getResource(path);
        assertThat("Cannot find file on classpath: " + path, htmlUrl, not(nullValue(URL.class)));

        try (InputStream fin = htmlUrl.openStream()) {
            return Jsoup.parse(fin, "UTF-8", "http://sw-test.com");
        }
    }

}
