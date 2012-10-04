<?php
/*
 This file is part of SmartLib Project.

SmartLib is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SmartLib is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without -+ even the implied warranty of
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
//include '../scripts/sessionInit.php';

?>
<!DOCTYPE html>
<html lang=en>

<!--        Head         -->
<head>
    <meta charset=UTF-8>
    <!--    TODO fill metadata for all pages-->
    <meta name="description" content="SmartLib : Library of Modern World"/>
    <meta name="keywords" content="smartLib, smart, library, books, ucy,
    university of cyprus, university, cyprus, cs"/>
    <title>Install SmartLib</title>
    <link href="images/favicon.ico" rel="icon" type="image/x-icon"/>
    <!--    Include Scripts-->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8/jquery.min.js"></script>
    <link href=../styles.css rel=stylesheet>
    </link>
    <script src="../scripts/mainScripts.js"></script>
    <script>
        //Gather new data
        var dbhost;
        var dbusername;
        var dbpassword;
        var dbname;

        var libfullname;
        var libshortname;
        var orgname;
        var liburl;
        var depturl;
        var orgurl;
        var email;
        var addr1;
        var addr2;
        var addr3;
        var town;
        var country;
        var telephone;
        var fax;
        var key;
        var salt;
        var pepper;

    </script>
</head>


<body>

<!-- Header -->

<!--Toast Messages -->
<div class=clearfix>
<div class="toast-message" id="notoast">No Errors Found</div>
<div id=content>

<article>
<br>
<header>
    <h1>Smartlib Installation: &nbsp&nbspSetup MyQL Database 1/3</h1>
</header>
<div class="installContent"><br>


<div id="install-panel">
<div id="install-form1">
    <fieldset>
        <label>&nbsp&nbsp&nbsp&nbspHost:
            <input id="install-form1-dbhost"
                   type="text" autofocus="autofocus"
                   placeholder="dbhost.example.com"
                   value=""/><span class="required">*</span>
        </label><br>
        <label>Username:
            <input id="install-form1-dbuser"
                   type="text"
                   placeholder="db username"
                   value=""/><span class="required">*</span>
        </label><br>
        <label>Password:
            <input id="install-form1-dbpass"
                   type="password"
                   placeholder="db password"
                   value=""/><span class="required">*</span>
        </label><br>
        <label>&nbsp&nbsp&nbsp&nbspName:
            <input id="install-form1-dbname"
                   type="text"
                   placeholder="myDatabase"
                   value=""/><span class="required">*</span>
        </label><br>
        <br>
        <button class="clearButton" type="button"
                onclick="simpleFormReset()"
                >Clear
        </button>
        <button class="sendButton" type="button"
                onclick="validateDatabase()"
                >Next
        </button>
    </fieldset>
</div>


<script>
//Initialize form fields
$(document).ready(function () {

//                            $("input: text, password").
//                                    each(function () {
//                                        updateFormFieldStatusSimple($(this))
//                                    });
//
//                            $("input: url").
//                                    each(function () {
//                                        updateFormFieldStatusURL($(this))
//                                    });
//
//                            $("input: email").
//                                    each(function () {
//                                        updateFormFieldStatusEmail($(this))
//                                    });
//
//                            $("input: tel").
//                                    each(function () {
//                                        updateFormFieldStatusTelephone($(this))
//                                    });
//                            $('input:text, input:password')


    $('input[type=text], input[type=password]').live("keyup", function () {
        //Something is filled
        updateFormFieldStatusSimple(this);
    });

//                            $('').keyup(function () {
//                                //Something is filled
//                                updateFormFieldStatusSimple(this);
//                            });


    $('input[type=url]').live("keyup", function () {

        updateFormFieldStatusURL(this);
    });


    $('input[type=email]').live("keyup", function () {
        updateFormFieldStatusEmail(this);
    });

    $('input[type=tel]').live("keyup", function () {
        updateFormFieldStatusTelephone(this);
    });


});


// Reset Simple forms
function simpleFormReset() {


    $("#install-panel input").val("");
    $("#install-panel input").
            each(function () {
                updateFormFieldStatusSimple($(this))
            });
}

//Validates Database and saves Credentials to configuration file
function validateDatabase() {

    //Gather data
    dbhost = document.getElementById('install-form1-dbhost').value;
    dbusername = document.getElementById('install-form1-dbuser').value;
    dbpassword = document.getElementById('install-form1-dbpass').value;
    dbname = document.getElementById('install-form1-dbname').value;


    if (dbhost == "" || dbusername == "" || dbpassword == "" || dbname == "") {
        showToastMessage("Please fill all fields", 0);
        return;
    }


    var params = "host=" + dbhost + "&username=" + dbusername
            + "&password=" + dbpassword + "&name=" + dbname;


    runAPostWebpage("validatedb.php", params, switchToInstall2, "");//TODO CALLBACK WHEN SUCCESS MOVE TO SETUP 2!
}

//Switches installation procedure to phase 2
function switchToInstall2() {
//Change title
    $("h1").html("Smartlib Installation: &nbsp&nbspFill Library Information 2/3");

//replace with form 2
    $("#install-form1").replaceWith(
            '<div id="install-form2">\
            <fieldset>\
            <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbsp&nbspLibrary Full Name:\
            <input id="install-form2-libname"\
    type="text" autofocus="autofocus"\
    placeholder="University of Cyprus Library"\
    value=""/><span class="required">*</span>\
            </label><br>\
            <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbspLibrary Short Name:\
            <input id="install-form2-libshortname"\
    type="text"\
    placeholder="UCY"\
    value=""/><span class="required">*</span>\
            </label><br>\
            <label>&nbsp&nbsp&nbsp&nbsp\
    Organization Full Name:\
            <input id="install-form2-liborgname"\
    type="text"\
    placeholder="Our Department"\
    value=""/><span class="required">*</span>\
            </label><br>\
            <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbsp&nbsp&nbspSmartLib\'s URL:\
    <input id="install-form2-liburl"\
    type="url"\
    placeholder="http://www.your domain"\
    value=""/><span class="required">*</span>\
            </label><br>\
            <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbsp&nbspDepartment\'s URL:\
    <input id="install-form2-libdepturl"\
    type="url"\
    placeholder="http://www.our department"\
    value=""/><span class="required">*</span>\
            </label><br>\
            <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbsp&nbspOrganization URL:\
            <input id="install-form2-liborgurl"\
    type="url"\
    placeholder="http://www.organization"\
    value=""/><span class="required">*</span>\
            </label><br>\
            <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspEmail:\
<input id="install-form2-libemail"\
    type="email"\
    value=""/><span class="required">*</span>\
            </label><br>\
            <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbsp&nbsp&nbsp&nbspFull Address:\
            <input id="install-form2-libaddr1"\
    type="text"\
    placeholder="Address Line 1"\
    value=""/><span class="required">*</span>\
            </label>\
            <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbsp&nbsp&nbsp&nbsp\
            <input id="install-form2-libaddr2"\
    type="text"\
    placeholder="Address Line 2"\
    value=""/><span class="required">*</span>\
            </label>\
            <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbsp&nbsp&nbsp&nbsp\
            <input id="install-form2-libaddr3"\
    type="text"\
    placeholder="P.O. Box 00000"\
    value=""/><span class="required">*</span>\
            </label>\
            </label><br>\
            <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspTown:\
<input id="install-form2-libtown"\
    type="text"\
    placeholder=""\
    value=""/><span class="required">*</span>\
            </label><br>\
            <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspCountry:\
<input id="install-form2-libcountry"\
    type="text"\
    placeholder=""\
    value=""/><span class="required">*</span>\
            </label><br>\
            <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspTelephone:\
<input id="install-form2-libtelephone"\
    type="tel"\
    placeholder="+XXX-XXXXXXXX"\
    value=""/><span class="required">*</span>\
            </label><br>\
            <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspFax:\
<input id="install-form2-libfax"\
    type="tel"\
    placeholder="+XXX-XXXXXXXX"\
    value=""/>\
            </label><br>\
            <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp\
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspSafety key:\
            <input id="install-form2-libkey"\
    type="password"\
    value=""/><span class="required">*</span>\
    <p class="info">\
            used for extra safety on operations like: book exporting\
            </p>\
            </label>\
            <br>\
            <label>&nbsp&nbspSalt on users credentials:\
            <input id="install-form2-libsalt"\
    type="password"\
    placeholder="abc123"\
    value=""/><span class="required">*</span>\
            </label><br>\
            <label>Pepper on users credentials:\
            <input id="install-form2-libpepper"\
    type="password"\
    placeholder="qwe890"\
    value=""/><span class="required">*</span>\
    <p class="info">\
            used to encrypt credentials of library user\'s\
            </p>\
            </label>\
            <br>\
    <button class="clearButton" type="button"\
    onclick="simpleFormReset()"\
            >Clear\
            </button>\
            <button class="sendButton" type="button"\
    onclick="saveDataToFile()"\
            >Next\
            </button>\
            </fieldset>\
            </div>');


}


function saveDataToFile() {

    var foundError = 0;

    $("#install-panel input").
            each(function () {
                //If is a required field
                if (!$(this) == $("#install-form2-libfax")) {

                    if ($(this).val() == "") {
                        showToastMessage("Please fill all fields", 0);
                        foundError = 1;
                        return;
                    }
                }


            });

    if (foundError) return;

    //Get data
    libfullname = document.getElementById('install-form2-libname').value;
    libshortname = document.getElementById('install-form2-libshortname').value;
    orgname = document.getElementById('install-form2-liborgname').value;
    liburl = document.getElementById('install-form2-liburl').value;
    depturl = document.getElementById('install-form2-libdepturl').value;
    orgurl = document.getElementById('install-form2-liborgurl').value;
    email = document.getElementById('install-form2-libemail').value;
    addr1 = document.getElementById('install-form2-libaddr1').value;
    addr2 = document.getElementById('install-form2-libaddr2').value;
    addr3 = document.getElementById('install-form2-libaddr3').value;
    town = document.getElementById('install-form2-libtown').value;
    country = document.getElementById('install-form2-libcountry').value;
    telephone = document.getElementById('install-form2-libtelephone').value;
    fax = document.getElementById('install-form2-libfax').value;
    key = document.getElementById('install-form2-libkey').value;
    salt = document.getElementById('install-form2-libsalt').value;
    pepper = document.getElementById('install-form2-libpepper').value;


    var errorMessage = "";


    if (!isURLValid(liburl) || !isURLValid(depturl) || !isURLValid(orgurl)) {
        errorMessage += "Some of URL's not valid<br>";
        foundError = 1;
    }

    if (!isEmailValid(email)) {
        errorMessage += "Email Address not valid<br>";
        foundError = 1;
    }


    if (!isTelephoneValid(telephone)) {
        errorMessage += "Telephone number not valid";
        foundError = 1;
    }

    if (fax != "") {
        if (!isTelephoneValid(fax)) {
            errorMessage += "Fax number not valid";
            foundError = 1;
        }

    }

    //Found error
    if (foundError) {
        showToastMessage(errorMessage, 0);
        return;
    }

    //Write data to Configuration file
    var paramsWriteData = "dbname=" + dbname + "&dbhost=" + dbhost + "&dbuser=" + dbusername
            + "&dbpass=" + dbpassword + "&libname=" + libfullname + "&libshortname=" + libshortname +
            "&orgname=" + orgname + "&addr1=" + addr1 + "&addr2=" + addr2 + "&addr3=" + addr3 +
            "&town=" + town + "&country=" + country + "&telephone=" + telephone +
            "&fax=" + fax + "&email=" + email + "&depturl=" + depturl + "&orgurl=" + orgurl +
            "&key=" + key + "&pepper=" + pepper + "&salt=" + salt;


    //Write data to configuration file
    runAPostWebpage("writeData.php", paramsWriteData, switchToInstall3, "");


}


function switchToInstall3() {
    $("h1").html("Smartlib Installation: &nbsp&nbspEnroll your library 3/3");

//replace with form 3
    $("#install-form2").replaceWith(
            '<div id="install-form3"><p>You are almost done! What is left, is to enroll \
            your library to SmartLib Central Database.<br>\
    Then your library will reviewed by SmartLib Admin, and if accepted, it will \
    be available as a login option to Smartphone and Tablet applications on Android and iOS.<br><br><br>\
    To Enroll, just click finish!</p>\
    <br><br>\
            <button class="sendButton" type="button"\
    onclick="enrollLibrary()"\
            >Finish\
            </button>\
            </fieldset>\
            </div>');

}

function enrollLibrary() {

    var params = "name=" + libshortname + "&url=" + liburl + "&email=" + email +
            "&telephone=" + telephone + "&town=" + town + "&country=" + country;

    runAPostWebpage("http://www.cs.ucy.ac.cy/projects/smartLib/MASTER/enroll.php", params, switchToFinished, "");


}

function switchToFinished() {
    $("h1").html("Smartlib Installation Finished! 3/3");

//replace with form 3
    $("#install-form3").replaceWith(
            '<p>Thank you for installing SmartLib.<br>\
            Your credentials are saved in CONFIG.php in the root folder of SmartLib.<br>\
            Please make sure you change the permissions back to "755" for safety.\
            Also on images folder you can change the existing images to your own logos<br><br>\
    You will informed by email, about activation progress of your library.<br><br>\
    For now, you create the first library account, which will be administrator/owner\
    account.<br> To visit your SmartLib\'s Webpage<br>\
    <div id="install-form4"><br><br>\
            <a href="../" target="_parent" class="nodec">\
            <button class="sendButton" type="button"\
            >Click Here\
            </button></a>\
            </div>');
}

</script>
</div>
</article>
</div>


</div>
<br><br><br><br>
<footer class="clearfix">
    <div id="downFooter">
        <a id="deptImage" href="http://www.dsml.cs.ucy.ac.cy"
           class="imageHandle" target="_blank">
            <img align="left"
                 src="../images/department.png"/>
        </a>
        <a id="orgImage" href="http://www.cs.ucy.ac.cy"
           class="imageHandle" target="_blank">
            <img align="left"
                 src="../images/organization.png"/>
        </a>

        <p class=copyright>
            Copyright &copy; 2012 <a class="nodec" href=""
                                     target="_blank">Data Management Systems Laboratory
        </a>
            <br><a class="nodec" href="http://www.cs.ucy.ac.cy"
                   target="_blank">University of Cyprus
        </a></p>
    </div>


</footer>
</body>
</html