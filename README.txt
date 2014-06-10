=== UCSC Edibility ===
A UCSC dining hall application with push notifications
Team: Jaiyu Zeng, Joe Wise, Jordan Sommers
Emails: jzen5@ucsc.edu, jdwise@ucsc.edu, jtsommer@ucsc.edu

=== Presentation Link ===
Our Prezi Presentation
http://prezi.com/pqwpphh70zod/

=== Included Resources ===
edibility-android/
	Our Android project
edibility-cloud/
	Cloud code used for implementing server components using Parse.com
	Relevant code we wrote is in edibility-cloud/cloud/
appcompat_v7-edibility/
	An Eclipse project for the Android compatibility library renamed for our project
	This is include to allow for easy building of the project if necessary
Edibility.apk

=== Notes ===
Android Classes:
- BackgroundDownloader and BDCompletionTask for handling an asynchronous download
- ConfirmFoodDialog a popup dialog with checkboxes for subscribing and removing alerts
- EdibilityApplication an application class that handles initialization features
- FoodItem a data class that manages subscription data for a specific food (for all colleges)
- FoodListActivity the primary screen for displaying the menu data
- LocationSelectionActivity the home screen for selecting a dining hall or the alerts list
- NotificationReceiver a class for intercepting push notifications from the server in order
	to pre-process and display a more relevant message to the user
- SavedPreferences a class for managing alert subscriptions and saving/loading SharedPreferences
- Utilities a helper class of static functions used for various data functions in the project

There are two sub-packages: 
- the source files for the Header ListView Open Source project
- deserialization is a package that includes all the classes needed for proper Gson loading
