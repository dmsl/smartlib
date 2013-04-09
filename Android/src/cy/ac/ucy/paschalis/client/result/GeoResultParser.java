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

package cy.ac.ucy.paschalis.client.result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cy.ac.ucy.paschalis.Result;

/**
 * Parses a "geo:" URI result, which specifies a location on the surface of
 * the Earth as well as an optional altitude above the surface. See
 * <a href="http://tools.ietf.org/html/draft-mayrhofer-geo-uri-00">
 * http://tools.ietf.org/html/draft-mayrhofer-geo-uri-00</a>.
 *
 * @author Sean Owen
 */
public final class GeoResultParser extends ResultParser {

  private static final Pattern GEO_URL_PATTERN = 
      Pattern.compile("geo:([\\-0-9.]+),([\\-0-9.]+)(?:,([\\-0-9.]+))?(?:\\?(.*))?", Pattern.CASE_INSENSITIVE);
  
  @Override
  public GeoParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    Matcher matcher = GEO_URL_PATTERN.matcher(rawText);
    if (!matcher.matches()) {
      return null;
    }

    String query = matcher.group(4);

    double latitude;
    double longitude;
    double altitude;
    try {
      latitude = Double.parseDouble(matcher.group(1));
      if (latitude > 90.0 || latitude < -90.0) {
        return null;
      }
      longitude = Double.parseDouble(matcher.group(2));
      if (longitude > 180.0 || longitude < -180.0) {
        return null;
      }
      if (matcher.group(3) == null) {
        altitude = 0.0;
      } else {
        altitude = Double.parseDouble(matcher.group(3));
        if (altitude < 0.0) {
          return null;
        }
      }
    } catch (NumberFormatException nfe) {
      return null;
    }
    return new GeoParsedResult(latitude, longitude, altitude, query);
  }

}