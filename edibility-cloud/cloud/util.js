// Function that converts a location code and food item pair into a channel name
// Format: locationCode(two digit string)_fooditemlowercasenospace
exports.channelName = function(locationCode, foodItem) {
	var channel = locationCode + '_' + foodItem;
	// Strip whitespace and convert to lower case
	return channel.replace(/\s/g, "").toLowerCase();
};
