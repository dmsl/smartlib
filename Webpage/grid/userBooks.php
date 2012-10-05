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
    <button type="button" value="" id="toggleSearchButton"
            class="toggleSearchButton">Advanced Search
    </button>
    <div id="simpleSearchLoggedIn">
        <input type="text" id="search_cd_loggedIn" value="" autofocus="autofocus"/>
    </div>
    <button type="button" id="searchButtonID"
            class="searchButton">Search
    </button>
    <button type="reset" id="clearButtonID"
            class="clearButton">Clear
    </button>
</div>







<table id="userBooksList">
    <tr>
        <td/>
    </tr>
</table>
<div id="userBooksPager"></div>


<script type="text/javascript">

jQuery(document).ready(function () {

    jQuery("#userBooksList").jqGrid({

        url:'grid/server.php?q=userbooks',

        height:"auto",
//width:"auto",
        datatype:"json",
        shrinktofit:false,

        autowidth:true,

        colNames:['Cover', 'Title', 'Authors', 'Published', 'Pages', 'Language', 'Status', 'ISBN'],

        colModel:[

            {name:'imgURL', index:'imgURL', width:50, align:"center", search:false, editoptions:{readonly:true}, formatter:imageFunction,
                sortable:false},

            {name:'title', index:'title', width:130, align:"center",
                cellattr:function (rowId, tv, rawObject, cm, rdata) {
                    return 'style="white-space: normal;"'
                } },

            {name:'authors', index:'authors', width:80, align:"center", formatter:formatAuthors,
                cellattr:function (rowId, tv, rawObject, cm, rdata) {
                    return 'style="white-space: normal;"'
                } },

            {name:'publishedYear', index:'publishedYear', width:50, align:"center", search:false,
                sortable:false},

            {name:'pageCount', index:'pageCount', width:50, align:"center", search:false, sortable:false
            }
            ,

            {name:'lang', index:'lang', width:50, align:"center", search:false, editable:false, sortable:false }
            ,
            {name:'status', index:'status', width:70, align:"center", formatter:formatStatus,
                editable:true, edittype:"select", search:false, editoptions:{value:"0:Available;-1:Not Available;-2:Other - Not Avail"}}
            ,
            {name:'isbn', index:'isbn', width:150, align:"center"}

        ],


        mtype:'POST',

        editurl:"grid/editBooks.php",

        rowNum:5,

        //	rowList:[10,20],

        pager:'#userBooksPager',

        sortname:'title',

        viewrecords:true,
        gridview:true,
        sortorder:"desc"


    });

    jQuery("#userBooksList").jqGrid('navGrid', '#userBooksPager', {edit:false, add:false, del:false, search:false,
        refresh:false});


    function imageFunction(cellvalue, options, rowObject) {
        return  "<img src='" + cellvalue + "' width='80px' height='80px' />";
    }

    ;


    var isAdv = 0;


    //Toggle Search modes
    $('#toggleSearchButton').click(function () {

        //Switch to Simple
        if (isAdv) {
            //Reload Grid
            $('#clearButtonID').click();

            $("#search-panel label, #clearButtonID, #searchButtonID").animate({
                opacity:'0.0'
            }, "normal");

            $("#toggleSearchButton").animate({
                top:'+=60px'
            }, "slow", function () {

                //Change css
                $('#search-panel').css({top:'-20px', marginBottom:"20px"});

                //Replace form
                $('#advancedSearchLoggedIn').replaceWith(
                        '<div id="simpleSearchLoggedIn">\
                        <input type="text" class="input" value="" id="search_cd_loggedIn" autofocus="autofocus" />\
                        </div>'
                );


                $('#toggleSearchButton').text("Advanced Search");

                //Hide& Unveil content
                $('#simpleSearchLoggedIn').hide();
                $('#simpleSearchLoggedIn').fadeIn(1000);

                $("#search-panel #clearButtonID, #searchButtonID").animate({
                    opacity:'1.0'}, "normal");

            });


            $("#advsearch_cd_loggedIn_title").val("");
            $("#advsearch_cd_loggedIn_authors").val("");
            $("#advsearch_cd_loggedIn_isbn").val("");
            isAdv = 0;//Switched
        }


        //Switch to advance
        else {
            //Reload Grid
            $('#clearButtonID').click();

            $("#search-panel input, #clearButtonID, #searchButtonID").animate({
                        opacity:'0.0'
                    }
                    , "normal"

            );


            $("#toggleSearchButton").animate({
                top:'-=40px'
            }, "slow", function () {

                //Change css
                $('#search-panel').css({top:'0px', marginBottom:"10px"});

                $(".toggleSearchButton").css({ top:'-=20px'});

                //Replace form
                $('#simpleSearchLoggedIn').replaceWith(
                        '<div id="advancedSearchLoggedIn">\
                        <label>&nbsp&nbspTitle: \
                        <input type="text" class="input" value="" id="advsearch_cd_loggedIn_title" autofocus="autofocus"/></label> \
                        <label>Authors: \
                        <input type="text" class="input" value="" id="advsearch_cd_loggedIn_authors" /></label> \
                        <label>&nbsp&nbsp&nbspISBN: \
                        <input type="text" class="input" value="" id="advsearch_cd_loggedIn_isbn" /></label> \
                        </div>\
                        '
                );


                //Toggle button to Simple
                $('#toggleSearchButton').html("Simple Search");


                //Hide & Unveil content
                $('#advancedSearchLoggedIn').hide();
                $('#advancedSearchLoggedIn').fadeIn(1000);

                $("#search-panel #clearButtonID, #searchButtonID").animate({
                    opacity:'1.0'}, "normal");


            });

            $("#search_cd_loggedIn").val("");


            isAdv = 1;//Switched


        }


    });


    var timeoutHnd;

    $("#search-panel input").live("keydown", function (event) {

        if (event.keyCode == 13) {
            doSearch();
        }

        //Do some smart search every half second
        else {

            if (timeoutHnd)
                clearTimeout(timeoutHnd)
            timeoutHnd = setTimeout(doSearch, 500)

        }

    });


    //Do an Advanced or a simple search
    function doSearch() {
        if (isAdv) {
            doAdvancedSearch();
        }
        else {
            doSimpleSearch();
        }
    }


    //Advanced Search
    function doAdvancedSearch() {

        var grid = $('#userBooksList');
        grid.jqGrid('setGridParam', {search:true});

        var cd_title = jQuery('#advsearch_cd_loggedIn_title').val();
        var cd_authors = jQuery('#advsearch_cd_loggedIn_authors').val();
        var cd_isbn = jQuery('#advsearch_cd_loggedIn_isbn').val();

        var postData = grid.jqGrid('getGridParam', 'postData');
        $.extend(postData, {
            title:cd_title,
            authors:cd_authors,
            isbn:cd_isbn
        });

        gridReload();

    }


    //Simple Search
    function doSimpleSearch() {

        var grid = $('#userBooksList');
        grid.jqGrid('setGridParam', {search:true});

        var cd_mask = jQuery('#search_cd_loggedIn').val();

        var postData = grid.jqGrid('getGridParam', 'postData');
        $.extend(postData, {searchString:cd_mask});

        gridReload();

    }


    //Clear the search
    $('#clearButtonID').click(function () {

        if (isAdv) {
            $("#advsearch_cd_loggedIn_title").val("");
            $("#advsearch_cd_loggedIn_authors").val("");
            $("#advsearch_cd_loggedIn_isbn").val("");

            doAdvancedSearch();
        }
        else {
            $("#search_cd_loggedIn").val("");

            doSimpleSearch();
        }

        // return false;
    });


    $('#searchButtonID').click(function () {
        doSearch();

    });

    function gridReload() {
        $('#userBooksList').trigger("reloadGrid");
    }


    function formatAuthors(cellvalue, options, cellObject) {

        return  cellvalue.replace(new RegExp(", "), ",</br>")
    }


    function formatStatus(cellvalue, options, cellObject) {
        switch (cellvalue) {
            case "1":
                return 'Rented'
            case "0":
                return 'Available'
            case "-1":
                return 'Not Available'
            case "-2":
                return 'Other'

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
});
</script>
