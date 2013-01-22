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

package cy.ac.ucy.paschalis.client.android.result;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.widget.Toast;
import cy.ac.ucy.paschalis.client.android.CaptureActivity;
import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.paschalis.client.android.wifi.WifiConfigManager;
import cy.ac.ucy.paschalis.client.result.ParsedResult;
import cy.ac.ucy.paschalis.client.result.WifiParsedResult;

/**
 * Handles address book entries.
 *
 * @author viki@google.com (Vikram Aggarwal)
 */
public final class WifiResultHandler extends ResultHandler {

  private final CaptureActivity parent;

  public WifiResultHandler(CaptureActivity activity, ParsedResult result) {
    super(activity, result);
    parent = activity;
  }

  @Override
  public int getButtonCount() {
    // We just need one button, and that is to configure the wireless.  This could change in the future.
    return 1;
  }

  @Override
  public int getButtonText(int index) {
    return R.string.button_wifi;
  }

  @Override
  public void handleButtonPress(int index) {
    // Get the underlying wifi config
    WifiParsedResult wifiResult = (WifiParsedResult) getResult();
    if (index == 0) {
      String ssid = wifiResult.getSsid();
      String password = wifiResult.getPassword();
      String networkType = wifiResult.getNetworkEncryption();
      WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
      Toast.makeText(getActivity(), R.string.wifi_changing_network, Toast.LENGTH_LONG).show();
      WifiConfigManager.configure(wifiManager, ssid, password, networkType);
      parent.restartPreviewAfterDelay(0L);
    }
  }

  // Display the name of the network and the network type to the user.
  @Override
  public CharSequence getDisplayContents() {
    WifiParsedResult wifiResult = (WifiParsedResult) getResult();
    StringBuilder contents = new StringBuilder(50);
    String wifiLabel = parent.getString(R.string.wifi_ssid_label);
    ParsedResult.maybeAppend(wifiLabel + '\n' + wifiResult.getSsid(), contents);
    String typeLabel = parent.getString(R.string.wifi_type_label);
    ParsedResult.maybeAppend(typeLabel + '\n' + wifiResult.getNetworkEncryption(), contents);
    return contents.toString();
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_wifi;
  }
}