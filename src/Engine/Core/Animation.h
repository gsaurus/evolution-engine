#ifndef ANIMATION_H_INCLUDED
#define ANIMATION_H_INCLUDED

#include <vector>
#include <map>

#include "../Files/AReadable.h"
#include "Vector3D.h"
#include "../Utilities/HashMap.h"
#include "FixedFrame.h"
#include "KeyFrame.h"

class Animation: public AReadable{
  public:
    static const UInt NO_ATTACK;
    static const UInt ATTACK_WEAPON;
    static const UInt ATTACK_POINTS;
    static const UInt ATTACK_AROUND;
    static const UInt LOOP_ATTACK;

    static const UInt INVALID_SOUND_FRAME;

    // Movement Flags
    static const UInt ALLOW_H;
    static const UInt ALLOW_V;
    static const UInt ALLOW_BOTH;
    static const UInt ALLOW_FLIP;

    static const UInt ONLY_Z_UP;
    static const UInt ONLY_Z_DOWN;
    static const UInt BLOCK;




    //--------------
    // -- FIELDS --
    //--------------
    std::vector<FixedFrame*> fixedFrames;
    HashMap<int,KeyFrame*> keyFrames;
    std::map<int,Animation*> replaces;   // key: # weapon ; value: animation that replaces this one
    IntVector3D* endPosition;
    UInt totalTime; // extra calculation
    UInt index;     // UByte
    UInt attackOpt; // UByte
    // Sound control
    UInt sound;         // int16
    UInt soundFrame;    // UByte

    // movement control
    bool allowH;
    bool allowV;
    bool allowFlip;
    bool onlyZUp;
    bool onlyZDown;
    bool block;

    // fixedFrame control
    bool headSwap;
    bool weaponPoint;
    bool grabPoint;

    // keyPoints control
    bool velocityCtrl;

    // Animation control
    bool invinsible;
    bool endFlip;



    //--------------------
    // -- CONSTRUCTORS --
    //--------------------
    Animation();
    // destructor
    ~Animation();


    //---------------
    // -- METHODS --
    //---------------
    void computeTotalTime();

    Animation* getReplacement(int weapon);

    void readData(DataInputStream& dis) throw(IOException);
    //void writeData(DataOutputStream& dos) throw(IOException);


};


#endif // ANIMATION_H_INCLUDED
