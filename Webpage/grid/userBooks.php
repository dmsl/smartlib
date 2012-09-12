<!-- 
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
    
    
    -->
 	

<table id="userBooksList"></table>
<div id="userBooksPager"></div>
<div id="filter" style="margin-left:30%;display:none">Search Invoices</div>
<!--<input type="BUTTON" id="bedata" value="Edit Selected" />-->

<script type="text/javascript">

	jQuery(document).ready(function(){
    var lastsel2
	jQuery("#userBooksList").jqGrid({

		url:'grid/server.php?q=userbooks',

		height:"auto",
//width:"auto",
		datatype: "json",
		shrinktofit:false,
		
		autowidth:true,

		colNames:['Cover','Title','Authors','Published','Pages','Language', 'Status','ISBN'],

		colModel:[

			{name:'imgURL',index:'imgURL', width:50, align:"center", search:false 
			,editoptions:{readonly:true}, formatter:imageFunction,
			sortable:false},

			{name:'title',index:'title', width:130 , align:"center",
			cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;"' } },
			
			{name:'authors',index:'authors', width:80, align:"center", formatter:formatAuthors,
			cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;"' } },

			{name:'publishedYear',index:'publishedYear', width:50, align:"center", search:false ,
			sortable:false},		

			{name:'pageCount',index:'pageCount', width:50,align:"center", search:false, sortable:false,
						  }
			,		

			{name:'lang',index:'lang', width:50, align:"center", search:false, editable:false,sortable:false }	
			,{name:'status',index:'status', width:70, align:"center", formatter:formatStatus,
			editable: true,edittype:"select", search:false 
			,editoptions:{value:"0:Available;-1:Not Available;-2:Other - Not Avail"}}
			,
			{name:'isbn',index:'isbn', width:150, align:"center"}
			
		],
		
		
		
		
		mtype: 'POST',
		
		editurl: "grid/editBooks.php",
		
		rowNum:5,
		
	//	rowList:[10,20],

		pager: '#userBooksPager',

		sortname: 'title',

		viewrecords: true,
		gridview : true,
		sortorder: "desc"

	//	caption: "Library Books"

	});

	jQuery("#userBooksList").jqGrid('navGrid','#userBooksPager',{edit:false,add:false,del:false,search:false,
	refresh:false});
	
/*
$("#bedata").click(function(){
	var gr = jQuery("#userBooksList").jqGrid('getGridParam','selrow');
	if( gr != null ) jQuery("#userBooksList").jqGrid('editGridRow',gr,{height:280,reloadAfterSubmit:false}
	,{afterSubmit:true});
	else alert("Please Select Row");
});*/


jQuery("#userBooksList").jqGrid('navButtonAdd',"#userBooksPager",{caption:"Clear",title:"Clear Search",buttonicon :'ui-icon-refresh',
	onClickButton:function(){

    var grid = $("#userBooksList");
    grid.jqGrid('setGridParam',{search:false});
	
	grid.jqGrid('setGridParam', { postData: { filters: null} });
	$("#gs_title").val("");
	$("#gs_authors").val("");
	$("#gs_isbn").val("");
	

    var postData = grid.jqGrid('getGridParam','postData');
    $.extend(postData,{filters:""});
    // for singe search you should replace the line with
    // $.extend(postData,{searchField:"",searchString:"",searchOper:""});

    grid.trigger("reloadGrid",[{page:1}]);
		
		
	} 
});

jQuery("#userBooksList").jqGrid('filterToolbar');


/*
loadComplete: function () {
   var rowIds = $("#userBooksList").jqGrid('getDataIDs');

    $.each(rowIds, function (i, row) {
       var allRowsInGrid = $("#userBooksList").getRowData(row);

       if (rowData.status == "1") {
          $("#userBooksList").jqGrid('restoreRow', row);
		  
		  
                var cm = $("#userBooksList").jqGrid('getColProp', 'status');
				
				cm.editable=false;
              //  cm.edittype = 'select';
              //  cm.editoptions = { value: "1:A; 2:B; 3:C" };
             //   $("#userBooksList").jqGrid('editRow', row);
               // cm.edittype = 'text';
              //  cm.editoptions = null;
       }
	   else {
		    var cm = $("#userBooksList").jqGrid('getColProp', 'status');
             //   cm.edittype = 'select';
			 cm.editable=true;
		   // cm.editoptions = {value:"0:Available;-1:Not Available;-2:Other - Not Avail"};
		  // }
   });
}
*/

	
	function imageFunction(cellvalue, options, rowObject) 
	{
		return  "<img src='" + cellvalue + "' width='80px' height='80px' />";
	};


	});
	/*
	var timeoutHnd;
	
	function doSearch(ev){
		if(timeoutHnd)
			clearTimeout(timeoutHnd)
		timeoutHnd = setTimeout(gridReload,500)
	}
	
	function gridReload(){
		var cd_mask = jQuery("#search_cd").val();
		jQuery("#userBooksList").jqGrid('setGridParam',{url:"server.php?page=1&limit=10&searchString="+ cd_mask  +"&rows=10"}).trigger("reloadGrid");
	}*/
	
	
	function formatAuthors(cellvalue, options, cellObject) {

	return  cellvalue.replace(new RegExp(", "),",</br>")
}


function formatStatus(cellvalue, options, cellObject) {
	switch (cellvalue){
		case "1":  return 'Rented' 
		case "0":  return 'Available'
		case "-1":  return 'Not Available'
		case "-2":  return 'Other'
		
		}


	
}



function unformatAuthors(cellValue, options, cellObject) {
    return $(cellObject.html()).attr("originalValue");
}

	
	
//	$('#search').submit(function () {
//		
//				jQuery("#userBooksList").jqGrid('setGridParam',{url:"server.php?page=1&limit=10&searchString="+ cd_mask  +"&rows=10"}).trigger("reloadGrid");
//		return false;
//	});

</script>
