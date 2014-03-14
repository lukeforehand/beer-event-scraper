package com.aletrader;

class BeerEvent(
	val date: String,
	val name: String,
	val location: String,
	val contactPerson: String,
	val contactPhone: String,
	val entryFee: String,
	val entryDeadline: String) {
	
	override def toString(): String = {
		return ""
			.concat(date).concat("\t")
			.concat(name).concat("\t")
			.concat(location).concat("\t")
			.concat(contactPerson).concat("\t")
			.concat(contactPhone).concat("\t")
			.concat(entryFee).concat("\t")
			.concat(entryDeadline);
	}
	
}
