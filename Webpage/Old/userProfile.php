<!-- ITS MY BOOKS option from the menu! -->

<?php include 'header.php'; 
//CHECK get the username from the session! Where is session?
$username1 = $_SESSION['username'];
//books of user URL??? check what is my_search!!!
$url = "'booksOfUser.php?q=2&username=".$username1."'";
?>
<!-- CHECK this is show menu??? (Up menu) -->
<script type="text/javascript">
    jQuery(document).ready(function(){ 
		$('#menu li').eq(3).attr('id', 'menu_active');
	}); 
</script>


<p></p>

<center>

	<table id="user">

	</table>

	<img src='' />

</center>

<script type="text/javascript">

	jQuery(document).ready(function(){
	jQuery("#user").jqGrid({
		url: <?php echo $url ?>, // users URL!
		//editurl: '../smartscan/edit.php', 
		editurl: 'edit.php?q=2',  //CHECK why q=2?

		height:"auto",
		datatype: "json",
		
		//mtype: 'GET',
		colNames:['Image','ISBN', 'Title', 'Authors','Published Year','Page Count'],

		colModel:[
		
			{name:'image',index:'image', width:100, align:"center", search:false ,editoptions:{readonly:true}, formatter:imageFunction},

			{name:'isbn',index:'isbn', width:120, align:"center"},

			{name:'title',index:'title', width:200 , align:"left"},

			{name:'authors',index:'authors', width:240, align:"left"},

			{name:'publishedYear',index:'publishedYear', width:110, align:"center"},		

			{name:'pageCount',index:'pageCount', width:120,align:"center", search:false}

		],
		
		rowNum:10,

		rowList:[10,20],

		pager: '#pager2',

		sortname: 'isbn',

		viewrecords: true,

		sortorder: "desc",
		
		multiselect: true,
		//Grid Title!
		caption:"Your Books "

	});


	jQuery("#user").jqGrid('navGrid','#pager2',{
			edit:false,
			add:false,
			del:true,
			reloadAfterSubmit:true
		},
		
		{
			editData: { test: 'test'}
		},
		{
			editData: { test: 'test'}
		},
		{
			delData: { 
				isbn: function() {
					var grid = jQuery('#user');
					var sel_id = grid.getGridParam('selarrrow');;
					var isbn = [];
					$.each(sel_id, function(index, value) { 
  						var rowData = jQuery("#user").getRowData(value);
						var colData = rowData['isbn'];
						isbn.push(colData); 
					});
					console.log(isbn);
					return isbn;
				},
				uid: '<?php echo $username1 ?>'
			}
		}
	
	);
	
    	function imageFunction(cellvalue, options, rowObject) 
	{
		return  "<img src='" + cellvalue + "' width='80px' height='80px' />";
	};
	
	
	});
	

	
	function getCellValue(rowId, cellId) {
    	var cell = jQuery('#' + rowId + '_' + cellId);        
    	var val = cell.val();
    	return val;
	}

	var timeoutHnd;
	
	function doSearch(ev){
		if(timeoutHnd)
			clearTimeout(timeoutHnd)
		timeoutHnd = setTimeout(gridReload,500)
	}
	
	function gridReload(){
		var cd_mask = jQuery("#search_cd").val();
		jQuery("#user").jqGrid('setGridParam',{url:"my_search.php?page=1&limit=10&searchString="+ cd_mask  +"&username=<?php echo $username1 ?>&rows=10"}).trigger("reloadGrid");
	}

$('#search').submit(function () {
		gridReload();
		return false;
	});

</script>

<p></p>

<?php include 'footer.php'; ?>