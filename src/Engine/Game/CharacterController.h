#ifndef CHARACTER_CONTROLLER_H_INCLUDED
#define CHARACTER_CONTROLLER_H_INCLUDED

#import "../Core/ASpriteController.h"
#import "../Core/ActiveSprite.h"


class CharacterController: public ASpriteController{
    protected:
        //Key lastKey;    // to know on combinations
        std::set<ActiveSprite*> theHittenOnes;

        UByte runTime;  // timer to run (count the double forward)

        UByte attackNum;        // number of the attack animation being done
        UByte attackCounter;    // if no attack, the previous counter is reseted

        UByte knockCounter;     // counter for the time B is pressed

        UByte hitPause;
        UByte recoverTime;
        UByte ungrabTime;       // time to ungrab by pressing inverse key
        UByte blockHitTime;     // time to move back when blocking a hit
        bool noMoreFlip;        // can only flip twice

        bool setRun;
        bool tryingRoll;       // is trying a roll instead of run?
        bool gotHit;
        bool gotKnocked;
        bool hitSomeone;

        //UByte superCounter;     // counter for the time A is pressed

        // TODO counter for waiting animation, set with random

        void threathKey(Key k, bool press);
    public:
        CharacterController(CharSprite* sprite);
        void setCharacterSprite(CharSprite* sprite);

        bool testGrab(CharSprite* other);
        void testHit(ActiveSprite* otherSprite);
        void testThrow();

        /** return true if the animation changes */
        bool animationBreak();
        bool grabAnimationBreak();

        /** Some controls needs time to get in action */
        void update();

        /** if one directonal key is pressed, walk or run */
        void testWalk();
        void walk();
        void stop();

        void jump();
        void attack();
        void runAttack();
        void jumpAttack();
        void backAttack();
        void knockAttack();

        void grabWalk();
        void grabStop();

        void grabJump();
        void grabAttack();
        void grabRunAttack();
        void grabJumpAttack();

        void grabDefence();
        void grabDefenceThrow();

        void special();
        void jumpSpecial();
        void super();
        void helpCall();

        void groundBounce();
        void groundSuffer();

        //void knockDown();

        /** @see ActiveSprite::beingHit */
        UInt beingHit(const HitInfo& hitInfo);
};

#endif // CHARACTER_CONTROLLER_H_INCLUDED
