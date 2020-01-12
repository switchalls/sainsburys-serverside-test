package sw.sainsburys.serversidetest;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class JsoupConnectionProvider {

	public Connection createConnectionFor(String url) {
		return Jsoup.connect(url);
	}

}
