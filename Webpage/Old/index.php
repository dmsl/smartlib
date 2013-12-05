
<!-- Index Webpage -->
<!-- Include the Header of webpage -->
<?php include 'header.php'; ?>

<!-- Script for doing CHECK  -->
<script type="text/javascript">
		// TODO  
	    	jQuery(document).ready(function(){ 
				$('#menu li').eq(0).attr('id', 'menu_active')
				}
			); 

</script>

<!--  CHECK  -->
<?php 
//Show approprieate CHECK according to users status
// If user is not Logged In CHECK
if(!$username && !$password)
{
	# TODO What is index2????????
	# propably index2 is NOT-LOGGED IN index?
	//include Appropriate body TODO change this to body not login or something!!!
	include 'index2.php';
}

//User is Logged In
else

{
	#TODO  logged in index??????
	include 'index_afterLogin.php';
}

?>
<!-- Move staff from footer in here! -->
<!-- Include the Footer of webpage -->
<?php include 'footer.php'; ?>