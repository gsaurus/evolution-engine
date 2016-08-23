#include "ASpriteController.h"

ASpriteController::ASpriteController(CharSprite* sprite):sprite(sprite){
    // keys are not pressed
    presses[UP] = false;
    presses[DOWN] = false;
    presses[LEFT] = false;
    presses[RIGHT] = false;
    presses[A] = false;
    presses[B] = false;
    presses[C] = false;
    presses[D] = false;
    presses[E] = false;
    presses[F] = false;
    presses[CALL] = false;
}

ASpriteController::~ASpriteController(){
    // does nothing
}

void ASpriteController::threathKey(Key k, bool press){}

void ASpriteController::takeKey(Key k, bool pressed){
    if (presses[k] == pressed) return; // already pressed, does nothing
    presses[k] = pressed;
    threathKey(k, pressed);
}

void ASpriteController::keyPressed(Key k){ takeKey(k, true); }
void ASpriteController::keyReleased(Key k){ takeKey(k, false); }

const std::map<Key,bool> ASpriteController::getKeysState() const{
    return presses;
}
