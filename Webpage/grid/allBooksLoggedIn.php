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


<table id="s3list"></table>
<div id="s3pager"></div>
<div id="filter" style="margin-left:30%;display:none">Search Invoices</div>







  <script type="text/javascript">

	jQuery(document).ready(function(){
    var lastsel2
	jQuery("#s3list").jqGrid({

		url:'grid/server.php?q=allbooksloggedin',

		height:"auto",
//width:"auto",
		datatype: "json",
		shrinktofit:false,
		autowidth:true,

		colNames:['Cover','Title','Authors','Published','Pages','Owner','Status','Language', 'ISBN'],

		colModel:[

			{name:'imgURL',index:'imgURL', width:80, align:"center", search:false
			,editoptions:{readonly:true}, formatter:imageFunction,
			sortable:false},

			{name:'title',index:'title', width:200 , align:"center",
			cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;"' } },

			{name:'authors',index:'authors', width:100, align:"center", formatter:formatAuthors,
			cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;"' } },

			{name:'publishedYear',index:'publishedYear', width:70, align:"center", search:false,
			sortable:false},

			{name:'pageCount',index:'pageCount', width:70,align:"center", search:false, sortable:false},

			{name:'owners',index:'owners', width:70,align:"center", search:false, sortable:false},
			{name:'status',index:'status', width:70,align:"center", search:false, sortable:false,
			 formatter:formatStatus},

			{name:'lang',index:'lang', width:70, align:"center", search:false, editable:false,sortable:false }	,

			{name:'isbn',index:'isbn', width:160, align:"left",
			}
		],

	//	editurl: "server.php",

		rowNum:5,


		sortname: 'title',

		viewrecords: true,

		sortorder: "desc",

   	mtype: "POST",

   	pager: '#s3pager',
	gridview : true,


	});





jQuery("#s3list").jqGrid('navGrid','#s3pager',{edit:false,add:false,del:false,search:false,refresh:false});

jQuery("#s3list").jqGrid('navButtonAdd',"#s3pager",{caption:"Clear",title:"Clear Search",buttonicon :'ui-icon-refresh',
	onClickButton:function(){


    var grid = $("#s3list");
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

jQuery("#s3list").jqGrid('filterToolbar');


/*	jQuery("#s3list").jqGrid('navGrid','#s3pager',{edit:false,add:false,del:false,search:true});

*/


	function imageFunction(cellvalue, options, rowObject)
	{
		return  "<img src='" + cellvalue + "' width='80px' height='80px' />";
	};


	});

	var timeoutHnd;

	function doSearch(ev){
		if(timeoutHnd)
			clearTimeout(timeoutHnd)
		timeoutHnd = setTimeout(gridReload,500)
	}

	function gridReload(){
		var cd_mask = jQuery("#search_cd").val();
		jQuery("#s3list").jqGrid('setGridParam',{url:"server.php?page=1&limit=10&searchString="+ cd_mask  +"&rows=10"}).trigger("reloadGrid");
	}


	function formatAuthors(cellvalue, options, cellObject) {

	return  cellvalue.replace(new RegExp(", "),",</br>")
}


function unformatAuthors(cellValue, options, cellObject) {
    return $(cellObject.html()).attr("originalValue");
}

	function formatStatus(cellvalue, options, cellObject) {
	switch (cellvalue){
		case "1":  return 'Rented'
		case "0":  return 'Available'
		case "-1":  return 'Not Available'
		case "-2":  return 'Other'

		}
	}

//	$('#search').submit(function () {
//
//				jQuery("#s3list").jqGrid('setGridParam',{url:"server.php?page=1&limit=10&searchString="+ cd_mask  +"&rows=10"}).trigger("reloadGrid");
//		return false;
//	});

</script>