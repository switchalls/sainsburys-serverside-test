package sw.jsoup.sainsburys;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sw.jsoup.sainsburys.jsonbuilder.BerriesCherriesCurrantsJsonBuilder;

@SpringBootApplication
public class SpringBootConsoleApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootConsoleApplication.class, args);
    }

    private static final String SAISNBURIES_BERRIES_CHERRIES_CURRANTS_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";

    private final BerriesCherriesCurrantsJsonBuilder jsonBuilder;

    @Autowired
    public SpringBootConsoleApplication(BerriesCherriesCurrantsJsonBuilder jsonBuilder) {
        this.jsonBuilder = jsonBuilder;
    }

    @Override
    public void run(String... args) throws Exception {
        String targetUrl = SAISNBURIES_BERRIES_CHERRIES_CURRANTS_URL;
        if (args.length > 0) {
            targetUrl = args[0];
        }

        final JSONObject result = jsonBuilder
                .withHtmlPage(targetUrl)
                .build();

        System.out.println(result);
    }

}
