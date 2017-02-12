/**
 * 
 */
package com.unami.wylas.adapter;

import java.util.HashMap;
import java.util.Map;

import au.com.xandar.meetmanager.Meet;
import au.com.xandar.meetmanager.Race;

/**
 * @author Luciano
 *
 */
public class MeetRace {
	protected Meet meet;
	protected Map<String, Race> race = new HashMap<>();

	public MeetRace(Meet meet) {
		this.meet = meet;
	}

	@Override
	public String toString() {
		return "MeetRace [meet=" + meet + ", race=" + race + "]";
	}
}
