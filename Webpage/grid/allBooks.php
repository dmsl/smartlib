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

<div id="search-panel" style="top: 10px;">
    <form id="simpleSearchLoggedOut" action="#" method="post">
        <input type="text" class="input" <?php
            echo 'value="' . $bookISBN . '"'
            ?> id="search_cd_loggedOut" autofocus="autofocus"/>

        <button type="submit" value="" class="searchButton">Search</button>

        <button type="reset" value="" id="clearButtonID"
                class="clearButton">Clear
        </button>
    </form>
</div>

<div id="search-no-results-found">No results found.</div>
<div id="search-panel-div" style="display: none;">

    <table id="allBooksList"></table>
    <div id="allBooksPager"></div>
</div>

<script type="text/javascript">

//Records got
var resultRecords = '0';

jQuery(document).ready(function () {

    var windowSpace = $(window).height();

    var recordNum = 10;

    //Show more records
    if (windowSpace > 1000) {
        recordNum = 20;
    }

    // Searches with URL of index page. eg index?ISBN=123
    var searchedOutside = 0;

    // If search field is filled, do search
    if ($("#search_cd_loggedOut").val() != "") {
        searchedOutside = 1;
    }


    jQuery("#allBooksList").jqGrid({

        url:'grid/server.php?q=allbooks&rows=5',
        mtype:"POST",
        height:"auto",

        datatype:"local",
        shrinktofit:false,
        //            width:"auto",
//            autowidth:true,

        colNames:['Cover', 'Title', 'Authors', 'Published', 'Pages', 'Language', 'ISBN'],

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

            {name:'publishedYear', index:'publishedYear', width:70, align:"center",
                sortable:false},

            {name:'pageCount', index:'pageCount', width:70, align:"center", search:false, sortable:false},

            {name:'lang', index:'lang', width:70, align:"center", search:false, editable:false, sortable:false }    ,

            {name:'isbn', index:'isbn', width:100, align:"left"
            }
        ],

        //	editurl: "server.php",

        rowNum:recordNum,

        //	rowList:[10,20],

        pager:'#allBooksPager',

        sortname:'title',

        viewrecords:true,

        sortorder:"asc",

        gridComplete:function () {
            var gridDiv = $('#search-panel-div');
            var grid = $('#allBooksList');

            var cd_mask = jQuery("#search_cd_loggedOut").val();

            resultRecords = grid.jqGrid('getGridParam', 'reccount');

            if (cd_mask == "") {
                //Hide no results message
                $('#search-no-results-found')
                        .slideUp(500)
                        .animate(
                        { opacity:0 },
                        { queue:false, duration:300 }
                );

                //Hide table
                gridDiv
                        .css('opacity', 1)
                        .slideUp(300)
                        .animate(
                        { opacity:0 },
                        { queue:false, duration:500 }
                );
            }
            else if (resultRecords == '0') {

                //Hide table
                gridDiv
                        .css('opacity', 1)
                        .slideUp(300)
                        .animate(
                        { opacity:0 },
                        { queue:false, duration:500 }
                );


                //Show no results message in all cases except when outside search is made
                if (!searchedOutside || searchedOutside == 2) {
                    //Show no results message
                    $('#search-no-results-found')
                            .css('opacity', 0)
                            .slideDown(500)
                            .animate(
                            { opacity:1 },
                            { queue:false, duration:500 }
                    );
                }

                // Increase search outside val.
                if (searchedOutside) {
                    searchedOutside = 2;
                }


            }
            else {
                //Hide no results message
                $('#search-no-results-found')
                        .slideUp(500)
                        .animate(
                        { opacity:0 },
                        { queue:false, duration:300 }
                );

                //Show results
                grid.jqGrid('setGridWidth', $(window).width() / 1.2, true);

                gridDiv
                        .css('opacity', 0)
                        .slideDown(500)
                        .animate(
                        { opacity:1 },
                        { queue:false, duration:500 }
                );

            }

        }


    });

    jQuery("#allBooksList").jqGrid('navGrid', '#allBooksPager', {edit:false, add:false, del:false, search:false,
        refresh:false});


    function imageFunction(cellvalue, options, rowObject) {
        return  "<img src='" + cellvalue + "' width='80px' height='80px' />";
    }


    $('#simpleSearchLoggedOut').submit(function () {

        doSearch();

        return false;
    });


    //Clear the search
    $('#clearButtonID').click(function () {
        $("#search_cd_loggedOut").val("");
        doSearch();

        var grid = $('#allBooksList');

        //Not searching anymore
        grid.jqGrid('setGridParam', {search:false});


        //Clear filters
        var postData = grid.jqGrid('getGridParam', 'postData');
        $.extend(postData, {filters:""});

        grid.jqGrid('setGridParam', { 'datatype':'local' });


        initSearchResults();

        return false;
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


    //Search in Grid
    function doSearch() {

        var gridDiv = $('#search-panel-div');
        var grid = $("#allBooksList");
        //We will make search
        grid.jqGrid('setGridParam', {search:true});

        var cd_mask = $("#search_cd_loggedOut").val();

        if (cd_mask == '') {

            initSearchResults();

        }
        else {
            //We will make search
            grid.jqGrid('setGridParam', { 'datatype':'json' });
            grid.jqGrid('setGridParam', {search:true});

            //Run query
            var postData = grid.jqGrid('getGridParam', 'postData');
            $.extend(postData, {searchString:cd_mask});

            gridReload();

        }

    }


    function gridReload() {

        $('#allBooksList').trigger("reloadGrid");

    }


    function formatAuthors(cellvalue, options, cellObject) {

        return  cellvalue.replace(new RegExp(", "), ",</br>")
    }


    function unformatAuthors(cellValue, options, cellObject) {
        return $(cellObject.html()).attr("originalValue");
    }


    var val = $("#search_cd_loggedOut").val().trim();
    if (val.length) {
        setTimeout(function () {
            $('#simpleSearchLoggedOut').submit();
        }, 500);

    }


    /**
     * Initializes Search Results:
     * removes all results or no-results message
     * */
    function initSearchResults() {
        //Hide no results message
        $('#search-no-results-found')
                .slideUp(500)
                .animate(
                { opacity:0 },
                { queue:false, duration:300 }
        );

        //Hide table
        $('#search-panel-div')
                .css('opacity', 1)
                .slideUp(300)
                .animate(
                { opacity:0 },
                { queue:false, duration:500 }
        );
    }


});//End of document load

</script>