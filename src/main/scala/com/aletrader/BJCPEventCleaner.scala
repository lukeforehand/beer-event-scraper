package com.aletrader;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.safety.Whitelist;
import org.jsoup.Jsoup;

import scala.collection.JavaConversions._;

object BJCPEventCleaner {

	def cleanEvent(html: String, regions: Array[String], numResults: Int): Array[BeerEvent] = {
		
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

		var events = new ArrayList[BeerEvent];

		var bjcpDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		var dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		var currentDate = dateFormat.format(new Date());

		var counter = 0;
		while (counter < rawEvents.size()) {
			// two rows per event
			var rawEvent = "";
			rawEvent = rawEvent.concat(rawEvents.get(counter).trim()).concat("\t");
			counter += 1;
			rawEvent = rawEvent.concat(rawEvents.get(counter).trim());
			counter += 1;
			rawEvent = rawEvent.replaceAll(160.asInstanceOf[Char].toString(), " ");
			var rawEventSplits = rawEvent.split("\t");
			var date = dateFormat.format(bjcpDateFormat.parse(rawEventSplits(0)));
			var name = rawEventSplits(1);
			var location = rawEventSplits(2);
			var contactPerson = "";
			var contactPhone = "";
			var entryFee = "";
			var entryDeadline = "";
			for (rawField <- rawEventSplits) {
				if (rawField.contains("Entry Fee:")) {
					entryFee = rawField.replaceAll("Entry Fee:", "").trim();
				} else if (rawField.contains("Entry Deadline:")) {
					entryDeadline = dateFormat.format(bjcpDateFormat.parse(rawField.replaceAll("Entry Deadline:", "").trim()));
				} else if (rawField.contains("Contact:")) {
					contactPerson = rawField.replaceAll("Contact:", "").trim();
				} else if (rawField.contains("Phone:")) {
					contactPhone = rawField.replaceAll("Phone:", "").trim();
				}
			}

			// filter to regions
			if (regions != null && location.contains(",")) {
				var currentRegion = location.split(",")(1).trim();
				for (region <- regions) {
					if (currentRegion.equals(region) && (entryDeadline.equals("") || currentDate.compareTo(entryDeadline) < 0)) {
						events.add(new BeerEvent(date, name, location, contactPerson, contactPhone, entryFee, entryDeadline));
					}
				}
			} else {
				events.add(new BeerEvent(date, name, location, contactPerson, contactPhone, entryFee, entryDeadline));
			}

		}

		// sort by date
		events.sortBy(_.date);
		// trim to numResults
		var trimmedEvents = events.subList(0, if (numResults > events.size()) events.size() else numResults);

		return trimmedEvents.toArray(new Array[BeerEvent](trimmedEvents.size()));

	}

}
