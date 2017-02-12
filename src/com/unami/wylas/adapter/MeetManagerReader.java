/**
 * 
 */
package com.unami.wylas.adapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

import au.com.xandar.meetmanager.ServiceInfrastructure;

/**
 * @author Luciano
 *
 */
public abstract class MeetManagerReader<T> {

	private static final String COMMENTED_LINE = "#";
	private DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.LONG);

	protected ServiceInfrastructure serviceInfrastructure;

	protected MeetManagerReader(ServiceInfrastructure serviceInfrastructure) {
		this.serviceInfrastructure = serviceInfrastructure;
	}

	protected String getStringProperty(String propertyName) {
		return serviceInfrastructure.getProperties().get(propertyName);
	}

	protected String getFieldDelimiter() {
		return getStringProperty(FileMeetManagerService.PROPERTY_NAME_FIELD_DELIMITER);
	}

	public Collection<T> read(File file) throws ParseException, IOException {
		log("parsing file " + file);

		Collection<T> list = new ArrayList<>();
		if (file.isFile() && file.canRead()) {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			try {
				line = reader.readLine();
				while (line != null) {
					// just ignore commented lines
					if (!line.startsWith(COMMENTED_LINE) && line.length() > 0)
						list.add(parse(line));

					line = reader.readLine();
				}

			} finally {
				reader.close();
			}

			//archive(file);
		}

		log("file read: " + list);
		return list;
	}

//	protected void archive(File file) {
//		File archiveDir = new File(file.getParentFile() + File.pathSeparator + "archive", file.getName());
//		File destFile = new File(archiveDir, file.getName());
//
//		log("archive file " + file + " to : " + destFile);
//
//		archiveDir.mkdir();
//		file.renameTo(destFile);
//	}

	abstract protected T parse(String line) throws ParseException;

	public Collection<T> readAll(File path) throws IOException, ParseException {
		log("reading files from path: " + path);

		Collection<T> list = new ArrayList<>();

		if (path.isDirectory()) {
			File[] files = getMatchedFiles(path, getFileNamePattern());
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				list.addAll(read(file));
			}
		}
		return list;
	}

	protected File[] getMatchedFiles(File path, String regexp) {
		return path.listFiles((dir, name) -> Pattern.matches(regexp, name));
	}

	protected void log(String message) {
		Date now = Calendar.getInstance().getTime();
		System.out.println(dateFormat.format(now) + " " + this + " " + message);
	}

	abstract protected String getFileNamePattern();

}
