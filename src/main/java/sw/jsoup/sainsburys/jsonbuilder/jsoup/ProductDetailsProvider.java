package sw.jsoup.sainsburys.jsonbuilder.jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductDetailsProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDetailsProvider.class);

    private static final String PRODUCT_DETAILS_LINK_SELECTOR = "a";

    private Map<String, Document> documentCache = new HashMap<>();
    
    private final JsoupConnectionProvider connectionProvider;

    @Autowired
    public ProductDetailsProvider(JsoupConnectionProvider connectionProvider) {    	
    	this.connectionProvider = connectionProvider;
    }

    public Document loadProductDetails(Element source, String sourceUrl) {
		final Element productDetailsLink = source.selectFirst(PRODUCT_DETAILS_LINK_SELECTOR);

		final String detailsUrl = this.getAbsoluteUrl(
				this.getBaseUrl(sourceUrl),
				productDetailsLink.attr("href"));

		if (this.documentCache.containsKey(detailsUrl)) {
			return this.documentCache.get(detailsUrl);
		}

		LOGGER.info("Parsing linked page " + detailsUrl);

		try {
			final Document htmlPage = this.connectionProvider
				.createConnectionFor(detailsUrl)
				.get();
			
			// only need to optimise lookups for same product
			this.documentCache.clear();
			
			this.documentCache.put(detailsUrl, htmlPage);
			
			return htmlPage;

		} catch (IOException e) {
			// propagate as RuntimeException to allow use of method in lambda
			throw new IllegalStateException("Cannot load product details page: " + detailsUrl, e);
		}
	}
    
	private String getAbsoluteUrl(String baseUrl, String url) {
		if (url.startsWith(".")) {
			return baseUrl + "/" + url;
		}

		return url;
	}

	private String getBaseUrl(String url) {
		final int lastSlash = url.lastIndexOf('/');
		if (lastSlash > 0) {
			return url.substring(0, lastSlash);
		}
		
		return url;
	}

}
