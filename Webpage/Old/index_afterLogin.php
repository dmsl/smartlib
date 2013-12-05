
<?php
//Double check, if user has entered this page without being logged in
if(!$username && !$password)

{
	echo 'You cannot directly access this page';
	//TODO show another webpage! instead of only this message!
}
else
{
	//Show the webpage


	?>
<p></p>

<center>
	<!-- what is list 2??? CHECK IS THIS ALL NEEDED??  -->
	<table id="list2">
	</table>
	<img src='' />
</center>

<script type="text/javascript">

	jQuery(document).ready(function(){
		
    var lastsel2;
    
	jQuery("#list2").jqGrid({

		url:'../smartlib/server_afterLogin.php?q=2',

		height:"auto",

		datatype: "json",
		//INFO: now before the isbn it shows the username of the owner of the book!
		colNames:['Image','Username','ISBN', 'Title', 'Authors','Published Year','Page Count','Rate'],

		colModel:[

			{name:'image',index:'image', width:100, align:"center", search:false ,editoptions:{readonly:true}, formatter:imageFunction},	
			
			{name:'username',index:'username', width:80, align:"left"},

			{name:'isbn',index:'isbn', width:120, align:"center"},

			{name:'title',index:'title', width:260 , align:"left"},

			{name:'authors',index:'authors', width:150, align:"left"},

			{name:'publishedYear',index:'publishedYear', width:70, align:"center"},		

			{name:'pageCount',index:'pageCount', width:70,align:"center", search:false},		

			{name:'rate',index:'rate', width:170, align:"center", search:false, editable:false }		

		],
		// TODO change this. It rates based on star.php!
		gridComplete: function() {
			$('.tipsS').tipsy({ gravity: 'w', fade: true, html: true });
			$("[id^=book-]").each(function(index) {
				$(this).rating('star.php', {maxvalue:5, curvalue: $(this).attr('rate')});
			});
		},
		
		//######### Function: onSelectRow #########
		//TODO check this also!
		onSelectRow: function(rate){
			
		if(rate && rate!==lastsel2){
			jQuery('#list2').jqGrid('restoreRow',lastsel2);
			jQuery('#list2').jqGrid('editRow',rate,true);
			lastsel2=(rate+lastsel2)/2;
		}
	},
	
		editurl: "server.php",
		
		rowNum:10,

		rowList:[10,20],

		pager: '#pager2',

		sortname: 'isbn',

		viewrecords: true,

		sortorder: "desc",

		caption:"Books"

	});

	jQuery("#list2").jqGrid('navGrid','#pager2',{edit:false,add:false,del:false});
	
	function imageFunction(cellvalue, options, rowObject) 
	{
		return  "<img src='" + cellvalue + "' width='80px' height='80px' />";
	};


	});
	
	var timeoutHnd;
	
	function doSearch(ev){
		if(timeoutHnd)
			clearTimeout(timeoutHnd)
		timeoutHnd = setTimeout(gridReload,500);
	}
	
	//Here we do Search after the login!!
	function gridReload(){
		var cd_mask = jQuery("#search_cd").val();
		jQuery("#list2").jqGrid('setGridParam',
				{url:"search_after_login.php?page=1&limit=10&searchString="+
					cd_mask
					 +"&rows=10"}).trigger("reloadGrid");
	}
	
	$('#search').submit(function () {
		gridReload();
		return false;
	});

</script>

<p></p>

<?
//Close the else statement(from top)
}


?>

