# Makefile for Edibility
PROJECT=edibility-android
CLOUD=edibility-cloud
APPCOMPAT=appcompat_v7-edibility
README=README.txt

# Default target for parse deploys
# For deploying code to parse servers
parsesync:
	cd edibility-cloud; parse deploy

# Target to zip up source files
zip:
	zip -r edibility.zip ${APPCOMPAT} ${README} ${PROJECT} ${CLOUD} Edibility.apk --exclude \*/bin/\* \*/gen/\*
