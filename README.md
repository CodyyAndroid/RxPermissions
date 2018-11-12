# RxPermissions
Android runtime permissions powered by RxJava

[![](https://jitpack.io/v/CodyyAndroid/RxPermissions.svg)](https://jitpack.io/#CodyyAndroid/RxPermissions)

To use this library your `minSdkVersion` must be >= 9.

## How to
**Step 1. Add the JitPack repository to your build file**

Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
**Step 2. Add the dependency**
```
dependencies {
	        //compile 'com.github.CodyyAndroid:RxPermissions:v1.0.2'//based on Rxjava 1.*
	        //compile 'com.github.CodyyAndroid:RxPermissions:v2.0.3'//based on Rxjava2.*
	        implementation 'com.github.CodyyAndroid:RxPermissions:v2.1.0'//based on Rxjava2.*
	}
```
## [API](https://jitpack.io/com/github/CodyyAndroid/RxPermissions/v2.0.3/javadoc/)
## Usage

Create a `RxPermissions` instance :

```java
RxPermissions rxPermissions = new RxPermissions(getSupportFragmentManager()); // where this is an android.support.v7.app.AppCompatActivity instance
RxPermissions rxPermissions = new RxPermissions(getChildFragmentManager()); // where this is an android.support.v4.app.Fragment instance
```

Example : request the CAMERA permission (with Retrolambda for brevity, but not required)

```java
// Must be done during an initialization phase like onCreate
rxPermissions
    .request(Manifest.permission.CAMERA)
    .subscribe(granted -> {
        if (granted) { // Always true pre-M
           // I can control the camera now
        } else {
           // Oups permission denied
        }
    });
```

If you need to trigger the permission request from a specific event, you need to setup your event
as an observable inside an initialization phase.

You can use [JakeWharton/RxBinding](https://github.com/JakeWharton/RxBinding) to turn your view to
an observable (not included in the library).

Example :

```java
// Must be done during an initialization phase like onCreate
RxView.clicks(findViewById(R.id.enableCamera))
    .compose(rxPermissions.ensure(Manifest.permission.CAMERA))
    .subscribe(granted -> {
        // R.id.enableCamera has been clicked
    });
```

If multiple permissions at the same time, the result is combined :

```java
rxPermissions
    .request(Manifest.permission.CAMERA,
             Manifest.permission.READ_PHONE_STATE)
    .subscribe(granted -> {
        if (granted) {
           // All requested permissions are granted
        } else {
           // At least one permission is denied
        }
    });
```

You can also observe a detailed result with `requestEach` or `ensureEach` :

```java
rxPermissions
    .requestEach(Manifest.permission.CAMERA,
             Manifest.permission.READ_PHONE_STATE)
    .subscribe(permission -> { // will emit 2 Permission objects
        if (permission.granted) {
           // `permission.name` is granted !
        } else if (permission.shouldShowRequestPermissionRationale)
           // Denied permission without ask never again
        } else {
           // Denied permission with ask never again
           // Need to go to the settings
        }
    });
```

Look at the `app` sample for more.
## [LICENSE](https://github.com/CodyyAndroid/RxPermissions/blob/master/LICENSE)
```
Copyright 2016 codyy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

