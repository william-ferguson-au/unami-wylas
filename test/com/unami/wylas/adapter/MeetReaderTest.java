package com.unami.wylas.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.regex.Pattern;

import org.junit.Test;

import au.com.xandar.meetmanager.Meet;

public class MeetReaderTest {
	private MeetReader reader = new MeetReader(new FileMeetManagerService());

	@Test
	public void testRead() throws ParseException, IOException {
		Collection<Meet> list = reader.read(new File("./data/meets-unami-2017.txt"));
		System.out.println(list);
		assertEquals(10, list.size());
	}

	@Test
	public void testGetMatchedFiles() {
		String regexp = reader.getFileNamePattern();
		assertTrue(Pattern.matches(regexp, "meets-unami-2017.txt"));
		assertFalse(Pattern.matches(regexp, "meets-unami-2017"));

		assertNull(reader.getMatchedFiles(new File("./data/meets-unami-2017.txt"), "not a dir"));
		assertEquals(0, reader.getMatchedFiles(new File("./data"), "none").length);

		File[] found = reader.getMatchedFiles(new File("./data"), regexp);
		assertEquals(1, found.length);
	}
}
