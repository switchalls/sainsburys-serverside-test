package sw.sainsburys.serversidetest.jsonbuilder.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Component
public class JsoupConnectionProvider {

	public Connection createConnectionFor(String url) {
		return Jsoup.connect(url);
	}

}
