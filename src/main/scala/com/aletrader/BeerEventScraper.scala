
package com.aletrader;

import org.apache.commons.io.IOUtils;
import java.net.URL;
import java.io.ByteArrayInputStream;
import java.util.ResourceBundle;

object BeerEventScraper {
	
	def main(args: Array[String]) {

		var numResultsPerSite = 5;

		var sites = ResourceBundle.getBundle("sites");
		var keys = sites.getKeys();

		while (keys.hasMoreElements()) {
			var url = keys.nextElement();
			var format = sites.getString(url);
			var html = new String(IOUtils.toByteArray(new URL(url).openStream()), "UTF-8");
			var cleanedEvents = clean(html, format, numResultsPerSite);
			println(cleanedEvents.deep.mkString("\n"));
		}

	}

	def clean(html: String, format: String, numResults: Int): Array[String] = format match {
		case "bjcp" => BJCPEventCleaner.cleanEvent(html, numResults);
	}

}