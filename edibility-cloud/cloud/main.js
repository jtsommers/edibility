// 
// Cloud functions and jobs
// 
// Functions are created with Parse.Cloud.define and are triggered by using a URL
//  with POST parameters (things like API key, etc)
// 
// Jobs are created with Parse.Cloud.job and are available in the Parse dashboard for
//  Edibility to be scheduled to run at specific times
// 

// Test function to send a single notification that mimics an actual food subscription
Parse.Cloud.define("testpush", function(request, response) {
	Parse.Push.send(
		{
			channels: [ "foodalert_30_belgianwaffles" ],
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

// Test function to send a single notification to a development subscription channel
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

// Initial concept for notifying all dining halls for a specific meal
// Uses notifications on a per food-college basis which turned out to not be possible within limitations
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

// 
// Scheduled jobs to notify individual meals as a group of notification channels
// 
Parse.Cloud.job("notifybreakfastgroup", function(request, response) {
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

Parse.Cloud.job("notifylunchgroup", function(request, response) {
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

Parse.Cloud.job("notifydinnergroup", function(request, response) {
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
// 
// End of scheduled jobs for notifying meals
// 


// Concept job that was intended to notify each meal and college separately
// The idea was that in Parse, there would be 15 different jobs and parameters would differentiate the job
// Parse jobs don't support running multiple scheduled jobs from a single job function
// Ultimately this job isn't used
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
