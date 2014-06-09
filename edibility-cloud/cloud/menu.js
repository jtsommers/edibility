// "Constants" needed for translating college names to location numbers
var DINING_HALLS = {
	"cowell"	: "05",
	"crown"		: "20",
	"eight"		: "30",
	"nine"		: "40",
	"porter"	: "25"
};

var DEBUG = true;

// API URL
var KIMONO_URL = "http://www.kimonolabs.com/api/6guup5y4";
// API KEY
var KIMONO_KEY = "07783e2151f40eda9c2a00d625232a54";

var Menu = function(college, successCallback, errorCallback) {
	this.college = college;
	this.location = DINING_HALLS[college];
	this.successCallback = successCallback;
	this.errorCallback = errorCallback;
	this.isDownloaded = false;
	this.download();
};

// Static function to create a new menu and send notifications out for a specific meal
Menu.notifyCollegeForMeal = function(college, meal, callbacks) {
	console.log("Sending out notifications for " + college);
	var m = new Menu(
		college, 
		function() {
			m.notifyForMeal(meal);
			callbacks.success();
		}, 
		function(error) {
			callbacks.error(error);
		}
	);
};

Menu.notifyCollegeForMealGroup = function(college, meal, callbacks) {
	console.log("Sending out grouped notification for " + college);
	var m = new Menu(
		college, 
		function() {
			m.notifyForMealGroup(meal);
			callbacks.success();
		}, 
		function(error) {
			callbacks.error(error);
		}
	);
};

Menu.notifyAllForMeal = function(meal, callbacks) {
	var diningHalls = Object.keys(Menu.DINING_HALLS);
	var locationCount = diningHalls.length;
	// Callbacks keep track of successess and failures and call appropriate callbacks 
	// after callbacks for all menus have been triggered
	var successCount = 0, errorCount = 0;
	var callbackTracking = {
		"success": function() {
			successCount++;
			if (successCount >= locationCount) {
				// All dining halls notified successfully
				callbacks.success();
			} else if ((successCount + errorCount) >= locationCount) {
				// Some dining halls were notified successfully
				console.warn("Menu::notifyAllForMeal error count " + errorCount);
				callbacks.error(errorCount + " dining halls failed to send notifications");
			}
		},
		"error": function(error) {
			// Something went wrong
			console.log(error);
			errorCount++;
			// Notify callback if all menus have been triggered
			if ((successCount + errorCount) >= locationCount) {
				if (successCount === 0) {
					console.error("No successful dining hall notifications");
				} else {
					console.warn("Menu::notifyAllForMeal error count " + errorCount);
				}
				callbacks.error(errorCount + " dining halls failed to send notifications");
			}
		}
	};
	for (var i = 0; i < locationCount; i++) {
		Menu.notifyCollegeForMeal(diningHalls[i], meal, callbackTracking);
	}
};

Menu.notifyMealsOnly = function(meal, callbacks) {
	var diningHalls = Object.keys(Menu.DINING_HALLS);
	var locationCount = diningHalls.length;
	// Callbacks keep track of successess and failures and call appropriate callbacks 
	// after callbacks for all menus have been triggered
	var successCount = 0, errorCount = 0;
	var callbackTracking = {
		"success": function() {
			successCount++;
			if (successCount >= locationCount) {
				// All dining halls notified successfully
				callbacks.success();
			} else if ((successCount + errorCount) >= locationCount) {
				// Some dining halls were notified successfully
				console.warn("Menu::notifyForMealsOnly error count " + errorCount);
				callbacks.error(errorCount + " dining halls failed to send notifications");
			}
		},
		"error": function(error) {
			// Something went wrong
			console.log(error);
			errorCount++;
			// Notify callback if all menus have been triggered
			if ((successCount + errorCount) >= locationCount) {
				if (successCount === 0) {
					console.error("No successful dining hall notifications");
				} else {
					console.warn("Menu::notifyAllForMeal error count " + errorCount);
				}
				callbacks.error(errorCount + " dining halls failed to send notifications");
			}
		}
	};
	for (var i = 0; i < locationCount; i++) {
		Menu.notifyCollegeForMealGroup(diningHalls[i], meal, callbackTracking);
	}
};

Menu.DINING_HALLS = DINING_HALLS;

Menu.prototype.download = function() {
	var self = this;
	Parse.Cloud.httpRequest({
		url: KIMONO_URL,
		params: {
			"apikey"		: KIMONO_KEY,
			"locationNum"	: this.location
		},
		success: function(httpResponse) {
			if (httpResponse.data !== null) {
				// Successfully parsed response as JSON
				self.data = httpResponse.data;
				// Do post download processing
				self.onDownloadSuccess();
				// Call the success callback
				self.success();
			} else {
				self.error("Data could not parse");
			}
		},
		error: function(httpResponse) {
			// Notify the callback
			self.error('Request failed with response code ' + httpResponse.status);
			// Log an error to the cloud console
			console.error('Request failed with response code ' + httpResponse.status);
		}
	});
};

Menu.prototype.onDownloadSuccess = function() {
	if (this.data !== null) {
		var km = this.data;
		// Check that the Kimono parse succeeded
		if (km.lastrunstatus === "success") {
			// Pass along the results object to be sanitized and parsed
			this.sanitizeData(km.results);
		} else {
			// Non-success values indicative of a closed dining hall
			this.closed = true;
			console.log("Kimono data indicates " + this.college + " is closed");
		}
	} else {
		console.error("Data not set in Menu::onDownloadSuccess");
	}
};

Menu.prototype.sanitizeData = function(data) {
	if (DEBUG) {
		console.log("Sanitizing data at " + this.college);
	}
	// Confirm existence of meal names
	if (data.meals && data.meals.length > 0) {
		this.meals = [];
		for (var i in data.meals) {
			if (data.meals.hasOwnProperty(i)) {
				var m = data.meals[i];
				// Just grab the meal name from each meal object
				this.meals.push(m.mealName);
			}
		}
		// Save food to an associative array based on which meal
		// This is going to look a bit crazy because dinner and lunch arrays get reversed in Kimono
		var numMeals = this.meals.length;
		if (numMeals > 1) {
			// This should cover all cases, even weekends have lunch(brunch) and dinner
			// Last food object, this references lunch
			var foods = data.food[numMeals - 1];
			this.loadMeal(this.meals[numMeals - 2], foods);
			// Second to last food object, this references dinner
			foods = data.food[numMeals - 2];
			this.loadMeal(this.meals[numMeals - 1], foods);
			// Check for breakfast
			if (numMeals > 2) {
				this.loadMeal(this.meals[0], data.food[0]);
			}
		} else {
			// For some reason there's only one meal, I don't think this is a thing, but best not leave it up to chance
			console.warn("Only one meal at dining hall: " + this.college);
			this.loadMeal(this.meals[0], food[0]);
		}
	} else {
		console.error("Meal names not found");
	}
};

Menu.prototype.loadMeal = function(mealName, foodObject) {
	if (foodObject.firstItem !== "" && foodObject.allFood.length > 0) {
		this[mealName] = foodObject.allFood;
	} else {
		console.error("No food detected trying to load " + mealName);
	}
};

Menu.prototype.notifyForMeal = function(mealName) {
	// Dining hall is closed, break to avoid sending false notifications
	if (this.closed) {
		return;
	}
	var Util = require('cloud/util.js');
	// Search for meal we'd like to send out notifications for
	var mealIndex = -1;
	for (var i = 0; i < this.meals.length; i++) {
		if (this.meals[i].toLowerCase().indexOf(mealName) !== -1) {
			mealIndex = i;
			break;
		}
	}
	// Meal not found, oops
	if (mealIndex === -1) {
		console.error("Could not find meal " + mealName);
		return;
	}
	// Load the food items for the meal
	var foods = this[this.meals[mealIndex]];
	for (i = 0; i < foods.length; i++) {
		var food = foods[i];
		var channel = Util.channelName(this.location, food);
		var alert = food + " available at " + this.college;
		Util.sendPush(channel, alert, this.college);
	}
};

Menu.prototype.notifyForMealGroup = function(mealName) {
	// Dining hall is closed, break to avoid sending false notifications
	if (this.closed) {
		return;
	}
	var Util = require('cloud/util.js');
	if (!this.meals) {
		// What went wrong?
		console.error("Wait, what? " + this.college);
	}
	// Search for meal we'd like to send notifications for
	var mealIndex = -1;
	for (var i = 0; i < this.meals.length; i++) {
		if (this.meals[i].toLowerCase().indexOf(mealName) !== -1) {
			mealIndex = i;
			break;
		}
	}
	// Meal not found, oops
	if (mealIndex === -1) {
		console.error("Could not find meal " + mealName);
		return;
	}
	// Load the food items for the meal
	var channels = [];
	var foods = this[this.meals[mealIndex]];
	for (i = 0; i < foods.length; i++) {
		var food = foods[i];
		var channel = Util.channelName(this.location, food);
		channels.push(channel);
	}
	// Send push to all channels
	var alert = mealName.substr(0, 1).toUpperCase() + mealName.substr(1) + " food subscription available at " + this.college;
	Util.sendPush(channels, alert, this.college);
};

Menu.prototype.success = function() {
	if (typeof(this.successCallback) === 'function') {
		this.successCallback();
	} else {
		console.warn("Attempted to call non-function success callback");
	}
};

Menu.prototype.error = function(error) {
	if (typeof(this.errorCallback) === 'function') {
		this.errorCallback(error);
	} else {
		console.warn("Attempted to call non-function error callback");
	}
};

exports.Menu = Menu;
