# FlexibleRatingBar
FlexibleRatingBar is an improved version of Android's default RatingBar. 


Want your rating bar to size itself appropriately to a given area? 
Want it to actually listen when you tell it to display 5 stars and match_parent? 
Want to be able to easily change the color? 
Want stars with more or fewer than 5 vertices? 
Tired of it cutting off your stars, or introducing odd padding issues if you try to scale it? 

FlexibleRatingBar fixes all of that.

![](http://i.imgur.com/oAYAwB7.png)

## Including in your Project

FlexibleRatingBar should work all the way back to 2.3, and possibly earlier than that. To get started, add the following dependency to your `build.gradle`.
    
    (currently getting this set up with maven/jcenter. will update when available)
    


## Usage

```xml
<amagi82.flexibleratingbar.FlexibleRatingBar
  android:id="@+id/flexibleRatingBar"
  android:layout_width="wrap_content"
  android:layout_height="wrap_content"
  android:numStars="6"
  android:rating="1.5"
  android:stepSize="0.5"
  app:colorFillOff="@android:color/transparent"
  app:colorFillOn="@color/tealA400"
  app:colorFillPressedOff="@android:color/transparent"
  app:colorFillPressedOn="@color/tealA200"
  app:colorOutlineOff="@color/teal800"
  app:colorOutlineOn="@color/teal900"
  app:colorOutlinePressed="@color/teal500"
  app:polygonVertices="7"
  app:polygonRotation="0"
  app:strokeWidth="5dp"/>
```
Simply add `<amagi82.flexibleratingbar.FlexibleRatingBar/>` to your layout, and use the same as you would any other widget. 
`layout_width` and `layout_height` will scale the rating bar appropriately. 
In addition, you can alter any of the above settings in xml, or do it in code. 
The colors should be self-explanitory. `polygonVertices` is the number of points on your star. 
You can go down to 2, which creates a diamond, or enter 0 and get circles.
By default, polygons are drawn with one point centered at the top. `polygonRotation` rotates it by int degrees. 
`strokeWidth` is the thickness of the outline around the star.

In code, you can change any of the above with `myRatingBar.setColorFillOff(Color.argb(55,145,122,235))` and so on. 
In addition, you can call `setInteriorAngleModifier`, which adjusts the interior angle of the polygons. 
Default is 2.2, and I recommend keeping it between 1(round) and 3(very pointy).  

## Developed By

Jim Pekarek - <amagi82@gmail.com>

<a href="https://plus.google.com/u/0/+JimPekarek">
  <img alt="Follow me on Google+"
       src="http://icons.iconarchive.com/icons/danleech/simple/64/google-plus-icon.png"/>
</a>
<a href="https://www.linkedin.com/pub/james-pekarek/7b/833/bb0">
  <img alt="Follow me on LinkedIn"
       src="http://icons.iconarchive.com/icons/danleech/simple/64/linkedin-icon.png"/>

## License
```
Copyright (C) 2015 James Pekarek
  
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
  
  http://www.apache.org/licenses/LICENSE-2.0
  
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
