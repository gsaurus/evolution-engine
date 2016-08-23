#ifndef HITFRAME_H_INCLUDED
#define HITFRAME_H_INCLUDED

#include "../Utilities/TypeUtils.h"
#include "Point.h"
#include "Vector3D.h"

class HitFrame{
    public:

        static const UInt NO_ACTION;

        //--------------
        // -- FIELDS --
        //--------------
        std::vector<Point16> actionPoints;
        FloatVector3D* throwImpulse;  // byte[3]

        UInt actionType;    // byte
        UInt throwDamage;   // byte
        float throwRot;
        bool frontalKnock;
        bool backKnock;
        bool sparseKnock;

        //------------------------------
        // constructors and destructors

        HitFrame();
        virtual ~HitFrame();
};

#endif // HITFRAME_H_INCLUDED
