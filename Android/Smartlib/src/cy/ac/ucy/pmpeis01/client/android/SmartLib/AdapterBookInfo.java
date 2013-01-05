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





import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cy.ac.ucy.pmpeis01.client.android.R;
import cy.ac.ucy.pmpeis01.client.android.Cache.ImageLoader;
import cy.ac.ucy.pmpeis01.client.android.SmartLib.Book.DataClassUser;





/**
 * @author paschalis
 * 
 *         Our customized adapter, to show the data to the opponent's row, as WE
 *         like!
 * 
 *         Binds usergame & timestamp to the appropriate TextView in the
 *         opponent's row
 * 
 */
public class AdapterBookInfo extends ArrayAdapter<Book> {

	int		resource;

	String	response;

	Context	context;

	App		app;

	boolean	searchBook;





	// Initialize adapter
	public AdapterBookInfo(Context context, int resource, List<Book> items,
			boolean pWatchBook) {
		super(context, resource, items);
		
		this.context=context;
		this.resource = resource;

		searchBook = pWatchBook;


	}





	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LinearLayout booksInfoView;

		// Get the current alert object
		Book bookInfo = getItem(position);

		// Inflate the view
		if (convertView == null){
			booksInfoView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, booksInfoView, true);
		}
		else{
			booksInfoView = (LinearLayout) convertView;
		}



		// Get layout's Data
		TextView bookISBN = (TextView) booksInfoView
				.findViewById(R.id.textViewBookISBN);

		TextView bookTitle = (TextView) booksInfoView
				.findViewById(R.id.textViewBookTitle);
		TextView bookAuthors = (TextView) booksInfoView
				.findViewById(R.id.textViewBookAuthors);
		TextView bookPublishedYear = (TextView) booksInfoView
				.findViewById(R.id.textViewBookPublishedYear);
		TextView bookPageCount = (TextView) booksInfoView
				.findViewById(R.id.textViewBookPageCount);
		TextView bookDateOfInsert = (TextView) booksInfoView
				.findViewById(R.id.textViewBookDateOfInsert);

		ImageView bookCoverImage = (ImageView) booksInfoView
				.findViewById(R.id.imageViewBookCover);

		TextView bookLanguage = (TextView) booksInfoView
				.findViewById(R.id.textViewBookLanguage);

		TextView bookStatusMsg = (TextView) booksInfoView
				.findViewById(R.id.textViewBookStatusMessage);

		// show The Image and save it to Library
		App.imageLoader.DisplayImage(bookInfo.imgURL, bookCoverImage);



		// Assign the appropriate data from our alert object above
		bookISBN.setText(bookInfo.isbn);
		bookTitle.setText(bookInfo.title);
		bookAuthors.setText(bookInfo.authors);
		bookPublishedYear.setText(Integer.valueOf(bookInfo.publishedYear)
				.toString());
		bookPageCount.setText(Integer.valueOf(bookInfo.pageCount).toString());
		bookDateOfInsert
				.setText(App.makeTimeStampHumanReadble(getContext(),bookInfo.dateOfInsert));
		bookLanguage.setText(bookInfo.lang);

		setBookStatusMessage(bookInfo.status, bookInfo.owners, bookStatusMsg);

		return booksInfoView;





	}





	/**
	 * Returns a string according to book status
	 * 
	 * @param status
	 * @param owners 
	 */
	private void setBookStatusMessage(int status, ArrayList<DataClassUser> owners, TextView tv) {

			switch (status) {
				
				
				case App.BOOK_STATE_USER_RENTED:
					// If opened from watch book activity
					if (searchBook){
						tv.setText(context.getString(R.string.own) + ": "
								+ context.getString(R.string.rented));
						tv.setTextColor(Color.parseColor("#04B306"));
						tv.setVisibility(View.VISIBLE);
					}
					//Opened from MyBooks
					else{
						tv.setText(R.string.rented);
						tv.setTextColor(Color.parseColor("#04B306"));//Green
						tv.setVisibility(View.VISIBLE);
					}
					;
					break;

				case App.BOOK_STATE_USER_AVAILABLE:
					// If opened from watch book activity
					if (searchBook){
						tv.setText(context.getString(R.string.own) + ": "
								+ context.getString(R.string.available));
						tv.setTextColor(Color.parseColor("#ff33b5e5"));//  holo-blue
						tv.setVisibility(View.VISIBLE);
					}
					//Opened from MyBooks
					else{
						tv.setText(R.string.available);
						tv.setTextColor(Color.parseColor("#ff33b5e5"));// holo-blue
						tv.setVisibility(View.VISIBLE);
					}

					break;
				case App.BOOK_STATE_USER_NO_RENTAL:
					// If opened from watch book activity
					if (searchBook){
						tv.setTextColor(Color.parseColor("#C2022C"));//red
						tv.setText(context.getString(R.string.own) + ": "
								+ context.getString(R.string.dontLent));
						tv.setVisibility(View.VISIBLE);
					}
					//Opened from MyBooks
					else{
						tv.setText(R.string.dontLent);
						tv.setTextColor(Color.parseColor("#C2022C"));
						tv.setVisibility(View.VISIBLE);
					}
					
					
					
					break;
				case App.BOOK_STATE_USER_OTHER:
					// If opened from watch book activity
					if (searchBook){
						tv.setTextColor(Color.parseColor("#C2022C"));
						tv.setText(context.getString(R.string.own) + ": "
								+ context.getString(R.string.otherDontLent));
						tv.setVisibility(View.VISIBLE);
					}
					//Opened from MyBooks
					else{
						tv.setText(R.string.otherDontLent);
						tv.setTextColor(Color.parseColor("#C2022C"));
						tv.setVisibility(View.VISIBLE);
					}
					break;
				case App.BOOK_STATE_USER_DONT_OWNS:
					// If opened from watch book activity
					if (searchBook){
						//Workaround
						boolean isAvail=false;
						
						//Find if its available or not
						for (DataClassUser dataClassUser : owners){
							if(dataClassUser.status==App.BOOK_STATE_USER_AVAILABLE){
								isAvail=true;
							}
						}
						
						//Book is available
						if(isAvail){
							tv.setText(R.string.available);
							tv.setTextColor(Color.parseColor("#ff33b5e5"));//  holo-blue
							tv.setVisibility(View.VISIBLE);
						}
						else{
							tv.setText(R.string.notAvailable);
							tv.setTextColor(Color.parseColor("#C2022C"));
							tv.setVisibility(View.VISIBLE);
						}
						
						
					}
					//Opened from MyBooks
					else{
						tv.setText(R.string.dontOwn);
						tv.setTextColor(Color.parseColor("#C2022C"));
						tv.setVisibility(View.VISIBLE);
					}
					
					
				
					
					break;
				default:
					tv.setVisibility(View.INVISIBLE);
					break;

			}

	}







}
