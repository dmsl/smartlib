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

import android.app.Activity;
import cy.ac.ucy.pmpeis01.client.android.R;
import cy.ac.ucy.pmpeis01.client.result.GeoParsedResult;
import cy.ac.ucy.pmpeis01.client.result.ParsedResult;

/**
 * Handles geographic coordinates (typically encoded as geo: URLs).
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class GeoResultHandler extends ResultHandler {
  private static final int[] buttons = {
      R.string.button_show_map,
      R.string.button_get_directions
  };

  public GeoResultHandler(Activity activity, ParsedResult result) {
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
    GeoParsedResult geoResult = (GeoParsedResult) getResult();
    switch (index) {
      case 0:
        openMap(geoResult.getGeoURI());
        break;
      case 1:
        getDirections(geoResult.getLatitude(), geoResult.getLongitude());
        break;
    }
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_geo;
  }
}
