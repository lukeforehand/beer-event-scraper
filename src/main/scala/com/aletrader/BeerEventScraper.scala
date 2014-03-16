
package com.aletrader;

import org.apache.commons.io.IOUtils;
import java.net.URL;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.text.SimpleDateFormat;
import java.util.Date;

import scala.collection.JavaConversions._;

object BeerEventScraper {
	
	def main(args: Array[String]) {

		var regions = Array("IA", "KY", "WI", "MI", "IL", "MN", "IN", "MO", "KS", "NE", "OH");
		var numResults = 10;

		var sites = ResourceBundle.getBundle("sites");
		var keys = sites.getKeys();

		var beerEvents = new ArrayList[BeerEvent];

		while (keys.hasMoreElements()) {
			var url = keys.nextElement();
			var format = sites.getString(url);
			var html = new String(IOUtils.toByteArray(new URL(url).openStream()), "UTF-8");
			beerEvents.addAll(scrape(html, format));
		}

		//TODO: de-duplicate events from multiple sites

		var dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		var currentDate = dateFormat.format(new Date());

		// filter by regions and entry deadline
		if (regions != null) {
			for (beerEvent <- new ArrayList(beerEvents)) {
				if (beerEvent.location.contains(",")) {
					var region = beerEvent.location.split(",")(1).trim();
					if (!regions.contains(region) || (!beerEvent.entryDeadline.equals("") && currentDate.compareTo(beerEvent.entryDeadline) >= 0)) {
						beerEvents.remove(beerEvent);
					}
				}
			}			
		}

		// sort by entry deadline
		Collections.sort(beerEvents, new Comparator[BeerEvent]() {
			def compare(event1: BeerEvent, event2:BeerEvent): Int = {
				var deadline1 = event1.entryDeadline;
				var date1 = event1.date;
				var deadline2 = event2.entryDeadline;
				var date2 = event2.date;
				if (deadline1 == "" || deadline2 == "") return date1.compareTo(date2);
				return deadline1.compareTo(deadline2);
			}
		});

		// trim to numResults
		println(generateReport(beerEvents.subList(0, if (numResults > beerEvents.size()) beerEvents.size() else numResults)));

	}

	def scrape(html: String, format: String): ArrayList[BeerEvent] = format match {
		case "bjcp" => BJCPEventCleaner.scrapeEvents(html);
	}

	def generateReport(beerEvents: List[BeerEvent]): String = {
		return "<html><table border=1>"
			.concat("\n")
			.concat("<tr><th>deadline<th>event date<th>name<th>location<th>contact<th>phone<th>fee\n<tr><td>")
			.concat(beerEvents.toArray().deep.mkString("\n<tr><td>").replaceAll("\t", "<td>"));
	}

}