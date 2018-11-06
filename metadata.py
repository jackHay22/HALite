import plistlib
import sys

LONG_RELEASE = sys.argv[2]
BUNDLE_PLIST = sys.argv[1]
release_numbers = LONG_RELEASE.split(".")
SHORT_RELEASE = release_numbers[0] + "." + release_numbers[1]

plist = plistlib.readPlist(BUNDLE_PLIST)
plist["NSHumanReadableCopyright"] = "(C) 2018 Ben Parfitt, Jack Hay, and Oliver Keh"
plist["CFBundleVersion"] = LONG_RELEASE
plist["CFBundleShortVersionString"] = SHORT_RELEASE
plistlib.writePlist(plist, BUNDLE_PLIST)
