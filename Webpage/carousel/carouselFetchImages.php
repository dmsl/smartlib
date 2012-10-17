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

require_once("../CONFIG.php");

// Array indexes are 0-based, jCarousel positions are 1-based.
$first = max(0, intval($_GET['first']) - 1);
$last = max($first + 1, intval($_GET['last']) - 1);

$length = $last - $first + 1;

// ---
//Get Popular Books

//URL for the Google Books API
$device = "?device=web";
$mykey = "&mykey=" . _MY_KEY;

$buildURL = _LIB_URL . "/mobile/popularBooks.php" . $device . $mykey;

$json = file_get_contents($buildURL, 0, null, null);
$data = json_decode($json);


//Found results

$isbns = array();
$images = array();
$titles = array();

//Get ISBN-13 Code of book
foreach ($data as $item) {


    $img_url = $item->imgURL;


    if ($img_url == "images/nocover.png") {
        $img_url = "http://" . getCustom2ndURL() . "images/nocover.png";
    }
    array_push($images, htmlspecialchars($img_url));
    array_push($isbns, $item->isbn);
    array_push($titles, htmlspecialchars($item->title));

}


$total = count($images);

$selectedImg = array_slice($images, $first, $length);
$selectedIsbn = array_slice($isbns, $first, $length);
$selectedTitles = array_slice($titles, $first, $length);
// ---

header('Content-Type: text/xml');

echo '<data>';

// Return total number of images so the callback
// can set the size of the carousel.
echo '  <total>' . $total . '</total>';

foreach ($selectedImg as $img) {
    echo '  <image>' . $img . '</image>';
}

foreach ($selectedIsbn as $isbns) {
    echo '  <isbn>' . $isbns . '</isbn>';
}
foreach ($selectedTitles as $titles) {
    echo '  <title>' . $titles . '</title>';
}


echo '</data>';


//Returns the URL user is, without include the last page in the URL path
function xmlspecialchars($text)
{
    return str_replace('&#039;', '&apos;', htmlspecialchars($text, ENT_QUOTES));
}


function getCustom2ndURL()
{

    $len = strlen($_SERVER['REQUEST_URI']);
    $fullURL = $_SERVER['REQUEST_URI'];
    $found = 0;


    for ($i = $len - 1; $i > 0; $i--) {


        //Remove the last name of the URI
        if ($fullURL[$i] == "/") {

            $found = 1;

            $urlResult = substr($fullURL, 0, $i);
            break;
        }

    }


    $len = strlen($urlResult);
    $fullURL = $urlResult;
    $found = 0;


    for ($i = $len - 1; $i > 0; $i--) {


        //Remove the last name of the URI
        if ($fullURL[$i] == "/") {

            $found = 1;

            $urlResult = substr($fullURL, 0, $i + 1);
            break;
        }

    }


    if (!$found)
        $urlResult = $_SERVER['REQUEST_URI'];

    return $_SERVER['SERVER_NAME'] . $urlResult;

}


?>