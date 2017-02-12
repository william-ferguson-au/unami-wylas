package com.unami.wylas.adapter;

import au.com.xandar.meetmanager.RaceEntry;

/**
 * Used to join RaceEntry with Race via <code>raceId</code>
 * 
 * @author Luciano
 *
 */
public class MeetRaceEntry {
	protected String raceId;
	protected RaceEntry raceEntry;

	@Override
	public String toString() {
		return "MeetRaceEntry [raceId=" + raceId + ", raceEntry=" + raceEntry + "]";
	}
}
