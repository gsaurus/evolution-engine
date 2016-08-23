#ifndef CHARSPRITE_H_INCLUDED
#define CHARSPRITE_H_INCLUDED

#include "ActiveSprite.h"
#include "../Utilities/TypeUtils.h"
#include "../Core/Character.h"
#include "../Core/CharacterHeader.h"
#include "../Core/FramesCollection.h"
#include "../Core/Pallet.h"
#include "../Core/ASpriteController.h"

class CharacterHeader;
class ASpriteController;

class CharSprite: public ActiveSprite{    // TODO: protected?
    protected:
        const Character* charObj;
        CharacterHeader* charHeader;    // needed for states
        const FramesCollection* bodyObj;
        const FramesCollection* headObj;

        ASpriteController* controller;  // the controller of this sprite, to send messages

        /** grabbing someone? Who? */
        CharSprite* grabbedOne;
        /** being grabbed by someone? Who? */
        CharSprite* theGrabber;

        const std::string* name;
        UInt nameIndex;

        /** max energy, for bar drawing and energy refill */
        UInt maxEnergy;
        /** the current energy*/
        UInt energy;

        /** the current animation */
        UInt currAnimIndex;
        const Animation* currentAnim;
        /** the current frame # of the current animation */
        int currentFrame;  // current anim frame
        /** timer to play the animation in it's correct time */
        int animTimer;     // timer to change anim frames

        // Player extra control
        bool movingLeft;
        bool movingRight;
        bool movingUp;
        bool movingDown;
        bool running;   // if it's running or runJump
        bool facedLeft;

        bool beingThrown;
        //bool beingKnocked;

        /** being grabbed from back */
        bool grabbedBack;
        /** grabbing someone from its back */
        bool grabbingOnBack;

        /** changes the current frame */
        void setFrame(FixedFrame* frame);


        /** get a velocity componet based on the character state */
        void updatePlayerVelComponent(float& posComp, float& airVel, bool negative, bool vertical=false);

        /** Control walk, jump and other animations that allows horizontal and/or vertical user movement */
        void updatePlayerVelocity();
        /** update current position, accordingly to the velocity */
        void updatePosition(); // TODO: may need something else for fast walk, easy to change though

        /** update the center point */
        void updateCenterPoint();

        /** update the grabbed one position */
        void updateGrabbedPosition();
        void renderPosition();


    public:
        // TODO: set this info on ActiveSprite
        /** the character velocity, determined by keyFrames */
        FloatVector3D velocity;
        float rotationalVel;    // when being thrown, rotate

        // player controlled velocity in air
        float airPlayerVelX;
        float airPlayerVelY;

    public:
        // TODO: void setFlip(bool flip) or something, allow changing flip and other options on specific animations!

        /** default constructor (does nothing) */
        CharSprite();
        /** copy the main information to other sprite */
        void copyTo(CharSprite& other);
        void reload();
        void initialize(const Character* charObj, CharacterHeader* charHeader, const FramesCollection* bodyObj, const FramesCollection* headObj,
                            UInt name, UInt energy, IntVector3D startPos, UInt anim=0);

        void setController(ASpriteController* controller);


        /**
         * makes the animation move, accordingly to it's internal clock
         * return true if the frame had changed
         */
        bool update();      // TODO: may return some feedback, perhaps a KeyFrame
                                    // TODO: may need something for fast walk

        void resetSpeed();

        /** get the end position (if exists) after doing the current animation */
        const IntVector3D* getEndPosition() const;

        /** return true if current frame has a keyframe */
        bool hasHitFrame() const;

        /** return a hit info for the current hitframe */
        const HitFrame* getHits() const;

        //void move(int x, int y, int z);

        /**
         * changes the current animation
         * @return true if the animation exists and was set
         */
        bool setAnimation(UInt anim);
        void reloadCurrentFrame();

        void grab(CharSprite* other, bool fromBack = false);
        void unGrab();
        void knockDown();
        void breakAnim();

        void beThrown(const HitFrame* throwInfo);
        void setBeingThrown(bool thrown);

        bool isBeingThrown() const;
        //bool isBeingKnocked() const;

        /** get the animation index */
        UInt getCurrentAnimIndex() const;
        UInt getCurrentAnimFrame() const;
        UInt getAnimTotalFrames() const;
        UInt getRotation() const;

        bool animationEnded() const;

        /** moves the sprite */
        void move(float dx, float dy);

        /**
         * set the character faced left (true) or right (false)
         * @return true if it flipped
         */
        bool faceLeft(bool faceLeft);
        // Player movement control
        void moveLeft(bool move);
        void moveRight(bool move);
        void moveUp(bool move);
        void moveDown(bool move);
        void run(bool runMove);
        bool isRunning() const;
        bool isFacedLeft() const;
        bool isInAir() const;
        bool isStaticJumping() const;

        bool isMovingLeft() const;
        bool isMovingRight() const;
        bool isMovingUp() const;
        bool isMovingDown() const;

        bool isOnBlockingMove() const;

        bool isInvincibleAnimPlaying() const;
        bool attackedFromBack() const;
        void faceToHitter();

        /** stop all player movement */
        void stopAll();

        CharSprite* getGrabbed() const;
        CharSprite* getTheGrabber() const;
        bool isBeingBackGrabbed() const;
        bool isGrabbingOnBack() const;

        /** @see ActiveSprite::beingHit */
        virtual UInt beingHit(HitInfo hitInfo);

        // TODDO: void wallsCollision(SomethingAboutLevelObstacles* walls);


        // void simulate(float percentageDone);
};

#endif // CHARSPRITE_H_INCLUDED
