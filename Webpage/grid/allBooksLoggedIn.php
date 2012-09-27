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



<div id="search-panel">
    <button type="button" value="" id="toggleAdvancedSearchButtonID" class="toggleSearchButton"
            >Advanced Search
    </button>
    <form id="simpleSearchLoggedIn" action="#" method="post">
        <input type="text" class="input" id="search_cd_loggedIn"/>

        <button type="submit" value="" id="simpleSearchButtonID" class="searchButton">Search</button>

        <button type="reset" value="" id="clearButtonID"
                class="clearButton">Clear
        </button>
    </form>

</div>


<!--<form id="simpleSearch" action="#" method="post">-->
<!--<!--    <div class="bg">-->-->
<!---->
<!--        <input type="text" class="input" id="search_cd" onkeydown="doSimpleSearch(arguments[0]||event)">-->
<!--        <input type="submit" class="submit" value="">-->
<!--<!--    </div>-->-->
<!--</form>-->






<table id="allBooksLoggedInList">
    <tr>
        <td/>
    </tr>
</table>
<div id="allBooksLoggedInPager"></div>
<!--<div id="filter" style="margin-left:30%;display:none">Search Invoices</div>-->







<script type="text/javascript">

jQuery(document).ready(function () {

    // Grid Table
    jQuery("#allBooksLoggedInList").jqGrid({
        url:'grid/server.php?q=allbooksloggedin',
        datatype:"json",
        mtype:"POST",
        height:"auto",
        //width:"auto",

        shrinktofit:false,
        autowidth:true,

        //Header layer
        colNames:['Cover', 'Title', 'Authors', 'Published', 'Pages', 'Owner', 'Status', 'Language', 'ISBN'],

        //Body Layer
        colModel:[

            {name:'imgURL', index:'imgURL', width:80, align:"center", search:false, editoptions:{readonly:true}, formatter:imageFunction,
                sortable:false},

            {name:'title', index:'title', width:200, align:"center",
                cellattr:function (rowId, tv, rawObject, cm, rdata) {
                    return 'style="white-space: normal;"'
                } },

            {name:'authors', index:'authors', width:100, align:"center", formatter:formatAuthors,
                cellattr:function (rowId, tv, rawObject, cm, rdata) {
                    return 'style="white-space: normal;"'
                } },

            {name:'publishedYear', index:'publishedYear', width:70, align:"center", search:false,
                sortable:false},

            {name:'pageCount', index:'pageCount', width:70, align:"center", search:false, sortable:false},

            {name:'owners', index:'owners', width:70, align:"center", search:false, sortable:false},
            {name:'status', index:'status', width:70, align:"center", search:false, sortable:false,
                formatter:formatStatus},

            {name:'lang', index:'lang', width:70, align:"center", editable:false, sortable:false }    ,

            {name:'isbn', index:'isbn', width:160, align:"left"
            }
        ],

////            TODO is this necessary?
//        jsonReader:{
//            imgURL:"imgURL",
//            title:"title",
//            authors:"authors",
//            publishedYear:"publishedYear",
//            pageCount:"pageCount",
//            owners:"owners",
//            status:"status",
//            lang:"lang",
//            isbn:"isbn"
//        },

        //	editurl: "server.php",

        rowNum:5,


        sortname:'title',

        viewrecords:true,

        sortorder:"asc",


        pager:'#allBooksLoggedInPager',
        gridview:true

//            caption: "All BOoks"


    });

    // Grid Table
    //Attach a pager
    jQuery("#allBooksLoggedInList").jqGrid('navGrid', '#allBooksLoggedInPager',
            {edit:false, add:false, del:false, search:false, refresh:false});


//    // Grid Table
//    //Add clear button to Navigation TODO MOVE THIS CLEAR BUTTON!
//    jQuery("#allBooksLoggedInList").jqGrid('navButtonAdd', "#allBooksLoggedInPager", {caption:"Clear", title:"Clear Search", buttonicon:'ui-icon-refresh',
//        onClickButton:function () {
//
//
//            var grid = $("#allBooksLoggedInList");
//
//            grid.jqGrid('setGridParam', {search:false});
//
//            grid.jqGrid('setGridParam', { postData:{ filters:null} });
//
//            //Clear search fields TODO move this outside
//            $("#gs_title").val("");
//            $("#gs_authors").val("");
//            $("#gs_isbn").val("");
//
//
//            var postData = grid.jqGrid('getGridParam', 'postData');
//
//
//            $.extend(postData, {filters:""});
//            // for singe search you should replace the line with
//            // $.extend(postData,{searchField:"",searchString:"",searchOper:""});
//
//            grid.trigger("reloadGrid", [
//                {page:1}
//            ]);
//
//        }
//    });

    //TODO Grid Table TODO is the INLINE SEARCH Move this!!
//    jQuery("#allBooksLoggedInList").jqGrid('filterToolbar');


    /*	TODO CHECK FOR RM!
    jQuery("#allBooksLoggedInList").jqGrid('navGrid','#allBooksLoggedInPager',{edit:false,add:false,del:false,search:true});

    */


    function imageFunction(cellvalue, options, rowObject) {
        return  "<img src='" + cellvalue + "' width='80px' height='80px' />";
    }


    //Swap to Advanced Search
    $('#toggleAdvancedSearchButtonID').click(function () {


//    $("").animate({
//            opacity:'0.0',5000
//});
//

        $("#search-panel input, #clearButtonID, #simpleSearchButtonID").animate({
                    opacity:'0.0'
                }
                , "normal"

        );


        $(".toggleSearchButton").animate({
//        opacity:'0.5',
            top:'-=40px'
        }, "slow", function () {


            $('#simpleSearchLoggedIn').replaceWith(
                    '<form id="advancedSearchLoggedIn" action="#" method="post">\
                    <label>&nbsp&nbspTitle: \
                    <input type="text" class="input" id="advsearch_cd_loggedIn_title" /></label> \
                    <label>Authors: \
                    <input type="text" class="input" id="advsearch_cd_loggedIn_authors" /></label> \
                    <label>&nbsp&nbspOwner: \
                    <input type="text" class="input" id="advsearch_cd_loggedIn_owner" /></label> \
                    <label>&nbsp&nbsp&nbspISBN: \
                    <input type="text" class="input" id="advsearch_cd_loggedIn_isbn" /></label> \
                    <button type="submit" value="" class="searchButton">Search</button>\
                    <button type="reset" value="" id="advclearButtonID"\
                    class="clearButton">Clear</button>\
                    </form>\
                    '
            );

            //Toggle button
            $('#toggleAdvancedSearchButtonID').replaceWith(
                    '<button type="button" value="" id="toggleSimpleSearchButtonID" class="toggleSearchButton"\
                   >Simple Search</button>'
            )

//        $('#toggleAdvancedSearchButtonID').


            $('#advancedSearchLoggedIn').css({visibility:"hidden"});

        });


    });


    $('#simpleSearchLoggedIn').submit(function () {

        doSearch();

        return false;
    });


//Search in Grid
    function doSearch() {

        var grid = $('#allBooksLoggedInList');
        //We will make search
        grid.jqGrid('setGridParam', {search:true});

        var cd_mask = jQuery('#search_cd_loggedIn').val();

        var postData = grid.jqGrid('getGridParam', 'postData');
        $.extend(postData, {searchString:cd_mask});

        gridReload();

    }


    //Clear the search
    $('#clearButtonID').click(function () {
        $("#search_cd_loggedIn").val("");
        doSearch();
        // gridReload();
        return false;
    });


    function gridReload() {

        $('#allBooksLoggedInList').trigger("reloadGrid");
//    jQuery("#allBooksLoggedInList").jqGrid('setGridParam', {url:"grid/server.php?q=allbooksloggedin"}).trigger("reloadGrid");
//   jQuery("#allBooksLoggedInList").jqGrid('setGridParam', {url:"grid/server.php?q=allbooksloggedin&_search=true&page=1&limit=10&searchString=" + cd_mask + "&rows=10"}).trigger("reloadGrid");
        //jQuery("#allBooksLoggedInList").jqGrid('setGridParam',{url:"server.php?page=1&limit=10&searchString="+ cd_mask + "nm_mask="+nm_mask ,page:1}).trigger("reloadGrid");
    }


// Format authors to be seperated with comma
    function formatAuthors(cellvalue, options, cellObject) {

        return  cellvalue.replace(new RegExp(", "), ",</br>");
    }


    function unformatAuthors(cellValue, options, cellObject) {
        return $(cellObject.html()).attr("originalValue");
    }


//Formats the status of the book
    function formatStatus(cellvalue, options, cellObject) {
        switch (cellvalue) {
            case "1":
                return 'Rented'
            case "0":
                return 'Available'
            case "-1":
                return 'Not Available'
            case "-2":
                return 'Other - NA'

        }
    }

//TODO FIX THIS WHEN SEARCH FINISHED!
//    	$('#search').submit(function () {
//            gridReload();
//
////    				jQuery("#allBooksLoggedInList").jqGrid('setGridParam',{url:"server.php?page=1&limit=10&searchString="+ cd_mask  +"&rows=10"}).trigger("reloadGrid");
////    		return false;
//    	});


}); //END OF DOCUMENT READY FUNCTION

</script>