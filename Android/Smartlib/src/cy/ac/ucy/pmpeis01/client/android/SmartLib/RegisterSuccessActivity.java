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

package cy.ac.ucy.pmpeis01.client.android.SmartLib;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import cy.ac.ucy.pmpeis01.client.android.R;
import cy.ac.ucy.pmpeis01.client.android.history.HistoryActivity;





public class RegisterSuccessActivity extends SherlockActivity {

	App	app;





	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		app = (App) getApplication();

		setContentView(R.layout.activity_register_success);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		TextView textViewWelcomeTitle = (TextView) findViewById(R.id.textViewRegisterSuccessWelcomeTitle);
		TextView textViewWelcomeNotes = (TextView) findViewById(R.id.textViewRegisterSuccessWelcomeNotes);

		getSupportActionBar().setTitle(
				getString(R.string.welcome) + ", " + app.registerUser.name);

		textViewWelcomeTitle
				.setText(getString(R.string.msgAccountActivationTitle));

		textViewWelcomeNotes
				.setText(getString(R.string.msgActivateYourAccount) + ": "
						+ app.registerUser.email);

		TextView loginLibraryLocation = (TextView) findViewById(R.id.textViewLibraryLocation);
		loginLibraryLocation.setText(app.library.location);


		ImageView loginLogo = (ImageView) findViewById(R.id.imageViewLoginLibraryLogo);

		// Show logo
		
		try{
			App.imageLoader.DisplayImage(app.library.getImageURL(), loginLogo,null);
		}
		catch (NullPointerException e){
			// noth
		}
		

	}





	@Override
	public boolean onCreateOptionsMenu(Menu menu) {


		menu.add(Menu.NONE, App.MENU_GLOBAL_SETTINGS, Menu.NONE,
				R.string.menu_settings)
				.setIcon(R.drawable.ic_menu_settings_holo_light)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);



		return true;
	}





	@Override
	public void onBackPressed() {

		super.onBackPressed();

	};





	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			case App.MENU_SCANNED_BOOKS_ID:{
				Intent myIntent = new Intent(RegisterSuccessActivity.this,
						HistoryActivity.class);
				RegisterSuccessActivity.this.startActivity(myIntent);
			}
				return true;

		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		//Set library's logo as ActionBar Icon
		App.imageLoader.DisplayActionBarIcon(app.library.getImageURL(),
				getApplicationContext(), getSupportActionBar());
		
	    if (App.refreshLang) {
	        refresh();
	    }
	    super.onResume();
	}

	/**Refresh activity's language
	 * 
	 */
	private void refresh() {
		App.refreshLang=false;
	    finish();
	    Intent myIntent = new Intent(RegisterSuccessActivity.this, RegisterSuccessActivity.class);
	    startActivity(myIntent);
	}
	
	

}
