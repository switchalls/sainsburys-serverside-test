package sw.springboot.console.jsonbuilder;

import org.json.JSONObject;

public interface BerriesCherriesCurrantsJsonBuilder {

	BerriesCherriesCurrantsJsonBuilder withHtmlPage(String url);

	JSONObject build() throws Exception;

}