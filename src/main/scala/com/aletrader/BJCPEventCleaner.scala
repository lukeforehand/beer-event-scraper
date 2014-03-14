package com.aletrader;

import org.jsoup.safety.Whitelist;
import org.jsoup.Jsoup;

object BJCPEventCleaner extends SiteCleaner {

	def cleanEvent(html: String): Array[String] = {

		var whitelist  = Whitelist.none();
		whitelist.addTags("h3");

		var cleaned = Jsoup.clean(html, whitelist);


		return Array(cleaned);

	}

}
