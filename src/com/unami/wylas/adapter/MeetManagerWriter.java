package com.unami.wylas.adapter;

import au.com.xandar.meetmanager.Race;
import au.com.xandar.meetmanager.RaceEntry;
import au.com.xandar.meetmanager.ServiceInfrastructure;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

public class MeetManagerWriter {

	private static final String COMMENTED_LINE = "#";

	protected ServiceInfrastructure serviceInfrastructure;

	protected MeetManagerWriter(ServiceInfrastructure serviceInfrastructure) {
		this.serviceInfrastructure = serviceInfrastructure;
	}

	protected String getStringProperty(String propertyName) {
		return this.serviceInfrastructure.getProperties().get(propertyName);
	}

	protected String getFieldDelimiter() {
		return this.getStringProperty(FileMeetManagerService.PROPERTY_NAME_FIELD_DELIMITER);
	}

	public int write(Writer target, Race race, Collection<RaceEntry> results) throws IOException, IllegalAccessException {
		if (target != null && results != null) {

			BufferedWriter writer = new BufferedWriter(target);
			try {
				writer.write("#" + (race == null ? "no race information" : race.toString()));
				writer.newLine();
				writer.write(this.getHeader(race));

				for (RaceEntry raceEntry : results) {
					writer.newLine();
					writer.write(this.parse(raceEntry));
				}
			} finally {
				writer.close();
			}

			return results.size();
		} else {
			return 0;
		}
	}

	private String getHeader(Race race) {
		String delimiter = this.getFieldDelimiter();
		StringBuilder buffer = new StringBuilder();
		buffer.append("#");
		Field[] fields = RaceEntry.class.getDeclaredFields();

		for (int i = 0; i < fields.length; ++i) {
			Field field = fields[i];
			int modifiers = field.getModifiers();
			if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
				buffer.append(field.getName());
				buffer.append(delimiter);
			}
		}

		return buffer.toString();
	}

	private String parse(RaceEntry raceEntry) throws IllegalArgumentException, IllegalAccessException {
		String delimiter = this.getFieldDelimiter();
		StringBuilder buffer = new StringBuilder();
		Field[] fields = raceEntry.getClass().getDeclaredFields();

		for (int i = 0; i < fields.length; ++i) {
			Field field = fields[i];
			int modifiers = field.getModifiers();
			if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
				buffer.append(field.get(raceEntry));
				buffer.append(delimiter);
			}
		}

		return buffer.toString();
	}
}

