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



/**
 * The Library User
 * 
 * @author paschalis
 * 
 */
public class User {



	/** Username User puts on login form */
	String				username;

	/** Password User puts on login form */
	String				password;

	/** Name of user */
	String				name;

	/** Surname of user */
	String				surname;

	String				email;

	String				telephone;

	/* @formatter:off */
	/** 
	 * Level Explanation
	 * 0: ούτε ειδοποιήσεις εντός εφαρμογής, ούτε email
	 * 1: μόνο ειδοποιήσεις εντός εφαρμογής
	 * 2: μόνο ειδοποιήσεις μέσω email
	 * 3: και ειδοποιήσεις εντός εφαρμογής αλλά και email
	 * 
	 */
	/* @formatter:on */
	int					allowRequests			= ALLOW_REQUESTS_NOT_SET;


	public static final int	ALLOW_REQUESTS_NOTHING	= 0;

	public static final int	ALLOW_REQUESTS_APP		= 1;

	public static final int	ALLOW_REQUESTS_EMAIL		= 2;

	public static final int	ALLOW_REQUESTS_ALL		= 3;

	public static final int	ALLOW_REQUESTS_NOT_SET	= -10;


	/* @formatter:off */
	/** Level of User
	Level 2: Μπορεί να κάνει κάποιον Level2(mod), ή να διαγράψει κάποιον χρήστη από την Βιβλιοθήκη
	Level 1: Μπορεί να   εισάγει τις προσωπικές του Βιβλιοθήκες
	Level 0: Ο χρήστης δεν ενεργοποίησε ακόμη τον λογαριασμό του μέσω του email του.
	Level -1: Ο χρήστης είναι επισκέπτης στο σύστημα.

	 * */
	/* @formatter:on */
	int					level				= LEVEL_NOT_SET;

	public int	totalBooks=0;

	public static final int	LEVEL_MOD				= 2;

	public static final int	LEVEL_ACTIVATED		= 1;

	public static final int	LEVEL_NOT_ACTIVATED		= 0;

	public static final int	LEVEL_VISITOR			= -1;
	
	/** Exists only in Android enviroment, to flag user credencials as invalid */
	public static final int	LEVEL_BANNED			= -2;

	public static final int	LEVEL_NOT_SET			= -10;
}
