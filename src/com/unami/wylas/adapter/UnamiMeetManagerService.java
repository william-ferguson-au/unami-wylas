/**
 * 
 */
package com.unami.wylas.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import au.com.xandar.meetmanager.Course;
import au.com.xandar.meetmanager.DQItem;
import au.com.xandar.meetmanager.Gender;
import au.com.xandar.meetmanager.Meet;
import au.com.xandar.meetmanager.MeetDescription;
import au.com.xandar.meetmanager.MeetManagerService;
import au.com.xandar.meetmanager.Race;
import au.com.xandar.meetmanager.RaceEntry;
import au.com.xandar.meetmanager.RaceState;
import au.com.xandar.meetmanager.Round;
import au.com.xandar.meetmanager.Stroke;

/**
 * @author Luciano
 *
 */
public class UnamiMeetManagerService implements MeetManagerService {

	private static final String FILE_LOCATION = "File Location";

	LinkedHashMap<String, String> properties = new LinkedHashMap<>();

	public UnamiMeetManagerService() {
		properties.put(FILE_LOCATION, "");
	}

	boolean started = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceInfrastructure#getProperties()
	 */
	@Override
	public LinkedHashMap<String, String> getProperties() {
		return properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceInfrastructure#getReadableName()
	 */
	@Override
	public String getReadableName() {
		return "UNAMI Meet Manager Adapter";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceInfrastructure#isStarted()
	 */
	@Override
	public boolean isStarted() {
		return started;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceInfrastructure#start()
	 */
	@Override
	public void start() {
		started = true;
		System.out.println(this + " started.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceInfrastructure#stop()
	 */
	@Override
	public void stop() {
		started = false;
		System.out.println(this + " stopped.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceAPI#getApiVersion()
	 */
	@Override
	public int getApiVersion() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceAPI#getDQItems(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<DQItem> getDQItems(String meetId, String raceId) {
		List<DQItem> list = new ArrayList<>();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceAPI#getMeet(java.lang.String)
	 */
	@Override
	public Meet getMeet(String meetId) {
		Meet meet = new Meet();
		meet.course = Course.ShortCourse;
		meet.description = "property value " + properties.get(FILE_LOCATION);
		meet.endDate = new Date();// Calendar.getInstance()
		meet.meetId = "1";
		meet.nrLanes = 2;
		meet.startDate = new Date();

		return meet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceAPI#getMeetDescriptions()
	 */
	@Override
	public List<MeetDescription> getMeetDescriptions() {
		MeetDescription md = new MeetDescription();
		md.description = "Unami demo meet";
		md.meetId = "1";

		List<MeetDescription> list = new ArrayList<>();
		list.add(md);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceAPI#getMeets()
	 */
	@Override
	public List<Meet> getMeets() {
		// TODO Auto-generated method stub

		Meet meet = new Meet();
		meet.course = Course.ShortCourse;
		meet.description = "Unami demo meet";
		meet.endDate = new Date();// Calendar.getInstance()
		meet.meetId = "1";
		meet.nrLanes = 2;
		meet.startDate = new Date();

		List<Meet> list = new ArrayList<>();
		list.add(meet);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.com.xandar.meetmanager.ServiceAPI#getRaceEntries(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<RaceEntry> getRaceEntries(String meetId, String raceId) {
		RaceEntry re = new RaceEntry();
		re.competitorClub = "vinhedo";
		re.competitorFirstName = "Luciano";
		re.competitorId = "1";
		re.competitorLastName = "Gobi";
		re.heatPosition = 1;
		re.laneNr = 4;

		List<RaceEntry> list = new ArrayList<>();
		list.add(re);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceAPI#getRaces(java.lang.String)
	 */
	@Override
	public List<Race> getRaces(String meetId) {
		Race r = new Race();
		r.raceId = "r1";
		r.availableNrLanes = 6;
		r.description = "100m Peito";
		r.distance = 100;
		r.eventNr = 1;
		r.gender = Gender.Male;
		r.heatNr = 2;
		r.stroke = Stroke.Breaststroke;
		r.eventAlpha = "";
		r.highAge = 44;
		r.lowAge = 40;
		r.relay = false;
		r.round = Round.Final;

		List<Race> list = new ArrayList<>();
		list.add(r);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.com.xandar.meetmanager.ServiceAPI#raceStateChanged(java.lang.String,
	 * java.lang.String, au.com.xandar.meetmanager.RaceState)
	 */
	@Override
	public void raceStateChanged(String meetId, String raceId, RaceState state) {
		// TODO Auto-generated method stub
		System.out.println("raceStateChanged[" + meetId + "," + raceId + "," + state + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceAPI#testConnection()
	 */
	@Override
	public boolean testConnection() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.com.xandar.meetmanager.ServiceAPI#updateRaceEntries(java.lang.String,
	 * java.lang.String, java.util.List)
	 */
	@Override
	public void updateRaceEntries(String meetId, String raceId, List<RaceEntry> entries) {
		// TODO Auto-generated method stub
		System.out.println("updateRaceEntries[" + meetId + "," + raceId + "," + entries + "]");
	}

}
