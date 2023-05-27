# Recipe Source App

## Demo
https://youtu.be/thIgGrc7Q5k

## FUNCTIONAL DESCRIPTION
The app is titled Recipe Source App, and it’s a What’s In My Fridge App which finds recipes according to ingredients selected. \

### CODE DESCRIPTION 
My app has 3 Activities, and 8 fragments. 
The first activity is a splash screen which is just display a graphic. A handler is used to delay the graphic, so it is seen and then an intent to either the login in page or the generate recipe fragment depending on whether the user is already logged in or not.<br/><br/>
The second activity holds the login and registration fragment. The login fragment is the default for this activity and from the login activity, the user may click the Create Account button to register. In the login fragment, the user is authenticated with Firebase Authentication and if successful in authentication, the Main Activity page is called, a similar process is done for registration with firebase creating a new user. <br/><br/>
The third activity, the main activity, holds the bulk of the fragments. It has an action bar and a navigation drawer to use to go through different fragments. The fragments are managed by the fragment manager here. Each fragment has been added as a menu item on the navigation, and from here, the user navigates through the app. To communicate between, a View Model class is used and updated as needed in the different fragments.  When you first register and login for the first time, a dialog box pops up and asks you to sign in agreement to the terms of use. Only after agreeing can you move to the next fragment, the main page which is an image view with a welcome message to the user.<br/><br/>
Clicking the action bar and selecting generate fragment will take the user to a page that displays all the ingredients. These ingredients are provided by the Spoonacular API and it is a list of their top 1,000 ingredients which has been stored in firebase for easy access. On this page, we have a recycler view and a generate button. The recycler view is set up using the Firebase Paging Adapter (see Adapters Package) and this was recommended for large, mostly unchanging data. Given a query of my ingredient collection in firebase, the recycler can get those ingredients and display them in the recycler view. I have also set up search capability so that as a user types, a new query is created for firebase and passed to a new instance of the recycler view adapter. The view holder of the adapter has been set up to allow ingredients to be added on click with a color change and remove with another click, which also resets the color. The adapter is also, with the help of the view model, able to remember the position of a clicked ingredient so that when a user scrolls pasts a clicked ingredient, and returns, the ingredient remains clicked. This ingredient also remains clicked if you search for the ingredient. <br/><br/>
Once you press the generate button, the fragment manager replaces the current page with another fragment which is a list of generated recipes. The recipes are gotten from Spoonacular API as a combination of find recipes by ingredients which returns a list of recipes and their ids, along with recipe information bulk which takes the recipe Ids and returns a list of recipes (see Request Manager).  Callback with interfaces are used to ensure that the functions wait till a result is gotten. The recipes are converted with a Gson converter. Classes for each part of the recipe was created to allow for smooth conversion (see Models package). The limit of recipes shown is 20 recipes for this app but Spoonacular allows up to 100. Each recipe can be viewed or added to My Recipes, which on click of the Add Recipe button, sends the recipe to firebase to be stored. <br/><br/>
If you click the View Recipe button, another fragment, the recipe info fragment opens which is the page to see more details about the recipe. This is done again with the help of the view model which stores the position of the recipe, and the information is used to assign the appropriate data to the appropriate views on the page.<br/><br/>
If you click on the action bar at any point, you can visit the My Recipes fragment and see all the recipes a user has added. The default button here is the remove button whereas for the generated recipes fragment, it is the Add Recipe button. This is done with the help of the view holder in the adapter to know which fragment is calling it. <br/><br/>
This app also has the about and logout button. The logout button calls firebase to sign out the user and end the main activity. <br/><br/>
This app also has a Utilities page called Tools where all the Firebase calls are stored, and relies on interface call backs to ensure there are no race conditions due to the asynchronous calls made by firebase.

### SPEC SHEET 
Firebase – This is used to store all user information and recipe.<br/>
Spoonacular API – This API is used to get recipes to be used in the app.<br/>
Picasso – For image loading.<br/>
Retrofit – For the requests to the Spoonacular API.

### LIST OF FEATURES
The first Activity is a splash screen which is loaded when you open the app for the first time. The next Activity is a combination of two fragments, the login and registration page. The login page is what comes after the splash screen. From here, you can also click a button to go the registration page. After logging in, if you are logging in for the first time, you must agree to the terms of use. Then you are taken to the home page. From here, you can access the action bar and go to generate recipes where you can pick and choose from a list of over 1,000 ingredients. You can search through and filter the ingredients. When you have chosen your ingredients, you can generate recipes based on those ingredients. From the list of generated recipes, you can view a recipe or add it to your recipe list, which can be viewed in the View Past Recipes page. You can also visit the about page which gives some other information about the app and developer contact details. You can also log out by clicking the logout button.

### TEST CASES
This app has been tested for crashes, that back stack is working properly.

### KNOWN BUGS
Flowables are not cleared on the firebase stream used which causes an error when trying to navigate to the main activity. There is error handling to catch the exception thrown. 
Due to ending the main activity when a user clicks logout, any firebase asynchronous calls might be interrupted which causes it to throw an error. Will work on how to ensure the fragments onPause() can clean up any possible errors. 
Devices leave an instance of a user even after logging out. This will show if you login, logout then uninstall the app. The app will log you in automatically despite not being logged in. It will immediately close the app and then open the log in app like it should but it does have that bug that I am working on.


