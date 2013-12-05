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


//TODO FIX LINKS FOR APPS!

//android	
$ua = strtolower($_SERVER['HTTP_USER_AGENT']);
if (stripos($ua, 'android') !== false) { // && stripos($ua,'mobile') !== false) {
    header('Location: https://play.google.com/store/apps/details?id=cy.ac.ucy.pmpeis01.client.android');
    die();
}

//ipad
$isiPad = (bool)strpos($_SERVER['HTTP_USER_AGENT'], 'iPad');

//iphone
$isiPhone = (bool)strpos($_SERVER['HTTP_USER_AGENT'], 'iPhone');

//ipod
$isiPod = (bool)strpos($_SERVER['HTTP_USER_AGENT'], 'iPod');

if ($isiPad || $isiPhone || $isiPod) {
    header('http://itunes.apple.com/');
    die();
}

?>