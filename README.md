Magnet Message Common
========

About
-----
Message Common repository contains the source code that is shared between [Magnet Message Android SDK](https://github.com/magnetsystems/message-sdk-java-android) and [Magnet Message Server](https://github.com/magnetsystems/message-server).

### Build Instructions
----------------------

#### Pre-requisites
- JDK 1.6 or above.
- Maven 3 or above.

1. `git clone https://github.com/magnetsystems/message-common`
2. `mvn clean install -s settings-magnet.xml -gs settings-magnet.xml`

The build will generate a jar file containing the common classes and install it
to your local maven repository. Jar file will be located at
target/mmx-common-api-\<version\>.jar
