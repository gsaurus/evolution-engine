#ifndef ASPRITECONTROLLER_H_INCLUDED
#define ASPRITECONTROLLER_H_INCLUDED

#import "CharSprite.h"
#import <map>

enum Key{UP, DOWN, LEFT, RIGHT, A, B, C, D, E, F, CALL, NONE};

typedef std::map<Key,bool> KeysState;

class CharSprite;

/** Class that controls a sprite */
class ASpriteController{
    protected:
        CharSprite* sprite;
        // TODO (Gil#1#): with SFML 1.4 this storage is made internally
        KeysState presses;

        virtual void threathKey(Key k, bool press);
    public:
        ASpriteController(CharSprite* sprite);
        virtual ~ASpriteController();

        void takeKey(Key k, bool pressed);
        void keyPressed(Key k);
        void keyReleased(Key k);
        const KeysState getKeysState() const;

        /** abruptally stops the animation, or says that the animation had finished */
        virtual bool animationBreak() = 0;
        virtual UInt beingHit(const HitInfo& hitInfo) = 0;

        virtual void update()=0;
};

#endif // ASPRITECONTROLLER_H_INCLUDED
