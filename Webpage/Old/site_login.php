<?php include 'header.php'; ?>

<script type="text/javascript">
    jQuery(document).ready(function(){ 
		$('#menu li').eq(4).attr('id', 'menu_active')
	}); 
</script>

<?php


//INFO: display login form
function index(){


	echo "<form id='ContactForm' action='?act=login' method='post'><center><table id='login'>"

	."<tr><td>Username:</td> <td><div class='bg'><input type='text' name='username' size='30'></div></td></tr>"

	."<tr><td>Password:</td> <td><div class='bg'><input type='password' name='password' size='30'></div></td></tr>"

	."<tr><td colspan=2 align ='right'><input type='submit' class='button' value='Login'></td></tr>"

	."</table></center>"

	."</form>";


}




//INFO: Login to database

function login(){



	//Collect your info from login form
	$username = $_REQUEST['username'];
	$password = $_REQUEST['password'];


	//Connecting to database
	$connect = mysql_connect("dbserver.cs.ucy.ac.cy:3306","smartscan","UZaXYqXBdKX6crmq");

	if(!$connect)
	{
		die("Connection Error: " . mysql_error());
	}

	//Selecting database
	$database="smartscan";
	mysql_select_db($database) or die("Error conecting to db.");


	//INFO validating below seems a mess!! Fix those!
	//Find if entered data is correct

	$result = mysql_query("SELECT user_id,username,password,email FROM USER WHERE username='$username' AND password='$password'");

	$row = mysql_fetch_array($result);

	$id = $row['user_id'];

	$select_user = mysql_query("SELECT user_id,username,password,email FROM USER WHERE user_id='$id'");

	$row2 = mysql_fetch_array($select_user);

	$user = $row2['username'];
	//TODO move null checks above! before the queries!!!!!!!!!
	if($username == null){
		if($password==null)
			die("Please insert a username and  password!");
		else
			die("Please insert a username");
	}

	if($username != $user){
		if($password==null)
			die("Please insert a password");
		else
			die("Username is wrong!");
	}



	$pass_check = mysql_query("SELECT user_id,username,password,email FROM USER WHERE username='$username' AND user_id='$id'");



	$row3 = mysql_fetch_array($pass_check);
	$email = $row3['email'];



	$select_pass = mysql_query("SELECT user_id,username,password,email FROM USER WHERE username='$username' AND user_id='$id' AND email='$email'");



	$row4 = mysql_fetch_array($select_pass);

	$real_password = $row4['password'];


	//Move this above too
	if($username!=null and $password==null){

		die("Please insert a username and a password!");
	}



	if($password != $real_password){

		die("Your password is wrong!");

	}


	//OMG! CHECK!
	//Now if everything is correct let's finish his/her/its login

	$_SESSION['username'] = $username;
	$_SESSION['password'] = $password;

	$ok=1;
	//CHECK CHANGE
	header("Location: http://www.cs.ucy.ac.cy/thesis/smartscan/index.php");
	 
	?>
<script type="text/javascript">
   window.location.replace("http://www.cs.ucy.ac.cy/thesis/smartscan/index.php");
   </script>
<?
exit();

}

$act = $_REQUEST['act'];


switch($act){


	default;

	index();

	break;


	case "login";

	login();

	break;



}



?>

<?php include 'footer.php'; ?>