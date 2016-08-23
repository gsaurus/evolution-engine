#include "CharSprite.h"
#include "FixedFrame.h"
#include <algorithm>
#include "../Invariants/GameInvariants.h"

#include <iostream> //TODO: remove this

//protected:
//    const Character* charObj;
//    const FramesCollection* bodyObj;
//    const FramesCollection* headObj;
//
//    const std::string& name;
//    UInt maxEnergy;
//    UInt energy;
//
//    const Animation* currentAnim;
//    UInt currentFrame;  // current anim frame
//    UInt animTimer;     // timer to change anim frames
//
//    bool active;


CharSprite::CharSprite():charObj(0),bodyObj(0),headObj(0), controller(0),
                         grabbedOne(0), theGrabber(0), name(0)
{
    // Does nothing
}


void CharSprite::copyTo(CharSprite& other){
    // TODO: more stuff
    other.charHeader = charHeader;
    other.nameIndex = nameIndex;
    other.pos = pos;            // by copy
    other.velocity = velocity;  // by copy
    other.rotationalVel = rotationalVel;
    other.maxEnergy = maxEnergy;
    other.energy = energy;
    other.currAnimIndex = currAnimIndex;
    other.currentFrame = currentFrame;
    other.animTimer = animTimer;
    other.airPlayerVelX = airPlayerVelX;
    other.airPlayerVelY = airPlayerVelY;
    other.active = active;

    other.movingLeft = movingLeft;
    other.movingRight = movingRight;
    other.movingUp = movingUp;
    other.movingDown = movingDown;
    other.running = running;
}

void CharSprite::reload(){
    // TODO: more stuff
    charHeader->load();
    this->charObj = charHeader->charObj;
    this->bodyObj = charHeader->bodyObj;
    this->headObj = charHeader->headObj;
    if (nameIndex < charObj->names.size()) this->name = &charObj->names.at(nameIndex);
    else name = 0;
    // set animation and position states
    this->SetPosition(pos.x,pos.y-pos.z);
    currentAnim = charObj->animSet.get(currAnimIndex);
    setFrame(currentAnim->fixedFrames[currentFrame]);
}



void CharSprite::initialize(const Character* charObj, CharacterHeader* charHeader, const FramesCollection* bodyObj, const FramesCollection* headObj,
                            UInt name, UInt energy, IntVector3D startPos, UInt anim)
{
    this->charObj = charObj;
    this->charHeader = charHeader;
    this->bodyObj = bodyObj;
    this->headObj = headObj;
    if (name < charObj->names.size()) this->name = &charObj->names.at(name);
    else name = 0;
    nameIndex = name;
    this->maxEnergy = energy;
    this->energy = energy;
    pos.x = startPos.x;
    pos.y = startPos.y;
    pos.z = startPos.z;
    airPlayerVelX = 0;
    airPlayerVelY = 0;
    this->SetPosition(startPos.x,startPos.y-startPos.z);
    setAnimation(anim);
    active = false;
    invincible = false;
    facedLeft = false;
    beingThrown = false;
    running = false;

    stopAll();
}


void CharSprite::setController(ASpriteController* controller){
    this->controller = controller;
}

void CharSprite::setFrame(FixedFrame* f){
    int index = f->frameIndex;
    const sf::Image& img = bodyObj->get(index).getImage();
    if (index > bodyObj->size()) this->SetImage(bodyObj->get(0).getImage());
    else this->SetImage(img);
    this->SetSubRect(sf::IntRect(0,0,img.GetWidth(),img.GetHeight()));

    if (beingThrown){
        // being thrown, base point on image center
        this->SetCenter(img.GetWidth()/2, img.GetHeight()/2);
    }else{
        // change base point
        const Point16& pt = bodyObj->get(index).getCM();
        // if fliped, flip the center
        if (facedLeft) this->SetCenter(img.GetWidth()-pt.x,pt.y);
        else this->SetCenter(pt.x,pt.y);

        // grabbing someone, control it's position
        if (grabbedOne != 0){
            if (f->grab!=0){
                // set it's position
                // TODO: add y?
                if (facedLeft) grabbedOne->pos.x = pos.x - f->grab->x;
                else grabbedOne->pos.x = pos.x + f->grab->x;
                grabbedOne->pos.y = pos.y-1;
                grabbedOne->pos.z = pos.z - f->grab->y;
                // set it's animation
                if (grabbedOne->getCurrentAnimIndex() != f->grab->anim
                    && grabbedOne->getCurrentAnimIndex() != GameInvariants::BEING_BACK_GRABBED_DEFENCE){
                    grabbedOne->setAnimation(f->grab->anim);
                    grabbedOne->reloadCurrentFrame();
                }
                if (facedLeft)
                    grabbedOne->SetRotation(360-f->grab->angle);
                else grabbedOne->SetRotation(f->grab->angle);
            }
            else unGrab();
        }
    }
}



void CharSprite::updateGrabbedPosition(){
    const FixedFrame* f = currentAnim->fixedFrames[currentFrame];
    if (f->grab!=0){
        // set it's position
        // TODO: add y?
        if (facedLeft) grabbedOne->pos.x = pos.x - f->grab->x;
        else grabbedOne->pos.x = pos.x + f->grab->x;
        grabbedOne->pos.y = pos.y-1;
        grabbedOne->pos.z = pos.z - f->grab->y;
        grabbedOne->renderPosition();
    }
    else unGrab();
}




// ----------------------------
//  --------- UPDATE ---------
// ----------------------------



bool CharSprite::update(){
    if (currentAnim == 0 || bodyObj == 0) return false;
    int dura;   // frame duration
    if (currentFrame<0) dura = 0;
    else{
        const FixedFrame* f = currentAnim->fixedFrames[currentFrame];
        dura = ( f!=0 ? (int)f->duration : 0 );
    }
    bool frameChanged = ++animTimer >= dura;
    if (frameChanged){
        animTimer = 0;  // reset timer
        ++currentFrame; // next frame
        // end animation, send message and loop animation if the answer is false
        if (currentFrame==(int)currentAnim->fixedFrames.size()){
            // has end position changing?
            if (currentAnim->endPosition!=0){
                if (facedLeft) pos.x -= currentAnim->endPosition->getX();
                else pos.x += currentAnim->endPosition->getX();
                pos.y += currentAnim->endPosition->getY();
                pos.z += currentAnim->endPosition->getZ();
                if (currentAnim->endFlip){
                    faceLeft(!facedLeft);
                }
            }
            // test if the animation breaks
            if (controller!=0 && controller->animationBreak()) return true;
            currentFrame = 0;   // if no break, loop
        }
        setFrame(currentAnim->fixedFrames[currentFrame]);

        // Test if is a KeyFrame
        if (currentAnim->keyFrames.containsKey(currentFrame)){
            const KeyFrame* kf = currentAnim->keyFrames.get(currentFrame);
            // Character Impulse
            if (kf->impulse != 0 && kf->impulse->x != KeyFrame::INVALID_VEL){
                if (kf->impulse->x != KeyFrame::NO_CHANGE_VEL){
                    // if it's faced left, the x speed is contrary
                    velocity.x = (facedLeft ? -kf->impulse->x : kf->impulse->x );
                }
                if (kf->impulse->y != KeyFrame::NO_CHANGE_VEL)
                    velocity.y = kf->impulse->y;
                if (kf->impulse->z != KeyFrame::NO_CHANGE_VEL)
                    velocity.z = kf->impulse->z;
            }
        }
    }
    updatePosition();
    if (this->grabbedOne!=0) updateGrabbedPosition();
    return frameChanged;
}


void CharSprite::updatePlayerVelComponent(float& posComp, float& airVel, bool negative, bool vertical){
    float delta;
    // set the component correctly
    if (vertical){
        if (velocity.x!=0){
            float mod = ::fabs(velocity.x);
            delta = std::min(charObj->walkVel*GameInvariants::VERTICAL_VEL_FACTOR,mod*GameInvariants::VERTICAL_VEL_FACTOR);
        }else delta = charObj->walkVel*GameInvariants::VERTICAL_VEL_FACTOR;
    }else delta = charObj->walkVel;

    if (pos.z <= 0){
        if (negative) posComp-=delta;
        else posComp+=delta;
    }else{
        delta = (running? charObj->runJumpVel : charObj->jumpVel);
        if (vertical) airVel*=GameInvariants::VERTICAL_VEL_FACTOR;
        if (negative) airVel-=GameInvariants::AIR_CONTROL; //GameInvariants::AIR_CONTROL*delta;
        else airVel+=GameInvariants::AIR_CONTROL; //GameInvariants::AIR_CONTROL*delta;
        if (airVel > 0){
            if (airVel > delta) airVel = delta;
        }else if (airVel < -delta) airVel = -delta;
        posComp += airVel;
    }
}


// Control walk, jump and other animations that allows horizontal and/or vertical user ment
void CharSprite::updatePlayerVelocity(){
    // Horizontally
    if (currentAnim->allowH){
        if (movingLeft || movingRight)
            updatePlayerVelComponent(pos.x, airPlayerVelX, movingLeft);
        else if (pos.z != 0 && airPlayerVelX!=0)
            pos.x += airPlayerVelX;
    }
    // Vertically
    if (currentAnim->allowV){
        if(movingUp || movingDown)
            updatePlayerVelComponent(pos.y, airPlayerVelY, movingUp, true);
        else if (pos.z != 0 && airPlayerVelY!=0)
            pos.y += airPlayerVelY;
    }
    if (currentAnim->allowFlip && (movingLeft || movingRight))
        faceLeft(movingLeft);
}



void CharSprite::updatePosition(){
    // do not update position if being grabbed
    if (theGrabber==0){
        // velocity control
        pos.x+=velocity.x;
        pos.y+=velocity.y;
        // don't update z if not needed
        if (pos.z>0 || velocity.z>0) pos.z+=velocity.z;
        // player movement control (x & y only)
        updatePlayerVelocity();
        // vertical gravity control
        // TODO: different to when when being thrown
        // test floor colision
        if (pos.z<0){
            pos.z = 0;
            velocity.z = 0;
            // reset vel control
            airPlayerVelX = 0;
            airPlayerVelY = 0;
            if (controller!=0) controller->animationBreak();
        }else if (pos.z>0) velocity.z+=GameInvariants::GRAVITY;
        if (rotationalVel!=0) this->SetRotation(this->GetRotation()+rotationalVel);
    }
    // finally update
    renderPosition();
}


void CharSprite::renderPosition(){
    SetPosition(pos.x, pos.y-pos.z);
}

void CharSprite::resetSpeed(){
    // reset, only on x and y
    velocity.x = velocity.y = 0;
    // and rotation too
    rotationalVel = 0;
}



bool CharSprite::setAnimation(UInt anim){
    this->SetRotation(0);
    // filter invalid animations
    if (charObj == 0 || !charObj->animSet.containsKey(anim)
        || (charObj->animSet.get(anim)->onlyZUp && velocity.z < 0)
        || (charObj->animSet.get(anim)->onlyZDown && velocity.z > GameInvariants::FALLING_LIMIAR)
    ){
        //if (controller!=0) controller->animationBreak();
        return false;
    }
    currAnimIndex = anim;
    currentAnim = charObj->animSet.get(anim);
    currentFrame = -1;  // to be updated in update()
    animTimer = -1;
    invincible = currentAnim->invinsible;
    resetSpeed();
    update();
    // set the first frame
    //setFrame(currentAnim->fixedFrames[0]);
    return true;
}


void CharSprite::grab(CharSprite* other, bool fromBack){
    grabbedOne = other;
    if (other != 0){
        grabbingOnBack = fromBack;
        other->theGrabber = this;
        other->grabbedBack = fromBack;
        other->stopAll();
    }
}


void CharSprite::unGrab(){
    if (grabbedOne==0) return;
    grabbedOne->theGrabber = 0;
    grabbedOne->breakAnim();
    grabbedOne = 0;
}


void CharSprite::knockDown(){
    // if grabbed, ungrab imediactly
    if (theGrabber!=0){
        CharSprite* grabber = theGrabber;
        theGrabber->unGrab();
        // only break anim if the grabber isn't knock attacking it
        if (hitInfo.hitter != grabber)
            grabber->breakAnim();
    }
    setAnimation(GameInvariants::BEING_KNOCKED);
    velocity.x = GameInvariants::KNOKED_X_VEL;  // TODO: add random
    velocity.z = GameInvariants::KNOKED_Z_VEL;
    // look at the hit information
    if (hitInfo.hitter!=0){
        //face to the hitter
        faceLeft(hitInfo.fromLeft);
        if (hitInfo.hits->sparseKnock){
            // it's a sparse knock, throw the character away
            double angle = angleBetween(hitInfo.hitter->pos.x, hitInfo.hitter->pos.y,pos.x,pos.y);
            velocity.y = (::sin(angle)*velocity.x)/2;
            velocity.x*= ::cos(angle);
        }else if (!facedLeft) velocity.x*=-1;
    }

}


void CharSprite::breakAnim(){
    currentFrame = currentAnim->fixedFrames.size()-1;
    if (controller!=0) controller->animationBreak();
}



void CharSprite::beThrown(const HitFrame* throwInfo){
    int cX = this->GetImage()->GetWidth()/2;
    int cY = this->GetImage()->GetHeight()/2;
    float dx = this->GetCenter().x - cX;
    float dz = this->GetCenter().y - cY;
    float angle = ::toRadians(this->GetRotation());

    rotate(dx,dz,angle);

    pos.x+=dx;
    pos.z+=dz;
    this->SetCenter(cX,cY);
    velocity.x = throwInfo->throwImpulse->x;
    velocity.y = throwInfo->throwImpulse->y;
    velocity.z = throwInfo->throwImpulse->z;
    rotationalVel = throwInfo->throwRot;
    if (theGrabber!=0 && theGrabber->isFacedLeft()){
        velocity.x*=-1;
        rotationalVel*=-1;
    }
    beingThrown = true;
    invincible = true;
}

void CharSprite::setBeingThrown(bool thrown){
    beingThrown = thrown;
}


bool CharSprite::isBeingThrown() const{ return beingThrown; }
//bool CharSprite::isBeingKnocked() const{ return beingKnocked; }

UInt CharSprite::getCurrentAnimIndex() const{ return currAnimIndex; }
UInt CharSprite::getCurrentAnimFrame() const{ return currentFrame; }
UInt CharSprite::getAnimTotalFrames() const{ return currentAnim->fixedFrames.size(); }

UInt CharSprite::getRotation() const{ return (UInt)GetRotation(); }


void CharSprite::reloadCurrentFrame(){
    setFrame(currentAnim->fixedFrames[currentFrame]);
}


bool CharSprite::faceLeft(bool faceLeft){
    if (facedLeft == faceLeft || currentAnim==0) return false;  // same way, does nothing
    this->FlipX(faceLeft);
    this->facedLeft = faceLeft;
    if (currentFrame>=0 && currentFrame<(int)currentAnim->fixedFrames.size())
        setFrame(currentAnim->fixedFrames[currentFrame]);
    return true;
}
// Player movement control
void CharSprite::moveLeft(bool move){ movingLeft = move; }
void CharSprite::moveRight(bool move){ movingRight = move; }
void CharSprite::moveUp(bool move){ movingUp = move; }
void CharSprite::moveDown(bool move){ movingDown = move; }
void CharSprite::run(bool runMove){
    running = runMove;
    // just in case of a jump, set the airVel to it's current vel
    if (runMove) airPlayerVelX = velocity.x;
    else airPlayerVelX = 0; // else, cancel that velocity
}

bool CharSprite::isRunning() const{ return running; }
bool CharSprite::isFacedLeft() const{ return facedLeft; }
bool CharSprite::isInAir() const{ return pos.z>0; }
bool CharSprite::isMovingLeft() const { return movingLeft; }
bool CharSprite::isMovingRight() const { return movingRight; }
bool CharSprite::isMovingUp() const { return movingUp; }
bool CharSprite::isMovingDown() const { return movingDown; }
bool CharSprite::isStaticJumping() const{
    return airPlayerVelX == 0 && airPlayerVelY == 0;
}
bool CharSprite::isInvincibleAnimPlaying() const{ return currentAnim->invinsible; }
bool CharSprite::isOnBlockingMove() const{
    return currentAnim->block;
}
bool CharSprite::attackedFromBack() const{
    return hitInfo.fromLeft != facedLeft;
}
void CharSprite::faceToHitter(){
    faceLeft(hitInfo.fromLeft);
}


/** stop all player movement */
void CharSprite::stopAll(){
    movingLeft = movingRight = movingUp = movingDown = false;
}


bool CharSprite::animationEnded() const{
    return currentFrame>=(int)currentAnim->fixedFrames.size()-1;
}

CharSprite* CharSprite::getGrabbed() const{ return grabbedOne; }
CharSprite* CharSprite::getTheGrabber() const{ return theGrabber; }
bool CharSprite::isBeingBackGrabbed() const{ return grabbedBack; }
bool CharSprite::isGrabbingOnBack() const{ return grabbingOnBack; }


const IntVector3D* CharSprite::getEndPosition() const{
    if (currentAnim == 0) return 0;
    return currentAnim->endPosition;
}

bool CharSprite::hasHitFrame() const{
    // TODO: ask about action type?
    return currentAnim != 0 && currentAnim->keyFrames.containsKey(currentFrame);
}

const HitFrame* CharSprite::getHits() const{
    if (!hasHitFrame()) return 0;
    return currentAnim->keyFrames.get(currentFrame);
}



void CharSprite::move(float dx, float dy){
    pos.x+=dx;
    pos.y+=dy;
    // also move who's grabbing it
    if (theGrabber!=0) theGrabber->move(dx,dy);
}


//void CharSprite::updateLastPos(){
//    lastPos = pos;
//}
//void CharSprite::simulate(float percentageDone){
//    FloatVector3D tmp;
//    if (pos.x == lastPos.x) tmp.x = pos.x;
//    //else tmp.x = pos.x*percentageDone + lastPos.x*(1-percentageDone);
//    else tmp.x = lastPos.x + (pos.x-lastPos.x)*percentageDone;
//    if (pos.y == lastPos.y) tmp.y = pos.y;
//    else tmp.y = pos.y*percentageDone + lastPos.y*(1-percentageDone);
//    if (pos.z == lastPos.z) tmp.z = pos.z;
//    else tmp.z = pos.z*percentageDone + lastPos.z*(1-percentageDone);
//    // TODO: shadow without the Z
//    this->SetPosition(tmp.x,tmp.y+tmp.z);
//}


UInt CharSprite::beingHit(HitInfo hitInfo){
    this->hitInfo = hitInfo;
    if (controller!=0) return controller->beingHit(hitInfo);
    return 0;
}
