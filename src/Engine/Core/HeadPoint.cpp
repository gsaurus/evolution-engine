
#include "HeadPoint.h"

//public: UByte frame;

//--------------------
// -- CONSTRUCTORS --
//--------------------
HeadPoint::HeadPoint(int x, int y, UByte frame):
    Point16(x,y),frame(frame)
{}

HeadPoint::HeadPoint(DataInputStream& dis){ //throws IOException
    readData(dis);
}

HeadPoint::HeadPoint():Point16(),frame(0){}
HeadPoint::HeadPoint(const HeadPoint& other):Point16(other),frame(other.frame) {}


//---------------
// -- METHODS --
//---------------

void HeadPoint::readData(DataInputStream& dis){ //throws IOException
    Point16::readData(dis);
    frame = dis.readByte();
}

//public void writeData(DataOutputStream dos) throws IOException {
//    dos.writeShort(x);
//    dos.writeShort(y);
//    frame.writeData(dos);
//}
