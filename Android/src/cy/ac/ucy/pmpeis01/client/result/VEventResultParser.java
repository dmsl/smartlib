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

package cy.ac.ucy.pmpeis01.client.result;

import java.util.List;

import cy.ac.ucy.pmpeis01.Result;

/**
 * Partially implements the iCalendar format's "VEVENT" format for specifying a
 * calendar event. See RFC 2445. This supports SUMMARY, LOCATION, GEO, DTSTART and DTEND fields.
 *
 * @author Sean Owen
 */
public final class VEventResultParser extends ResultParser {

  @Override
  public CalendarParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    int vEventStart = rawText.indexOf("BEGIN:VEVENT");
    if (vEventStart < 0) {
      return null;
    }

    String summary = matchSingleVCardPrefixedField("SUMMARY", rawText, true);
    String start = matchSingleVCardPrefixedField("DTSTART", rawText, true);
    if (start == null) {
      return null;
    }
    String end = matchSingleVCardPrefixedField("DTEND", rawText, true);
    String location = matchSingleVCardPrefixedField("LOCATION", rawText, true);
    String organizer = stripMailto(matchSingleVCardPrefixedField("ORGANIZER", rawText, true));

    String[] attendees = matchVCardPrefixedField("ATTENDEE", rawText, true);
    if (attendees != null) {
      for (int i = 0; i < attendees.length; i++) {
        attendees[i] = stripMailto(attendees[i]);
      }
    }
    String description = matchSingleVCardPrefixedField("DESCRIPTION", rawText, true);

    String geoString = matchSingleVCardPrefixedField("GEO", rawText, true);
    double latitude;
    double longitude;
    if (geoString == null) {
      latitude = Double.NaN;
      longitude = Double.NaN;
    } else {
      int semicolon = geoString.indexOf(';');
      try {
        latitude = Double.parseDouble(geoString.substring(0, semicolon));
        longitude = Double.parseDouble(geoString.substring(semicolon + 1));
      } catch (NumberFormatException nfe) {
        return null;
      }
    }

    try {
      return new CalendarParsedResult(summary,
                                      start,
                                      end,
                                      location,
                                      organizer,
                                      attendees,
                                      description,
                                      latitude,
                                      longitude);
    } catch (IllegalArgumentException iae) {
      return null;
    }
  }

  private static String matchSingleVCardPrefixedField(CharSequence prefix,
                                                      String rawText,
                                                      boolean trim) {
    List<String> values = VCardResultParser.matchSingleVCardPrefixedField(prefix, rawText, trim, false);
    return values == null || values.isEmpty() ? null : values.get(0);
  }

  private static String[] matchVCardPrefixedField(CharSequence prefix, String rawText, boolean trim) {
    List<List<String>> values = VCardResultParser.matchVCardPrefixedField(prefix, rawText, trim, false);
    if (values == null || values.isEmpty()) {
      return null;
    }
    int size = values.size();
    String[] result = new String[size];
    for (int i = 0; i < size; i++) {
      result[i] = values.get(i).get(0);
    }
    return result;
  }

  private static String stripMailto(String s) {
    if (s != null && (s.startsWith("mailto:") || s.startsWith("MAILTO:"))) {
      s = s.substring(7);
    }
    return s;
  }

}