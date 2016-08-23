#include "Character.h"

//public:
//    HashMap<int,Animation*> animSet;
//
//    std::vector<const std::string> names;
//    std::vector<const std::string> bodies;
//    std::vector<const std::string> heads;
//    // TODO: const std::string icon
//
//    bool autoPallete;
//    // TODO: more later


// default constructor
Character::Character():AFileResolver(GameInvariants::CHARACTER_VERSION){
    // Does nothing
}

Character::Character(const std::string& fileName):AFileResolver(GameInvariants::CHARACTER_VERSION){
    setFileName(fileName);
}

// Destructor
Character::~Character(){
    // delete the animations memory
    HashMap<int,Animation*>::iterator it = animSet.begin();
    for( ; it!= animSet.end() ; ++it){
        delete it->second;
    }
}



//-----------------------------
// ------- LOAD / SAVE -------

void Character::readHeader(DataInputStream& dis) throw(IOException){
    // read names
    int n = dis.readByte();
    names.clear();
    names.reserve(n);
    for(int i=0; i<n; i++){
        int size = dis.readByte();
        std::string txt; txt.reserve(size);
        for(int j=0; j<size; j++)
             txt.push_back((char)dis.readByte()); // v1.1
        names.push_back(txt);
    }

    // read body names
    n = dis.readByte();
    bodies.clear();
    bodies.reserve(n);
    for(int i=0; i<n; i++){
        int size = dis.readByte();
        std::string txt; txt.reserve(size);
        for(int j=0; j<size; j++)
             txt.push_back((char)dis.readByte()); // v1.1
        bodies.push_back(txt);
    }


    // read heads
    n = dis.readByte();
    heads.clear();
    heads.reserve(n);
    for(int i=0; i<n; i++){
        int size = dis.readByte();
        std::string txt; txt.reserve(size);
         for(int j=0; j<size; j++)
             txt.push_back((char)dis.readByte()); // v1.1
        heads.push_back(txt);
    }


    // read icons
    n = dis.readByte();
    icons.clear();
    icons.reserve(n);
    for(int i=0; i<n; i++){
        int size = dis.readByte();
        std::string txt; txt.reserve(size);
         for(int j=0; j<size; j++)
             txt.push_back((char)dis.readByte()); // v1.1
        icons.push_back(txt);
    }

    // read options
    walkVel = dis.readByte()/GameInvariants::VEL_DIVIDER;
    jumpVel = dis.readByte()/GameInvariants::VEL_DIVIDER;
    runJumpVel = dis.readByte()/GameInvariants::VEL_DIVIDER;
    //gravity = -(dis.readByte()/100.);   // TODO 100? constant?
    autoPallete = dis.readBool();
}


void Character::readFoother(DataInputStream& dis) throw(IOException){
        int n = dis.readByte();
        for(int i=0; i<n; i++){
            int ori = dis.readByte();
            int dest = dis.readByte();
            int wep = dis.readByte();
            animSet[ori]->replaces[wep] = animSet[dest];
        }
}


void Character::readData(DataInputStream& dis) throw(IOException){

    readHeader(dis);



    //-----------------
    // read animations
    int n = dis.readInt16();
    animSet.clear();
    for(int i=0; i<n; ++i){
        Animation* anim = new Animation();
        anim->headSwap = !heads.empty();
        anim->readData(dis);
        animSet[anim->index] = anim;
    }

    readFoother(dis);
}



//void Character::writeHeader(DataOutputStream& dos){ //throws IOException
//    headsControll();
//
//
//     // write names
//    int n = names.size();
//    dos.write(n);
//    for(String s:names){
//        dos.write(s.length());
//        dos.writeBytes(s);
//    }
//
//    // write heads
//    n = heads.size();
//    dos.write(n);
//    for(String s:heads){
//        dos.write(s.length());
//        dos.writeBytes(s);
//    }
//
//    // write animation collections names
//    n = bodies.size();
//    dos.write(n);
//    for(String s:bodies){
//        dos.write(s.length());
//        dos.writeBytes(s);
//    }
//
//    // write options
//    dos.writeBoolean(autoPallete);
//}
