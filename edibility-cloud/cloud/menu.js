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
