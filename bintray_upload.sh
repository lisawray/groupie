#!/bin/bash

./gradlew library:bintrayUpload
./gradlew library-databinding:bintrayUpload
./gradlew library-kotlin-android-extensions:bintrayUpload
./gradlew library-viewbinding:bintrayUpload
