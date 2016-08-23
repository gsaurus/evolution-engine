//==========================================================
//
// Evolution Engine Proto - demos
//
// Copyright (C) 2008-2010 Gil Costa
//
//==========================================================




Contents

1. About
2. How to play
   2.1 Controls
3. Modding
   3.1. modifying parameters
   3.2. modifying options
   3.3. using the editors
4. License notes
5. acknowledgments





1. ABOUT

EVE proto was an early version of Evolution Engine. It was abandoned, but was useful to test some features and for the rational of the current development. This proto release has several bugs and crashes every now and then. It wasn't that buggy when the demo videos were produced, but after that most of the code went messed up with other experiences. Summing to that, some stable backups were lost and this is what is left.

This package contains two of the various demos produced with the proto, plus a general testing scene. Videos of the demos can be seen on the EVE youtube channel:
http://www.youtube.com/user/EvEngine





2. HOW TO PLAY

 - Run "ff1 demo.exe" to play the Final Fight clone demo.
 - Run "sor1 demo.exe" to play the Streets of Rage 1 clone demo.
 - Run "proto v5.exe" to play a testing scene.
 

 
2.1 Controls


Player 1:
Key::Up,    - up
Key::Down,  - down
Key::Left,  - left
Key::Right, - right
Key::Z,     - A
Key::X,     - B
Key::C,     - C
Key::N,     - X
Key::B,     - Y
Key::V      - cop call

       
Player 2:

Key::I,     - up
Key::K,     - down
Key::J,     - left
Key::L,     - right
Key::Q,     - A
Key::W,     - B
Key::E,     - C
Key::D,     - X
Key::S,     - Y
Key::A      - cop call
        
        
Player 3:

Key::T,     - up
Key::G,     - down
Key::F,     - left
Key::H,     - right
Key::Num1,  - A
Key::Num2,  - B
Key::Num3,  - C
Key::Num4,  - X
Key::Num5,  - Y
Key::A      - cop call
     




3. MODDING


IMPORTANT: Data produced with the proto are not compatible with future versions. It is a discontinued version.
However one can add or modify data to test features, or just for fun.

It's easier to modify the general scene than the sor1 and ff1 demos, because those have hardcoded triggers.



3.1 Modifying Parameters

Each demo has a text file about engine parameters:
PARAMETERS - FF1.txt    - ff1 demo
PARAMETERS - SOR1.txt   - sor1 demo
PARAMETERS.txt          - general scene

By opening them we see a couple of values followed by a comment about what the value is. So the files content are self-explanatory. Try to modify some of the values and see what happens.



3.1 Modifying Options

Each demo has a text file about game options:
Debug-options - FF1.txt    - ff1 demo
Debug-options - SOR1.txt   - sor1 demo
Debug-options.txt          - general scene

By opening them we see a couple of values and comments (starting with "\\"). Note that comments don't work everywhere, so it's better to not add many comments there...
Those files are organized as follows:

 - First line has two values for screen resolution.
 - Second line has the value 1 for fullscreen, 0 otherwise.
 
Then follows:
 - Audio folder, where sounds will be loaded from
 - Music file name (path relative to proto root)
 - scene file name (game scenes are stored on the folder 'levels'.
 - scene dimensions. This is usually the same as the size of (one of) the playable layer(s). However for tilling levels it may be different.
 
Follows the starting characters of the game (look that characters that comes up by code triggering are excluded). Each character is as follows:
 - character filename (those are on the 'characters' folder)
 - body, head and pallet. The first two values must be zero, they correspond to unfinished features. Pallet is the number of the pallet of the character. Each character may have several pallets, those are files prefixed by the character name, suffixed by _#, where # is the number of the pallet. Those files are stored on the folder 'pallets'.
 - layer of the scene where the character is put on. Usually they are all put on the same layer. This is a very important parameter, characters should be put in different layers accordingly to the level.
 - start position, relative to the layer where the character is.
 - controller: -1, -2 and -3 for keyboard controlled characters, 1, 2, 3, 4 and 5 for rough AI controll. Zero means inactive. The AI is really bad, it was quickly coded for quick tests on the characters interaction only.
 
There is no limit on the number of characters set at start up of the demos.

Note1: Characters plays sounds by indexing them on the specified audio folder. If a character requires a sound that doesn't exist, the game doesn't run.

Note2: Changing the level file should work only on the general scene (because of not using triggers). When you change it's level, make sure to put all characters in a valid layer. For example if you change the level to "ga background.scn", then all characters should be put on layer zero, because that scene has only one layer.



3.3 Using the editors

The jar files contained on the package are editors. They can be used to produce new data or (more likely) to modify the proto data files. Other tools were produced for unfinished features, those aren't on this package. Also more data files were used on the proto experiences, other than the ones on this package.
Note: If you can't run the jar files, you need to install Java RTE (any recent version is ok).

There are no documentation about those tools because of being deprecated, so one using it should self-learn it. However there is a video on you-tube that briefly shows how to use some of the tools:
http://www.youtube.com/watch?v=fTJwvfwqSGY

The best way to get started with the editors is to run the jars and open some of the existing content. You'll find out how it's organized. Try to modify some content a bit and see what happens.

Note1: the layer editor isn't robust when modifying it's size, so make sure you know exactly what size the layer must have before designing it. Layers can be made as matrixes of images or a set of images freely spread on the layer space.

Note2: To add sounds to your own demo:
  - create a new folder inside the audio or use an existing one
  - put your sounds in wave format (.wav) on that folder
  - Rename your files to "#.wav", where # is a number to identify each sound on the engine.
  - move or copy the file "AudioCoDec.jar" to that folder. The jar file is originally on the audio folder
  - Run that jar file. It will create or override the audio files from all wav files contained on the same folder as the jar.
When creating/modifying a character, the sound indexes are used for collisions, voices and sound effects.
  
Note3: The easiest way to create pallets is to use an image equal to one of the frames (it should be croped), replace the colors as you want on an image editor program, and load that image on the pallet editor ("auto detect" button), over the respective frame. Try it with the "replace hacku-oh pallet example.png" file for the "haku-ho1.icl" images collection.



4. LICENSE NOTES

Keep this readme file if redistributing the prototype. Add a note about modifications if distributing a modified version.
You are responsible for the way you use this prototype, including all added and/or modified contents (so remember not to distribute versions with copyrighted contents without permission, etc).



5. ACKNOWLEDGMENTS

- Everyone that is accompanying and encouraging the development.
- Yuzoboy for the Beatnik On The Ship beta remix
- SFML: Simple and Fast Multimedia Library, for the multimedia support.