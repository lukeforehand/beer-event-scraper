
package com.aletrader;

import org.apache.commons.io.IOUtils;
import java.net.URL;
import java.io.ByteArrayInputStream;
import java.util.ResourceBundle;

object BeerEventScraper {
	
	def main(args: Array[String]) {

		var regions = Array("IA", "KY", "WI", "MI", "IL", "MN", "IN", "MO", "KS", "NE", "OH");
		var numResults = 10;

		var sites = ResourceBundle.getBundle("sites");
		var keys = sites.getKeys();

		while (keys.hasMoreElements()) {
			var url = keys.nextElement();
			var format = sites.getString(url);
			var html = new String(IOUtils.toByteArray(new URL(url).openStream()), "UTF-8");
			var beerEvents = clean(html, format, regions, numResults);
			println(generateReport(beerEvents));
		}

	}

	def clean(html: String, format: String, regions: Array[String], numResults: Int): Array[BeerEvent] = format match {
		case "bjcp" => BJCPEventCleaner.cleanEvent(html, regions, numResults);
	}

	def generateReport(beerEvents: Array[BeerEvent]): String = {
		return "date\tname\tlocation\tcontact\tphone\tfee\tdeadline\n".concat(beerEvents.deep.mkString("\n"));
	}

}