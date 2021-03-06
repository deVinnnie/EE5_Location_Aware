Building Instructions
---------------------

Use 'Import Project' in Android Studio (Don't use 'Open Project', unless you have no 'Import' option)
Point to the repository directory (Default under Windows: 'Documents/Github/EE5_Location_Aware')

Make sure to run 'Sync Project with gradle files' (Tools->Android) so that the .iml files are regenerated.

Project Modules Explained
-------------------------

* libLocationAware : Contains library code for communications, image manipulation and mathematics.
        This is a Android Library Module, it cannot run standalone on an Android Device.
        (Note: it does however contain Activity classes and layouts. These can be called by applications using the library module)
* demo_basic : Simple Android Application to run the code in app.
                This can be used as the basis for an application only based on Android.
* libgdx-core : For libgdx.
* openCVLibrary2410 : Used by library code, contains the OpenCV framework.
* demo_arrows : The pointing arrows demo using libgdx. This app first launches the SetupActivity.
                You need to press return (the physical button) to get to the actual app. (The first view is to setup the camera and networking)
                New projects based on this one will need to copy some lines within the main build.gradle file and change the name of the project accordingly.

Coupling demo applications to library code.
-------------------------------------------

Edit the build.gradle file and make sure the dependencies are correct:

    dependencies {
        ...
        compile project(':libLocationAware')
    }

Don't copy any activity definitions from the library manifest file to the application manifest.
(Previous instructions are no longer valid. This was the way to do it before the ManifestMerger became active by default)

Make sure the SetupActivity is launched using:

    startActivityForResult(new Intent(getApplicationContext(), com.EE5.SetupActivity.class),0);

Use `startActivityForResult()` for a blocking call. Use `startActivity()` otherwise.

Accessing Position Data
-----------------------

The collected data is stored in the GlobalResources class (See `com.EE5.util.GlobalResources`).
This is a so called Singleton. There can only be one instance of this class.
Instead of calling a constructor, you use the static method `GlobalResources.getInstance()` which will return a reference to the existing GlobalResources instance.

Use `GlobalResources.getInstance().getDevice().getPosition()` to get the position of THIS device.

Use `GlobalResources.getInstance().getDevices()` to get a list of other (connected) devices.

You can use the following construct to iterate over all devices.

    for (Map.Entry<String, Tuple<Position,String>> entry : GlobalResources.getInstance().getDevices().getAll()) {
        System.out.println("key=" + entry.getKey() + "; position=" + entry.getValue().element1 + "; data=" + entry.getValue().element2);
    }

Primer on using git
-------------------

Short answer: use the [Github Client for Windows](https://windows.github.com/).

Long answer (CLI):

* `git clone git@github.com:deVinnnie/EE5_Location_Aware.git` Copies the files to your computer.
* Make changes to the code
* `git add --all` Tell git to index all new files you (might have) created.
* Use `git status` to see which files were changed.
* Make a new commit with `git commit -m 'Some Useful desciption'`
* Push the commit(s) with `git push` to send the commits to the main repository.
* Use `git pull` to download commits from the main repository.

Installing OpenCVManager
------------------------

This method is to be used on the development phones. These do not have a Google Account and thus cannot access the Google Play Store.
The OpenCVManager apk is located in the OpenCV android sdk.

Use the following commands to push the apk to the phone.
(Assumes the phone is connected to computer, and recognized by adb)

    cd OpenCV-2.4.10-android-sdk/apk/
    adb install OpenCV_2.4.10_Manager_2.19_armeabi.apk

A similar approach can be used to install the apk on a simulator. The apk directory contains different .apk files for different architectures. Make sure you pick the right one.

Various links
-------------

* [Java Synchronisation (Monitors)](http://www.programcreek.com/2011/12/monitors-java-synchronization-mechanism/)
* [Git branching and merging](http://git-scm.com/book/en/v2/Git-Branching-Basic-Branching-and-Merging)
* [Android Activities onStart, onCreate, onResume etc...](http://www.tutorialspoint.com/android/android_acitivities.htm)
