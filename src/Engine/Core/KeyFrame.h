#ifndef KEYFRAME_H_INCLUDED
#define KEYFRAME_H_INCLUDED

#include "Point.h"
#include "Vector3D.h"
#include "../Utilities/TypeUtils.h"
#include "../Invariants/GameInvariants.h"
#include "HitFrame.h"
#include "../Files/AReadable.h"

class KeyFrame: public HitFrame, public AReadable{
    public:
        static const int INVALID_VEL;
        static const int NO_CHANGE_VEL;
        static const UInt NO_ACTION;

        static const UByte FRONTAL_KNOCK;
        static const UByte SPARSE_KNOCK;
        static const UByte BACK_KNOCK;
        //--------------
        // -- FIELDS --
        //--------------
        FloatVector3D* impulse;      // if it's mooving
        UInt index;             // UByte

        //--------------------
        // -- CONSTRUCTORS --
        //--------------------
        KeyFrame(DataInputStream dis) throw(IOException);
        KeyFrame();

        //---------------
        // -- METHODS --
        //---------------

        void readData(DataInputStream& dis) throw(IOException);
        //void writeData(DataOutputStream& dos){ //throws IOException


};

#endif // KEYFRAME_H_INCLUDED
