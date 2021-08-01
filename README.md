# Case3FlashLight

Android RuntimePermissionsBasic Sample

This basic sample shows runtime permissions available in the Android M and above.
It shows how to use the new runtime permissions API to check and request permissions through the
support library.
 

Introduction
------------

Android M introduced runtime permissions. Applications targeting M and above need to request their
permissions at runtime.
This sample introduces the basic use of the runtime permissions API through the support library by
verifying permissions (ActivityCompat#checkSelfPermission(Context, String)), requesting permissions (ActivityCompat#requestPermissions(Activity, String[], int))
and handling the permission request callback (ActivityCompat.OnRequestPermissionsResultCallback).
An application can display additional context and justification for a permission after calling
ActivityCompat#shouldShowRequestPermissionRationale#shouldShowRequestPermissionRationale(Activity, String).

See the "RuntimePermissions" sample for a more complete description and reference implementation.

Pre-requisites
--------------

- Android SDK 28
- Android Build Tools v28.0.3
- Android Support Repository

## Screenshots
<img src="screenshot.png" height="400" alt="Screenshot"/> 

## Getting Started
* Android Studio: Download this case from github and open it directly in Android Studio.
* Other IDE: This case uses the Gradle build system. To build this project, use the "gradlew build" command or use "Import Project".

## Support
1. Email: hypech.com@gmail.com
2. Paperback: [Hands-on App Development in Android Studio 4.2](https://www.amazon.com/gp/product/B096TL8VMP) (22 Complete Android Cases with Step-by-Step Instruction to Grow from Novice to Android Expert)
3. Preview: [Hands-on App Development in Android Studio 4.2](https://play.google.com/books/reader?id=9g8zEAAAQBAJ)
4. GitHub: https://github.com/hypech/
5. Youtube: https://youtu.be/-fGgwyymOyI
6. Twitter: https://twitter.com/hypechor
7. Web: http://hypech.com

If you've found an error in this case, please file an issue on top left of this screen besides <>code.

Patches are encouraged, and may be submitted by forking this project and submitting a pull request through GitHub. 

<pre>
Copyright 2021 The Learning Android with Cases Open Source Project
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
      http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
</pre>
