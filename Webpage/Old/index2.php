<!--  
CHECK !
This is NOT logged in page.
Propably is ONLY for the tables. 
And the only diff is here it shows rate results , and in index_after_login 
you can change the rating of the book.
-->

<p></p>

<center>
	<!--  
CHECK !
WHAT is list2?
-->
	<table id="list2">

	</table>
	<img src='' />

</center>

<script type="text/javascript">
	// TODO learn about below function
	jQuery(document).ready(function(){
		
	//pale kate me to list2
	//Show the Grid with the Book Records
	jQuery("#list2").jqGrid({
		// TODO change that url. CHECK server.php also!
		url:'../smartscan/server.php?q=2',

		height:"auto",
		//why not??? CHECK 
		//autowidth:true,

		datatype: "json",
		//change column names!! TODO 
		colNames:[/*'Username',*/'Image','ISBN', 'Title', 'Authors','Published Year','Page Count','Rate'],

		colModel:[

			{name:'image',index:'image', width:120, align:"center", search:false ,editoptions:{readonly:true}, formatter:imageFunction}	,

			{name:'isbn',index:'isbn', width:130, align:"center", valign:"middle",editoptions:{readonly:true}},

			{name:'title',index:'title', width:250 , align:"left",editoptions:{readonly:true}},

			{name:'authors',index:'authors', width:180, align:"left",editoptions:{readonly:true}},

			{name:'publishedYear',index:'publishedYear', width:120, align:"center",editoptions:{readonly:true}},		

			{name:'pageCount',index:'pageCount', width:100,align:"center", search:false,editoptions:{readonly:true}},		

			{name:'rate',index:'rate', width:130, align:"center", search:false ,editoptions:{readonly:true}}			
						

		],
		//INFO Poses grammes 8eloune na vlepoume stin selida
		rowNum:10,
		//INFO epilogi, gia to posa posa 8elume na fenonde ta results. 
		rowList:[10,20],

		pager: '#pager2',
		//Default sort column! CHANGE to book name
		sortname: 'isbn',

		viewrecords: true,

		sortorder: "desc",
		//INFO Title of the grid
		caption:"Books"

	});

	//CHECK what is this? ??????????
	jQuery("#list2").jqGrid('navGrid','#pager2',{edit:false,add:false,del:false});
	
	function imageFunction(cellvalue, options, rowObject) 
	{
		return  "<img src='" + cellvalue + "' width='80px' height='80px' />";
	};

	});// End of jQuery Function

	var timeoutHnd;
	//######### Function: doSearch #########
	//CHECK what is this? ??????????
	//reloading if a 500 seconds passed???
	function doSearch(ev){
		if(timeoutHnd)
			clearTimeout(timeoutHnd);
		timeoutHnd = setTimeout(gridReload,500);
	}

	//######### Function: gridReload #########
	function gridReload(){
		var cd_mask = jQuery("#search_cd").val();
		
		jQuery("#list2").jqGrid('setGridParam',
				{url:"search.php?page=1&limit=10&searchString="+ cd_mask  +"&rows=10"})
				.trigger("reloadGrid");
	}
	//CHECK what is this? ??????????
	$('#search').submit(function () {
		gridReload();
		return false;
	});
</script>

<p></p>

