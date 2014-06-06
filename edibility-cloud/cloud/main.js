
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
	response.success("Hello world!");
});

Parse.Cloud.define("testpush", function(request, response) {
	Parse.Push.send(
		{
			channels: [ "testchannel" ],
			data: {
				alert: "Test Notification Received"
			}
		}, 
		{
			success: function() {
				// Push was successful
				response.success("Test Push Sent");
			},
			error: function(error) {
				// Handle error
				response.error("Test Push Not Sent");
			}
		}
	);
});

Parse.Cloud.job("testjob", function(request, response) {
	Parse.Push.send(
		{
			channels: [ "testchannel" ],
			data: {
				alert: "Test Notification Received"
			}
		}, 
		{
			success: function() {
				// Push was successful
				response.success("Test Push Sent");
			},
			error: function(error) {
				// Handle error
				response.error("Test Push Not Sent");
			}
		}
	);
});

Parse.Cloud.define("testmenu", function(request, response) {
	var Menu = require('cloud/menu.js').Menu;
	var testMenu = new Menu(
		'eight', 
		function(){
			response.success("Success downloading test menu");
		}, 
		function(error) {
			response.error(error);
		}
	);
});

Parse.Cloud.define("notifybreakfast", function(request, response) {
	var Menu = require('cloud/menu.js').Menu;
	var c8menu = new Menu(
		'eight',
		function() {
			c8menu.notifyForMeal('breakfast');
			response.success("Sent notifications for breakfast");
		},
		function(error) {
			response.error(error);
		});
});
