package sw.springboot.console.jsonbuilder.functions.impl;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sw.springboot.console.jsonbuilder.functions.TotalsFunction;

@Component
public class VatFunction implements TotalsFunction<Double> {

	private final GrossFunction grossFunction;
	
	@Autowired
	public VatFunction(GrossFunction grossFunction) {
		this.grossFunction =  grossFunction;
	}
	
	@Override
	public String getName() {
		return "vat";
	}

	@Override
	public Double apply(JSONArray source) {
		final double gross = this.grossFunction.apply(source);
		return gross * 0.2d;
	}

}
