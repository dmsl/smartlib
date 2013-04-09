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




/**
 * Information of a Library
 * 
 * @author paschalis
 * 
 */
public class Library {

	/** Library Name */
	public String	name;
	String URL;
	String town;
	String country;
	String telephone;
	/** Email Address of Library */
	String email;

	
	//Bitmap logoImage=null;

	/** Location of library */
	String	location;
	


	/** Position the library has, on the choose list */
	int		positionOnList	= -1;
	User userLoginAttemp=null;
	
	// Final URLs
	
	private static final String SCRIPTS_PATH =  "/mobile";
	
	/** URL Extension to Image  */
	private static final String imageURL = "/images/logo.png";

	private static final String loginURL = "/authenticate.php";

	private static final String registerURL = "/SQLregister.php";
	
	private static final String insertBookByISBN = "/insertBookByISBN.php";
	
	private static final String stateOfBook = "/stateOfBook.php";
	
	private static final String lentABook = "/lentABook.php";
	
	private static final String returnABook = "/returnABook.php";
	
	private static final String getUserBooks = "/getUserBooks.php";
	
	private static final String getChangeStatusOfBook= "/changeStatusOfBook.php";
	
	private static final String deleteABook= "/deleteABook.php";

	
	private static final String search= "/search.php";
	
	private static final String requestABook= "/requestABook.php";
	
	private static final String incomingRequests= "/incomingRequests.php";
	
	private static final String outgoingRequests= "/outgoingRequests.php";
	
	private static final String booksIGave= "/borrowBooks_iGave.php";
	
	private static final String booksITook= "/borrowBooks_iTook.php";
	
	private static final String replyToBookRequest= "/replyToBookRequest.php";
	
	private static final String deleteABookRequest= "/deleteABookRequest.php";
	
	private static final String popularBooks= "/popularBooks.php";

	private static final String sendMessage= "/sendMessage.php";
	
//	/**
//	 * Constructor
//	 * 
//	 * @param pName
//	 * @param pLogoURL
//	 */
//	public Library(String pName, String pURL, String pLocation, String pEmail) {
//		name = pName;
//		URL = pURL;
//		location = pLocation;
//		email =pEmail;
//		
//	}
	
	/**
	 * @return the Image URL
	 */
	String getImageURL(){
		return URL+imageURL;
	}
	
	/**
	 * @return the Login URL
	 */
	String getLoginURL(){
		return URL+SCRIPTS_PATH+loginURL;
	}

	
	/**
	 * @return the Register URL
	 */
	String getRegisterURL(){
		return URL+SCRIPTS_PATH+registerURL;
	}

	String getInsertBookByISBN_URL() {
		return URL+SCRIPTS_PATH+insertBookByISBN;
	}
	
	String getStateOfBook_URL() {
		return URL+SCRIPTS_PATH+stateOfBook;
	}
	
	String getLentABook_URL() {
		return URL+SCRIPTS_PATH+lentABook;
	}
	
	String getReturnABook_URL() {
		return URL+SCRIPTS_PATH+returnABook;
	}

	
	String getUserBooks_URL() {
		return URL+SCRIPTS_PATH+getUserBooks;
	}
	
	String getChangeStatusOfBook_URL() {
		return URL+SCRIPTS_PATH+getChangeStatusOfBook;
	}


	String getDeleteABook_URL() {
		return URL+SCRIPTS_PATH+deleteABook;
	}
	
	String getSearch_URL() {
		return URL+SCRIPTS_PATH+search;
	}
	
	String getRequestABook_URL() {
		return URL+SCRIPTS_PATH+requestABook;
	}
	
	String getIncomingRequests_URL() {
		return URL+SCRIPTS_PATH+incomingRequests;
	}

	String getOutgoingRequests_URL() {
		return URL+SCRIPTS_PATH+outgoingRequests;
	}

	String getBooksIGave_URL() {
		return URL+SCRIPTS_PATH+booksIGave;
	}

	String getBooksITook_URL() {
		return URL+SCRIPTS_PATH+booksITook;
	}
	
	String getReplyToBookRequest_URL() {
		return URL+SCRIPTS_PATH+replyToBookRequest;
	}
	
	
	String getdeleteABookRequest_URL() {
		return URL+SCRIPTS_PATH+deleteABookRequest;
	}
	
	String getpopularBooks_URL() {
		return URL+SCRIPTS_PATH+popularBooks;
	}
	
	
	String getSendMessage_URL() {
		return URL+SCRIPTS_PATH+sendMessage;
	}
	
	
	
	









	
	
}
