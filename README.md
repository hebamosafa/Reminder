# Reminder
Reminder App gives the ability of saving important dates to remind the user with it & saves all previous dates in local database.
it can remind the user with the saved event by notification or ringing
## Reminder structure 
### Main Activity
* It shows all the dates saved before in the database & allows user to add new date by click the add button which in turn generate intent   to another activity to get the user data info.
* If the user clicks on any date , the app captuer the id of the recycleview selcted item and get all its data in another activity using     loaders
 ### The second activity which make the user enter:
  * The event name
  * The date (by popping up the Calender)
  * Snooze (Radio Buttons)
* After filling out the data we start to save it in local sqlite database to be showed in the main activity and start broadcast receiver 
### Preferances Activity
* This app uses shared preferances & its change listener to be aware of the user desicion for reminder(NOtofication or ringing)
