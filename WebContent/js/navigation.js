var map = null;
var jsonData;
var jsonData1;
var summaryPanel;
// var host = 'http://172.28.10.60/';
//var host = 'http://localhost:8080/ITS/';
var host = 'http://traffic.hcmut.edu.vn/ITS/';

function getColor(index) {
	if (index == 0) {
		return "#0000FF";
	} else if (index == 1) {
		return "#FF0000";
	} else if (index == 2) {
		return "#008000";
	} else if (index == 3) {
		return "#FFFF00";
	} else if (index == 4) {
		return "#8B008B";
	} else if (index == 5) {
		return "#00FFFF";
	} else {
		return "#000000";
	}
}

function getCellX(lat, lng) {
	var d = (lat - 10.609309) / 0.01;
	return parseInt(d, 10);
}

function getCellY(lat, lng) {
	var d = (lng - 106.493811) / 0.01;
	return parseInt(d, 10);
}

function getSearchingBox(x1, y1, x2, y2) {
	var rs = [];
	var dx = Math.abs(x1 - x2);
	var dy = Math.abs(y1 - y2);

	var raito = Math.max(dx, 1) / Math.max(dy, 1);
	var safe = Math.round(Math.abs(dx - dy) / 4);

	rs[0] = Math.max(Math.min(x1, x2) - 1, 0);
	rs[1] = Math.max(Math.min(y1, y2) - 1, 0);
	rs[2] = Math.min(Math.max(x1, x2) + 1, 39);
	rs[3] = Math.min(Math.max(y1, y2) + 1, 39);

	if (raito > 2) {
		rs[1] = Math.max(rs[1] - safe, 0);
		rs[3] = Math.min(rs[3] + safe, 39);
	} else if (raito < 0.5) {
		rs[0] = Math.max(rs[0] - safe, 0);
		rs[2] = Math.min(rs[2] + safe, 39);
	}
	return rs;
}

function getAlgorithm(code) {
	if (code == 'distance')
		return 'motor/distance_dijkstra';
	else if (code == 'static_time')
		return 'motor/static_time_dijkstra';
	else if (code == 'profile_time')
		return 'motor/profile_time_dijkstra';
	else if (code == 'real_time')
		return 'motor/real_time_dijkstra';
	else if (code == 'preference')
		return 'motor/preference_real_time_dijkstra';
	else if (code == 'truck')
		return 'truck/distance_dijkstra';
}

function initialize() {

	var hcm = null;
	var zoom = null;

	if (map != null) {
		hcm = map.getCenter();
		zoom = map.getZoom();
	}

	if (hcm == null)
		hcm = new google.maps.LatLng(10.75, 106.6667);
	if (zoom == null)
		zoom = 14;

	var mapOptions = {
		zoom : zoom,
		mapTypeId : google.maps.MapTypeId.ROADMAP,
		center : hcm
	};
	map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

	loadContextMenu();

	summaryPanel = document.getElementById('directions-panel');
	summaryPanel.innerHTML = '';
}

function loadContextMenu() {

	var contextMenuOptions = {};
	contextMenuOptions.classNames = {
		menu : 'context_menu',
		menuSeparator : 'context_menu_separator'
	};

	// create an array of ContextMenuItem objects
	// an 'id' is defined for each of the four directions related items
	var menuItems = [];
	menuItems.push({
		className : 'context_menu_item',
		eventName : 'directions_origin_click',
		id : 'directionsOriginItem',
		label : 'Add source'
	});

	menuItems.push({
		className : 'context_menu_item',
		eventName : 'directions_destination_click',
		id : 'directionsDestinationItem',
		label : 'Add destination'
	});

	// a menuItem with no properties will be rendered as a separator
	menuItems.push({});
	menuItems.push({
		className : 'context_menu_item',
		eventName : 'zoom_in_click',
		label : 'Zoom in'
	});
	menuItems.push({
		className : 'context_menu_item',
		eventName : 'zoom_out_click',
		label : 'Zoom out'
	});
	menuItems.push({});
	menuItems.push({
		className : 'context_menu_item',
		eventName : 'center_map_click',
		label : 'Center map here'
	});

	contextMenuOptions.menuItems = menuItems;

	var contextMenu = new ContextMenu(map, contextMenuOptions);

	google.maps.event.addListener(map, 'rightclick', function(mouseEvent) {
		contextMenu.show(mouseEvent.latLng);
	});

	// create markers to show directions origin and destination
	// both are not visible by default
	var markerOptions = {};
	markerOptions.icon = 'http://www.google.com/intl/en_ALL/mapfiles/markerA.png';
	markerOptions.map = null;
	markerOptions.position = new google.maps.LatLng(0, 0);
	markerOptions.title = 'Directions origin';

	originMarker = new google.maps.Marker(markerOptions);

	markerOptions.icon = 'http://www.google.com/intl/en_ALL/mapfiles/markerB.png';
	markerOptions.title = 'Directions destination';
	destinationMarker = new google.maps.Marker(markerOptions);

	// listen for the ContextMenu 'menu_item_selected' event
	google.maps.event
			.addListener(
					contextMenu,
					'menu_item_selected',
					function(latLng, eventName) {
						switch (eventName) {
						case 'directions_origin_click':
							originMarker.setPosition(latLng);
							if (!originMarker.getMap()) {
								originMarker.setMap(map);
							}

							var URLL = host + 'rest/segment_id/'
									+ latLng.lat() + '/' + latLng.lng();
							// alert(latLng.lat() );

							$(document)
									.ready(
											function() {
												$
														.ajax({
															type : "GET",
															dataType : "json",
															url : URLL,
															async : false,
															success : function(
																	data) {
																document
																		.getElementById('srcLat').value = latLng
																		.lat();
																document
																		.getElementById('srcLng').value = latLng
																		.lng();
																document
																		.getElementById('srcSegment').value = data.segment_id;
															}
														});
											});

							break;
						case 'directions_destination_click':
							destinationMarker.setPosition(latLng);
							if (!destinationMarker.getMap()) {
								destinationMarker.setMap(map);
							}
							var URLL = host + 'rest/segment_id/' + latLng.lat()
									+ '/' + latLng.lng();
							$(document)
									.ready(
											function() {
												$
														.ajax({
															type : "GET",
															dataType : "json",
															url : URLL,
															async : false,
															success : function(
																	data) {
																document
																		.getElementById('destLat').value = latLng
																		.lat();
																document
																		.getElementById('destLng').value = latLng
																		.lng();
																document
																		.getElementById('destSegment').value = data.segment_id;
															}
														});
											});

							break;

						case 'zoom_in_click':
							map.setZoom(map.getZoom() + 1);
							break;
						case 'zoom_out_click':
							map.setZoom(map.getZoom() - 1);
							break;
						case 'center_map_click':
							map.panTo(latLng);
							break;
						}

					});
}

function calcRoute() {
	
	var load = document.getElementById('load').value;
	var weight = document.getElementById('weight').value;
	var totalLoad = document.getElementById('totalLoad').value;


	var time = document.getElementById('time').value;


	var srcLat = document.getElementById('srcLat').value;
	var srcLng = document.getElementById('srcLng').value;
	var destLat = document.getElementById('destLat').value;
	var destLng = document.getElementById('destLng').value;
	var srcSegment = document.getElementById('srcSegment').value;
	var destSegment = document.getElementById('destSegment').value;

	var srcLatLng = new google.maps.LatLng(srcLat, srcLng);
	var tgtLatLng = new google.maps.LatLng(destLat, destLng);

	new google.maps.Marker({
		position : srcLatLng,
		map : map,
		icon : 'http://www.google.com/intl/en_ALL/mapfiles/markerA.png',
		title : 'Source'
	});

	new google.maps.Marker({
		position : tgtLatLng,
		map : map,
		icon : 'http://www.google.com/intl/en_ALL/mapfiles/markerB.png',
		title : 'Destination'
	});
	
	var algo_type = document.getElementById('algo').value;

	var algo = getAlgorithm(algo_type);

	// alert(algo);
	
	
	var URLL  = host + 'rest/' + algo + '/' + srcLat + '/' + srcLng + '/'
	+ srcSegment + '/' + destLat + '/' + destLng + '/' + destSegment;
	
	if (algo_type == 'preference'){
		var user = 'testuser';
		URLL  = host + 'rest/' + algo + '/'+ user + '/' + srcLat + '/' + srcLng + '/'
		+ srcSegment + '/' + destLat + '/' + destLng + '/' + destSegment;
	}
	
	
	if (algo_type == 'truck') {
		
		 URLL = URLL + '/' + time + '/' + weight + '/' + load + '/' + totalLoad ;
	}
	

	$(document).ready(function() {
		$.ajax({
			type : "GET",
			dataType : "json",
			url : URLL,
			async : false,
			success : function(data) {
				jsonData = data;
			}
		});
	});
	
	if (jsonData.path == "null") {
		alert("Path not found!");
	}else {

	var points = jsonData.path;
	var flightPlanCoordinates = [];
	for (var i = 0; i < points.length; i++) {
		flightPlanCoordinates[i] = new google.maps.LatLng(points[i].lat,
				points[i].lng);
	}

	var flightPath = new google.maps.Polyline({
		path : flightPlanCoordinates,
		geodesic : true,
		strokeColor : '#FF0000',
		strokeOpacity : 2.0,
		strokeWeight : 4
	});

	flightPath.setMap(map);
	}
}
/*
 * function viewCell() {
 * 
 * var x = document.getElementById('x').value; var y =
 * document.getElementById('y').value;
 * 
 * var URLL = 'http://localhost:8080/ITS/rest/get_segments/' + x + '/' + y;
 * 
 * $(document).ready(function() { $.ajax({ type : "GET", dataType : "json", url :
 * URLL, async : false, success : function(data) { jsonData1 = data; } }); });
 * 
 * var segments = jsonData1.segments; for (var i = 0; i < segments.length; i++) {
 * var flightPlanCoordinates = [];
 * 
 * flightPlanCoordinates[0] = new google.maps.LatLng(segments[i].lat1,
 * segments[i].lng1); flightPlanCoordinates[1] = new
 * google.maps.LatLng(segments[i].lat2, segments[i].lng2);
 * 
 * var flightPath = new google.maps.Polyline({ path : flightPlanCoordinates,
 * geodesic : true, strokeColor : '#FF0000', strokeOpacity : 2.0, strokeWeight :
 * 2 });
 * 
 * flightPath.setMap(map); } }
 */

function viewCell(x, y) {
	var URLL = host + 'rest/get_segments/' + x + '/' + y;

	$(document).ready(function() {
		$.ajax({
			type : "GET",
			dataType : "json",
			url : URLL,
			async : false,
			success : function(data) {
				jsonData1 = data;
			}
		});
	});

	var segments = jsonData1.segments;
	for (var i = 0; i < segments.length; i++) {
		var flightPlanCoordinates = [];

		flightPlanCoordinates[0] = new google.maps.LatLng(segments[i].lat1,
				segments[i].lng1);
		flightPlanCoordinates[1] = new google.maps.LatLng(segments[i].lat2,
				segments[i].lng2);

		var flightPath = new google.maps.Polyline({
			path : flightPlanCoordinates,
			geodesic : true,
			strokeColor : '#00FF00',
			strokeOpacity : 2.0,
			strokeWeight : 2
		});

		flightPath.setMap(map);

	}
}

function view() {
	var srcLat = document.getElementById('srcLat').value;
	var srcLng = document.getElementById('srcLng').value;
	var destLat = document.getElementById('destLat').value;
	var destLng = document.getElementById('destLng').value;
	var srcX = getCellX(srcLat, srcLng);
	var srcY = getCellY(srcLat, srcLng);
	var destX = getCellX(destLat, destLng);
	var destY = getCellY(destLat, destLng);
	var box = getSearchingBox(srcX, srcY, destX, destY);
	// alert(box[0]);
	var i, j;
	for (i = box[0]; i <= box[2]; i++)
		for (j = box[1]; j <= box[3]; j++)
			viewCell(i, j);
	// viewCell(destX, destY);
}

function viewSerachingBox() {
	var lat1 = document.getElementById('srcLat').value;
	var lng1 = document.getElementById('srcLng').value;
	var lat2 = document.getElementById('destLat').value;
	var lng2 = document.getElementById('destLng').value;

	var URLL = host + 'rest/get_searching_box/' + lat1 + '/' + lng1 + '/'
			+ lat2 + '/' + lng2;

	$(document).ready(function() {
		$.ajax({
			type : "GET",
			dataType : "json",
			url : URLL,
			async : false,
			success : function(data) {
				jsonData1 = data;
			}
		});
	});

	var segments = jsonData1.segments;
	for (var i = 0; i < segments.length; i++) {
		var flightPlanCoordinates = [];

		flightPlanCoordinates[0] = new google.maps.LatLng(segments[i].lat1,
				segments[i].lng1);
		flightPlanCoordinates[1] = new google.maps.LatLng(segments[i].lat2,
				segments[i].lng2);

		var flightPath = new google.maps.Polyline({
			path : flightPlanCoordinates,
			geodesic : true,
			strokeColor : '#00FF00',
			strokeOpacity : 2.0,
			strokeWeight : 2
		});

		flightPath.setMap(map);

	}
}

google.maps.event.addDomListener(window, 'load', initialize);