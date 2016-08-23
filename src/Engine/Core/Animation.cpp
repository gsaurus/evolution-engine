#include "Animation.h"


const UInt Animation::NO_ATTACK = 0;
const UInt Animation::ATTACK_WEAPON = 1;
const UInt Animation::ATTACK_POINTS = 2;
const UInt Animation::ATTACK_AROUND = 3;
const UInt Animation::LOOP_ATTACK = 10;

const UInt Animation::INVALID_SOUND_FRAME = 255;

//const UInt Animation::ALLOW_H = 1;
//const UInt Animation::ALLOW_V = 2;
//const UInt Animation::ALLOW_BOTH = 3;
//const UInt Animation::ALLOW_FLIP = 10;
//
//const UInt Animation::ONLY_Z_UP = 100;
//const UInt Animation::ONLY_Z_DOWN = 200;

const UInt Animation::ALLOW_H = 1;
const UInt Animation::ALLOW_V = 2;
const UInt Animation::ALLOW_FLIP = 4;

const UInt Animation::ONLY_Z_UP = 8;
const UInt Animation::ONLY_Z_DOWN = 16;
const UInt Animation::BLOCK = 32;



//--------------------
// -- CONSTRUCTORS --
//--------------------
Animation::Animation(){
    endPosition = 0;
}

Animation::~Animation(){
    std::vector<FixedFrame*>::iterator it1 = fixedFrames.begin();
    for(; it1!=fixedFrames.end(); ++it1){
        delete *it1;
    }
    HashMap<int,KeyFrame*>::iterator it2 = keyFrames.begin();
    for(; it2!=keyFrames.end(); ++it2){
        delete it2->second;
    }
}


//---------------
// -- METHODS --
//---------------


Animation* Animation::getReplacement(int weapon){
    if (replaces.find(weapon) == replaces.end())
        return 0;
    else return replaces[weapon];
}

void Animation::computeTotalTime(){
    totalTime = 0;
    std::vector<FixedFrame*>::const_iterator it;
    for(it = fixedFrames.begin(); it!=fixedFrames.end(); ++it)
        totalTime+=(*it)->duration;
}


void Animation::readData(DataInputStream& dis) throw(IOException){
    replaces.clear();
    // index
    index =dis.readByte();
    // trivial booleans
    invinsible = dis.readBool();
    velocityCtrl = dis.readBool();
    grabPoint = dis.readBool();
    weaponPoint = dis.readBool();

    // end position
    bool endPositionChange = dis.readBool();
    if (endPositionChange){
        endPosition = new IntVector3D();
        endPosition->readInt16Data(dis);
        endFlip = dis.readBool();
    }

    // attack options
    attackOpt = dis.readByte();

    // fixed frames
    int size = dis.readInt16();     // TODO int16?! byte is enough
    fixedFrames.reserve(size);
    for(int i=0; i<size; ++i){
        FixedFrame* f = new FixedFrame();
        if (headSwap) f->head = new HeadPoint();
        if (weaponPoint) f->weapon = new WeaponPoint();
        if (grabPoint) f->grab = new GrabPoint();
        f->readData(dis);
        fixedFrames.push_back(f);
    }

    // key frames
    size = dis.readByte();
    keyFrames.clear();
    for(int i=0; i<size; ++i){
        KeyFrame* f = new KeyFrame();
        UInt tmp = attackOpt%10;
        if (tmp == ATTACK_POINTS || tmp == ATTACK_AROUND)
            f->actionType = 0;
        else f->actionType = HitFrame::NO_ACTION;
        if (velocityCtrl)
            f->impulse = new FloatVector3D();
        f->readData(dis);
        keyFrames[f->index] = f;
    }

    // sound
    soundFrame = dis.readByte();
    if (soundFrame!=INVALID_SOUND_FRAME)
        sound = dis.readInt16();

    // user movement (v1.2)
    UInt tmp = dis.readByte();
    allowH = tmp & ALLOW_H;
    allowV = tmp & ALLOW_V;
    allowFlip = tmp & ALLOW_FLIP;
    onlyZUp = tmp & ONLY_Z_UP;
    onlyZDown = tmp & ONLY_Z_DOWN;
    block = tmp & BLOCK;

//    UInt allowMov = dis.readByte();
//    UInt cent = (allowMov/ONLY_Z_UP) * ONLY_Z_UP;
//    UInt flip = (allowMov-cent)/ALLOW_FLIP;
//    UInt tmp = allowMov%ALLOW_FLIP;
//    allowH = tmp == ALLOW_H || tmp == ALLOW_BOTH;
//    allowV = tmp == ALLOW_V || tmp == ALLOW_BOTH;
//    allowFlip = flip > 0;
//    onlyZUp = cent == ONLY_Z_UP;
//    onlyZDown = cent == ONLY_Z_DOWN;


    computeTotalTime();
}



//void Animation::writeData(DataOutputStream& dos){ //throws IOException
//    framesControll();
//    // index
//    index.writeData(dos);
//    // trivial booleans
//    dos.writeBoolean(invinsible);
//    dos.writeBoolean(velocityCtrl);
//    dos.writeBoolean(grabPoint);
//    dos.writeBoolean(weaponPoint);
//
//    // end position
//    boolean endPositionChange = endPosition != null;
//    dos.writeBoolean(endPositionChange);
//    if (endPositionChange){
//        endPosition.writeData(dos);
//        dos.writeBoolean(endFlip);
//    }
//
//    // attack options
//    attackOpt.writeData(dos);
//
//    // fixed frames
//    dos.writeShort(fixedFrames.size());
//    for(FixedFrame f:fixedFrames)
//        f.writeData(dos);
//
//    // key frames
//    dos.write(keyFrames.size());
//    for(KeyFrame f:keyFrames.values())
//        f.writeData(dos);
//
//    // sound
//    soundFrame.writeData(dos);
//    if (soundFrame.get()!=INVALID_SOUND_FRAME)
//        dos.writeShort(sound);
//}
