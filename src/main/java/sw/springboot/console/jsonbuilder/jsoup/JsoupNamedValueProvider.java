package sw.springboot.console.jsonbuilder.jsoup;

import java.util.Optional;

import org.jsoup.nodes.Element;

public interface JsoupNamedValueProvider<T> {

	String getName();

	Optional<T> getValue(Element source, String sourceUrl);

}
