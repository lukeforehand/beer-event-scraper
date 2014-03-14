
package com.aletrader;

import java.util.ResourceBundle;
import org.glassfish.jersey.client.ClientConfig;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

object BeerEventScraper {
	
	val config = new ClientConfig();
	val client = ClientBuilder.newClient(config);

	def main(args: Array[String]) {

		var numResultsPerSite = 5;

		var sites = ResourceBundle.getBundle("sites");
		var keys = sites.getKeys();

		while (keys.hasMoreElements()) {
			var url = keys.nextElement();
			var format = sites.getString(url);
			var html = client.target(url).request(MediaType.TEXT_PLAIN).get().readEntity(classOf[String]);
			var cleanedEvents = clean(html, format, numResultsPerSite);
			println(cleanedEvents.deep.mkString("\n"));
		}

	}

	def clean(html: String, format: String, numResults: Int): Array[String] = format match {
		case "bjcp" => BJCPEventCleaner.cleanEvent(html, numResults);
	}

}