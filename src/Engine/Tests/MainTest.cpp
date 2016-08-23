#include "../Files/AFileResolver.h"
#include "../Files/DataInputStream.h"
#include "../Files/DataOutputStream.h"
#include "../Files/DataOutputStream.h"
#include "../Files/NoCoDec.h"

#include "../Files/IOException.h"


#include "../Core/FramesCollection.h"
#include "../Core/CollectionWithFrames.h"
#include "../Core/Pallet.h"
#include "../Core/Character.h"

#include "../Core/DataBase.h"
#include "../Core/CharacterHeader.h"
#include "../Core/CharSprite.h"

#include "../Game/CharacterController.h"

#include <iostream>
#include <sstream>
#include <string>

#include <SFML/Graphics.hpp>
#include <SFML/Audio.hpp>

static const float FRAME_RATE = 0.0166666;

//-lsfml-audio-s-d
//-lsfml-graphics-s-d
//-lsfml-window-s-d
//-lsfml-system-s-d



//int main(){
//    sf::RenderWindow app(sf::VideoMode(800, 600, 32), "SFML Graphics");
//    sf::Image sunset;
//    sunset.LoadFromFile("sunset.png");
//    sf::Sprite backSpr(sunset);
//    while (app.IsOpened()){
//        sf::Event Event;
//        while (app.GetEvent(Event)){
//            if (Event.Type == sf::Event::Closed)
//                app.Close();
//        }
//
//        app.Draw(backSpr);
//        app.Display();
//    }
//
//}


// ---------------------------
//  ---- Initializations ----
// ---------------------------

void init(){
    AFileResolver::setDefaultCoDec();
}


void end(){
    AFileResolver::deleteCoDec();
}

// --------------------------------------
// Data Input / Output Streams valitation
// --------------------------------------


void dataIOStreams(){
    DataOutputStream dos;
    dos.writeByte(56);
    dos.writeInt16(-3);
    //dos.writeInt16(5013);
    dos.writeByte(128);
    dos.writeBool(true);
    //dos.writeInt32(9876543);
    dos.writeInt32(-50);
    dos.writeBool(false);

    Data data = dos.getData();
    DataInputStream dis(data);

    std::cout << (int)dis.readByte()  << "\n";
    std::cout << dis.readInt16()      << "\n";
    std::cout << (int)dis.readByte()  << "\n";
    std::cout << dis.readBool()       << "\n";
    std::cout << dis.readInt32()      << "\n";
    std::cout << dis.readBool()       << "\n";

}






void decodeImage(){
    NoCoDec codec;
    Data dat = codec.decodeFromFile("tmp.png");
    sf::Image img;
    if (!img.LoadFromMemory(dat.getData(),dat.size()))
        std::cout<< "img err\n";
    else std::cout<< "img sucess\n";
    dat.clean();
}


void decodeImage2(){
    NoCoDec codec;
    Data dat = codec.decodeFromFile("data.2");
    DataInputStream dis(dat);
    int size = dat.size();
    Data dat2 = dis.read(size);
    dat.clean();

    sf::Image img;
    if (!img.LoadFromMemory(dat2.getData(),dat2.size()))
        std::cout<< "img2 err\n";
    else std::cout<< "img2 sucess\n";
    dat2.clean();
}



sf::Image decodeSimpleData(){
 NoCoDec codec;
    Data dat = codec.decodeFromFile("data.out");
    DataInputStream dis(dat);
    int size = dis.readInt32();
    Data dat2 = dis.read(size);
    //dat.clean();

    sf::Image img;
    if (!img.LoadFromMemory(dat2.getData(),dat2.size()))
        std::cout<< "simple err\n";
    else std::cout<< "simple sucess\n";
    return img;
}


void displayImage(sf::Image img){
    // Create the main rendering window
    sf::RenderWindow app(sf::VideoMode(800, 600, 32), "SFML Graphics");

    // Start game loop
    while (app.IsOpened())
    {
        // Process events
        sf::Event Event;
        while (app.GetEvent(Event))
        {
            // Close window : exit
            if (Event.Type == sf::Event::Closed)
                app.Close();
        }


//        std::cout << app.GetFrameTime() << "\n";
//        sf::Sprite sprite(img);
//        for(int i=0; i<640; i+=10)
//            for(int j=0; j<480; j+=10){
//                sprite.SetX(i);
//                sprite.SetY(j);
//                app.Draw(sprite);
//            }

        app.Draw(sf::Sprite(img));
        // Display window contents on screen
        app.Display();
    }

}




// ----------------------------------------
// ----------------------------------------
// ----------------------------------------






// ----------------------------------------
//  ---- FORMATED FILES IO Validation ----
// ----------------------------------------


// ----------------
//  --- FRAMES ---
// ----------------

FramesCollection* readCollection(std::string fileName){
    FramesCollection* col = new FramesCollection();
    col->setFileName(fileName);
    try{
        col->load();
    }catch(IOException e){
        std::cout<< (int)e.getError() << " -> " << e.what() << std::endl;
    }
    return col;
}


void displayFrameOfCollection(std::string fileName){
    FramesCollection* col = readCollection(fileName);
    displayImage((*col)[col->size()/2].getImage());
    delete col;
}


// -----------------
//  --- GOODIES ---
// -----------------

GoodiesCollection* readGoodies(std::string fileName){
    GoodiesCollection* col = new GoodiesCollection();
    col->setFileName(fileName);
    try{
        col->load();
    }catch(IOException e){
        std::cout<< (int)e.getError() << " -> " << e.what() << std::endl;
    }
    return col;
}


void displayFrameOfGoodies(std::string fileName){
    GoodiesCollection* goodies = readGoodies(fileName);
    std::string colName = goodies->getCollectionName(goodies->size()/2);
    displayFrameOfCollection("animations\\" + colName);
    delete goodies;
}



// -----------------
//  --- WEAPONS ---
// -----------------

WeaponsCollection* readWeapons(std::string fileName){
    WeaponsCollection* col = new WeaponsCollection();
    col->setFileName(fileName);
    try{
        col->load();
    }catch(IOException e){
        std::cout<< (int)e.getError() << " -> " << e.what() << std::endl;
    }
    return col;
}


void displayFrameOfWeapons(std::string fileName){
    WeaponsCollection* weapons = readWeapons(fileName);
    std::string colName = weapons->getCollectionName(weapons->size()/2);
    displayFrameOfCollection("animations\\" + colName);
    delete weapons;
}


// ----------------
//  --- PALLET ---
// ----------------

Pallet* readPallet(std::string fileName){
    Pallet* pall = new Pallet(fileName);
    try{
        pall->load();
    }catch(IOException e){
        std::cout<< (int)e.getError() << " -> " << e.what() << std::endl;
    }
    return pall;
}



void applyPalletToFrameOfWeapons(Pallet* pall, std::string fileName){
    FramesCollection* col = readCollection(fileName);
    sf::Image img = (*col)[col->size()/2].getImage();
    sf::Image img2;
    pall->applyToImage(img,img2);
    displayImage(img2);
    delete col;
}


void displayFrameOfPallet(std::string fileName){
    Pallet* pall = readPallet(fileName);
    std::string colName = AFileResolver::getTruncatedPath(pall->getFileName());
    colName = colName.substr(0,colName.find_last_of('_'));
    colName+=".icl";
    //displayFrameOfCollection("animations\\" + colName);
    applyPalletToFrameOfWeapons(pall, "animations\\" + colName);
    delete pall;
}



// -------------------
//  --- CHARACTER ---
// -------------------

Character* readCharacter(std::string fileName){
    Character* ch = new Character(fileName);
    try{
        ch->load();
    }catch(IOException e){
        std::cout<< (int)e.getError() << " -> " << e.what() << std::endl;
    }
    return ch;
}



void displayFrameOfCharacter(std::string fileName){
    Character* ch = readCharacter(fileName);
    std::string colName = ch->bodies[ch->bodies.size()/2];
    displayFrameOfCollection("animations\\" + colName);
    delete ch;
}






// ----------------------------------------
// ----------------------------------------
// ----------------------------------------






// -------------------------------
//  ---- DATABASE VALIDATION ----
//    ---- Simple Version ----
// -------------------------------

//void openingStuff(){
//    const Character* c1 = DataBase::getCharacter("Axel.ch");
//    const Character* c2 = DataBase::getCharacter("haku-oh.ch");
//    std::cout << c1->getFileName() << " and " << c2->getFileName() << "\n";
//    const Character* c3 = DataBase::getCharacter("Axel.ch");
//    if (c1 == c3)
//        std::cout << "they are the same :) \n";
//
//    //DataBase::characters.remove("haku-oh.ch");
//    DataBase::removeCharacter("haku-oh.ch");
//    //(*c2) = (*c1);
//
//    const Character* c5 = DataBase::getCharacter("haku-oh.ch");
//    if (c2!=c4)
//        std::cout << "they are different :) \n";
//    else std::cout << "they are the same :O \n";
//    std::cout << c1->getFileName() << " and " << c2->getFileName() << "\n";
//    std::cout << c3->getFileName() << " and " << c4->getFileName() << "\n";
//
//    std::cout << "All opened names:\n";
//    HashMap<char*,Character*>::iterator it = DataBase::characters.begin();
//    for( ; it!=DataBase::characters.end() ; ++it)
//        std::cout << it->first << "\n";
//}


void openingStuff(){
    CharacterHeader c1("haku-oh.ch",0,0,0);
    c1.load();
    CharacterHeader c2("Axel2.ch",0,0,0);
    try{ c2.load();
    }catch(IOException e){ std::cout<< (int)e.getError() << " -> " << e.what() << std::endl; }
}

void openingMoreStuff(){
    CharacterHeader c1("haku-oh.ch",0,1,1);
    c1.load();
    CharacterHeader c2("haku-oh.ch",0,2,2);
    c2.load();
}

void openingEvenMoreStuff(){
    CharacterHeader c1("haku-oh.ch",0,1,2);
    c1.load();
    CharacterHeader c2("Axel2.ch",0,0,0);
    try{ c2.load();
    }catch(IOException e){ std::cout<< (int)e.getError() << " -> " << e.what() << std::endl; }
}


void closingStuff(){
    DataBase::clearEverything();
    std::cout << ".";
}

void openAndCloseStuff(){
    for(int i=0; i<10; i++){
        openingStuff();
        openingMoreStuff();
        openingEvenMoreStuff();

        CharacterHeader c1("haku-oh.ch",0,0,0);
        CharacterHeader c2("haku-oh.ch",0,1,1);
        CharacterHeader c3("Axel2.ch",0,0,1);
        CharacterHeader c4("haku-oh.ch",0,2,2);
        CharacterHeader c5("haku-oh.ch",0,3,1);
        CharacterHeader c6("haku-oh.ch",0,4,0);
        CharacterHeader c7("Axel2.ch",0,0,0);
        c1.load();
        c2.load();
        c3.load();
        c4.load();c5.load();c6.load();c7.load();

        CharSprite s1,s2,s3,s4,s5,s6,s7,s8,s9,s10;
        c1.createSprite(s1,0,30,IntVector3D(100,150,0),0);
        c2.createSprite(s2,0,50,IntVector3D(200,150,0),1);
        c3.createSprite(s3,0,100,IntVector3D(300,150,0),21);
        c4.createSprite(s4,0,100,IntVector3D(400,150,0),0);
        c5.createSprite(s5,0,100,IntVector3D(500,150,0),0);
        c6.createSprite(s6,0,100,IntVector3D(100,300,0),0);
        c7.createSprite(s7,0,100,IntVector3D(200,300,0),20);
        c2.createSprite(s8,0,50,IntVector3D(300,300,0),1);
        c7.createSprite(s9,0,50,IntVector3D(400,300,0),58);
        c3.createSprite(s10,0,50,IntVector3D(500,300,0),32);


        closingStuff();
    }
}





// --------------------------
//  ---- GAME-PLAY TESTS----
//    -- Simple Version --
// --------------------------

void drawWindow(){
    sf::RenderWindow app(sf::VideoMode(640, 480, 32), "Streets Of Rage Evolution (demo)");
    app.Close();
}


void playDemo1(){
    // Create the main rendering window
    sf::RenderWindow app(sf::VideoMode(640, 480, 32), "Streets Of Rage Evolution (demo)");

    CharacterHeader c1("haku-oh.ch",0,0,0);
    CharacterHeader c2("haku-oh.ch",0,1,1);
    CharacterHeader c3("Axel.ch",0,0,0);
    CharacterHeader c4("haku-oh.ch",0,2,2);
    CharacterHeader c5("haku-oh.ch",0,3,1);
    CharacterHeader c6("haku-oh.ch",0,4,0);
    c1.load();
    c2.load();
    c3.load();
    c4.load();c5.load();c6.load();

    CharSprite s1,s2,s3,s4,s5,s6,s7,s8,s9,s10;
    c1.createSprite(s1,0,30,IntVector3D(100,150,0),0);
    c2.createSprite(s2,0,50,IntVector3D(200,150,0),1);
    c3.createSprite(s3,0,100,IntVector3D(300,150,0),52);
    c4.createSprite(s4,0,100,IntVector3D(400,150,0),0);
    c5.createSprite(s5,0,100,IntVector3D(500,150,0),0);
    c6.createSprite(s6,0,100,IntVector3D(500,330,0),0);
    c3.createSprite(s7,0,100,IntVector3D(200,330,0),23);
    c2.createSprite(s8,0,50,IntVector3D(300,330,0),1);
    c3.createSprite(s9,0,50,IntVector3D(400,330,0),59);
    c3.createSprite(s10,0,50,IntVector3D(100,330,0),54);

    // some background
    sf::Image sunset;
    sunset.LoadFromFile("sunset.png");
    sf::Sprite backSpr(sunset);

    // Start game loop
    float count = 0;
    while (app.IsOpened()){
        // Process events
        sf::Event Event;
        while (app.GetEvent(Event)){
            // Close window : exit
            if (Event.Type == sf::Event::Closed) app.Close();
        }
        count += app.GetFrameTime();
        while (count>=0.0166667){
            std::cout << count << "\n";
            count -=0.0166667;
            for(int i=0; i<1; i++){
                s1.update();
                s2.update();
                s3.update();
                s4.update();
                s5.update();
                s6.update();
                s7.update();
                s8.update();
                s9.update();
                s10.update();
            }
        }

        app.Draw(backSpr);
        app.Draw(s1);
        app.Draw(s2); app.Draw(s3); app.Draw(s4);
        app.Draw(s5); app.Draw(s6); app.Draw(s7);
        app.Draw(s8); app.Draw(s9); app.Draw(s10);

        //app.Draw(sf::Sprite(img));
        // Display window contents on screen
        app.Display();
    }
    DataBase::clearEverything();
}










void playDemo2(){
    // Create the main rendering window
    sf::RenderWindow app(sf::VideoMode(640, 480, 32), "Streets Of Rage Evolution (demo)");

    CharacterHeader c1("Axel.ch",0,0,0);
    c1.load();

    CharSprite* s1 = new CharSprite();
    CharSprite* s1copy = new CharSprite();

    //c1.createSprite(s1,0,100,IntVector3D(300,150,0),52);
    c1.createSprite(*s1,0,100,IntVector3D(200,330,0),23);
    //c1.createSprite(s1,0,50,IntVector3D(400,330,0),59);
    //c1.createSprite(s1,0,50,IntVector3D(100,330,0),54);

    // some background
    sf::Image sunset;
    sunset.LoadFromFile("sunset.png");
    sf::Sprite backSpr(sunset);

    // Start game loop
    float count = 0;
    while (app.IsOpened()){
        // Process events
        sf::Event Event;
        while (app.GetEvent(Event)){
            switch (Event.Type){
                case sf::Event::Closed: app.Close(); break;
                case sf::Event::KeyPressed:
                    switch(Event.Key.Code){
                        case sf::Key::Right: s1->moveRight(true); break;
                        case sf::Key::Left: s1->moveLeft(true); break;
                        case sf::Key::A: s1->copyTo(*s1copy); break;
                        case sf::Key::S:
                            delete s1;
                            s1 = s1copy;
                            s1->reload();
                            s1copy = new CharSprite();
                            s1->copyTo(*s1copy);
                            break;
                        default: break;
                    }
                    break;
                case sf::Event::KeyReleased:
                    switch(Event.Key.Code){
                        case sf::Key::Right: s1->moveRight(false); break;
                        case sf::Key::Left: s1->moveLeft(false); break;
                        default: break;
                    }
                    break;
                default: break;
            }
        }
        count += app.GetFrameTime();
        while (count>=0.0166667){
            std::cout << count << "\n";
            count -=0.0166667;
            s1->update();
        }

        app.Draw(backSpr);
        app.Draw(*s1);

        //app.Draw(sf::Sprite(img));
        // Display window contents on screen
        app.Display();
    }
    delete s1copy;
    DataBase::clearEverything();
}






void playDemo3(){
    // Create the main rendering window
    sf::RenderWindow app(sf::VideoMode(480, 360, 32), "Streets Of Rage Evolution (demo)");
    bool fullScreen = false;
    bool pause = false;

    // sound working?
    sf::SoundBuffer soundBuff;
    sf::Sound sound;
    if (soundBuff.LoadFromFile("sound1.wav")){
        sound.SetBuffer(soundBuff);
        //sound.Play();
    }


    // music working?
    sf::Music music;
    if (music.OpenFromFile("Fighting In The Streets V2.ogg")){
        music.SetLoop(true);
        music.Play();
    }

    CharacterHeader c1("Axel.ch",0,0,0);
    CharacterHeader c2("Maxel.ch",0,0,2);
    //CharacterHeader c3("Axel.ch",0,0,2);
    c1.load();
    c2.load();
    //c3.load();

    CharSprite* s1 = new CharSprite();
    CharSprite s2, s3;  // the enemy..
    CharSprite* s1copy = new CharSprite();
    CharacterController ctrl1(s1);
    CharacterController ctrl2(&s2);
    CharacterController ctrl3(&s3);

    //c1.createSprite(s1,0,100,IntVector3D(300,150,0),52);
    c1.createSprite(*s1,0,GameInvariants::LIFE_BAR, IntVector3D(50,app.GetHeight()-79,0),GameInvariants::NORMAL);
    c2.createSprite(s2,0,GameInvariants::LIFE_BAR, IntVector3D(300,app.GetHeight()-80,0),GameInvariants::NORMAL);
    c1.createSprite(s3,0,GameInvariants::LIFE_BAR, IntVector3D(100,app.GetHeight()-78,0),GameInvariants::NORMAL);
    s2.faceLeft(true);

    //s1->grab(&s2);
    //s1->setAnimation(GameInvariants::GRABBING_FRONT);
    //c1.createSprite(s1,0,50,IntVector3D(400,330,0),59);
    //c1.createSprite(s1,0,50,IntVector3D(100,330,0),54);

    // some background
    sf::Image sunset;
    sunset.LoadFromFile("sunset.png");
    sf::Sprite backSpr(sunset);

    sf::String fps;
    fps.SetColor(sf::Color::White);
    fps.SetSize(15);
    fps.SetStyle(sf::String::Bold);
    fps.SetPosition(5,app.GetHeight()-80);


    sf::String txt1;
    txt1.SetColor(sf::Color::White);
    txt1.SetSize(12);
    txt1.SetStyle(sf::String::Bold);
    txt1.SetPosition(5,app.GetHeight()-60);

    sf::String txt2;
    txt2.SetColor(sf::Color::White);
    txt2.SetSize(12);
    txt2.SetStyle(sf::String::Bold);
    txt2.SetPosition(5,app.GetHeight()-45);

    sf::String txt3;
    txt3.SetColor(sf::Color::White);
    txt3.SetSize(12);
    txt3.SetStyle(sf::String::Bold);
    txt3.SetPosition(5,app.GetHeight()-30);

    sf::String txt4;
    txt4.SetColor(sf::Color::White);
    txt4.SetSize(12);
    txt4.SetStyle(sf::String::Bold);
    txt4.SetPosition(5,app.GetHeight()-15);

    // Start game loop
    float count = 0;
    sf::Clock clock;    // time controller
    sf::Clock taskClock;    // to control tasks timing
    while (app.IsOpened()){
        // Process events
        // TODO: Event Handling is taking too much time
        sf::Event Event;
        while (app.GetEvent(Event)){
            switch (Event.Type){
                case sf::Event::Closed: app.Close(); break;
                case sf::Event::KeyPressed:
                    switch(Event.Key.Code){
                        // player 1
                        case sf::Key::T: ctrl1.keyPressed(UP); break;
                        case sf::Key::G: ctrl1.keyPressed(DOWN); break;
                        case sf::Key::F: ctrl1.keyPressed(LEFT); break;
                        case sf::Key::H: ctrl1.keyPressed(RIGHT); break;
                        case sf::Key::Q: ctrl1.keyPressed(A); break;
                        case sf::Key::W: ctrl1.keyPressed(B); break;
                        case sf::Key::E: ctrl1.keyPressed(C); break;
                        case sf::Key::D: ctrl1.keyPressed(D); break;
                        case sf::Key::S: ctrl1.keyPressed(E); break;
                        case sf::Key::A: ctrl1.keyPressed(CALL); break;

                        // player 2
                        case sf::Key::Up:   ctrl2.keyPressed(UP); break;
                        case sf::Key::Down: ctrl2.keyPressed(DOWN); break;
                        case sf::Key::Left: ctrl2.keyPressed(LEFT); break;
                        case sf::Key::Right:ctrl2.keyPressed(RIGHT); break;
                        case sf::Key::Z:    ctrl2.keyPressed(A); break;
                        case sf::Key::X:    ctrl2.keyPressed(B); break;
                        case sf::Key::C:    ctrl2.keyPressed(C); break;
                        case sf::Key::LShift:   ctrl2.keyPressed(D); break;
                        case sf::Key::LControl: ctrl2.keyPressed(E); break;
                        case sf::Key::Space:    ctrl2.keyPressed(CALL); break;

                        // player 3
                        case sf::Key::I:    ctrl3.keyPressed(UP); break;
                        case sf::Key::K:    ctrl3.keyPressed(DOWN); break;
                        case sf::Key::J:    ctrl3.keyPressed(LEFT); break;
                        case sf::Key::L:    ctrl3.keyPressed(RIGHT); break;
                        case sf::Key::V:    ctrl3.keyPressed(A); break;
                        case sf::Key::B:    ctrl3.keyPressed(B); break;
                        case sf::Key::N:    ctrl3.keyPressed(C); break;
                        case sf::Key::M:    ctrl3.keyPressed(D); break;
                        case sf::Key::Comma:ctrl3.keyPressed(E); break;
                        case sf::Key::Slash:ctrl3.keyPressed(CALL); break;




                        // screen, views, etc
                        case sf::Key::F1: app.SetSize(480,360); break;
                        case sf::Key::F2: app.SetSize(640,480); break;
                        case sf::Key::F3: app.SetSize(960,720); break;
                        // TODO: return is an if, that can disable this case
                        case sf::Key::Return:
                            if (Event.Key.Alt){
                                if (fullScreen) app.Create(sf::VideoMode(480,360, 32), "Streets Of Rage Evolution (demo)");
                                else app.Create(sf::VideoMode(640,480, 32), "Streets Of Rage Evolution (demo)", sf::Style::Fullscreen);
                                fullScreen = !fullScreen;
                            }else pause = !pause;
                            break;

                        // load/save state
                        case sf::Key::F5 : s1->copyTo(*s1copy); break;
                        case sf::Key::F8:
                            delete s1;
                            s1 = s1copy;
                            s1->reload();
                            s1copy = new CharSprite();
                            s1->copyTo(*s1copy);
                            ctrl1.setCharacterSprite(s1);
                            break;
                        default: break;
                    }
                    break;

                case sf::Event::KeyReleased:
                    switch(Event.Key.Code){
                        // player 1
                        case sf::Key::T: ctrl1.keyReleased(UP); break;
                        case sf::Key::G: ctrl1.keyReleased(DOWN); break;
                        case sf::Key::F: ctrl1.keyReleased(LEFT); break;
                        case sf::Key::H: ctrl1.keyReleased(RIGHT); break;
                        case sf::Key::Q: ctrl1.keyReleased(A); break;
                        case sf::Key::W: ctrl1.keyReleased(B); break;
                        case sf::Key::E: ctrl1.keyReleased(C); break;
                        case sf::Key::D: ctrl1.keyReleased(D); break;
                        case sf::Key::S: ctrl1.keyReleased(E); break;
                        case sf::Key::A: ctrl1.keyReleased(CALL); break;

                        // player 2
                        case sf::Key::Up:   ctrl2.keyReleased(UP); break;
                        case sf::Key::Down: ctrl2.keyReleased(DOWN); break;
                        case sf::Key::Left: ctrl2.keyReleased(LEFT); break;
                        case sf::Key::Right:ctrl2.keyReleased(RIGHT); break;
                        case sf::Key::Z:    ctrl2.keyReleased(A); break;
                        case sf::Key::X:    ctrl2.keyReleased(B); break;
                        case sf::Key::C:    ctrl2.keyReleased(C); break;
                        case sf::Key::LShift:   ctrl2.keyReleased(D); break;
                        case sf::Key::LControl: ctrl2.keyReleased(E); break;
                        case sf::Key::Space:    ctrl2.keyReleased(CALL); break;

                        // player 3
                        case sf::Key::I:    ctrl3.keyReleased(UP); break;
                        case sf::Key::K:    ctrl3.keyReleased(DOWN); break;
                        case sf::Key::J:    ctrl3.keyReleased(LEFT); break;
                        case sf::Key::L:    ctrl3.keyReleased(RIGHT); break;
                        case sf::Key::V:    ctrl3.keyReleased(A); break;
                        case sf::Key::B:    ctrl3.keyReleased(B); break;
                        case sf::Key::N:    ctrl3.keyReleased(C); break;
                        case sf::Key::M:    ctrl3.keyReleased(D); break;
                        case sf::Key::Comma:ctrl3.keyReleased(E); break;
                        case sf::Key::Slash:ctrl3.keyReleased(CALL); break;

                        default: break;
                    }
                    break;
                default: break;
            }
        }
        std::stringstream out1;
        //float val = ::round(taskClock.GetElapsedTime()*100000)/100.;
        float val = ::round(taskClock.GetElapsedTime()*1000);
        out1 << "Event Handling: " << val;
        txt1.SetText(out1.str());
        if (val<=0.5) txt1.SetColor(sf::Color::White);
        else if (val<=1) txt1.SetColor(sf::Color::Yellow);
        else if (val<=5) txt1.SetColor(sf::Color(255,128,64));
        else txt1.SetColor(sf::Color::Red);
        //std::cout << "Event Handling: "<< taskClock.GetElapsedTime() << "\n";
        taskClock.Reset();

        count += clock.GetElapsedTime(); //app.GetFrameTime();
        std::stringstream outFPS;   // TODO: use tostring from utils
        outFPS << "FPS: " << ::round(1.f/count);
        fps.SetText(outFPS.str());
        while (count>=FRAME_RATE){
//            std::cout << count << "\n";
            count -=FRAME_RATE;
            if (!pause){
                ctrl1.testThrow();
                ctrl2.testThrow();
                ctrl3.testThrow();

                ctrl1.testHit(&s2);
                ctrl1.testHit(&s3);
                ctrl2.testHit(s1);
                ctrl2.testHit(&s3);
                ctrl3.testHit(s1);
                ctrl3.testHit(&s2);

                ctrl1.testGrab(&s2);
                ctrl1.testGrab(&s3);
                ctrl2.testGrab(s1);
                ctrl2.testGrab(&s3);
                ctrl3.testGrab(s1);
                ctrl3.testGrab(&s2);


                ctrl1.update();
                ctrl2.update();
                ctrl3.update();
            }
        }
        //std::cout << "UPDATE: "<< taskClock.GetElapsedTime() << "\n";
        std::stringstream out2;
        //val = ::round(taskClock.GetElapsedTime()*100000)/100.;
        val = ::round(taskClock.GetElapsedTime()*1000);
        out2 << "Update: " << val;
        txt2.SetText(out2.str());
        if (val<=0.5) txt2.SetColor(sf::Color::White);
        else if (val<=1) txt2.SetColor(sf::Color::Yellow);
        else if (val<=5) txt2.SetColor(sf::Color(255,128,64));
        else txt2.SetColor(sf::Color::Red);
        taskClock.Reset();

        app.Draw(backSpr);
        if (s1->pos.y>s2.pos.y){
            if (s1->pos.y>s3.pos.y){
                if (s2.pos.y>s3.pos.y){
                    app.Draw(s3);
                    app.Draw(s2);
                    app.Draw(*s1);
                }else{
                    app.Draw(s2);
                    app.Draw(s3);
                    app.Draw(*s1);
                }
            }else{
                app.Draw(s2);
                app.Draw(*s1);
                app.Draw(s3);
            }
        }else{
            if (s2.pos.y>s3.pos.y){
                if (s1->pos.y>s3.pos.y){
                    app.Draw(s3);
                    app.Draw(*s1);
                    app.Draw(s2);
                }else{
                    app.Draw(*s1);
                    app.Draw(s3);
                    app.Draw(s2);
                }
            }else{
                app.Draw(*s1);
                app.Draw(s2);
                app.Draw(s3);
            }
        }
        app.Draw(fps);
        app.Draw(txt1);
        app.Draw(txt2);
        app.Draw(txt3);
        app.Draw(txt4);

        // Display window contents on screen
        app.Display();

        //std::cout << "Display: "<< taskClock.GetElapsedTime() << "\n";
        std::stringstream out3;
        //val = ::round(taskClock.GetElapsedTime()*100000)/100.;
        val = ::round(taskClock.GetElapsedTime()*1000);
        out3 << "Display: " << val;
        txt3.SetText(out3.str());
        if (val<=0.5) txt3.SetColor(sf::Color::White);
        else if (val<=1) txt3.SetColor(sf::Color::Yellow);
        else if (val<=5) txt3.SetColor(sf::Color(255,128,64));
        else txt3.SetColor(sf::Color::Red);
        taskClock.Reset();

        count += clock.GetElapsedTime();
        // makes the game sleep, releasing CPU usage
        if (count < FRAME_RATE){
            sf::Sleep(FRAME_RATE - count);
            count = FRAME_RATE;
        }
        clock.Reset();
        std::stringstream out4;
        //val = ::round(taskClock.GetElapsedTime()*10000)/10.;
        val = ::round(taskClock.GetElapsedTime()*1000);
        out4 << "Sleeping: " << val;
        txt4.SetText(out4.str());
        if (val<5) txt3.SetColor(sf::Color::Red);
        else if (val<10) txt3.SetColor(sf::Color(255,128,64));
        else if (val<15) txt3.SetColor(sf::Color::Yellow);
        else txt3.SetColor(sf::Color::White);
        taskClock.Reset();
    }
    delete s1copy;
    DataBase::clearEverything();
}





int main(){
    init();
    //dataIOStreams();  // sucess
    //decodeImage();    // sucess
    //decodeImage2();   // sucess
    //decodeSimpleData();   // sucess
    //displayImage(decodeSimpleData()); // sucess

    //displayFrameOfCollection("animations\\axel-body.icl");    // sucess
    //displayFrameOfGoodies("goodies\\goodies.gst");            // sucess
    //displayFrameOfWeapons("weapons\\weapons.wpn");          // sucess
    //displayFrameOfPallet("pallets\\haku-body_1.pll");         // sucess
    //displayFrameOfCharacter("characters\\Axel2.ch");            // sucess

    //openAndCloseStuff(); // sucess

    //playDemo1();          // sucess

    //playDemo2();          // sucess

    playDemo3();          //

    end();
    std::cout << "everything done!";
    return 0;
}
