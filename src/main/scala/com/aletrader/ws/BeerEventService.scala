package com.aletrader.ws;

import com.aletrader.BeerEventScraper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;


// make @Singleton to avoid instantiation of the repo every time
@Path("/")
class BeerEventService {

	val scraper = BeerEventScraper;

	@GET
	@Path("/ping")
	@Produces(Array(MediaType.TEXT_PLAIN))
	def ping(): String = {
		return "pong";
	}

	@GET
	@Path("/beer_events")
	@Produces(Array(MediaType.TEXT_HTML))
	def beerEvents(@QueryParam("results") results: Int): String = {
		return scraper.generateReport(scraper.getEvents(results));
	}

}