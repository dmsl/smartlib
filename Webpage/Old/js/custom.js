// JavaScript Document
$(document).ready(function() {
    var $map = $('#map');
	$map.gMap({
		zoom: 16,
		markers: [
			{ 'latitude' : 35.144968, 'longitude': 33.411183  }
		]
	});
 });