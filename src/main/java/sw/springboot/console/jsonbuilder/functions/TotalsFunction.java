package sw.springboot.console.jsonbuilder.functions;

import org.json.JSONArray;

public interface TotalsFunction<T> {

	String getName();
	
	T apply(JSONArray source);

}