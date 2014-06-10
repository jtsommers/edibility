// 
// Utility functions for server code
// 
// Defines several useful utility functions for use in Parse Sever Javascript code
// Import Utilities into other sever code using:
//  var Util = require('cloud/util.js');
// Then call utility functions using e.g. Util.channelName(params);
// 

// Function that converts a location code and food item pair into a channel name
// Format: locationCode(two digit string)_fooditemlowercasenospaceorspecialchars
exports.channelName = function(locationCode, foodItem) {
	var channel = "foodalert_" + locationCode + '_' + foodItem;
	// Strip whitespace and convert to lower case
	channel = channel.replace(/\s/g, "").toLowerCase();
	// Strip special characters not allowed in Parse channel names
	channel = channel.replace(/[,.'";:?!\/\\&@#$%^*()]/g, "");
	return channel;
};

// Utility function to send a Push notification alert
//  @param channel = single channel as string or an array of channels
//  @param alert = an alert string to send to the client
//    NOTE: this is only a fallback option, alerts are intercepted on the client
//    and message displayed is based on the channel name that triggered the alert
//  @param college = string from the following list:
//    eight, nine, cowell, porter, crown
exports.sendPush = function(channel, alert, college) {
	var alertString = "Food Alert! Check your subscriptions";
	if (alert !== null) {
		alertString = alert;
	}
	// If single element convert to array
	// Otherwise expected input is an array
	var c = (typeof(channel) === 'string') ? [channel] : channel;

	if (college === null) {
		college = "";
	}
	Parse.Push.send(
		{
			channels: c,
			// This data allows the message to be intercepted by a custom receiver
			data: {
				"header": alertString,
				"action": "edu.ucsc.teambacon.edibility.MESSAGE",
				"college": college
			}
		}, 
		{
			success: function() {
				// Push was successful
				// Purposefully empty, I think
				// console.log("Push sent to " + channel);
			},
			error: function(error) {
				// Handle error
				console.error(error);
			}
		}
	);
};
