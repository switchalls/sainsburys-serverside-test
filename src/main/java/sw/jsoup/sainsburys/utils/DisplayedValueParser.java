package sw.jsoup.sainsburys.utils;

import org.springframework.stereotype.Component;

/**
 * Routines for extracting values from display fields.
 * 
 * @author stewartw
 */
@Component
public class DisplayedValueParser {

	/**
	 * Extract numeric fields from displayed text.
	 * 
	 * <p>
	 * For example, extract numeric part of "£12.99".
	 * </p>
	 * 
	 * @param text The displayed text, eg. "£12.99"
	 * @return the numeric field, eg. 12.99
	 */
    public double getFirstNumericFieldFrom(String text) {
		// TODO - Replace with streams based solution or 3rd party function?
		
        final StringBuffer sb = new StringBuffer();

        int i = 0;
        while (i < text.length() && !isCharForNumber(text.charAt(i))) {
            i++;
        }

        while (i < text.length() && isCharForNumber(text.charAt(i))) {
            sb.append(text.charAt(i++));
        }

        return Double.parseDouble(sb.toString());
    }

    private boolean isCharForNumber(char c) {
        return Character.isDigit(c) || c == '.';
    }

}
