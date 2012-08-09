    <?php

////////// PLEASE FILL ALL BELOW INFORMATION ////////////

// The name of the database
define('DB_NAME', 'dbname'); 
//Host of the database
define('DB_HOST', 'dbserver'); 
//Dont change this - Automatically generated
define('DB_DSN','mysql:host='.DB_HOST.';dbname='.DB_NAME);
//DB Username
define('DB_USER', 'dbuser');
// Db password
define('DB_PASSWORD', 'dbpass');

//Full name of library
define('_FULL_NAME', 'Full Name');
//Short Name
define('_NAME', 'Name');
//Address of library! Seperated with <br> !!
define('_ADDRESS',
'Line one<br>
Line two<br>
Line Three<br>
One more line<br>'
);
//Full telephone
define('_TELEPHONE', '+xxx-xx-XXXXXXXX');
//Fax - Optional
define('_FAX', '+xx-xx-XXXXXXXX');
//Email
define('_EMAIL', 'email@yourdomain');
//Department URL
define('_DEPARTMENT_URL', 'http://www.URL/department');
//Organization URL
define('_ORGANIZATION_URL', 'http://www.URL');
//Key defined for safety in some webpages
define('_MY_KEY', 'a_key');

//Salt for database password protection
define('_SALT', 'salt_for_db_pass');

//Pepper for database password protection
define('_PEPPER', 'pepper_for_db_pass');

?>