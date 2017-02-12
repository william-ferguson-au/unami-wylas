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
import au.com.xandar.meetmanager.Meet;
import au.com.xandar.meetmanager.MeetDescription;
import au.com.xandar.meetmanager.MeetManagerService;
import au.com.xandar.meetmanager.Race;
import au.com.xandar.meetmanager.RaceEntry;
import au.com.xandar.meetmanager.RaceState;

/**
 * @author Luciano
 *
 */
public class FileMeetManagerService implements MeetManagerService {
	private DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.LONG);

	private static final String FILE_MEET_MANAGER_ADAPTER = "File Meet Manager Adapter";

	protected static final String PROPERTY_NAME_DATA_FILE_LOCATION = "data.path";
	protected static final String PROPERTY_NAME_DATE_FORMAT = "date.format";
	protected static final String PROPERTY_NAME_FIELD_DELIMITER = "field.delimiter";

	private LinkedHashMap<String, String> properties = new LinkedHashMap<>();
	// meetId is key
	private Map<String, MeetRace> meetRaceCache = new HashMap<>();

	public FileMeetManagerService() {
		properties.put(PROPERTY_NAME_DATA_FILE_LOCATION, "");
		properties.put(PROPERTY_NAME_DATE_FORMAT, "dd/MM");
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
		// log("properties=" + properties);
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
		log("properties: " + properties);

		try {
			loadMeets();
			started = true;
			log("started.");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			log("not started. " + e.getMessage());
		}
	}

	private File getDataLocation() {
		return new File(properties.get(PROPERTY_NAME_DATA_FILE_LOCATION));
	}

	protected void loadMeets() throws IOException, ParseException {
		MeetReader reader = new MeetReader(this);
		Collection<Meet> meetList = new ArrayList<>();
		meetList.addAll(reader.readAll(getDataLocation()));
		for (Meet meet : meetList) {
			MeetRace meetRace = new MeetRace(meet);
			meetRaceCache.put(meet.meetId, meetRace);
			loadRaces(meetRace.meet.meetId);
		}

	}

	protected void loadRaces(String meetId) throws IOException, ParseException {
		MeetRace meetRace = meetRaceCache.get(meetId);
		if (meetRace == null) {
			log("Invalid meetId " + meetId);
			throw new NullPointerException("Invalid meetId " + meetId);
		}

		RaceReader reader = new RaceReader(this, meetRace.meet.meetId);
		Collection<Race> raceList = reader.readAll(getDataLocation());
		for (Race race : raceList) {
			meetRace.race.put(race.raceId, race);
		}

		loadRaceEntries(meetId);
	}

	protected void loadRaceEntries(String meetId) throws IOException, ParseException {
		MeetRace meetRace = meetRaceCache.get(meetId);
		if (meetRace == null) {
			log("Invalid meetId " + meetId);
			throw new NullPointerException("Invalid meetId " + meetId);
		}

		RaceEntryReader entryReader = new RaceEntryReader(this, meetRace.meet.meetId);
		Collection<MeetRaceEntry> raceEntryList = entryReader.readAll(getDataLocation());
		for (MeetRaceEntry meetRaceEntry : raceEntryList) {
			if (meetRace.race.containsKey(meetRaceEntry.raceId)) {
				Race race = meetRace.race.get(meetRaceEntry.raceId);
				race.raceEntries.add(meetRaceEntry.raceEntry);
			} else
				log("race not found: " + meetRaceEntry);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceInfrastructure#stop()
	 */
	@Override
	public void stop() {
		meetRaceCache.clear();

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
		Meet meet = meetRaceCache.get(meetId).meet;

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
		Collection<MeetRace> meets = meetRaceCache.values();
		for (MeetRace meet : meets) {
			MeetDescription md = new MeetDescription();
			md.meetId = meet.meet.meetId;
			md.description = meet.meet.description;

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

		Collection<MeetRace> entries = meetRaceCache.values();
		for (MeetRace meetRace : entries) {
			meetList.add(meetRace.meet);
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
		log("getRaceEntries " + meetId + " " + raceId);

		List<RaceEntry> list = new ArrayList<>();
		MeetRace meetRace = meetRaceCache.get(meetId);
		if (meetRace == null) {
			log("Invalid meetId " + meetId);
			return list;
		}

		Race race = meetRace.race.get(raceId);
		if (race == null) {
			log("Invalid meetId " + meetId);
			return list;
		}

		if (race.raceEntries == null || race.raceEntries.isEmpty())
			try {
				loadRaceEntries(meetId);
			} catch (IOException | ParseException e) {
				log("Error to reload race entries for meetId " + meetId + " raceId " + raceId + " " + e.getMessage());
				e.printStackTrace();
			}

		list.addAll(race.raceEntries);

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
		log("getRaces " + meetId);

		List<Race> list = new ArrayList<>();
		MeetRace meetRace = meetRaceCache.get(meetId);
		if (meetRace == null) {
			log("Invalid meetId " + meetId);
			return list;
		}

		if (meetRace.race.isEmpty())
			try {
				loadRaces(meetId);
			} catch (IOException | ParseException e) {
				log("Error reloading races for meetId " + meetId + " " + e.getMessage());
				e.printStackTrace();
			}

		list.addAll(meetRace.race.values());

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
		log("raceStateChanged[" + meetId + "," + raceId + "," + state + "]");

		if (meetRaceCache.containsKey(meetId)) {
			MeetRace meetRace = meetRaceCache.get(meetId);
			if (meetRace.race.containsKey(raceId)) {
				Race race = meetRace.race.get(raceId);
				log("before " + race);
				race.raceState = state;
				log("after  " + race);

			} else
				log("race not found: " + raceId);
		} else
			log("meet not found: " + meetId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.com.xandar.meetmanager.ServiceAPI#testConnection()
	 */
	@Override
	public boolean testConnection() {
		log("testConnection=true");
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
		log("updateRaceEntries[" + meetId + "," + raceId + "," + entries + "]");

		if (meetRaceCache.containsKey(meetId)) {
			MeetRace meetRace = meetRaceCache.get(meetId);
			if (meetRace.race.containsKey(raceId)) {
				Race race = meetRace.race.get(raceId);
				log("before " + race);
				race.raceEntries = entries;
				log("after  " + race);

			} else
				log("race not found: " + raceId);
		} else
			log("meet not found: " + meetId);
	}

	protected void log(String message) {
		Date now = Calendar.getInstance().getTime();
		System.out.println(dateFormat.format(now) + " " + this + " " + message);
	}

}
