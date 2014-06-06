// "Constants" needed for translating college names to location numbers
var DINING_HALLS = {
	"cowell"	: "05",
	"crown"		: "20",
	"eight"		: "30",
	"nine"		: "40",
	"porter"	: "25"
};

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
				self.success(); // Temporary, don't callback with response text
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
		console.log("Sending notification for " + food);
		var channel = Util.channelName(this.location, food);
		Util.sendPush(channel, food, this.college);
	}
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
