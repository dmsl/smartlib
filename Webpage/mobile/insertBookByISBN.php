<?php 

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

session_start();
$_SESSION['isMobileDevice']=0;

//Get the device, ISBN & Username
$device = $_POST['device'];
$pISBN = $_POST['isbn'];
$pUsername = $_POST['username'];

$_SESSION['UserID']="";
$_SESSION['BookInfoID']="";



$URL="https://www.googleapis.com/books/v1/";
$question="volumes?q=+";
$Qisbn="isbn:";
$pasqKey="&key=AIzaSyDEe8uL165TMaqASbFRxTVfPSfbQvwCKd4";



//Find out if we are on mobile device
if($device=="android" || $device=="iOS"){
	$_SESSION['isMobileDevice']=1;

}
//else{
//	//curently works only for mobile devices
//	die();
//}

//Connect to database
include ('../dbConnect.php');

if($_SESSION['isMobileDevice']){


	//Check if book already exists to database
	if(checkIfBookAlreadyExists($pUsername, $pISBN))
		mobileSendBookAlreadyExistsError();

	//URL for the Google Books API
	$buildURL=$URL.$question.$Qisbn.$pISBN.$pasqKey;


	$json = file_get_contents($buildURL,0,null,null);
	$data = json_decode($json);

	//Found results
	if($data->totalItems>0){

		$foundISBN13=0;

		//Get ISBN-13 Code of book
		foreach ($data->items as $item) {

			foreach ($item->volumeInfo->industryIdentifiers as $pID_ISBN) {
				//Found isbn 13
				if($pID_ISBN->type=="ISBN_13"){
					$foundISBN13=1;
					$book_ISBN=$pID_ISBN->identifier;

					break;
				}

					
			}
			//Found isbn in object
			if($foundISBN13) break;


		}



		//Title of Book
		foreach ($data->items as $item) {

			$book_title=$item->volumeInfo->title;

			if($book_title!=""){
				break;
			}



		}


		//All Authors of book
		foreach ($data->items as $item) {

			foreach ($item->volumeInfo->authors as $author) {
				$book_authors.= $author.", " ;
					
			}


			if($book_authors!=""){
				break;
			}


		}

		$book_authors= substr($book_authors , 0 , (strlen($book_authors)-2));
		$book_authors=  convertAccentsAndSpecialToNormal($book_authors);


		//Published Year
		foreach ($data->items as $item) {

			$book_publishedYear=$item->volumeInfo->publishedDate;

			if($book_publishedYear!=""){
				break;
			}

		}


		//Page count
		foreach ($data->items as $item) {

			$book_pageCount=$item->volumeInfo->pageCount;

			if($book_pageCount!=""){
				break;
			}



		}


		//Image URL & Fix if image not found
		foreach ($data->items as $item) {

			$book_imgURL=$item->volumeInfo->imageLinks->thumbnail;

			if($book_imgURL!=""){
				break;
			}



		}
		if($book_imgURL == ""){
			$book_imgURL= "images/nocover.png";
		}


		//Language
		foreach ($data->items as $item) {

			$book_language=$item->volumeInfo->language;

			if($book_language!=""){
				break;
			}



		}


		//Add Book to Database
		addBookToDatabase($book_ISBN,$book_title,$book_authors,$book_publishedYear,
				$book_pageCount,$book_imgURL,$book_language);


	}
	else{
		//Inform mobile devices about not existance of book
		mobileSendBookDontExistsError();
	}

}


////////////////////////////////// Functions
function addBookToDatabase($ISBN,$title,$authors,$publishedYear,
		$pageCount,$imgURL,$language){

	//Book Info Dont exists
	// Need to create record for Book Info
	if($_SESSION['BookInfoID']==""){
		
		$queryInsertStr= sprintf(
		"INSERT INTO SMARTLIB_BOOK_INFO (isbn, title, authors, publishedYear, pageCount,"
		."imgURL,lang) VALUES ('%s','%s','%s','%s','%s','%s','%s')"
				,
				mysql_real_escape_string($ISBN),
				mysql_real_escape_string($title),
				mysql_real_escape_string($authors),
				mysql_real_escape_string($publishedYear),
				mysql_real_escape_string($pageCount),
				mysql_real_escape_string($imgURL),
				mysql_real_escape_string($language)
				);
		
		$insertBookInfo = mysql_query($queryInsertStr) or dbError(mysql_error());
	
		//Get the just generated BookInfoID
		$result = mysql_query("SELECT BI_ID FROM SMARTLIB_BOOK_INFO WHERE isbn='".$ISBN."'") or dbError(mysql_error());
		
		$_SESSION['BookInfoID'] = mysql_result($result, 0);
	}
	
	//Create entry for Book between Book Info ID, and User ID
	$insertBook
	= mysql_query("INSERT INTO SMARTLIB_BOOK (U_ID, BI_ID) VALUES ('".$_SESSION['UserID']."',".
			"'".$_SESSION['BookInfoID']."')") or dbError(mysql_error());
	
	//Inform user about Successfull Insertion
	mobileSendSuccess();

}






//Checks if books already exists for user
function checkIfBookAlreadyExists($pUser,$pISBN){
	
	
	$queryFindUser= sprintf("SELECT U_ID FROM SMARTLIB_USER WHERE username='%s'",
			mysql_real_escape_string($pUser));

	// Find Unique ID of User

	$result = mysql_query($queryFindUser) or dbError(mysql_error());
	//$result = mysql_query("SELECT U_ID FROM USER WHERE username='".$pUser."'") or dbError(mysql_error());

	$_SESSION['UserID'] = mysql_result($result, 0);
	
	if($_SESSION['UserID'] ==""){
		mobileSendWeirdError();
	}

	//Find Unique ID of Book Info
	$result = mysql_query("SELECT BI_ID FROM SMARTLIB_BOOK_INFO WHERE isbn='".$pISBN."'") or dbError(mysql_error());

	$_SESSION['BookInfoID'] = mysql_result($result, 0);

	if($_SESSION['BookInfoID']==""){
		return 0;
	}

	//Find if Book of user exists
	// Check username uniqueness
	$sqlString ="SELECT B_ID FROM SMARTLIB_BOOK WHERE U_ID='".$_SESSION['UserID']."'".
			" AND BI_ID='".$_SESSION['BookInfoID']."'";


	$bookMatches = mysql_query($sqlString);

	$bookExists = mysql_num_rows($bookMatches);

	if($bookExists > 0){
		return 1;

	}

	//Book dont exists
	return 0;

}



// Sends error to mobile device: Book dont exists in Google API
function mobileSendWeirdError(){
	$result = array(
			"result"=>"-12"
	);
	//Encode Answer
	echo json_encode($result);

	die();
}


// Sends error to mobile device: Book dont exists in Google API
function mobileSendBookDontExistsError(){
	$result = array(
			"result"=>"-2"
	);
	//Encode Answer
	echo json_encode($result);

	die();
}

// Sends error to mobile device: Book already exists in User's Library
function mobileSendBookAlreadyExistsError(){
	$result = array(
			"result"=>"0"
	);
	//Encode Answer
	echo json_encode($result);

	die();
}


// TODO Sends error to mobile device using JSON Object Format
function mobileSendDatabaseError(){
	$result = array(
			"result"=>"-11"
	);
	//Encode Answer
	echo json_encode($result);

	die();
}

// TODO Sends error to mobile device using JSON Object Format
function mobileSendSuccess(){
	$result = array(
			"result"=>"1"
	);
	//Encode Answer
	echo json_encode($result);

	die();
}


//Returns the URL user is, without include the last page in the URL path

function getCustomURL(){

	$len = strlen($_SERVER['REQUEST_URI']);

	for( $i= $len-1; $i>0; $i--){
		//Remove the last name of the URI
		if($_SERVER['REQUEST_URI'][$i]=="/"){

			$found=1;

			$urlResult = substr($_SERVER['REQUEST_URI'],0,$i+1);
			break;
		}

	}

	if(!$found)
		$urlResult = $_SERVER['REQUEST_URI'];

	return $_SERVER['SERVER_NAME']. $urlResult;

}




//
function dbError($pError){

	if($_SESSION['isMobileDevice']){
		//Inform Mobile Device about database Error
		mobileSendDatabaseError();
	}

	//if there is DB Error, inform user and move him back
	inform($pError);


	if($_SESSION['currentPage']==""){
		$_SESSION['currentPage']="index.php";
	}

	header("Location: ".$_SESSION['currentPage']);
	die();

}



/**
 * Replaces special characters in a string with their "non-special" counterpart.
 */
function convertAccentsAndSpecialToNormal($string) {
	$table = array(
			'À'=>'A', 'Á'=>'A', 'Â'=>'A', 'Ã'=>'A', 'Ä'=>'A', 'Å'=>'A', 'Ă'=>'A', 'Ā'=>'A', 'Ą'=>'A', 'Æ'=>'A', 'Ǽ'=>'A',
			'à'=>'a', 'á'=>'a', 'â'=>'a', 'ã'=>'a', 'ä'=>'a', 'å'=>'a', 'ă'=>'a', 'ā'=>'a', 'ą'=>'a', 'æ'=>'a', 'ǽ'=>'a',

			'Þ'=>'B', 'þ'=>'b', 'ß'=>'Ss',

			'Ç'=>'C', 'Č'=>'C', 'Ć'=>'C', 'Ĉ'=>'C', 'Ċ'=>'C',
			'ç'=>'c', 'č'=>'c', 'ć'=>'c', 'ĉ'=>'c', 'ċ'=>'c',

			'Đ'=>'Dj', 'Ď'=>'D', 'Đ'=>'D',
			'đ'=>'dj', 'ď'=>'d',

			'È'=>'E', 'É'=>'E', 'Ê'=>'E', 'Ë'=>'E', 'Ĕ'=>'E', 'Ē'=>'E', 'Ę'=>'E', 'Ė'=>'E',
			'è'=>'e', 'é'=>'e', 'ê'=>'e', 'ë'=>'e', 'ĕ'=>'e', 'ē'=>'e', 'ę'=>'e', 'ė'=>'e',

			'Ĝ'=>'G', 'Ğ'=>'G', 'Ġ'=>'G', 'Ģ'=>'G',
			'ĝ'=>'g', 'ğ'=>'g', 'ġ'=>'g', 'ģ'=>'g',

			'Ĥ'=>'H', 'Ħ'=>'H',
			'ĥ'=>'h', 'ħ'=>'h',

			'Ì'=>'I', 'Í'=>'I', 'Î'=>'I', 'Ï'=>'I', 'İ'=>'I', 'Ĩ'=>'I', 'Ī'=>'I', 'Ĭ'=>'I', 'Į'=>'I',
			'ì'=>'i', 'í'=>'i', 'î'=>'i', 'ï'=>'i', 'į'=>'i', 'ĩ'=>'i', 'ī'=>'i', 'ĭ'=>'i', 'ı'=>'i',

			'Ĵ'=>'J',
			'ĵ'=>'j',

			'Ķ'=>'K',
			'ķ'=>'k', 'ĸ'=>'k',

			'Ĺ'=>'L', 'Ļ'=>'L', 'Ľ'=>'L', 'Ŀ'=>'L', 'Ł'=>'L',
			'ĺ'=>'l', 'ļ'=>'l', 'ľ'=>'l', 'ŀ'=>'l', 'ł'=>'l',

			'Ñ'=>'N', 'Ń'=>'N', 'Ň'=>'N', 'Ņ'=>'N', 'Ŋ'=>'N',
			'ñ'=>'n', 'ń'=>'n', 'ň'=>'n', 'ņ'=>'n', 'ŋ'=>'n', 'ŉ'=>'n',

			'Ò'=>'O', 'Ó'=>'O', 'Ô'=>'O', 'Õ'=>'O', 'Ö'=>'O', 'Ø'=>'O', 'Ō'=>'O', 'Ŏ'=>'O', 'Ő'=>'O', 'Œ'=>'O',
			'ò'=>'o', 'ó'=>'o', 'ô'=>'o', 'õ'=>'o', 'ö'=>'o', 'ø'=>'o', 'ō'=>'o', 'ŏ'=>'o', 'ő'=>'o', 'œ'=>'o', 'ð'=>'o',

			'Ŕ'=>'R', 'Ř'=>'R',
			'ŕ'=>'r', 'ř'=>'r', 'ŗ'=>'r',

			'Š'=>'S', 'Ŝ'=>'S', 'Ś'=>'S', 'Ş'=>'S',
			'š'=>'s', 'ŝ'=>'s', 'ś'=>'s', 'ş'=>'s',

			'Ŧ'=>'T', 'Ţ'=>'T', 'Ť'=>'T',
			'ŧ'=>'t', 'ţ'=>'t', 'ť'=>'t',

			'Ù'=>'U', 'Ú'=>'U', 'Û'=>'U', 'Ü'=>'U', 'Ũ'=>'U', 'Ū'=>'U', 'Ŭ'=>'U', 'Ů'=>'U', 'Ű'=>'U', 'Ų'=>'U',
			'ù'=>'u', 'ú'=>'u', 'û'=>'u', 'ü'=>'u', 'ũ'=>'u', 'ū'=>'u', 'ŭ'=>'u', 'ů'=>'u', 'ű'=>'u', 'ų'=>'u',

			'Ŵ'=>'W', 'Ẁ'=>'W', 'Ẃ'=>'W', 'Ẅ'=>'W',
			'ŵ'=>'w', 'ẁ'=>'w', 'ẃ'=>'w', 'ẅ'=>'w',

			'Ý'=>'Y', 'Ÿ'=>'Y', 'Ŷ'=>'Y',
			'ý'=>'y', 'ÿ'=>'y', 'ŷ'=>'y',

			'Ž'=>'Z', 'Ź'=>'Z', 'Ż'=>'Z', 'Ž'=>'Z',
			'ž'=>'z', 'ź'=>'z', 'ż'=>'z', 'ž'=>'z',

			'“'=>'"', '”'=>'"', '‘'=>"'", '’'=>"'", '•'=>'-', '…'=>'...', '—'=>'-', '–'=>'-', '¿'=>'?', '¡'=>'!', '°'=>' degrees ',
			'¼'=>' 1/4 ', '½'=>' 1/2 ', '¾'=>' 3/4 ', '⅓'=>' 1/3 ', '⅔'=>' 2/3 ', '⅛'=>' 1/8 ', '⅜'=>' 3/8 ', '⅝'=>' 5/8 ', '⅞'=>' 7/8 ',
			'÷'=>' divided by ', '×'=>' times ', '±'=>' plus-minus ', '√'=>' square root ', '∞'=>' infinity ',
			'≈'=>' almost equal to ', '≠'=>' not equal to ', '≡'=>' identical to ', '≤'=>' less than or equal to ', '≥'=>' greater than or equal to ',
			'←'=>' left ', '→'=>' right ', '↑'=>' up ', '↓'=>' down ', '↔'=>' left and right ', '↕'=>' up and down ',
			'℅'=>' care of ', '℮' => ' estimated ',
			'Ω'=>' ohm ',
			'♀'=>' female ', '♂'=>' male ',
			'©'=>' Copyright ', '®'=>' Registered ', '™' =>' Trademark ',
	);

	$string = strtr($string, $table);
	// Currency symbols: £¤¥€  - we dont bother with them for now
	$string = preg_replace("/[^\x9\xA\xD\x20-\x7F]/u", "", $string);

	return $string;
}



/*
 * result:1 BOOK_INSERTED
* result:0 BOOK_ALREADY_EXISTS
* result: -11 DATABASE_ERROR
* result:-2 BOOK_ISBN_DONT_EXISTS (dont exists in Google Books API)
* result: -12 Weird didnt matched a username with its unique id. this must be a programmers error
*

*
*
* */



//echo  '<br />';






//foreach ($data->totalItems as $items) {
//		echo $items->firstName . '<br />';
//	}


//echo "</br>";echo "</br>";echo "</br>";
//echo "title";

//echo $data[0]->title;

//	echo "</br>";
//	echo "isbn";

//	echo $data[0]->isbn;

//echo $data['isbn']; // "apples"
//	echo $data['title']; // "apples"
//echo $data['b']; // "bananas"



//echo "JSON decoded: ".$json_output;

//	echo $data->{'isbn'}."</br>";
//echo $data->{'title'}."</br>";
//	echo $data->{'authors'}."</br>";
//	echo $data->{'publishedYear'}."</br>";
//	echo $data->{'pageCount'}."</br>";
//	echo $data->{'language'}."</br>";



/*	foreach ( $json_output->trends as $trend )
 {
echo "{$trend->name}\n";
}
*/


/* My key: AIzaSyDEe8uL165TMaqASbFRxTVfPSfbQvwCKd4
 *
Da vinci code ISBN: 9780785941743, 9780593055052, 9780739326749, 9788467202397, 9780828815130


WORKS FOR DAVINCI CODE!
https://www.googleapis.com/books/v1/volumes?q=+isbn:9780739326749&key=AIzaSyDEe8uL165TMaqASbFRxTVfPSfbQvwCKd4


--------------------------------------------
OLD CODE:

"https://ajax.googleapis.com/ajax/services/search/books?v=1.0&q=ISBN"
+ isbn+ "&key=ABQIAAAA94MJuF2gFa9KqeGTuI0EjBSkkyQUZK2-HCU2v8Q1ngVNh8D3JBThgk7ik35T7gM0BpiNLjAu-mKyIg&userip=192.168.0.1");


https://ajax.googleapis.com/ajax/services/search/books?v=1.0&q=ISBN9780739326749&key=ABQIAAAA94MJuF2gFa9KqeGTuI0EjBSkkyQUZK2-HCU2v8Q1ngVNh8D3JBThgk7ik35T7gM0BpiNLjAu-mKyIg
-----------------------------------------

Performing a search

You can perform a volumes search by sending an HTTP GET request to the following URI:

https://www.googleapis.com/books/v1/volumes?q=search+terms
This request has a single required parameter:

q - Search for volumes that contain this text string. There are special keywords you can specify in the search terms to search in particular fields, such as:
intitle: Returns results where the text following this keyword is found in the title.
inauthor: Returns results where the text following this keyword is found in the author.
inpublisher: Returns results where the text following this keyword is found in the publisher.
subject: Returns results where the text following this keyword is listed in the category list of the volume.
isbn: Returns results where the text following this keyword is the ISBN number.
lccn: Returns results where the text following this keyword is the Library of Congress Control Number.
oclc: Returns results where the text following this keyword is the Online Computer Library Center number.
*
*
*/




?>