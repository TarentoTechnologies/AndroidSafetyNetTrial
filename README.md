# AndroidSafetyNetTrial
Android SafetyNet Attestation API trial


## Steps to run the samples (tested in Ubuntu)
- Clone the repo

### To run the server
- On the command prompt, navigate to the ServerAPI folder within the repo
- Edit the application.properties to allow the acceptable SHA256 hashes of the APK and Signing certificate
- From the Server API folder, run the following command-  mvn package && cp src/main/resources/application.properties target/application.properties && java -jar target/safetynet-1.0.0.jar --spring.config.location=target/application.properties

### To run the app
- Open the Android project in Android Studio
- Update the gradle.properties file with the URL of the server and the API key generated for using Google Safety Net as per the [google guidelines](https://developer.android.com/training/safetynet/attestation.html#add-api-key)
- Generate 2 signed APKs with 2 certificates from the same source code
- Install the first APK on a device and try the Verify Device button. Verification will succeed if the app's signing certificate matches that mentioned in the server's application.propertis

### Important Note
I could not generate the signing certificate hash, so I ran the app on a device and captured the certificate hash that was returned in the app's attestation signature and used this value in the server application.properties. Post this, I was able to detect if the same source code was signed using a different certificate. 



