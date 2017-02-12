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
public class RaceEntryReader extends MeetManagerReader<MeetRaceEntry> {
	private String meetId;

	protected RaceEntryReader(ServiceInfrastructure serviceInfrastructure, String meetId) {
		super(serviceInfrastructure);
		this.meetId = meetId;
	}

	@Override
	protected MeetRaceEntry parse(String line) throws ParseException {
		String[] fields = line.split(getStringProperty(FileMeetManagerService.PROPERTY_NAME_FIELD_DELIMITER));

		if (fields.length < 6)
			throw new ArrayIndexOutOfBoundsException("Invalid line: [" + line + "]");

		MeetRaceEntry mre = new MeetRaceEntry();
		RaceEntry re = new RaceEntry();
		int i = 0;
		mre.raceId = fields[i++];
		mre.raceEntry = re;

		re.laneNr = Integer.parseInt(fields[i++]);// 4;
		re.competitorId = fields[i++];// "1";
		re.competitorFirstName = fields[i++];// "Luciano";
		re.competitorLastName = fields[i++];// "Gobi";
		re.competitorClub = fields[i++];// "vinhedo";

		return mre;
	}

	@Override
	protected String getFileNamePattern() {
		return "meet" + meetId + "-raceentries.*";
	}

}
