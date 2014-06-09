
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
	response.success("Hello world!");
});

Parse.Cloud.define("testpush", function(request, response) {
	Parse.Push.send(
		{
			channels: [ "foodalert_30_frenchwaffles" ],
			data: {
				header: "Test Notification Received",
				action: "edu.ucsc.teambacon.edibility.MESSAGE",
				college: "eight"
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
			channels: [ "here_is_a_ghost" ],
			data: {
				header: "Test Notification Received",
				action: "edu.ucsc.teambacon.edibility.MESSAGE"
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

Parse.Cloud.job("notifybreakfast", function(request, response) {
	var Menu = require('cloud/menu.js').Menu;
	Menu.notifyAllForMeal(
		"breakfast", 
		{
			"success": function() {
				response.success("All notifications for breakfast sent");
			},
			"error": function(error) {
				response.error(error);
			}
		}
	);
});

Parse.Cloud.define("notifybreakfast", function(request, response) {
	var Menu = require('cloud/menu.js').Menu;
	Menu.notifyAllForMeal(
		"breakfast", 
		{
			"success": function() {
				response.success("All notifications for breakfast sent");
			},
			"error": function(error) {
				response.error(error);
			}
		}
	);
});

Parse.Cloud.job("notifybreakfast2", function(request, response) {
	var Menu = require('cloud/menu.js').Menu;
	Menu.notifyMealsOnly(
		"breakfast", 
		{
			"success": function() {
				response.success("All notifications for breakfast sent as group");
			},
			"error": function(error) {
				response.error(error);
			}
		}
	);
});

Parse.Cloud.job("notifylunch2", function(request, response) {
	var Menu = require('cloud/menu.js').Menu;
	Menu.notifyMealsOnly(
		"lunch", 
		{
			"success": function() {
				response.success("All notifications for lunch sent as group");
			},
			"error": function(error) {
				response.error(error);
			}
		}
	);
});

Parse.Cloud.job("notifydinner2", function(request, response) {
	var Menu = require('cloud/menu.js').Menu;
	Menu.notifyMealsOnly(
		"dinner", 
		{
			"success": function() {
				response.success("All notifications for dinner sent as group");
			},
			"error": function(error) {
				response.error(error);
			}
		}
	);
});

Parse.Cloud.job("granularnotifications", function(request, response) {
	if (request.params && request.params.meal && request.params.college) {
		var Menu = require('cloud/menu.js').Menu;
		Menu.notifyCollegeForMeal(
			request.params.college, 
			request.params.meal, 
			{
				"success": function() {
					response.success("All notifications for breakfast sent");
				},
				"error": function(error) {
					response.error(error);
				}
			}
		);
	} else {
		response.error("Request parameters not properly set");
	}
});
