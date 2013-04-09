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

package cy.ac.ucy.paschalis.client.android.history;



import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import cy.ac.ucy.paschalis.client.android.CaptureActivity;
import cy.ac.ucy.paschalis.client.android.Intents;
import cy.ac.ucy.paschalis.client.android.R;







public final class HistoryActivity extends SherlockListActivity {

	// private static final int SEND_ID = Menu.FIRST;

	private static final int		CLEAR_ALL_ID			= Menu.FIRST;

	private static final int		CLEAR_ALREADY_ADDED_ID	= Menu.FIRST + 1;

	private static final int		CLEAR_ERROR_BOOKS_ID	= Menu.FIRST + 2;

	private HistoryManager		historyManager;

	private HistoryItemAdapter	adapter;

	
	




	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.historyManager = new HistoryManager(this);
		adapter = new HistoryItemAdapter(this);
		setListAdapter(adapter);
		
		//adapter.setNotifyOnChange(true);
		
		
		ListView listview = getListView();
		registerForContextMenu(listview);
	}





	@Override
	protected void onResume() {
		super.onResume();
		List<HistoryItem> items = historyManager.buildHistoryItems();
		adapter.clear();
		for (HistoryItem item : items){
			adapter.add(item);
		}
		if (adapter.isEmpty()){
			adapter.add(new HistoryItem(null, null, null));
		}
	}





	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (adapter.getItem(position).getResult() != null){
			Intent intent = new Intent(this, CaptureActivity.class);
			intent.putExtra(Intents.History.ITEM_NUMBER, position);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}





	// @Override
	// public void onCreateContextMenu(ContextMenu menu, View v,
	// ContextMenu.ContextMenuInfo menuInfo) {
	// int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
	// if (position >= adapter.getCount()
	// || adapter.getItem(position).getResult() != null){
	// menu.add(Menu.NONE, position, position,
	// R.string.history_clear_one_history_text);
	// } // else it's just that dummy "Empty" message
	// }





	// @Override
	// public boolean onContextItemSelected(MenuItem item) {
	// int position = item.getItemId();
	// historyManager.deleteHistoryItem(position);
	// finish();
	// return true;
	// }





	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		if (historyManager.hasHistoryItems()){

			menu.add(Menu.NONE, CLEAR_ALREADY_ADDED_ID, 0,
					R.string.clearAlreadyAdded)
					.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
					.setVisible(false);

			menu.add(Menu.NONE, CLEAR_ERROR_BOOKS_ID, 0,
					R.string.clearErrorBooks)
					.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
					.setVisible(false);

			menu.add(Menu.NONE, CLEAR_ALL_ID, 0, R.string.clearAllBooks)
					.setIcon(R.drawable.ic_menu_close_clear_cancel);
			return true;
		}
		return false;
	}





	// Don't display the share menu item if the result overlay is showing.
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// super.onPrepareOptionsMenu(menu);

		if (historyManager.historyItemsNumber() > 0){

			if (historyManager.hasAlreadyAddedBooks()){
				menu.findItem(CLEAR_ALREADY_ADDED_ID).setVisible(true);
			}
			else menu.findItem(CLEAR_ALREADY_ADDED_ID).setVisible(false);

			if (historyManager.hasErrorBooks()){
				menu.findItem(CLEAR_ERROR_BOOKS_ID).setVisible(true);
			}
			else menu.findItem(CLEAR_ERROR_BOOKS_ID).setVisible(false);

			menu.findItem(CLEAR_ALL_ID).setVisible(true);

		}
		else{

			menu.findItem(CLEAR_ALL_ID).setVisible(false);
			menu.findItem(CLEAR_ALREADY_ADDED_ID).setVisible(false);
			menu.findItem(CLEAR_ERROR_BOOKS_ID).setVisible(false);

		}

		return true;
	}





	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// case SEND_ID:
		// CharSequence history = historyManager.buildHistory();
		// Uri historyFile = HistoryManager.saveHistory(history
		// .toString());
		// if (historyFile == null){
		// AlertDialog.Builder builder = new AlertDialog.Builder(
		// this);
		// builder.setMessage(R.string.msg_unmount_usb);
		// builder.setPositiveButton(R.string.button_ok, null);
		// builder.show();
		// }
		// else{
		// Intent intent = new Intent(Intent.ACTION_SEND,
		// Uri.parse("mailto:"));
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		// String subject = getResources().getString(
		// R.string.history_email_title);
		// intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		// intent.putExtra(Intent.EXTRA_TEXT, subject);
		// intent.putExtra(Intent.EXTRA_STREAM, historyFile);
		// intent.setType("text/csv");
		// startActivity(intent);
		// }
		// break;
		// Deletes Already Added Books
			case CLEAR_ALREADY_ADDED_ID:{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.msg_sure);
				builder.setCancelable(true);
				builder.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int i2) {
								historyManager
										.removeAlreadyAddedBooks();
								dialog.dismiss();
								
								finish();
							}
						});
				builder.setNegativeButton(R.string.button_cancel, null);
				builder.show();
			}
				break;


			// Deletes Error Found Books
			case CLEAR_ERROR_BOOKS_ID:{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.msg_sure);
				builder.setCancelable(true);
				builder.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int i2) {
								historyManager.removeErrorBooks();
								dialog.dismiss();
								
								finish();
							}
						});
				builder.setNegativeButton(R.string.button_cancel, null);
				builder.show();
			}
				break;



			// Deletes All history
			case CLEAR_ALL_ID:{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.msg_sure);
				builder.setCancelable(true);
				builder.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int i2) {
								historyManager.clearHistory();
								dialog.dismiss();
								//adapter.notifyDataSetChanged();
								finish();
								
							}
						});
				builder.setNegativeButton(R.string.button_cancel, null);
				builder.show();
			}
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

}
