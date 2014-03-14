package com.aletrader;

import java.util.ArrayList;

import org.jsoup.safety.Whitelist;
import org.jsoup.Jsoup;

import scala.collection.JavaConversions._;

object BJCPEventCleaner extends SiteCleaner {

	def cleanEvent(html: String, numResults: Int): Array[String] = {
		
		var rawEvents = new ArrayList[String];

		// convert breaks to separate before parsing
		for (eventElement <- Jsoup.parseBodyFragment(html.replaceAll("<br>", "</p><p>")).getElementsByTag("tr")) {
			var event = "";
			for (eventDetail <- eventElement.getElementsByTag("p")) {
				event = event.concat(eventDetail.text() + "\t");
			}
			if (!event.trim().isEmpty()) {
				// clean empty rows
				rawEvents.add(event);
			}
		}

		var events = new ArrayList[String];

		var counter = 0;
		while (counter < rawEvents.size() && counter < (numResults * 2)) {
			// concat rows by two
			var event = "";
			event = event.concat(rawEvents.get(counter).trim()).concat("\t");
			counter += 1;
			event = event.concat(rawEvents.get(counter).trim());
			counter += 1;
			events.add(event);
		}

		return events.toArray(new Array[String](events.size()));

	}

}
