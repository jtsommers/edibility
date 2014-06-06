// Function that converts a location code and food item pair into a channel name
// Format: locationCode(two digit string)_fooditemlowercasenospace
exports.channelName = function(locationCode, foodItem) {
	var channel = "foodalert_" + locationCode + '_' + foodItem;
	// Strip whitespace and convert to lower case
	return channel.replace(/\s/g, "").toLowerCase();
};

exports.sendPush = function(channel, food, location) {
	var alertString = "Food Alert! Check your subscriptions";
	if (food !== null) {
		alertString = food + " is available";
	}
	if (location !== null) {
		alertString += (" at " + location);
	}
	Parse.Push.send(
		{
			channels: [ channel ],
			data: {
				alert: alertString
			}
		}, 
		{
			success: function() {
				// Push was successful
				response.success("Test Push Sent");
			},
			error: function(error) {
				// Handle error
				console.error("Push error sending to " + channel);
			}
		}
	);
};
