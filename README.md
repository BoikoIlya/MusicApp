# MusicApp

An application with music. At the moment 25.05.23, this is my biggest pet project after a year and half of android development. All logic + tests is 12k lines it took 4 monthes  or 200+ hr was tracked by WakaTime plagin in AndroidStudio. Project covered with Unit tests and was written with idea of testable and extensible code followed SOLID. A free API from Spotify, authorization by token and XML layout is used. The project is single-module divided into 11 packages, each of which has a clean architecture structure inside.

### Release build apk: [4.1-alpha.zip](https://github.com/BoikoIlya/MusicApp/files/11568281/4.1-alpha.zip)


## Stack:
- Unit tests Junit
- Kotlin
- XML
- Retrofit
- Room
- Dagger 2
- Coroutines
- State and shared flow
- Media3
- Firebase: FCM, FireStorage
- Paging 3
- Palette
- Glide
- Nav Component
- SharedPrefs

## Features:
- Trending: The first page displays popular playlists as well as tracks. It has add to favorites button in front of every item in list. If you have added track before and try to click this button, you will see dialog that offer you to replace track.   
- Favorites: Displays the added tracks that are in the room database. There is a sorting, search and a shuffle button. Also, the deletion takes place by swipe.
- Search history: displays the search history that is stored in the room database, it is possible to delete items separately and also clear the entire history at once. When entering data in the input field, a history search is performed, if 0 matches are found, 1 element is displayed with text as in the input field.
- Search: The found items are displayed. pagination is implemented. when you click on the input field, you return to a fragment of the search history.
- Playlist: On the main screen, when clicking on a playlist, a request is made for this playlist and tracks from it are displayed, a picture description and title. there is also a shuffle button. If description is displayed with elipsize at the end you can click on it and it will open full description in alert fragment.
- Player: Implemented MediaSessionService. When you start a track, a lower panel appears from below with information about the track and the next, previous, pause buttons. When you click on this panel, the bottom sheet goes out and the full information about the track is displayed, as well as more buttons: add and removal from favorites, the shuffle playback button (not to be confused with the shuffle button in favorites and playlist) also a repeat button. With the help of the palette library, a dynamic shadow is implemented under the photo. There is also a button at the bottom that navigates to a fragment queue, this can be done by swiping since there is a pager is implemented there.
- Queue: The queue of current tracks is displaying, there is a button that scrolls the list to the very top and a button that scrolls the list to the playing track.
- Dispatches: Implemented a cloud messaging firebase.
- Update system: every time you run the application, it makes a request to the firebase and receives the data of the latest update, if current app version don’t match version that have gone from firebase, a dialog fragment open, after clicking the download button, an intent occurs in the browser and the apk is downloaded.
Authorization in the API: when entering the application, it makes requests to the network by reading the token from the shared preferences (the validity of the token is 60 minutes) and in case of non-validity of the token, the code 401 comes, in this case a request is made for a new token and save in the shared preference and new requests are made that have been failed.
- Other: for devices with android version 13 and higher, will be shown permission to display notifications.



## ScreenShots:
![msg873234081-315243](https://github.com/BoikoIlya/MusicApp/assets/100340546/893f363f-a7b7-4f1b-afb2-f6ecaf07734f)
![msg873234081-315246](https://github.com/BoikoIlya/MusicApp/assets/100340546/3bc5fc90-272f-46a8-9a42-868ff3be410f)
![msg873234081-315250](https://github.com/BoikoIlya/MusicApp/assets/100340546/522396c3-7225-4bc2-b41d-963a922dbc08)
![msg873234081-315245](https://github.com/BoikoIlya/MusicApp/assets/100340546/d69a92b0-dda7-43a8-8511-c4378f59b803)
![msg873234081-315249](https://github.com/BoikoIlya/MusicApp/assets/100340546/7287a4a0-0eb7-4305-8856-d4ac13f5f673)
![msg873234081-315247](https://github.com/BoikoIlya/MusicApp/assets/100340546/3b9fe805-2299-414b-9ca5-57566fd990b3)
![msg873234081-315248](https://github.com/BoikoIlya/MusicApp/assets/100340546/0c00c586-d965-49cc-b829-afab048d93c6)
![msg873234081-315244](https://github.com/BoikoIlya/MusicApp/assets/100340546/c5531f0b-a5f7-42f6-a7e1-6a3b4d23197e)
