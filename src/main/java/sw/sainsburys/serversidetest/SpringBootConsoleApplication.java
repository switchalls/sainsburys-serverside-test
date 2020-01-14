package sw.sainsburys.serversidetest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootConsoleApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootConsoleApplication.class, args);
	}

	private static final String SAISNBURIES_BERRIES_CHERRIES_CURRANTS_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";

	private final BerriesHtmlParser htmlParser;
	
	@Autowired
	public SpringBootConsoleApplication(BerriesHtmlParser htmlParser) {
		this.htmlParser = htmlParser;
	}

	@Override
	public void run(String... args) throws Exception {		
		String targetUrl = SAISNBURIES_BERRIES_CHERRIES_CURRANTS_URL;
		if (args.length > 0) {
			targetUrl = args[0];
		}

		final JSONObject result = htmlParser.parse(targetUrl);
		System.out.println(result);		
	}

}
