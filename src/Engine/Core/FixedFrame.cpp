
#include "FixedFrame.h"

//--------------
// -- FIELDS --
//--------------
//public:
//
//    int frameIndex;
//    UByte duration;
//    HeadPoint* head;
//    WeaponPoint* weapon;
//    GrabPoint* grab;



//--------------------
// -- CONSTRUCTORS --
//--------------------

FixedFrame::FixedFrame():
    frameIndex(0),duration(8),head(0),weapon(0),grab(0)
{}

FixedFrame::~FixedFrame(){
    if (head!=0) delete head;
    if (weapon!=0) delete weapon;
    if (grab!=0) delete grab;
}

//---------------
// -- METHODS --
//---------------

void FixedFrame::readData(DataInputStream& dis) throw(IOException){
    frameIndex = dis.readInt16();
    duration = dis.readByte();
    if(head!=0) head->readData(dis);
    if(weapon!=0){
        weapon->readData(dis);
        if (weapon->x==WeaponPoint::INVALID_WEAPON_POINT){ delete weapon; weapon = 0; }
    }
    if(grab!=0){
        grab->readData(dis);
        if (grab->x==GrabPoint::INVALID_GRAB_POINT){ delete grab; grab = 0; }
    }
}

//void writeData(DataOutputStream& dos){ //throws IOException {
//    dos.writeShort(frameIndex);
//    duration.writeData(dos);
//    if(head!=null) head.writeData(dos);
//    if(weapon!=null) weapon.writeData(dos);
//    if(grab!=null) grab.writeData(dos);
//}
