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

/**
 * @author Sean Owen
 */
public final class GeoParsedResult extends ParsedResult {

  private final double latitude;
  private final double longitude;
  private final double altitude;
  private final String query;

  GeoParsedResult(double latitude, double longitude, double altitude, String query) {
    super(ParsedResultType.GEO);
    this.latitude = latitude;
    this.longitude = longitude;
    this.altitude = altitude;
    this.query = query;
  }

  public String getGeoURI() {
    StringBuilder result = new StringBuilder();
    result.append("geo:");
    result.append(latitude);
    result.append(',');
    result.append(longitude);
    if (altitude > 0) {
      result.append(',');
      result.append(altitude);
    }
    if (query != null) {
      result.append('?');
      result.append(query);
    }
    return result.toString();
  }

  /**
   * @return latitude in degrees
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * @return longitude in degrees
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * @return altitude in meters. If not specified, in the geo URI, returns 0.0
   */
  public double getAltitude() {
    return altitude;
  }

  /**
   * @return query string associated with geo URI or null if none exists
   */
  public String getQuery() {
    return query;
  }

  @Override
  public String getDisplayResult() {
    StringBuilder result = new StringBuilder(20);
    result.append(latitude);
    result.append(", ");
    result.append(longitude);
    if (altitude > 0.0) {
      result.append(", ");
      result.append(altitude);
      result.append('m');
    }
    if (query != null) {
      result.append(" (");
      result.append(query);
      result.append(')');
    }
    return result.toString();
  }

}