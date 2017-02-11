/**
 * 
 */
package com.unami.wylas.adapter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public class FileMeetManagerService implements MeetManagerService {
	private DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.LONG);

	private static final String FILE_MEET_MANAGER_ADAPTER = "File Meet Manager Adapter";

	protected static final String PROPERTY_NAME_DATA_FILE_LOCATION = "data.path";
	protected static final String PROPERTY_NAME_DATE_FORMAT = "date.format";
	protected static final String PROPERTY_NAME_MEETS_FILE_NAME_PATTERN = "meets.filename.pattern";
	protected static final String PROPERTY_NAME_RACES_FILE_NAME_PATTERN = "races.filename.pattern";
	protected static final String PROPERTY_NAME_RACE_ENTRIES_FILE_NAME_PATTERN = "raceentries.filename.patter";
	protected static final String PROPERTY_NAME_FIELD_DELIMITER = "field.delimiter";

	private LinkedHashMap<String, String> properties = new LinkedHashMap<>();
	private Map<String, Meet> meetCache = new HashMap<>();

	public FileMeetManagerService() {
		properties.put(PROPERTY_NAME_DATA_FILE_LOCATION, "");
		properties.put(PROPERTY_NAME_DATE_FORMAT, "dd/MM");
		properties.put(PROPERTY_NAME_MEETS_FILE_NAME_PATTERN, "meets[.]*.txt");
		properties.put(PROPERTY_NAME_RACES_FILE_NAME_PATTERN, "races[.]*.txt");
		properties.put(PROPERTY_NAME_RACE_ENTRIES_FILE_NAME_PATTERN, "raceentries[.]*.txt");
		properties.put(PROPERTY_NAME_FIELD_DELIMITER, ";");
	}

	boolean started = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceInfrastructure#getProperties()
	 */
	@Override
	public LinkedHashMap<String, String> getProperties() {
		log("properties=" + properties);
		return properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceInfrastructure#getReadableName()
	 */
	@Override
	public String getReadableName() {
		log("readableName=" + FILE_MEET_MANAGER_ADAPTER);
		return FILE_MEET_MANAGER_ADAPTER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceInfrastructure#isStarted()
	 */
	@Override
	public boolean isStarted() {
		log("started=" + started);
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
		log("started.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceInfrastructure#stop()
	 */
	@Override
	public void stop() {
		started = false;
		log("stopped.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceAPI#getApiVersion()
	 */
	@Override
	public int getApiVersion() {
		log("apiVersion=0");
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

		log("getDQItems=" + list);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceAPI#getMeet(java.lang.String)
	 */
	@Override
	public Meet getMeet(String meetId) {
		Meet meet = meetCache.get(meetId);

		log("getMeet=" + meet);
		return meet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceAPI#getMeetDescriptions()
	 */
	@Override
	public List<MeetDescription> getMeetDescriptions() {
		List<MeetDescription> list = new ArrayList<>();
		Collection<Meet> meets = meetCache.values();
		for (Meet meet : meets) {
			MeetDescription md = new MeetDescription();
			md.meetId = meet.meetId;
			md.description = meet.description;

			list.add(md);
		}

		log("getMeetDescriptions=" + list);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceAPI#getMeets()
	 */
	@Override
	public List<Meet> getMeets() {
		List<Meet> meetList = new ArrayList<>();

		MeetReader reader = new MeetReader(this);
		try {
			meetList.addAll(reader.readAll(new File(properties.get(PROPERTY_NAME_DATA_FILE_LOCATION))));
			for (Meet meet : meetList) {
				meetCache.put(meet.meetId, meet);
			}

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		log("getMeets: " + meetList);
		return meetList;
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

		log("getRaceEntries=" + list);

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

		log("getRaces=" + list);

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
		log("raceStateChanged[" + meetId + "," + raceId + "," + state + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceAPI#testConnection()
	 */
	@Override
	public boolean testConnection() {
		log(" testConnection=true");
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
		System.out.println(Calendar.getInstance() + ":" + this + " updateRaceEntries[" + meetId + "," + raceId + ","
				+ entries + "]");
	}

	protected void log(String message) {
		Date now = Calendar.getInstance().getTime();
		System.out.println(dateFormat.format(now) + " " + this + " " + message);
	}

}
