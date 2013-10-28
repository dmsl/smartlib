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


<!--
  jCarousel library
-->
<script type="text/javascript" src="carousel/lib/jquery.jcarousel.min.js"></script>

<!--
  jCarousel skin stylesheet
-->

<link rel="stylesheet" type="text/css" href="carousel/skins/ie7/skin.css"/>


<script src="carousel/jquery.capSlide.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="carousel/capstyle.css" media="screen"/>


<script type="text/javascript">

    function mycarousel_initCallback(carousel) {
        // Disable autoscrolling if the user clicks the prev or next button.
        carousel.buttonNext.bind('click', function () {
            carousel.startAuto(0);
        });

        carousel.buttonPrev.bind('click', function () {
            carousel.startAuto(0);
        });

        // Pause autoscrolling if the user moves with the cursor over the clip.
        carousel.clip.hover(function () {
            carousel.stopAuto();
        }, function () {
            carousel.startAuto();
        });
    }
    ;

    jQuery(document).ready(function () {
        jQuery('#mycarousel').jcarousel({
            auto:100,
            wrap:'last',
            initCallback:mycarousel_initCallback
        });
    });


    function mycarousel_itemLoadCallback(carousel, state) {
        // Check if the requested items already exist
        if (carousel.has(carousel.first, carousel.last)) {
            return;
        }

        jQuery.get(
                'carousel/carouselFetchImages.php',
                {
                    first:carousel.first,
                    last:carousel.last
                },
                function (xml) {
                    mycarousel_itemAddCallback(carousel, carousel.first, carousel.last, xml);
                },
                'xml'
        );
    }
    ;

    function mycarousel_itemAddCallback(carousel, first, last, xml) {
        // Set the size of the carousel
        carousel.size(parseInt(jQuery('total', xml).text()));

        var isbns = new Array();
        var j = 0;


        jQuery('isbn', xml).each(function (i) {
            //Save all isbn's to array
            isbns[j++] = jQuery(this).text();

        });
        var titles = new Array();
        j = 0;
        jQuery('title', xml).each(function (i) {
            //Save all titles's to array
            titles[j++] = jQuery(this).text();

        });

        jQuery('image', xml).each(function (i) {
            carousel.add(first + i, mycarousel_getItemHTML(jQuery(this).text(),
                    isbns[i], titles[i]
            ));
        });
    }
    ;

    /**
     * Item html creation helper.
     */
    function mycarousel_getItemHTML(url, isbn, title) {


        return '<div id="capslide_img_cont' + isbn + '" class="ic_container">' +
                '<a href="https://books.google.com/books?vid=isbn' + isbn + '" target="_blank"  class="carouselImage">' +
                '<img src="' + url + '" width="130" height="170" alt="' + title + '" id="' + isbn + '"' +
                '"/></a>' +
                '<div class="ic_caption">\
                    <h3>' + title + '</h3>\
        <p class="ic_text" ></p>\
                </div>\
        </div>'
                +
                '<script>\
                 jQuery(document).ready(function() {\
                     jQuery("#' + isbn + '").mouseenter\
                        (\
                                function(){\
                                    $("#capslide_img_cont' + isbn + '").capslide()({\
                                        caption_color	: \'white\',\
                                        caption_bgcolor	: \'#305FA1\',\
                                        showcaption	    : false,\
                                        border			: \'0px\'\
                                    });}); }); <\/script>';


    }


    jQuery(document).ready(function () {


        jQuery('#mycarousel').jcarousel({
            // Uncomment the following option if you want items
            // which are outside the visible range to be removed
            // from the DOM.
            // Useful for carousels with MANY items.

            itemVisibleOutCallback:{onAfterAnimation:function (carousel, item, i, state, evt) {
                carousel.remove(i);
            }},
            itemLoadCallback:mycarousel_itemLoadCallback
        });
    });

</script>