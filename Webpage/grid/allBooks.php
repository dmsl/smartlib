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
 

<table id="allBooksList"></table>

<div id="allBooksPager"></div>

  <script type="text/javascript">

	jQuery(document).ready(function(){
    var lastsel2
	jQuery("#allBooksList").jqGrid({

		url:'grid/server.php?q=allbooks&rows=5',

		height:"auto",
//width:"auto",
		datatype: "json",
		shrinktofit:false,
		autowidth:true,

		colNames:['Cover','Title','Authors','Published','Pages','Language', 'ISBN'],

		colModel:[

			{name:'imgURL',index:'imgURL', width:80, align:"center", search:false 
			,editoptions:{readonly:true}, formatter:imageFunction,
			sortable:false},

			{name:'title',index:'title', width:200 , align:"center",
			cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;"' } },
			
			{name:'authors',index:'authors', width:100, align:"center", formatter:formatAuthors,
			cellattr: function (rowId, tv, rawObject, cm, rdata) { return 'style="white-space: normal;"' } },

			{name:'publishedYear',index:'publishedYear', width:70, align:"center",
			sortable:false},		

			{name:'pageCount',index:'pageCount', width:70,align:"center", search:false, sortable:false},		

			{name:'lang',index:'lang', width:70, align:"center", search:false, editable:false,sortable:false }	,	

			{name:'isbn',index:'isbn', width:160, align:"center",
			}
		],
		
	//	editurl: "server.php",
		
		rowNum:5,

	//	rowList:[10,20],

		pager: '#allBooksPager',

		sortname: 'title',

		viewrecords: true,

		sortorder: "desc",

	//	caption: "Library Books"

	});

	jQuery("#allBooksList").jqGrid('navGrid','#allBooksPager',{edit:false,add:false,del:false,search:false});
	


	
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
		jQuery("#allBooksList").jqGrid('setGridParam',{url:"server.php?page=1&limit=10&searchString="+ cd_mask  +"&rows=10"}).trigger("reloadGrid");
	}
	
	
	function formatAuthors(cellvalue, options, cellObject) {

	return  cellvalue.replace(new RegExp(", "),",</br>")
}


function unformatAuthors(cellValue, options, cellObject) {
    return $(cellObject.html()).attr("originalValue");
}

	
	
//	$('#search').submit(function () {
//		
//				jQuery("#allBooksList").jqGrid('setGridParam',{url:"server.php?page=1&limit=10&searchString="+ cd_mask  +"&rows=10"}).trigger("reloadGrid");
//		return false;
//	});

</script>