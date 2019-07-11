  FROM ubuntu:latest


    RUN cd /opt

    RUN mkdir android-sdk-linux && cd android-sdk-linux/

    RUN apt-get update -qq \
    && apt-get install -y openjdk-8-jdk \
    && apt-get install -y wget \
    && apt-get install -y expect \
    && apt-get install -y zip \
    && apt-get install -y unzip \
    && rm -rf /var/lib/apt/lists/*

    RUN wget https://dl.google.com/android/repository/tools_r25.2.3-linux.zip

    RUN unzip tools_r25.2.3-linux.zip -d /opt/android-sdk-linux

    RUN rm -rf tools_r25.2.3-linux.zip

    ENV ANDROID_HOME /opt/android-sdk-linux

    ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/

    ENV PATH ${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/platform-tools:${JAVA_HOME}/bin

    RUN echo y | android update sdk --no-ui --all --filter platform-tools | grep 'package installed'

    # SDKs
    # Please keep these in descending order!
    RUN echo y | android update sdk --no-ui --all --filter android-25 | grep 'package installed'
    RUN echo y | android update sdk --no-ui --all --filter android-24 | grep 'package installed'
    RUN echo y | android update sdk --no-ui --all --filter android-23 | grep 'package installed'
    RUN echo y | android update sdk --no-ui --all --filter android-18 | grep 'package installed'
    RUN echo y | android update sdk --no-ui --all --filter android-16 | grep 'package installed'

    # OR you can install everything like this
    # RUN echo y | android update sdk --no-ui | grep 'package installed'

    # build tools
    # Please keep these in descending order!
    RUN echo y | android update sdk --no-ui --all --filter build-tools-25.0.2 | grep 'package installed'
    RUN echo y | android update sdk --no-ui --all --filter build-tools-25.0.1 | grep 'package installed'
    RUN echo y | android update sdk --no-ui --all --filter build-tools-25.0.0 | grep 'package installed'
    RUN echo y | android update sdk --no-ui --all --filter build-tools-24.0.3 | grep 'package installed'
    RUN echo y | android update sdk --no-ui --all --filter build-tools-24.0.2 | grep 'package installed'
    RUN echo y | android update sdk --no-ui --all --filter build-tools-24.0.1 | grep 'package installed'
    RUN echo y | android update sdk --no-ui --all --filter build-tools-23.0.3 | grep 'package installed'
    RUN echo y | android update sdk --no-ui --all --filter build-tools-23.0.2 | grep 'package installed'
    RUN echo y | android update sdk --no-ui --all --filter build-tools-23.0.1 | grep 'package installed'

    RUN android list sdk --all

    # Update SDK
    # This is very important. Without this, your builds wouldn't run. Your image would aways get this error:
    # You have not accepted the license agreements of the following SDK components:
    # [Android SDK Build-Tools 24, Android SDK Platform 24]. Before building your project,
    # you need to accept the license agreements and complete the installation of the missing
    # components using the Android Studio SDK Manager. Alternatively, to learn how to transfer the license agreements
    # from one workstation to another, go to http://d.android.com/r/studio-ui/export-licenses.html

    #So, we need to add the licenses here while it's still valid.
    # The hashes are sha1s of the licence text, which I imagine will be periodically updated, so this code will
    # only work for so long.
    RUN mkdir "$ANDROID_HOME/licenses" || true
    RUN echo -e "\n8933bad161af4178b1185d1a37fbf41ea5269c55" > "$ANDROID_HOME/licenses/android-sdk-license"
    RUN echo -e "\n84831b9409646a918e30573bab4c9c91346d8abd" > "$ANDROID_HOME/licenses/android-sdk-preview-license"

    RUN apt-get clean

    RUN chown -R 1000:1000 $ANDROID_HOME

    VOLUME ["/opt/android-sdk-linux"]

    RUN mkdir -p /www

    WORKDIR /www

    ADD ./ /www

    RUN chmod +x gradlew