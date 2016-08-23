
#include "GrabPoint.h"

//public: UByte frame;

const int GrabPoint::INVALID_GRAB_POINT = -10000;

//--------------------
// -- CONSTRUCTORS --
//--------------------
GrabPoint::GrabPoint(int x, int y, UByte angle, UByte anim):
    Point16(x,y),angle(angle), anim(anim)
{}

GrabPoint::GrabPoint(DataInputStream& dis){ //throws IOException
    readData(dis);
}

GrabPoint::GrabPoint():Point16(),angle(0), anim(0){}
GrabPoint::GrabPoint(const GrabPoint& other):
    Point16(other),angle(other.angle), anim(other.anim)
{}


//---------------
// -- METHODS --
//---------------

void GrabPoint::readData(DataInputStream& dis){ //throws IOException
    x = dis.readInt16();
    if (x!=INVALID_GRAB_POINT){
        y = dis.readInt16();
        angle = (dis.readByte()*360)/256;
        anim = dis.readByte();
    }
}


//public void writeData(DataOutputStream dos) throws IOException {
//    dos.writeShort(x);
//    dos.writeShort(y);
//    frame.writeData(dos);
//}
