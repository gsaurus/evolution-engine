#include "Weapon.h"
#include "../Invariants/GameInvariants.h"


//--------------
// -- FIELDS --
//--------------
//    std::vector<UBPoint> actionPoints;
//    Vector3D* throwImpulse;  // byte[3]
//
//    UInt name;          // byte
//    UInt actionType;    // byte
//
//    UInt throwDamage;   // byte
//    bool rotation;


//--------------------
// -- CONSTRUCTORS --
//--------------------
Weapon::Weapon(DataInputStream& dis){ //throws IOException
    readData(dis);
}

Weapon::Weapon(){
    name = 255;
    actionType = 0;
    throwDamage = 0;
    rotation = false;
    throwImpulse = 0;
}

Weapon::~Weapon(){
    if (throwImpulse!=0){
        delete throwImpulse;
        throwImpulse = 0;
    }
}

//---------------
// -- METHODS --
//---------------


void Weapon::readData(DataInputStream& dis) throw(IOException){
    // v 1.1
    if (throwImpulse!=0){
        delete throwImpulse;
        throwImpulse = 0;
    }
    name = dis.readByte();
    actionType = dis.readByte();
    frontalKnock = false;
    if (actionType == GameInvariants::AT_THROW){
        throwImpulse = new FloatVector3D(dis);
        throwDamage = dis.readByte();
    }
    rotation = dis.readBool();
    int n = dis.readByte();
    actionPoints.clear();
    actionPoints.reserve(n);
    for(int i=0; i<n; ++i){
        UBPoint p(dis);
        actionPoints.push_back(Point16(p.x,p.y));
    }
}


//void Weapon::writeData(DataOutputStream dos) throws IOException {
//    name.writeData(dos);
//    actionType.writeData(dos);
//    if (actionType.get() == game.GameInvariants.AT_THROW && throwImpulse != null){
//        throwImpulse.writeData(dos);
//        throwDamage.writeData(dos);
//    }
//
//    dos.writeBoolean(rotation);
//    dos.write(actionPoints.size());
//    for(UBPoint p:actionPoints){
//        p.writeData(dos);
//    }
//}

