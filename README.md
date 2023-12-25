# BinThere
BinThere is an application designed to tackle environmental cleanliness through community engagement.
Its primary goal is to identify and manage litter hotspots by leveraging user-contributed data.
Users can add points of interest for litter they encounter, complete with photos, descriptions, and precise locations.
Additionally, the app aims to incorporate public bin locations, allowing users to not only
locate the nearest bins but also report overflowing ones and suggest new bin locations.


## Development
### MVP to MVVM Refactor
For the second assignment, I chose to work on the same project/repository and expand
on the application built so far. As the initial approach I was trying to refactor the app from MVP
to MVVM design pattern component by component while keeping the application functional in between
the changes. This turned out to be quite challenging and after much delay I decided to go back and
start with a bare minimum MVVM skeleton and reintroduce the features from the previous version
of the application. In the end the MVVM pattern seems to be more convenient for the development.
However,refactoring the application was quite costly and I could definitely do it more efficiently
next time around.

## Features
### Signup/Login
<img src="docs/images/signup_login.png" height="720px">

### Navigation bar
<img src="docs/images/navigation_bar.png" height="720px">

### Bins and litter list
<img src="docs/images/poi_list_view.png" height="720px">

### Add Bin/Litter (POI)
<img src="docs/images/add_poi_view_spinner.png" height="720px">
<img src="docs/images/photo_picker.png" height="720px">

### View Bin/Litter - Cleanup action
<img src="docs/images/poi_detail_view.png" height="720px">

### About View
<img src="docs/images/about_view.png" height="720px">

### Firebase backend
<img src="docs/images/firebase_realtime_db.png">

## App Diagram
TODO

## Resources
- [timberkt - Kotlin logging extensions for Timber](https://github.com/ajalt/timberkt)
- [Medium: Ian Lake - Layouts, Attributes, and you](https://medium.com/androiddevelopers/layouts-attributes-and-you-9e5a4b4fe32c)
- [Data Classes in Kotlin](https://antonioleiva.com/data-classes-kotlin/)
- [developer.android.com - Create dynamic lists with RecyclerView](https://developer.android.com/develop/ui/views/layout/recyclerview)
- [developer.android.com - Set up the app bar](https://developer.android.com/develop/ui/views/components/appbar/setting-up)
- [Kotlin - Parcelize - Parcelable implementation generator](https://developer.android.com/kotlin/parcelize)
- [developer.android.com - Storage Access Framework](https://developer.android.com/guide/topics/providers/document-provider)
- [developer.android.com - Splash screen](https://developer.android.com/develop/ui/views/launch/splash-screen)

## Samples
- [MapsActivityCurrentPlace](https://github.com/googlemaps-samples/android-samples/blob/main/tutorials/kotlin/CurrentPlaceDetailsOnMap/app/src/main/java/com/example/currentplacedetailsonmap/MapsActivityCurrentPlace.kt)
- [MyLocationDemoActivity](https://github.com/googlemaps-samples/android-samples/blob/main/ApiDemos/java/app/src/gms/java/com/example/mapdemo/MyLocationDemoActivity.java)