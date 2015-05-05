
Building Instructions
---------------------

Use 'Import Project' in Android Studio (Don't use 'Open Project')
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
* demo_arrows : The pointing arrows demo using libgdx.

Accessing Position Data
-----------------------

The collected data is stored in the GlobalResources class (See `com.EE5.util.GlobalResources`).
This is a so called Singleton. There can only be one instance of this class.
Instead of calling a constructor, you use the static method `GlobalResources.getInstance()` which will return a reference to the existing GlobalResources instance.

Use `GlobalResources.getInstance().getDevice().getPosition()` to get the position of THIS device.

Use `GlobalResources.getInstance().getDevices()` to get a list of other (connected) devices.

You can use the following construct to iterate over all devices.

    for (Map.Entry<String, Position> entry : GlobalResources.getInstance().getDevices().getMap().entrySet()) {
        System.out.println("key=" + entry.getKey() + "; value=" + entry.getValue());
    }
