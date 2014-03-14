
package com.aletrader;

import java.util.ResourceBundle;
import org.glassfish.jersey.client.ClientConfig;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

object BeerEventScraper {
	
	val config = new ClientConfig();
	val client = ClientBuilder.newClient(config);

	def main(args: Array[String]) {

		var sites = ResourceBundle.getBundle("sites");
		var keys = sites.getKeys();

		while (keys.hasMoreElements()) {
			var url = keys.nextElement();
			var format = sites.getString(url);
			var cleanedEvents = clean(url, format);
			println(cleanedEvents);

		}

	}

	def clean(url: String, format: String): Array[String] = format match {
		case "bjcp" => BJCPSiteCleaner.clean(client.target(url).request(MediaType.TEXT_PLAIN).get().readEntity(classOf[String]));
	}

}