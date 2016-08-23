#include "ActiveSprite.h"


ActiveSprite::ActiveSprite():active(false),invincible(false){
    // Does nothing
}

ActiveSprite::~ActiveSprite(){
    // does nothing
}



// --------------------------
//  ---- set/get active ----
// --------------------------


bool ActiveSprite::isActive() const{
    return active;
}
void ActiveSprite::setActive(bool active){
    this->active = active;
}

bool ActiveSprite::isInvincible() const{
    return invincible;
}

void ActiveSprite::setInvincible(bool invincible){
    this->invincible = invincible;
}

