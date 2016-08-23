
#include "WeaponPoint.h"

//public: UByte frame;

const int WeaponPoint::INVALID_WEAPON_POINT = -10000;

//--------------------
// -- CONSTRUCTORS --
//--------------------
WeaponPoint::WeaponPoint(int x, int y, UByte angle, bool inFront):
    Point16(x,y),angle(angle), inFront(inFront)
{}

WeaponPoint::WeaponPoint(DataInputStream& dis){ //throws IOException
    readData(dis);
}

WeaponPoint::WeaponPoint():Point16(),angle(0), inFront(false){}
WeaponPoint::WeaponPoint(const WeaponPoint& other):
    Point16(other),angle(other.angle), inFront(other.inFront)
{}


//---------------
// -- METHODS --
//---------------

void WeaponPoint::readData(DataInputStream& dis){ //throws IOException
    x = dis.readInt16();
    if (x!=INVALID_WEAPON_POINT){
        y = dis.readInt16();
        angle = (dis.readByte()*360)/256;
        inFront = dis.readBool();
    }
}


//public void writeData(DataOutputStream dos) throws IOException {
//    dos.writeShort(x);
//    dos.writeShort(y);
//    frame.writeData(dos);
//}
