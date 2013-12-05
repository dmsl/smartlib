
<!-- Include the Header -->
<?php include 'header.php'; ?>
<!-- CHECK Show a map of CS Building using Google Maps -->
<script
	type="text/javascript"
	src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script
	type="text/javascript" src="js/jquery.gmap.min.js"></script>
<script
	type="text/javascript" src="js/custom.js"></script>


<script type="text/javascript">
    jQuery(document).ready(function(){ 
		$('#menu li').eq(1).attr('id', 'menu_active')
	}); 
</script>

<div id="map"></div>

<?php

$name = $_REQUEST['name'];
$email = $_REQUEST['email'];
$message = $_REQUEST['message'];

//If all columns are set
if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['message'])) {
	
	//Need todo this CHECK? not empty fields!!
	if ($name != '' && $email != '' & $message != '') {
		//TODO change this!			
		$to = "gabi_gew@hotmail.com";
		$subject = "Smartscan Email";
		//TODO put subject too!
		$body = "Name: ". $name . "\n Email: " . $email. "\n Message: ". $message."\n";
		//Send mail to our mail system!
		if (mail($to, $subject, $body)) {
			echo "<div class='success'>Succesfully Send Email</div>";
		} else {
			echo "<div class='error'>Server error. Cannot send email</div>";
		}
	}
	else {
		echo "<div class='error'>Please fill all the required fields!</div>";
	}
}
?>

<center>
	<div id="contact">

		<p>
			Project Implementation: <a href="mailto:gkoupe01@cs.ucy.ac.cy">Gabriela
				Koupepidou</a>
		</p>
		<p>
			Project Supervision:<a href="http://www2.cs.ucy.ac.cy/~dzeina/"
				target="_blank">Dr. Demetris Zeinalipour</a>
		</p>
		<p>
			Data Management Systems Laboratory <br> Dept. of Computer Science <br>
			University of Cyprus <br> P.O. Box 20537 <br> 1678 Nicosia, CYPRUS <br>
		</p>

		<p>
			Email: <a href="mailto:dmsl@cs.ucy.ac.cy">dmsl@cs.ucy.ac.cy</a><br>
			Tel: +357-22-892755<br> Fax: +357-22-892701
		</p>
	</div>
	<!-- The Contact Form -->
	<div id="sendEmail">
		<form id="ContactForm" method="post">
			<center>
				<table id="register" cellspacing="5px" border="10">

					<tr>
						<td>Name:</td>
						<td><div class="bg">
								<input type="text" class="input" name="name" size="30">
							</div></td>
					</tr>

					<tr>
						<td>Email:</td>
						<td><div class="bg">
								<input type="text" class="input" name="email" size="30">
							</div></td>
					</tr>

					<tr>
						<td>Message:</td>
						<td height="200"><div class="bg">
								<textarea name="message" cols="5" rows="30"></textarea>
							</div></td>
					</tr>


					<tr>
						<td colspan="2" align="right"><input type="submit" class="button"
							value="Send"></td>
					</tr>

				</table>
			</center>

		</form>
		

	</div>

</center>

<!-- Include the Footer -->
<?php include 'footer.php'; ?>