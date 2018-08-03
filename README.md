# ItunesMusic-Player
Play Music from the Itunes Store

![alt text] (https://drive.google.com/open?id=1JReqz9DBhNOuXfZC2weiPn3s8ybgpg87)

Libraries Used:
1. Room - for SQLite database abstraction. Performing database transactions asynchronously and utilising its live data capabilities.
2. Dagger2 - Dependency Injection.
3. Glide - Image Loading.
4. Retrofit, OkHTTP, Gson - Networking, Logging and JSON parsing.
5. Joda Time - Time Formatting
6. RxJava2 - Asynchronous mode and reactive programming.
7. ViewPager Indicator
8. Seekbar.

Assumptions
1. Toolbar is a personalised one which allows itemised handling of its features as well as functions.
2. Width of each item in list.
3. ImageSize on Playing a song.
4. Only Preview url is to be played on playing a song.
5. AutocompleteTextView for loading search preferences.
6. Have not put in a Splash Screen because I did not find one in the resources provided.
7. Height of view pager indicator.

What I have tried to Implement.
1. MVVP with single responsibility principle throughout the app. 
2. Tried to keep the data processing and manipulation only with the view model.
3. All database transactions are asynchronous.
4. Navigation responsibilty restricted to the activity for better performance.
5. Reusibilty of views by using only a single activity.
6. Added progress bar for song preparation.
7. FAB for play/pause.
8. Reactive favourites addition and removal.
9. Proper sequence of handling clicks from the viewholder to the adapter to the fragment to the activity.
10. LifeCycle aware viewmodel has no instances of context leakage.


What more could I have done?
1. Write Junit test cases.
2. Test app on different mobile screen sizes.
3. Find a better alternative to break the list that the one deployed for view pager.
4. Add fragment enter and exit transitions.
5. Better way to highlight items in list.
6. Create my own backstack to manage fragments.
7. Implement the toolbar using builder pattern.
8. Implement a separate singleton manager mediaplayer class.
9. Use Exoplayer instead of Mediaplayer.



