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

package cy.ac.ucy.paschalis.client.android.wifi;

import java.util.List;
import java.util.regex.Pattern;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * @author Vikram Aggarwal
 * @author Sean Owen
 */
public final class WifiConfigManager {

  private static final String TAG = WifiConfigManager.class.getSimpleName();

  private static final Pattern HEX_DIGITS = Pattern.compile("[0-9A-Fa-f]+");

  private WifiConfigManager() {
  }

  public static void configure(final WifiManager wifiManager, 
                               final String ssid, 
                               final String password, 
                               final String networkTypeString) {
    Runnable configureRunnable = new Runnable() {
      @Override
      public void run() {
        // Start WiFi, otherwise nothing will work
        if (!wifiManager.isWifiEnabled()) {
          Log.i(TAG, "Enabling wi-fi...");
          if (wifiManager.setWifiEnabled(true)) {
            Log.i(TAG, "Wi-fi enabled");
          } else {
            Log.w(TAG, "Wi-fi could not be enabled!");
            return;
          }
          // This happens very quickly, but need to wait for it to enable. A little busy wait?
          int count = 0;
          while (!wifiManager.isWifiEnabled()) {
            if (count >= 10) {
              Log.i(TAG, "Took too long to enable wi-fi, quitting");
              return;
            }
            Log.i(TAG, "Still waiting for wi-fi to enable...");
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException ie) {
              // continue
            }
            count++;
          }
        }
        NetworkType networkType;
        try {
          networkType = NetworkType.forIntentValue(networkTypeString);
        } catch (IllegalArgumentException iae) {
          Log.w(TAG, "Bad network type; see NetworkType values: " + networkTypeString);
          return;
        }
        if (networkType == NetworkType.NO_PASSWORD) {
          changeNetworkUnEncrypted(wifiManager, ssid);
        } else {
          if (password == null || password.length() == 0) {
            throw new IllegalArgumentException();
          }
          if (networkType == NetworkType.WEP) {
            changeNetworkWEP(wifiManager, ssid, password);
          } else if (networkType == NetworkType.WPA) {
            changeNetworkWPA(wifiManager, ssid, password);
          }
        }
      }
    };
    new Thread(configureRunnable).start();
  }

  /**
   * Update the network: either create a new network or modify an existing network
   * @param config the new network configuration
   * @return network ID of the connected network.
   */
  private static void updateNetwork(WifiManager wifiManager, WifiConfiguration config) {
    Integer foundNetworkID = findNetworkInExistingConfig(wifiManager, config.SSID);
    if (foundNetworkID != null) {
      Log.i(TAG, "Removing old configuration for network " + config.SSID);
      wifiManager.removeNetwork(foundNetworkID);
      wifiManager.saveConfiguration();
    }
    int networkId = wifiManager.addNetwork(config);
    if (networkId >= 0) {
      // Try to disable the current network and start a new one.
      if (wifiManager.enableNetwork(networkId, true)) {
        Log.i(TAG, "Associating to network " + config.SSID);
        wifiManager.saveConfiguration();
      } else {
        Log.w(TAG, "Failed to enable network " + config.SSID);
      }
    } else {
      Log.w(TAG, "Unable to add network " + config.SSID);
    }
  }

  private static WifiConfiguration changeNetworkCommon(String ssid) {
    WifiConfiguration config = new WifiConfiguration();
    config.allowedAuthAlgorithms.clear();
    config.allowedGroupCiphers.clear();
    config.allowedKeyManagement.clear();
    config.allowedPairwiseCiphers.clear();
    config.allowedProtocols.clear();
    // Android API insists that an ascii SSID must be quoted to be correctly handled.
    config.SSID = quoteNonHex(ssid);
    return config;
  }

  // Adding a WEP network
  private static void changeNetworkWEP(WifiManager wifiManager, String ssid, String password) {
    WifiConfiguration config = changeNetworkCommon(ssid);
    config.wepKeys[0] = quoteNonHex(password, 10, 26, 58);
    config.wepTxKeyIndex = 0;
    config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
    updateNetwork(wifiManager, config);
  }

  // Adding a WPA or WPA2 network
  private static void changeNetworkWPA(WifiManager wifiManager, String ssid, String password) {
    WifiConfiguration config = changeNetworkCommon(ssid);
    // Hex passwords that are 64 bits long are not to be quoted.
    config.preSharedKey = quoteNonHex(password, 64);
    config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
    config.allowedProtocols.set(WifiConfiguration.Protocol.WPA); // For WPA
    config.allowedProtocols.set(WifiConfiguration.Protocol.RSN); // For WPA2
    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
    updateNetwork(wifiManager, config);
  }

  // Adding an open, unsecured network
  private static void changeNetworkUnEncrypted(WifiManager wifiManager, String ssid) {
    WifiConfiguration config = changeNetworkCommon(ssid);
    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
    updateNetwork(wifiManager, config);
  }

  private static Integer findNetworkInExistingConfig(WifiManager wifiManager, String ssid) {
    List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
    for (WifiConfiguration existingConfig : existingConfigs) {
      if (existingConfig.SSID.equals(ssid)) {
        return existingConfig.networkId;
      }
    }
    return null;
  }

  private static String quoteNonHex(String value, int... allowedLengths) {
    return isHexOfLength(value, allowedLengths) ? value : convertToQuotedString(value);
  }

  /**
   * Encloses the incoming string inside double quotes, if it isn't already quoted.
   * @param string the input string
   * @return a quoted string, of the form "input".  If the input string is null, it returns null
   * as well.
   */
  private static String convertToQuotedString(String string) {
    if (string == null || string.length() == 0) {
      return null;
    }
    // If already quoted, return as-is
    if (string.charAt(0) == '"' && string.charAt(string.length() - 1) == '"') {
      return string;
    }
    return '\"' + string + '\"';
  }

  /**
   * @param value input to check
   * @param allowedLengths allowed lengths, if any
   * @return true if value is a non-null, non-empty string of hex digits, and if allowed lengths are given, has
   *  an allowed length
   */
  private static boolean isHexOfLength(CharSequence value, int... allowedLengths) {
    if (value == null || !HEX_DIGITS.matcher(value).matches()) {
      return false;
    }
    if (allowedLengths.length == 0) {
      return true;
    }
    for (int length : allowedLengths) {
      if (value.length() == length) {
        return true;
      }
    }
    return false;
  }

}
