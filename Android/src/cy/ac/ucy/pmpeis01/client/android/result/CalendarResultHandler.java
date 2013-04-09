/*
 This file is part of SmartLib Project.

    SmartLib is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SmartLib is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SmartLib.  If not, see <http://www.gnu.org/licenses/>.
    
	Author: Paschalis Mpeis

	Affiliation:
	Data Management Systems Laboratory 
	Dept. of Computer Science 
	University of Cyprus 
	P.O. Box 20537 
	1678 Nicosia, CYPRUS 
	Web: http://dmsl.cs.ucy.ac.cy/
	Email: dmsl@cs.ucy.ac.cy
	Tel: +357-22-892755
	Fax: +357-22-892701
	

 */

package cy.ac.ucy.pmpeis01.client.android.result;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.pmpeis01.client.result.CalendarParsedResult;
import cy.ac.ucy.pmpeis01.client.result.ParsedResult;

/**
 * Handles calendar entries encoded in QR Codes.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CalendarResultHandler extends ResultHandler {

  private static final int[] buttons = {
      R.string.button_add_calendar
  };

  public CalendarResultHandler(Activity activity, ParsedResult result) {
    super(activity, result);
  }

  @Override
  public int getButtonCount() {
    return buttons.length;
  }

  @Override
  public int getButtonText(int index) {
    return buttons[index];
  }

  @Override
  public void handleButtonPress(int index) {
    if (index == 0) {
      CalendarParsedResult calendarResult = (CalendarParsedResult) getResult();

      String description = calendarResult.getDescription();
      String organizer = calendarResult.getOrganizer();
      if (organizer != null) { // No separate Intent key, put in description
        if (description == null) {
          description = organizer;
        } else {
          description = description + '\n' + organizer;
        }
      }

      addCalendarEvent(calendarResult.getSummary(),
                       calendarResult.getStart(),
                       calendarResult.isStartAllDay(),
                       calendarResult.getEnd(),
                       calendarResult.getLocation(),
                       description,
                       calendarResult.getAttendees());
    }
  }

  /**
   * Sends an intent to create a new calendar event by prepopulating the Add Event UI. Older
   * versions of the system have a bug where the event title will not be filled out.
   *
   * @param summary A description of the event
   * @param start   The start time
   * @param allDay  if true, event is considered to be all day starting from start time
   * @param end     The end time (optional)
   * @param location a text description of the event location
   * @param description a text description of the event itself
   * @param attendees attendees to invite
   */
  private void addCalendarEvent(String summary,
                                Date start,
                                boolean allDay,
                                Date end,
                                String location,
                                String description,
                                String[] attendees) {
    Intent intent = new Intent(Intent.ACTION_INSERT);
    intent.setType("vnd.android.cursor.item/event");
    long startMilliseconds = start.getTime();
    intent.putExtra("beginTime", startMilliseconds);
    if (allDay) {
      intent.putExtra("allDay", true);
    }
    long endMilliseconds;
    if (end == null) {
      if (allDay) {
        // + 1 day
        endMilliseconds = startMilliseconds + 24 * 60 * 60 * 1000;
      } else {
        endMilliseconds = startMilliseconds;
      }
    } else {
      endMilliseconds = end.getTime();
    }
    intent.putExtra("endTime", endMilliseconds);
    intent.putExtra("title", summary);
    intent.putExtra("eventLocation", location);
    intent.putExtra("description", description);
    if (attendees != null) {
      intent.putExtra(Intent.EXTRA_EMAIL, attendees);
      // Documentation says this is either a String[] or comma-separated String, which is right?
    }
    launchIntent(intent);
  }


  @Override
  public CharSequence getDisplayContents() {

    CalendarParsedResult calResult = (CalendarParsedResult) getResult();
    StringBuilder result = new StringBuilder(100);

    ParsedResult.maybeAppend(calResult.getSummary(), result);

    Date start = calResult.getStart();
    ParsedResult.maybeAppend(format(calResult.isStartAllDay(), start), result);

    Date end = calResult.getEnd();
    if (end != null) {
      if (calResult.isEndAllDay() && !start.equals(end)) {
        // Show only year/month/day
        // if it's all-day and this is the end date, it's exclusive, so show the user
        // that it ends on the day before to make more intuitive sense.
        // But don't do it if the event already (incorrectly?) specifies the same start/end
        end = new Date(end.getTime() - 24 * 60 * 60 * 1000);
      }
      ParsedResult.maybeAppend(format(calResult.isEndAllDay(), end), result);
    }

    ParsedResult.maybeAppend(calResult.getLocation(), result);
    ParsedResult.maybeAppend(calResult.getOrganizer(), result);
    ParsedResult.maybeAppend(calResult.getAttendees(), result);
    ParsedResult.maybeAppend(calResult.getDescription(), result);
    return result.toString();
  }

  private static String format(boolean allDay, Date date) {
    if (date == null) {
      return null;
    }
    DateFormat format = allDay
        ? DateFormat.getDateInstance(DateFormat.MEDIUM)
        : DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
    return format.format(date);
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_calendar;
  }
}
