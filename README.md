# AndroidSafetyNetTrial
Android SafetyNet Attestation API trial


## Steps to run the samples (tested in Ubuntu)
- Clone the repo

### To run the app
- Open the Android project in Android Studio
- Update the gradle.properties file with the URL of the server (URL of the server below and it should be accessible from the mobile app) and the API key generated for using Google Safety Net as per the [google guidelines](https://developer.android.com/training/safetynet/attestation.html#add-api-key)
- Generate a signed APKs with from the source code using a signing key
- Capture the APK and certificate signing SHA256 hashes following the ***important note*** below
- Install the APK on a device

### To run the server
- On the command prompt, navigate to the ServerAPI folder within the repo
- Edit the src/main/resources/application.properties and update the SHA256 hashes of the APK and Signing certificate captured earlier
- From the Server API folder, run the following command-  mvn package && cp src/main/resources/application.properties target/application.properties && java -jar target/safetynet-1.0.0.jar --spring.config.location=target/application.properties

### To test the app
- Install the APK on a device and try the Verify Device button. Verification will succeed if the app's signing certificate hash matches that mentioned in the server's application.properties
- Regenerate an APK with a different signing key at this point and install it on the device. This time the verification will fail.


### Important Note on generating SHA256 hashes in base64 (required for the Server's application.properties)
The following command can be used to find the SHA256 in base64 for the signing certificate
- keytool -exportcert -alias aliasname -keystore /path/to/jksfile | openssl sha256 -binary | openssl base64

The following command can be used to find the SHA256 in base64 of the APK
- cat /path/to/signedapk | openssl sha256 -binary | openssl base64


