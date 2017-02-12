package com.unami.wylas.adapter;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;

import org.junit.Test;

import au.com.xandar.meetmanager.Gender;
import au.com.xandar.meetmanager.Race;

public class RaceReaderTest {
	private RaceReader reader = new RaceReader(new FileMeetManagerService(), "1");

	@Test
	public void testParseString() {
		assertEquals(Gender.Male, Gender.valueOf("Male"));
		System.out.println(Gender.Male);
	}

	@Test
	public void testRead() throws IOException, ParseException {
		assertEquals(0, reader.readAll(new File("./")).size());

		Collection<Race> races = reader.readAll(new File("./data"));
		System.out.println(races);
		assertEquals(8, races.size());
	}
}
