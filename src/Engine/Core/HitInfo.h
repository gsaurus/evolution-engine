#ifndef HITINFO_H_INCLUDED
#define HITINFO_H_INCLUDED

#include "HitFrame.h"
#include "ActiveSprite.h"
#include "../Utilities/TypeUtils.h"

class ActiveSprite;

class HitInfo{
    public:
        const HitFrame* hits;
        const ActiveSprite* hitter;
        bool fromLeft;

        HitInfo(){}
        HitInfo(const HitFrame* hits, const ActiveSprite* hitter, bool fromLeft = false):
            hits(hits),hitter(hitter), fromLeft(fromLeft)
        {}
};

#endif // HITINFO_H_INCLUDED
