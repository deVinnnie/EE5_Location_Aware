
Building Instructions
---------------------

Use 'Import Project' in Android Studio (Don't use 'Open Project')
Point to the repository directory (Default under Windows: 'Documents/Github/EE5_Location_Aware')

Make sure to run 'Sync Project with gradle files' (Tools->Android) so that the .iml files are regenerated.

Project Modules Explained
-------------------------

* app : Contains library code for communications, image manipulation and mathematics.
        This is a Android Library Module, it cannot run standalone on an Android Device.
        (Note: it does however contain Activity classes and layouts. These can be called by applications using the library module)
* demo_basic : Simple Android Application to run the code in app.
                This can be used as the basis for an application only based on Android.
* libgdx-core : For libgdx.
* openCVLibrary2410 : Used by library code, contains the OpenCV framework.
* demo_arrows : The pointing arrows demo using libgdx.

