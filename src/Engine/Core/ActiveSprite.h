#ifndef ACTIVE_SPRITE_H_INCLUDED
#define ACTIVE_SPRITE_H_INCLUDED

#include <SFML/Graphics.hpp>

#include "../Utilities/TypeUtils.h"
#include "../Core/HitInfo.h"

class ActiveSprite: public sf::Sprite{
    protected:
        /** if the character is active, or just showing in the game in some simulated way */
        bool active;
        bool invincible;

        /** last hit information (clear after processed) */
        HitInfo hitInfo;

    public:
        /** current position */
        FloatVector3D pos;
        // /** says when the character is attacked from left or right sides */
        //bool attackedFromLeft;

    public:
        /** default constructor (does nothing) */
        ActiveSprite();
        virtual ~ActiveSprite();

        virtual bool isActive() const;
        virtual bool isInvincible() const;
        /** activate in game events, or deactivate (in that case, simulation events are on) */
        virtual void setActive(bool active);
        virtual void setInvincible(bool invincible);

        virtual bool setAnimation(UInt anim) = 0;

        virtual UInt getCurrentAnimIndex() const = 0;
        virtual UInt getCurrentAnimFrame() const = 0;
        virtual UInt getAnimTotalFrames() const = 0;
        virtual UInt getRotation() const = 0;
        virtual bool isFacedLeft() const = 0;

        /** @return the ammount of hitpoints lost */
        virtual UInt beingHit(HitInfo hits) = 0;
};

#endif // ACTIVE_SPRITE_H_INCLUDED
