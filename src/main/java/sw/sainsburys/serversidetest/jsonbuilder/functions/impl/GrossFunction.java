package sw.sainsburys.serversidetest.jsonbuilder.functions.impl;

import java.util.Map;
import java.util.function.ToDoubleFunction;

import org.json.JSONArray;
import org.springframework.stereotype.Component;

import sw.sainsburys.serversidetest.jsonbuilder.functions.TotalsFunction;

@Component
public class GrossFunction implements TotalsFunction<Double> {

	private static ToDoubleFunction<? super Object> JSONOBJECT_UNITPRICE = (p) ->
		((Map<String, Double>)p).get("unitPrice");

	@Override
	public String getName() {
		return "gross";
	}

	@Override
	public Double apply(JSONArray source) {
		return source.toList().stream()
				.mapToDouble(JSONOBJECT_UNITPRICE)
				.sum();
	}

}
