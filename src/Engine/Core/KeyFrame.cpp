#include "KeyFrame.h"
#include <iostream>

//public:
//    //--------------
//    // -- FIELDS --
//    //--------------
//    std::vector<Point16> actionPoints;
//    Vector3D* impulse;      // if it's mooving
//    Vector3D* throwImpulse; // give impulse when throwing someone
//    UInt index;             // UByte
//    UInt actionType;        // UByte
//    UInt throwDamage;       // UByte
const int KeyFrame::INVALID_VEL = -128;
const int KeyFrame::NO_CHANGE_VEL = -127;

const UByte KeyFrame::FRONTAL_KNOCK = 1;
const UByte KeyFrame::SPARSE_KNOCK = 2;
const UByte KeyFrame::BACK_KNOCK = 4;

//--------------------
// -- CONSTRUCTORS --
//--------------------
KeyFrame::KeyFrame(DataInputStream dis) throw(IOException){
    readData(dis);
}
KeyFrame::KeyFrame():HitFrame(), impulse(0),index(0)
{}

//---------------
// -- METHODS --
//---------------

void KeyFrame::readData(DataInputStream& dis) throw(IOException){
    if (throwImpulse){ delete throwImpulse; throwImpulse = 0; }
    actionPoints.clear();

    index = dis.readByte();
    // action
    if (actionType != HitFrame::NO_ACTION){
        actionType = dis.readByte();
        if (actionType >= GameInvariants::AT_KNOCK){
             UByte knockFlags= dis.readByte();
             frontalKnock = knockFlags & FRONTAL_KNOCK;
             backKnock = knockFlags & BACK_KNOCK;
             sparseKnock = knockFlags & SPARSE_KNOCK;
        }

        // poins
        int n = dis.readByte();
        actionPoints.reserve(n);
        for(int i=0; i<n; ++i)
            actionPoints.push_back(Point16(dis));
        // throw stuff
        if (actionType == GameInvariants::AT_THROW){
            throwImpulse = new FloatVector3D();
            throwImpulse->readSignedByteData(dis);
            //std::cout << throwImpulse->x << ", " << throwImpulse->y << ", " << throwImpulse->z << "\n";
            throwImpulse->x/=GameInvariants::VEL_DIVIDER;
            throwImpulse->y/=GameInvariants::VEL_DIVIDER;
            throwImpulse->z/=GameInvariants::VEL_DIVIDER;
            throwDamage = dis.readByte();
            throwRot = dis.readSignedByte();
            throwRot/=GameInvariants::ROT_VEL_DIVIDER;
        }
    }
    // movement impulse
    if (impulse){
        impulse->x = dis.readSignedByte();
        if (impulse->x!=INVALID_VEL){
            impulse->y = dis.readSignedByte();
            impulse->z = dis.readSignedByte();

            if (impulse->x != NO_CHANGE_VEL)impulse->x/=GameInvariants::VEL_DIVIDER;
            if (impulse->y != NO_CHANGE_VEL)impulse->y/=GameInvariants::VEL_DIVIDER;
        }
        //else impulse->x = impulse->y = impulse->z = 0;
    }
}


//void KeyFrame::writeData(DataOutputStream& dos){ //throws IOException
//    index.writeData(dos);
//    // action
//    if (actionType!=null){
//        actionType.writeData(dos);
//        // points
//        dos.write(actionPoints.size());
//        for(IntPoint p:actionPoints){
//            p.writeData(dos);
//        }
//        // throw stuff
//        if (actionType.get() == game.GameInvariants.AT_THROW && throwImpulse != null){
//            throwImpulse.writeData(dos);
//            throwDamage.writeData(dos);
//        }
//    }
//    // movement impulse
//    if (impulsed){
//        int toWrite = impulse.x;
//        if (impulse.x == 0 && impulse.y == 0 && impulse.z == 0)
//            toWrite = INVALID_VEL;
//        dos.writeByte(toWrite);
//        if (toWrite!=INVALID_VEL){
//            dos.writeByte(impulse.y);
//            dos.writeByte(impulse.z);
//        }
//    }
//}
