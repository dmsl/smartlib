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
        <input type="text" id="search_cd_loggedIn"
            <?php
            echo 'value="' . $bookISBN . '"'
            ?>
               autofocus="autofocus"/>
    </div>
    <button type="button" id="searchButtonID"
            class="searchButton">Search
    </button>
    <button type="reset" id="clearButtonID"
            class="clearButton">Clear
    </button>
</div>
<br>
<div id="search-no-results-found">No results found.</div>
<div id="search-panel-div" style="display: none;">
    <table id="allBooksLoggedInList"></table>
    <div id="allBooksLoggedInPager"></div>
</div>


<script type="text/javascript">

jQuery(document).ready(function () {


    var grid = $('#allBooksLoggedInList');
    var resultRecords = '0';
    var tmpTitle;
    var tmpAuthors;
    var tmpUsername;
    var tmpIsbn;
    var isAdv = 0;


    var windowSpace = $(window).height();

    var recordNum = 10;

    //Show more records
    if (windowSpace > 1000) {
        recordNum = 20;
    }

    // Searches with URL of index page. eg index?ISBN=123
    var searchedOutside = 0;

    // If search field is filled, do search
    if ($("#search_cd_loggedIn").val() != "") {
        searchedOutside = 1;

        timeoutHnd = setTimeout(function () {
            doSearch(isAdv);
        }, 10);

    }


    // Grid Table
    jQuery("#allBooksLoggedInList").jqGrid({
        url:'grid/server.php?q=allbooksloggedin',
        datatype:"local",
        mtype:"POST",
        height:"auto",
        width:"auto",

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

        rowNum:recordNum,

        sortname:'title',

        viewrecords:true,

        sortorder:"asc",


        pager:'#allBooksLoggedInPager',

        gridview:true,

        gridComplete:function () {


            var gridDiv = $('#search-panel-div');


            resultRecords = grid.jqGrid('getGridParam', 'reccount');

            var cd_mask = $("#search_cd_loggedIn").val();

            var cd_title = $('#advsearch_cd_loggedIn_title').val();
            var cd_authors = $('#advsearch_cd_loggedIn_authors').val();
            var cd_owner = $('#advsearch_cd_loggedIn_owner').val();
            var cd_isbn = $('#advsearch_cd_loggedIn_isbn').val();

            // If search fields are empty
            if ((cd_mask == "" && !isAdv) ||
                    ((cd_title == '' && cd_authors == '' && cd_owner == '' && cd_isbn == '') && isAdv)) {
                initSearchResults();
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


                //Force a search
                if (searchedOutside == 1) {
                    searchedOutside = 2;
                    doSimpleSearch();
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

    // Grid Table
    // Attach a pager
    jQuery("#allBooksLoggedInList").jqGrid('navGrid', '#allBooksLoggedInPager',
            {edit:false, add:false, del:false, search:false, refresh:false});


    function imageFunction(cellvalue, options, rowObject) {
        return  "<img src='" + cellvalue + "' class='jqgrid' />";
    }


    //Toggle Search modes
    $('.toggleSearchButton').click(function () {

        //Switch to Simple
        if (isAdv) {

            $("#advsearch_cd_loggedIn_title").val("");
            $("#advsearch_cd_loggedIn_authors").val("");
            $("#advsearch_cd_loggedIn_owner").val("");
            $("#advsearch_cd_loggedIn_isbn").val("");

            $("#search-panel label, #clearButtonID, #searchButtonID").animate({
                opacity:'0.0'
            }, "normal");


            $(".toggleSearchButton").animate({
                top:'+=80px'
            }, "slow", function () {

                //Change css
                $('#search-panel').css({top:'-20px', marginBottom:"20px"});

                //Replace form
                $('#advancedSearchLoggedIn').replaceWith(
                        '<div id="simpleSearchLoggedIn">\
                        <input type="text" class="input" id="search_cd_loggedIn" value="" autofocus="autofocus" />\
                        </div>'
                );


                $('.toggleSearchButton').text("Advanced Search");

                //Hide& Unveil content
                $('#simpleSearchLoggedIn').hide();
                $('#simpleSearchLoggedIn').fadeIn(1000);

                $("#search-panel #clearButtonID, #searchButtonID").animate({
                    opacity:'1.0'}, "normal");

            });

            isAdv = 0;//Switched

        }


        //Switch to advance
        else {
            //Reload Grid

            $("#search_cd_loggedIn").val("");

            $("#search-panel input, #clearButtonID, #searchButtonID").animate({
                        opacity:'0.0'
                    }
                    , "normal"

            );


            $(".toggleSearchButton").animate({
                top:'-=60px'
            }, "slow", function () {

                //Change css
                $('#search-panel').css({top:'0px', marginBottom:"10px"});

                $(".toggleSearchButton").css({ top:'-=20px'});

                //Replace form TODO CHECK
                $('#simpleSearchLoggedIn').replaceWith(
                        '<div id="advancedSearchLoggedIn">\
                        <label>&nbsp&nbspTitle: \
                        <input type="text" class="input" value="" id="advsearch_cd_loggedIn_title" autofocus="autofocus"/></label> \
                        <label>Authors: \
                        <input type="text" class="input" value="" id="advsearch_cd_loggedIn_authors" /></label> \
                        <label>&nbsp&nbspOwner: \
                        <input type="text" class="input" value="" id="advsearch_cd_loggedIn_owner" /></label> \
                        <label>&nbsp&nbsp&nbspISBN: \
                        <input type="text" class="input" value="" id="advsearch_cd_loggedIn_isbn" /></label> \
                        </div>\
                        '
                );


                //Toggle button to Simple
                $('.toggleSearchButton').html("Simple Search");


                //Hide & Unveil content
                $('#advancedSearchLoggedIn').hide();
                $('#advancedSearchLoggedIn').fadeIn(1000);

                $("#search-panel #clearButtonID, #searchButtonID").animate({
                    opacity:'1.0'}, "normal");


            });

            isAdv = 1;//Switched

        }


        $('#clearButtonID').click();


    });

    var timeoutHnd;


    $("#search-panel input").live("keydown", function (event) {

        //On Enter
        if (event.keyCode == 13) {

            //Do Search
            doSearch(isAdv);

        }
        else {
            if (timeoutHnd)
                clearTimeout(timeoutHnd);
            timeoutHnd = setTimeout(function () {

                if (isAdv) {
                    tmpTitle = jQuery('#advsearch_cd_loggedIn_title').val();
                    tmpAuthors = jQuery('#advsearch_cd_loggedIn_authors').val();
                    tmpUsername = jQuery('#advsearch_cd_loggedIn_owner').val();
                    tmpIsbn = jQuery('#advsearch_cd_loggedIn_isbn').val();

                    //Set autocomplete for owner
                    $("#advsearch_cd_loggedIn_owner").autocomplete(
                            {
                                source:"scripts/autocomplete/getUsernameList.php" + "?titl;e=" + tmpTitle
                                        + "&authors=" + tmpAuthors
                                        + "&isbn=" + tmpIsbn,
                                minLength:1,
                                select:function (event, ui) {
                                    $(this).val(ui.item.label);
                                    doAdvancedSearch();
                                }

                            }
                    );

                    //Set autocomplete for title
                    $("#advsearch_cd_loggedIn_title").autocomplete(
                            {
                                source:"scripts/autocomplete/getBookTitleList.php" + "?username=" + tmpUsername
                                        + "&authors=" + tmpAuthors
                                        + "&isbn=" + tmpIsbn,
                                minLength:1,
                                select:function (event, ui) {
                                    $(this).val(ui.item.label);
                                    doAdvancedSearch();
                                }

                            }
                    );


                    //Set autocomplete for Authors
                    $("#advsearch_cd_loggedIn_authors").autocomplete(
                            {
                                source:"scripts/autocomplete/getAuthorsNameList.php" + "?username=" + tmpUsername
                                        + "&title=" + tmpTitle
                                        + "&isbn=" + tmpIsbn,
                                minLength:1,
                                select:function (event, ui) {
                                    $(this).val(ui.item.label);
                                    doAdvancedSearch();
                                }

                            }
                    );
                }


                doSearch(isAdv);
            }, 500);

        }

    });


    //Clear the search
    $('#clearButtonID').click(function () {

        if (isAdv) {
            $("#advsearch_cd_loggedIn_title").val("");
            $("#advsearch_cd_loggedIn_authors").val("");
            $("#advsearch_cd_loggedIn_owner").val("");
            $("#advsearch_cd_loggedIn_isbn").val("");


        }
        else {
            $("#search_cd_loggedIn").val("");


        }

        var grid = $('#allBooksLoggedInList');

        //Not searching anymore
        grid.jqGrid('setGridParam', {search:false});


        //Clear filters
        var postData = grid.jqGrid('getGridParam', 'postData');
        $.extend(postData, {filters:""});

        grid.jqGrid('setGridParam', { 'datatype':'local' });


        initSearchResults();

    });


    $('#searchButtonID').click(function () {
        doSearch(isAdv);
    });


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


//Do an Advanced or a simple search
    function doSearch(isAdv) {

        if (isAdv) {
            doAdvancedSearch();
        }
        else {
            doSimpleSearch();
        }
    }


//Advanced Search
    function doAdvancedSearch() {


        var gridDiv = $('#search-panel-div');
        var grid = $('#allBooksLoggedInList');


        var cd_title = jQuery('#advsearch_cd_loggedIn_title').val();
        var cd_authors = jQuery('#advsearch_cd_loggedIn_authors').val();
        var cd_owner = jQuery('#advsearch_cd_loggedIn_owner').val();
        var cd_isbn = jQuery('#advsearch_cd_loggedIn_isbn').val();


        //If there is nothing to search, hide table
        if (cd_title == '' && cd_authors == '' && cd_owner == '' && cd_isbn == '') {
            initSearchResults();

        }
        else {


            grid.jqGrid('setGridParam', { 'datatype':'json' });
            grid.jqGrid('setGridParam', {search:true});

            //Run query
            var postData = grid.jqGrid('getGridParam', 'postData');
            $.extend(postData, {
                title:cd_title,
                authors:cd_authors,
                username:cd_owner,
                isbn:cd_isbn,
                searchString:''
            });

            gridReload();


        }


    }


//Simple Search
    function doSimpleSearch() {


        var gridDiv = $('#search-panel-div');
        var grid = $("#allBooksLoggedInList");

        var cd_mask = $("#search_cd_loggedIn").val();


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


    function gridReload() {
        $('#allBooksLoggedInList').trigger("reloadGrid");
    }


}); //END OF DOCUMENT READY FUNCTION


</script>