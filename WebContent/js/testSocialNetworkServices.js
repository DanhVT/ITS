function Login(){
	var jqxhr = $.post( "/ITS/rest/user/Login", { userId: "10205044173244057", name: "a0", fristName:"a0", lastName:"a0",
		birthday:"a0", email:"a0", updateTime:"a0",gender:"a0", local:"a0",verified:"a0",
		timezone:"a0", link:"a0", imei:"a0"})
		.done(function(data) {
		alert( data.tokenId + "_"+ data.status );
		})
		.fail(function() {
		alert( "error" );
		});
}

function Logout(){
	var jqxhr = $.post( "/ITS/rest/user/Logout", { tokenId: "a0"})
		.done(function(data) {
		alert( "OK" );
		})
		.fail(function() {
		alert( "error" );
		});
}

function GetUserInfo(){
	var jqxhr = $.post( "/ITS/rest/user/GetUserInfo", { userId: "143717375959388"})
		.done(function(data) {
		alert(data.userId);
		})
		.fail(function() {
		alert( "error" );
		});
}

function GetUserInfoFromToken(){
	var jqxhr = $.post( "/ITS/rest/user/GetUserInfoFromToken", { tokenId: "084e82c9-5c92-4273-b210-ecf58dd001fa"})
		.done(function(data) {
		alert(data.userId);
		})
		.fail(function() {
		alert( "error" );
		});
}

function CreateTrip(){
	var jqxhr = $.post( "/ITS/rest/trip/CreateTrip", { tokenId: "084e82c9-5c92-4273-b210-ecf58dd001fa",tripName:"Lộ trình đi Đà Lạt hè 2015", startTime: "a0", endTime:"a0"
		, fromX: "a0", fromY:"a0", fromZ:"a0",fromLocationName:"a0" ,fromDescription:"a0",
		toX: "a0",toY: "a0",toZ: "a0",fromLocationName:"a0",toDescription:"a0", privacy:"a0"})
	.done(function(data) {
	alert( data.code + "_"+ data.description);
	})
	.fail(function() {
	alert( "error" );
	});
}

function GetListTrip(){
	var jqxhr = $.post( "/ITS/rest/trip/GetListTrip", { tokenId: "c2ef5d9f-3a54-460d-9b2a-245dbac02b25"})
	.done(function(data) {
	alert(data.listTrip);
	})
	.fail(function() {
	alert( "error" );
	});
}

function GetListPrivateTrip(){
	var jqxhr = $.post( "/ITS/rest/trip/GetListPrivateTrip", { tokenId: "c2ef5d9f-3a54-460d-9b2a-245dbac02b25"})
	.done(function(data) {
	alert(data.listTrip);
	})
	.fail(function() {
	alert( "error" );
	});
}

function GetListPublicTrip(){
	var jqxhr = $.post( "/ITS/rest/trip/GetListPublicTrip", { tokenId: "ed16a4cd-1627-43c1-a6b3-9b3b925742ea"})
	.done(function(data) {
	alert(data.listTrip);
	})
	.fail(function() {
	alert( "error" );
	});
}

function GetListShareTrip(){
	var jqxhr = $.post( "/ITS/rest/trip/GetListShareTrip", { tokenId: "5edc1f1c-60f8-46b7-a537-1757ca00ea50"})
	.done(function(data) {
	alert(data.listTrip);
	})
	.fail(function() {
	alert( "error" );
	});
}

function GetTripInfo(){
	var jqxhr = $.post( "/ITS/rest/trip/GetTripInfo", { tokenId: "084e82c9-5c92-4273-b210-ecf58dd001fa", tripId: "e1fc1565-a3cd-4f3d-8dee-295dcbedea96"})
	.done(function(data) {
	alert( data.tripName +"_"+data.dateTime +"_"+ data.startTime + "_"+ data.endTime + "_"+data.fromDescription+ "_"+data.toDescription + "_"+data.listPoint);
	})
	.fail(function() {
	alert( "error" );
	});
}

function CreatePointOnTrip(){
	var jqxhr = $.post( "/ITS/rest/tripdetails/CreatePointOnTrip", { tokenId: "ed16a4cd-1627-43c1-a6b3-9b3b925742ea", tripId: "317e5cc8-b2e4-48e5-93e3-8efaa4a02cfe", x:"a0"
		, y: "a0", z:"a0", fromZ:"a0",locationName:"a0" ,pointDescription:"a0",order: "a0"})
	.done(function(data) {
	alert( data.code + "_"+ data.description);
	})
	.fail(function() {
	alert( "error" );
	});
}

function GetListPointOnTrip(){
	var jqxhr = $.post( "/ITS/rest/tripdetails/GetListPointOnTrip", { tokenId: "ed16a4cd-1627-43c1-a6b3-9b3b925742ea", tripId: "40b49ede-1976-4770-bc53-959b3b0481cc"})
	.done(function(data) {
		alert(data.listPoint.length);
		alert( data.listPoint);
	})
	.fail(function() {
	alert( "error" );
	});
}

function LikeTrip(){
	var jqxhr = $.post( "/ITS/rest/like/LikeTrip", { tokenId: "ed16a4cd-1627-43c1-a6b3-9b3b925742ea", tripId:"317e5cc8-b2e4-48e5-93e3-8efaa4a02cfe"})
	.done(function(data) {
	alert( data.code + "_"+ data.description);
	})
	.fail(function() {
	alert( "error" );
	});
}

function CountLikeOnTrip(){
	var jqxhr = $.post( "/ITS/rest/like/CountLikeOnTrip", { tokenId: "ed16a4cd-1627-43c1-a6b3-9b3b925742ea", tripId:"317e5cc8-b2e4-48e5-93e3-8efaa4a02cfe"})
	.done(function(data) {
	alert( data.numLike);
	})
	.fail(function() {
	alert( "error" );
	});
}

function SaveCommentOnTrip(){
	var jqxhr = $.post( "/ITS/rest/comment/SaveCommentOnTrip", { tokenId: "ed16a4cd-1627-43c1-a6b3-9b3b925742ea", tripId: "317e5cc8-b2e4-48e5-93e3-8efaa4a02cfe", content:"a0"})
		.done(function(data) {
		alert( data.code + "_"+ data.description);
		})
		.fail(function() {
		alert( "error" );
		});
}

function GetListCommentOnTrip(){
	var jqxhr = $.post( "/ITS/rest/comment/GetListCommentOnTrip", { tokenId: "ed16a4cd-1627-43c1-a6b3-9b3b925742ea", tripId: "317e5cc8-b2e4-48e5-93e3-8efaa4a02cfe"})
	.done(function(data) {
	alert( data.listComment);
	})
	.fail(function() {
	alert( "error" );
	});
}

function SaveListFriend(){
	var jqxhr = $.post( "/ITS/rest/friend/SaveListFriend", { tokenId: "c2ef5d9f-3a54-460d-9b2a-245dbac02b25", fromSocialNetwork: "fb", listFriend:"{listFriend:[{friendId:\"108807192785767\"},{friendId:\"1070069539677854\"},{friendId:\"10204444961224131\"},{friendId:\"994590300553861\"},{friendId:\"143511675981985\"},{friendId:\"111880782476634\"},{friendId:\"143717375959388\"}]}"})
	.done(function(data) {
	alert( data.code + "_"+ data.description);
	})
	.fail(function() {
	alert( "error" );
	});
}

function GetListFriend(){
	var jqxhr = $.post( "/ITS/rest/friend/GetListFriend", { tokenId: "04436ca9-99de-4bb6-b90a-296e0c03f566"})
	.done(function(data) {
	alert( data.listFriend);
	})
	.fail(function() {
	alert( "error" );
	});
}

function SaveShareOnTrip(){
	var jqxhr = $.post( "/ITS/rest/share/SaveShareOnTrip", { tokenId: "084e82c9-5c92-4273-b210-ecf58dd001fa",tripId:"e1fc1565-a3cd-4f3d-8dee-295dcbedea96", shareList:"[{userId:\"143717375959388\"},{userId:\"a00\"},{userId:\"a01\"},{userId:\"a02\"},{userId:\"a03\"},{userId:\"a04\"}]"})
	.done(function(data) {
		alert( data.code + "_"+ data.description);
	})
	.fail(function() {
	alert( "error" );
	});
}

function GetShareOnTrip(){
	var jqxhr = $.post( "/ITS/rest/share/GetShareOnTrip", { tokenId: "084e82c9-5c92-4273-b210-ecf58dd001fa",tripId:"e1fc1565-a3cd-4f3d-8dee-295dcbedea96"})
	.done(function(data) {
		alert(data);
	})
	.fail(function(e) {
		alert("Error");
	
	});
}

function TestMultiPath(){
	var URIJSON = "[{\"lat\":10.796379859206985,\"lng\":106.6732120513916,\"id\":\"89355\"},{\"lat\":10.753293645253464,\"lng\":106.65407180786133,\"id\":\"86877\"}]";
	var jqxhr = $.post( "/ITS/rest/motor/multiple_pointsPOST", { user: "testuser",param:URIJSON})
	.done(function(data) {
		alert(data);
	})
	.fail(function() {
	alert( "error" );
	});
}