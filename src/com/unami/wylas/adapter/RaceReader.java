/**
 * 
 */
package com.unami.wylas.adapter;

import java.text.ParseException;
import java.util.ArrayList;

import au.com.xandar.meetmanager.Gender;
import au.com.xandar.meetmanager.Race;
import au.com.xandar.meetmanager.Round;
import au.com.xandar.meetmanager.ServiceInfrastructure;
import au.com.xandar.meetmanager.Stroke;

/**
 * @author Luciano
 *
 */
public class RaceReader extends MeetManagerReader<Race> {
	private String meetId;

	protected RaceReader(ServiceInfrastructure serviceInfrastructure, String meetId) {
		super(serviceInfrastructure);
		this.meetId = meetId;
	}

	@Override
	protected Race parse(String line) throws ParseException {
		String[] fields = line.split(getStringProperty(FileMeetManagerService.PROPERTY_NAME_FIELD_DELIMITER));

		if (fields.length < 12) {
			throw new ArrayIndexOutOfBoundsException("Invalid line: [" + line + "]");
		}

		Race r = new Race();
		int i = 1;
		r.eventNr = Integer.parseInt(fields[i++]);// 1;
		r.eventAlpha = fields[i++];
		r.round = Round.valueOf(fields[i++]);// Final;
		r.heatNr = Integer.parseInt(fields[i++]);// 2;
		r.description = fields[i++];// "100m Peito";
		r.gender = Gender.valueOf(fields[i++]);// Male
		r.lowAge = Integer.parseInt(fields[i++]);
		r.highAge = Integer.parseInt(fields[i++]);
		r.distance = Integer.parseInt(fields[i++]);// 100;
		r.stroke = Stroke.valueOf(fields[i++]);// Breaststroke;
		r.relay = Boolean.parseBoolean(fields[i++]);// false;
		r.availableNrLanes = (fields.length > 12) ? Integer.parseInt(fields[i++]) : 0;

		r.raceId = fields[0].length() == 0 ? String.valueOf(r.eventNr * 100 + r.heatNr) : fields[0];
		r.raceEntries = new ArrayList<>();

		return r;
	}

	@Override
	protected String getFileNamePattern() {
		return "meet" + meetId + "-races.*";
	}

}
