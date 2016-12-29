/* Nạp dữ liệu trip demo */
function CreateTripDemo1(){
	var jqxhr = $.post( "/ITS/rest/trip/CreateTrip", { tokenId: "7a3e1ccc-e78d-4465-974d-353efea13ac3",tripName:"Đi họp sáng ", startTime: "07:05:23 22-07-2015", endTime:"09:12:08 22-07-2015"
		, fromX: "10.77974", fromY:"106.63259", fromZ:"-1", fromDescription:"Nhà tôi ",
		toX: "10.79704764",toY: "106.64558964",toZ: "-1",toDescription:"Đại Học Bách Khoa TP.HCM", privacy:"PUBLIC"})
	.done(function(data) {
	alert( data.code + "_"+ data.description);
	})
	.fail(function() {
	alert( "error" );
	});
}

function CreateTripDemo2(){
	var jqxhr = $.post( "/ITS/rest/trip/CreateTrip", { tokenId: "7a3e1ccc-e78d-4465-974d-353efea13ac3",tripName:"Đi tham dự hội thảo báo cáo ", startTime: "09:30:23 23-07-2015", endTime:"10:45:08 22-07-2015"
		, fromX: "10.77191", fromY:"106.65774", fromZ:"-1", fromDescription:"Đại học Bách Khoa ",
		toX: "10.72765",toY: "106.67157",toZ: "-1",toDescription:"Trung tâm điều hành ITS ", privacy:"PRIVATE"})
	.done(function(data) {
	alert( data.code + "_"+ data.description);
	})
	.fail(function() {
	alert( "error" );
	});
}

function FetchDataTrip(){
	CreateTripDemo1();
	CreateTripDemo2();
}

function CreateTripDetailDemo(){
	
}

