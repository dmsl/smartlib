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

package cy.ac.ucy.paschalis.client.android.SmartLib;



import java.util.ArrayList;





public class Book {

	public String	isbn;

	public String	title;

	public String	authors;

	public int	publishedYear;

	public int	pageCount;

	public String	dateOfInsert;

	public String	imgURL;

	public String	lang;

	public int	status;

	public static class DataClassUser {

		String	username;

		int		status;





		public DataClassUser(String user, int stat) {
			username = user;
			status = stat;
		}
	}


	public ArrayList<DataClassUser>	owners	= new ArrayList<DataClassUser>();




}
