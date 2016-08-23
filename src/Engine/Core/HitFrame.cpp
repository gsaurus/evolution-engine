#include "HitFrame.h"

const UInt HitFrame::NO_ACTION = 1000;
HitFrame::HitFrame():actionPoints(0),throwImpulse(0),actionType(NO_ACTION), throwDamage(0), throwRot(0){}
HitFrame::~HitFrame(){
    if (throwImpulse!=0)
        delete throwImpulse;
}
