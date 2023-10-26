# About
Demo app used during the talk given @ Droidcon London 2023 by @andreasluca and @marcobrador, "REST in Peace: A Journey
Through API Protection".

# Useful pointers
## Hardcoded Secrets
Can be found in `app/src/main/java/com/build38/droidconlondon/HardcodedSecretsProvider.kt`.

## Encrypted Secrets
Can be found in `app/src/main/java/com/build38/droidconlondon/EncryptedSecretsProvider.kt`.

## Certificate Pinning Configuration
Can be found in `app/src/main/res/xml/network_security_config.xml`.

:warning: Reference to previous file in `AndroidManifest.xml`'s `<application>` tag is also necessary.
