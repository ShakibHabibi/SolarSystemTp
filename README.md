# SolarSystemTp
![Figure 1](https://github.com/ShakibHabibi/SolarSystemTp/raw/master/screen_2.jpg)

This custom view is fully customizable. Feel free to add issues if you find any bugs. Also, merge requests are welcomed.

## Download
First You need to add jitpack to the repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Then you need to add the library dependency:
```
dependencies {
	        implementation 'com.github.ShakibHabibi:SolarSystemTp:{latest_version}'
	}
```
Now you are good to go.

## Attributes
Here are the customizable attributes and their default values:

| Name 		     |Type            |Default Value |
|----------------|-------------------|----------|
|progress|float|25f|
|progress_width|dimension|16dp| 
|progress_radius|dimension|68dp|
|progress_color|reference|#EF8700|
|progress_bg_color|reference|#33EF8700|
|number_of_rings|integer|5|
|rings_margin|dimension|16dp|
|rings_width|dimension|2dp|
|center_text|string|--|
|center_text_size|dimension|14sp|
|center_text_color|reference|#DE000000|
|center_text_font|reference|Plus Jakarta Sans Regular|
|planet_radius|float|8dp|
|planet_text_size|dimension|10sp|
|planet_text_font|reference|Plus Jakarta Sans Regular|

## Planets
The little circles are called plantes. You can add a list of planets at once:
```
val planets: List<Planet> = listOf(  
    Planet("Galaxy A52"),  
  Planet("My PC"),  
  Planet("Galaxy A52"),  
  Planet("iPhone 8"),  
  Planet("Galaxy Tab34"),  
  Planet("Galaxy A52"),  
  Planet("iPhone 8"),  
  Planet("iPhone 8"),  
  Planet("My PC")  
)  
  
val solarSystemTP=findViewById<SolarSystemTP>(R.id.solar)  
solarSystemTP.setPlanets(planets)
```
You can add or remove planet one by one using `addPlanet` and `removePlanet` methods.

**Notes:**
-  The max number of planets is 8.
- The order of planets' insertion is shown below the image. The location and order of planets are not editable.
- The planet's title length is 10 chars at most and they will be ellipsize end after.

![Figure 2](https://github.com/ShakibHabibi/SolarSystemTp/raw/master/screen_1.jpg)


## Progress
You can change the progress using `setProgress` method.