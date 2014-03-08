[![Build Status](https://travis-ci.org/trusthoarder/signer.png?branch=master)](https://travis-ci.org/trusthoarder/signer)
# Signer

1. If the user has a verified public key, go to (8).

2. Prompt user for search criteria.
3. Search pool, present results.
4. Confirm selection.
5. Download public key.
6. Send encrypted email to UIDs with verification tokens (plus URL).
7a. User selects unverified UID, enters token.
7b. User follows URL, UID is verified.

8a. View list of keys that you have not yet signed and pushed.
8c. Show your fingerprint's QR code.
8b. Scan a QR code; key is added to list.
8b.i. If the user has somehow given us their signing key, send them an
      encrypted, signed email with a script they can run to import and
      sign all the keys.

## Security

* Public key is stored on device, unencrypted, signed.
* List of keys to sign stored on device, encrypted, signed.
* HKP is sent over SSL.
* UI lock with PIN.


## Building & Testing

Signer is built using the Maven build system, version 3.1.1 or higher.
To build the distributable .apk:

    mvn clean package

The signer-<VERSION>.apk will be available under target/ directory.

To run unit and integration tests:

    mvn clean verify

This depends on the `ANDROID_HOME` and `JAVA_HOME` variables being set.
For example:

    export ANDROID_HOME=$HOME/adt-bundle-linux-x86_64-20131030/sdk
    export JAVA_HOME=/usr/lib/jvm/default-java

The `platform-tools` and `tools` directories must be in your path:

    export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools

This uses [maven-android-plugin][].

[maven-android-plugin]: https://code.google.com/p/maven-android-plugin/wiki/GettingStarted
