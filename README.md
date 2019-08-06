# EasyLedger

## An Android MVVM architecture Ledger app that extracts user's banking sms and convert to transcations and show in chart.

## Content
- Demo GIF
- Key Feature
- How it works?
- Thrid-party Library


### Demo GIF


![](open.gif)
![](chart.gif)


### Key Feature

- Single activity app
- ROOM
- LiveData
- ViewModel
- Google Firebase Cloud Message
- Backward Compatibility to API 16 (Jelly Bean)
- Battery friendly

### How it works?

:point_right:**Three ways to create a transaction**

1. User manually enter a transaction
2. App extracts banking sms and covert to a transaction (show in untagged area and need to be tagged/categorized by user)
3. App extracts banking sms, covert and categorize it automatically without any user input. (Autotagger function)

:point_right:**SMS extracting logic**

The app receives sms broadcast, when a new sms received, the app will first check sender number, if not matching with the internal data. The app will try to read msg content to see if there is any banks keywords. When app decide which bank this sms comes from, its relative extracting method will be applied to extract amount, time, bank and merchant name into a record/transaction.

:point_right:**What make app battery friendly**

When app got killed, the custom broadcast will be end, and the same time, a timestamp will be saved (EndTime). When app is re-launched, the app will make another timestampe (StartTime) then turn on broadcast receiver to extract msg. As soon as the StartTime is available, the app will read history sms between EndTime and StartTime, and add qulified transaction into DB without interrupt user's forground interaction. This way the app "behaves" like its never sleep for user and it's in fact very battery friendly.
Plus, due to the Model View ViewModel architecture, any change in DB will be reflect to UI, but won't causing any problem.


:point_right:**An ideal situation of user experience**

Ideally, when user use it for a while, there is no need to enter anything because app tracks user's history tagging behavior and mark/categorize transaction automatically, user only need to see the charts. 

E.g. When a banking text message is received by app (and user turn on message extraction in setting). The app will extract the information from the text message (Time, amount, merchant ...)  and present to "Untagged" area, so user can tag (categorize) them. e.g. if user got a msg shows that the merchant name is "John Bob House" and user tag it as "Restaurant". Then all the following msg that has same mechant name (John Bob House) , will be automatically categorized into Restaurant. Unless user tag it to another category in the future.


### Thrid-party Library
Thanks for the providers of these libraries!

- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
- [JodaTime](https://www.joda.org/joda-time/)
- [AutoFitTextView](https://github.com/grantland/android-autofittextview)
- [Gson](https://github.com/google/gson)
