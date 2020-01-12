package sw.sainsburys.serversidetest;

import java.io.IOException;

import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class BerriesHtmlParser {

	private JsoupConnectionProvider connectionProvider;
	
	public BerriesHtmlParser(JsoupConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	public JSONObject parse(String url) throws IOException {
		final Document berriesHtml = this.connectionProvider.createConnectionFor(url).get();
		final Element title = berriesHtml.select("div.productNameAndPromotions a").first();
		
		final JSONObject result = new JSONObject();
		result.put("title", title.text());
		
		return result;
	}

}
