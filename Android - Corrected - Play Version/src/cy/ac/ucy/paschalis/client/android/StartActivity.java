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

package cy.ac.ucy.paschalis.client.android;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

// import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.paschalis.client.android.App.DeviceType;
import cy.ac.ucy.paschalis.client.android.Cache.ImageLoader;
import cy.ac.ucy.paschalis.client.android.R;

import com.google.zxing.client.android.PreferencesActivity;
import com.google.zxing.client.android.history.HistoryActivity;

//import android.support.v4.app.FragmentTransaction;


/**
 * Shows user panel to choose the library to login. After that, it shows the
 * login panel. Also in this activity, user can Register to the chosen library
 *
 * @author paschalis
 */
public class StartActivity extends SherlockFragmentActivity implements
		ChooseLibraryFragment.OnLibrarySelectedListener     {


	App app;

	boolean isLibrarySelected;

	ChooseLibraryFragment chooseLibraryFragment;    //AGR1

	LoginTitleFragment loginTitleFragment;

	LoginFragment loginFragment;

	EmptyFragment emptyFragment;


	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		// SUMMER REMOVED
		// App.historyManager = new HistoryManager(this);
		// App.historyManager.trimHistory();

		App.imageLoader = new ImageLoader(StartActivity.this);

		app = (App) getApplication();

		//isLibrarySelected = false; //AGRIDIOTIS      AGR1

        isLibrarySelected = true;   //AGRIDIOTIS        AGR1

		// Invalidate the menu, so a new one will be created
		invalidateOptionsMenu();                //AGRIDIOTIS

		setContentView(R.layout.choose_library);        //AGRIDIOTIS


		// If we are on Regular Device
		if (findViewById(R.id.fragment_container) != null) {
			app.deviceType = DeviceType.Regular;

			// Avoid end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

	//	AGR1
                chooseLibraryFragment = new ChooseLibraryFragment();    //AGRIDIOTIS

			loginTitleFragment = new LoginTitleFragment();
			// In case this activity was started with special instructions
			// from an Intent,
			// pass the Intent's extras to the fragment as arguments
	//AGR1
			chooseLibraryFragment.setArguments(getIntent().getExtras());     //AGRIDIOTIS

			// Add the fragment to the 'fragment_container' FrameLayout &
			// COMMIT!!!
		//AGR1
		getSupportFragmentManager().beginTransaction()
		//AGR1
			.add(R.id.fragment_container, chooseLibraryFragment).     //    AGRIDIOTIS

		//AGR1
			add(R.id.title_layout_container, loginTitleFragment)
		//AGR1
				.commit();


		}
		// if we are on XL or L device, set it
		else {
			app.deviceType = DeviceType.Large;
			// If its a Tablet, do nothing!
		}


	}


	/*
	 * Inflate the Menu
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(android
	 * .view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {


		menu.add(Menu.NONE, App.MENU_START_ACTIVITY_REFRESH, Menu.FIRST,
				R.string.refresh).setIcon(R.drawable.ic_menu_refresh)
				.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		// SUMMER REMOVED
		// menu.add(Menu.NONE, App.MENU_SCANNED_BOOKS_ID, Menu.NONE,
		// R.string.menuScannedBooks)
		// .setIcon(R.drawable.ic_menu_recent_history)
		// .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		menu.add(Menu.NONE, App.MENU_GLOBAL_SETTINGS, Menu.NONE,
				R.string.menu_settings)
				.setIcon(R.drawable.ic_menu_settings_holo_light)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);


		getSupportMenuInflater().inflate(R.menu.activity_start, menu);


		return true;
	}


	/*
	 * Handle menu interaction
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockActivity#onOptionsItemSelected(android
	 * .view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemRegister: {
				Intent myIntent = new Intent(this, RegisterActivity.class);
				this.startActivity(myIntent);
				return true;
			}
			case R.id.itemClearLogin:
				EditText editTextLoginUsername = (EditText) findViewById(R.id.editTextLoginUsername);
				EditText editTextLoginPassword = (EditText) findViewById(R.id.editTextLoginPassword);


				editTextLoginUsername.setText("");
				editTextLoginPassword.setText("");

				return true;
			case App.MENU_GLOBAL_SETTINGS: {
				Intent myIntent = new Intent(StartActivity.this,
						PreferencesActivity.class);
				StartActivity.this.startActivity(myIntent);

			}
			return true;
			case App.MENU_SCANNED_BOOKS_ID: {
				Intent myIntent = new Intent(StartActivity.this,
						HistoryActivity.class);
				StartActivity.this.startActivity(myIntent);
			}
			return true;
			case android.R.id.home:


				onBackPressed();
				break;


		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		// If library is selected, change menu options
		if (isLibrarySelected) {          //   AGRIDIOTIS
			menu.findItem(R.id.itemClearLogin).setVisible(true);
			menu.findItem(R.id.itemRegister).setVisible(true);

			// Hide refresh only on regular devices(Smartphones)
			if (app.deviceType.equals(DeviceType.Regular))
				menu.findItem(App.MENU_START_ACTIVITY_REFRESH).setVisible(
						false);
		} else {
			menu.findItem(R.id.itemClearLogin).setVisible(false);
			menu.findItem(R.id.itemRegister).setVisible(false);
			menu.findItem(App.MENU_START_ACTIVITY_REFRESH).setVisible(true);
		}
		// SUMMER REMOVED
		// if (App.historyManager.historyItemsNumber() > 0){
		// //menu.findItem(App.MENU_ADD_BOOKS_ID).setVisible(true);
		// menu.findItem(App.MENU_SCANNED_BOOKS_ID).setVisible(true);
		// }
		// else{
		// //menu.findItem(App.MENU_ADD_BOOKS_ID).setVisible(false);
		// menu.findItem(App.MENU_SCANNED_BOOKS_ID).setVisible(false);
		// }
		//
		return true;
	}


	@Override
	protected void onResume() {
		if (App.refreshLang) {
			refresh();
		}
		super.onResume();
	}


	/**
	 * Refresh activity's language
	 */
	private void refresh() {
		App.refreshLang = false;
		finish();
		Intent myIntent = new Intent(StartActivity.this, StartActivity.class);
		startActivity(myIntent);
	}


	/**
	 * Interface Implementation. Callback called from
	 * {@link ChooseLibraryFragment} User selected Library And now login screen
	 * will show
	 */


                                                    //AGRIDIOTIS
	public void onLibrarySelected(Library lib) {

		// Save the library on App
		((App) getApplication()).library = lib;   //AGRIDIOTIS


        //isLibrarySelected = false;   //AGRIDIOTIS
		isLibrarySelected = true;   //AGRIDIOTIS
		// Invalidate the menu, so a new one will be created
		invalidateOptionsMenu();

		// Try to get the Login Fragment
		LoginFragment loginFrag = (LoginFragment) getSupportFragmentManager()
				.findFragmentById(R.id.login_fragment);

		// If we are on Large Layout
		if (loginFrag != null) {
			// We are in two-pane layout...

			// Call a method enable login screen
			loginFrag.updateLoginView(lib);          //AGRIDIOTIS

		}

		// Regular Layout
		else {
           if (loginFrag == null) {
			// We're in the one-pane layout
			// Swap fragments: choose library and login

			// Create fragment and give it an argument for the selected
			// library
			loginFragment = new LoginFragment();
			emptyFragment = new EmptyFragment();

			Bundle args = new Bundle();

			args.putInt(LoginFragment.ARG_POSITION, lib.positionOnList);
			loginFragment.setArguments(args);

			// Swap two frags
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();

			// Replace whatever is in the fragment_container view with this
			// fragment,
			// and add the transaction to the back stack so the user can
			// navigate back
			transaction.replace(R.id.fragment_container, loginFragment);


			transaction.replace(R.id.title_layout_container, emptyFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		} //AGRIDIOTIS

       // AGRIDIOTIS
        }
    }

	/**
	 * Interface Implementation. Callback called from
	 * {@link ChooseLibraryFragment} User selected Library And now login screen
	 * will show
	 */
	public void onBackPressed() {

//		if(!app.homeAsUpEnabled)
//			return;

		invalidateOptionsMenu();

		super.onBackPressed();
	}


}
