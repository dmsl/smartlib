var browser;

var browserVersion;

var isIE;

var screenshotGalleryInitialized = 0;

var reflectionsInitialized = 0;

browserDetection = function () {

    browser = navigator.userAgent.toLowerCase();

    browserVersion = parseFloat(browser.substring(browser.indexOf('msie ') + 5));

    isIE = ((browser.indexOf("msie") != -1) && (browser.indexOf("opera") == -1) && (browser.indexOf("webtv") == -1));

}

numberComparisonDescending = function (a, b) {

    return b - a;

}



initializeGallery = function (data) {

    var markup;

    $('#screenshots').html('<ul id="screenshot_gallery" class="jcarousel-skin-opensuse"></ul>');

    $.each(data, function (i, screenshot) {
        markup = '<li><a href="http://books.google.com/books?vid=isbn' + screenshot.isbn + '" title="' + screenshot.title+ '" rel="group1" target="_blank" ><img src="' + screenshot.image + '" width="140" height="150" /></a></li>';

        $('#screenshot_gallery').append(markup);

    });

    $('#screenshot_gallery').jcarousel({scroll: 4, itemFallbackDimension: 250});

    screenshotGalleryInitialized = 1;

}

showScreenshots = function () {

    //$("#toggle_screenshots").blur();

    if (screenshotGalleryInitialized == 0) {

        $.getJSON('../smartscan/test/test.php', initializeGallery);

    }

//    $('#screenshots').slideToggle('slow', function () {
//
//        //if (reflectionsInitialized == 0) {
////
////            $('#screenshot_gallery img').reflect();
////
////            reflectionsInitialized = 1;
////
////        }
//
//    });

    //$("#toggle_screenshots").addClass('expanded');

}

fixIE6alphaTransparency = function () {

    if (isIE && browserVersion == 6) {

        $('#quicklinks .top').each(pngfix);

        $('#quicklinks .buttontext').each(pngfix);

        $('#quicklinks .bottom').each(pngfix);

        $('#shop img').each(pngfix);

    }

}

$(document).ready(function () {

    browserDetection();

    fixIE6alphaTransparency();
	
	showScreenshots();

});

