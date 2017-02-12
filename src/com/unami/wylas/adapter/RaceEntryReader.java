/**
 * 
 */
package com.unami.wylas.adapter;

import java.text.ParseException;

import au.com.xandar.meetmanager.RaceEntry;
import au.com.xandar.meetmanager.ServiceInfrastructure;

/**
 * @author Luciano
 *
 */
public class RaceEntryReader extends MeetManagerReader<RaceEntry> {
	private String meetId;
	private String raceId;

	protected RaceEntryReader(ServiceInfrastructure serviceInfrastructure, String meetId, String raceId) {
		super(serviceInfrastructure);
		this.meetId = meetId;
		this.raceId = raceId;
	}

	@Override
	protected RaceEntry parse(String line) throws ParseException {
		String[] fields = line.split(getStringProperty(FileMeetManagerService.PROPERTY_NAME_FIELD_DELIMITER));

		if (fields.length < 6)
			throw new ArrayIndexOutOfBoundsException("Invalid line: [" + line + "]");

		RaceEntry re = new RaceEntry();
		int i = 0;
		re.heatPosition = Integer.parseInt(fields[i++]);// 1;
		re.laneNr = Integer.parseInt(fields[i++]);// 4;
		re.competitorId = fields[i++];// "1";
		re.competitorFirstName = fields[i++];// "Luciano";
		re.competitorLastName = fields[i++];// "Gobi";
		re.competitorClub = fields[i++];// "vinhedo";

		return re;
	}

	@Override
	protected String getFileNamePattern() {
		return "meet" + meetId + "-race" + raceId + ".*";
	}

}
