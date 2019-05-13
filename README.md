# ECSE 223 Block Project (Group 2)

## Status
- Progress: Finished with Iteration 5
- [Heroku Website (we don't need it)](http://block223-t02.herokuapp.com)
- [Our wiki (we don't need it)](https://github.com/W2019-ECSE223/ecse223-group-project-p-2/wiki/t02-Wiki-Page)

## Group Members
- Mark
- Elie Ruban
- Jeffery Tang
- Hanwen Wang
- Yuhang Zhang
- Lyon Zhao

## Iteration 2 and 3 Features
- Elie
  - 7.Update Block
  - 10.Remove Block from a level
- Jeffery
  - 5.Add Block
  - 6.Delete Block for good
- Mark
  - 11.Save Game
  - 12.Log in/out as player/admin
- Yuhang
  - 1.Add Game
  - 3.Delete Game
- Hanwen
  - 8.Position block
  - 9.Move block
- Lyon
  - 2.Define Game Settings
  - 4.Update Game

## Iteration 4-6 Features
- Elie
  - 2.Move ball
  - 7.Test game
  - 8.Publish game
- Jeffery
  - 4.Ball hits block
- Mark
  - 6.View hall of fame
- Yuhang
  - 3.Ball hits paddle or wall
- Hanwen
  - 5.Ball is out of bounds
- Lyon
  - 1.Start/pause/resume game
- Everyone
  - 9.Move paddle

## Import JFoenix
The app uses the JFoenix library on top of JavaFX. So you'll need to import JFoenix into your project:
1. Download JFoenix 9.
2. Go to Eclipse and open up your project's Java build path.
3. Click "Add External JARs" and click on the thing you installed.

If it doesn't work, you try using JFoenix 8 instead.

## Eclipse VM Arguments
**Note: this only applies if you use JDK 11**

For some reason, the JFX color picker requires extra configuration. To prevent the app from crashing at the page where you add blocks, you may need to fill in these [VM arguments](https://github.com/jfoenixadmin/JFoenix/issues/889) at Run Configurations > Arguments:
```
--module-path "C:\Program Files\Java\javafx-sdk-11.0.2\lib" --add-modules=javafx.controls,javafx.fxml
--add-opens
javafx.base/com.sun.javafx.runtime=ALL-UNNAMED
--add-opens
javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED
--add-opens
javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED
--add-opens
javafx.base/com.sun.javafx.binding=ALL-UNNAMED
--add-opens
javafx.base/com.sun.javafx.event=ALL-UNNAMED
--add-opens
javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED
```