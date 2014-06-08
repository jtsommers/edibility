// Function that converts a location code and food item pair into a channel name
// Format: locationCode(two digit string)_fooditemlowercasenospace
exports.channelName = function(locationCode, foodItem) {
	var channel = "foodalert_" + locationCode + '_' + foodItem;
	// Strip whitespace and convert to lower case
	return channel.replace(/\s/g, "").toLowerCase();
};

exports.sendPush = function(channel, alert) {
	var alertString = "Food Alert! Check your subscriptions";
	if (alert !== null) {
		alertString = alert;
	}
	// If single element convert to array
	// Otherwise expected input is an array
	var c = (typeof(channel) === 'string') ? [channel] : channel;
	Parse.Push.send(
		{
			channels: c,
			data: {
				alert: alertString
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
				console.error("Push error sending to " + channel);
			}
		}
	);
};
