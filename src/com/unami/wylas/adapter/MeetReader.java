/**
 * 
 */
package com.unami.wylas.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import au.com.xandar.meetmanager.Course;
import au.com.xandar.meetmanager.Meet;
import au.com.xandar.meetmanager.ServiceInfrastructure;

/**
 * Read all meets from input file.
 * 
 * The input file name pattern is defined by property name
 * <code>meets.filename.pattern</code>
 * 
 * The file format is string delimited which delimiter character is defined by
 * property <code>field.delimiter</code>
 * 
 * Each row represents one record in the following format:
 * 
 * <code>
 meetId;meetDescription;startDate;endDate;nrLanes;course
 * </code>
 * 
 * <code>endDate</code> is optional. If omitted, it will be same as
 * <code>startDate</code>.
 * 
 * Omitted <code>course</code> means short lane.
 * 
 * @author Luciano
 *
 */
public class MeetReader extends MeetManagerReader<Meet> {

	protected static final String MEETS_FILE_NAME_PATTERN = "meets.*\\.txt";

	protected MeetReader(ServiceInfrastructure serviceInfrastructure) {
		super(serviceInfrastructure);
	}

	@Override
	protected Meet parse(String line) throws ParseException {
		String[] fields = line.split(getFieldDelimiter());

		if (fields.length < 5)
			throw new ArrayIndexOutOfBoundsException("Invalid line: [" + line + "]");

		Meet meet = new Meet();
		int i = 0;
		meet.meetId = fields[i++];
		meet.description = fields[i++];
		meet.startDate = parseDate(fields[i++]);
		meet.endDate = fields[i].isEmpty() ? meet.startDate : parseDate(fields[i]);
		i++;
		meet.nrLanes = Integer.parseInt(fields[i++]);
		meet.course = fields.length > 5 && fields[i++].length() > 0 ? Course.valueOf(fields[i++]) : Course.ShortCourse;

		return meet;
	}

	private Date parseDate(String string) throws ParseException {
		DateFormat format = new SimpleDateFormat(getStringProperty(FileMeetManagerService.PROPERTY_NAME_DATE_FORMAT));
		Date date = format.parse(string);

		Calendar meetDate = Calendar.getInstance();
		meetDate.setTime(date);
		// year not set in meet file
		if (meetDate.get(Calendar.YEAR) == 1970)
			meetDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));

		return meetDate.getTime();
	}

	@Override
	protected String getFileNamePattern() {
		return MEETS_FILE_NAME_PATTERN;
	}
}
