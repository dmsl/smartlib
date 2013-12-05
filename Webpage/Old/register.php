<?php include 'header.php'; ?>

<!-- CHECK what JS below does???? -->
<script type="text/javascript">
    jQuery(document).ready(function(){ 
		$('#menu li').eq(3).attr('id', 'menu_active')
	}); 
</script>
<?php

//INFO This function displays the registration form
function register_form(){


	//$date = date('D, M, Y');


	echo "<form id='ContactForm' action='?act=register' method='post'><center><table id='register' cellspacing='5px'>"

	."<tr><td>Name:</td>					<td><div class='bg'><input type='text' class='input' name='name' size='30'></div></td></tr>"

	."<tr><td>Surname:</td>				<td><div class='bg'><input type='text' class='input' name='surname' size='30'></div></td></tr>"

	."<tr><td>Username:</td>			<td><div class='bg'><input type='text' class='input' name='username' size='30'></div></td></tr>"

	."<tr><td>Password:</td>			<td><div class='bg'><input type='password' class='input' name='password' size='30'></div></td></tr>"

	."<tr><td>Confirm your password:</td> <td><div class='bg'><input type='password' class='input' name='password_conf' size='30'></div></td></tr>"

	."<tr><td>Email:</td>				<td><div class='bg'><input type='text' class='input' name='email' size='30'></div></td></tr>"

	//."<input type='hidden' name='date' value='$date'>"


	."<tr><td colspan='2' align='right'><input type='submit' class='button' value='Register'></td></tr>"

	."</table></center>"

	."</form>";


}


//INFO This function registers users data

function register(){

	//Connecting to database
	//TODO make this AIO place!
	$connect = mysql_connect("dbserver.cs.ucy.ac.cy:3306","smartscan","UZaXYqXBdKX6crmq");

	if(!$connect){
		die("Connection Error: " . mysql_error());
	}

	//Selecting database
	$database="smartscan";
	mysql_select_db($database) or die("Error conecting to db.");

	//TODO put password strong checker
	//TODO show info about password encryption
	//TODO show image verification!
	
	//Collecting info
	$name = $_REQUEST['name'];
	$surname = $_REQUEST['surname'];
	$username = $_REQUEST['username'];
	$password = $_REQUEST['password'];
	$pass_conf = $_REQUEST['password_conf'];
	$email = $_REQUEST['email'];

	//$date = $_REQUEST['date'];


	//Here we will check do we have all inputs filled

	if(empty($name)){

		die("Please enter your name!<br>");

	}

	if(empty($surname)){

		die("Please enter your surname!<br>");

	}

	if(empty($username)){

		die("Please enter your username!<br>");

	}


	if(empty($password)){

		die("Please enter your password!<br>");

	}


	if(empty($pass_conf)){

		die("Please confirm your password!<br>");

	}


	if(empty($email)){

		die("Please enter your email!");

	}


	//INFO check username uniqueness
	$user_check = mysql_query("SELECT username FROM USER WHERE username='$username'");
	$do_user_check = mysql_num_rows($user_check);


	//INFO check email uniqueness
	$email_check = mysql_query("SELECT email FROM USER WHERE email='$email'");
	$do_email_check = mysql_num_rows($email_check);


	//Now display errors
	if($do_user_check > 0){
		die("Username is already in use!<br>");
	}


	if($do_email_check > 0){
		//TODO smthng else: You are already registered/Reset your password!
		die("Email is already in use!");
	}


	//INFO confirm users password
	if($password != $pass_conf){
		die("Passwords don't match!");

	}

	//TODO change staff below!
	//If everything is okay let's register this user
	$insert = mysql_query("INSERT INTO USER (name, surname, username, password, email) VALUES ('$name', '$surname','$username', '$password', '$email')");

	if(!$insert){
		die("There's a problem: ".mysql_error());

	}


	echo $username.", you are now registered. Thank you!<br><a href=site_login.php>Login</a> | <a href=index.php>Index</a>";


}

$act = $_REQUEST['act'];

switch($act){

//TODO check this switch statement!!!!!!!
	default;

	register_form();

	break;


	case "register";

	register();

	break;

}
?>

<?php include 'footer.php'; ?>