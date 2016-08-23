#include "CharacterController.h"
#include "../Invariants/GameInvariants.h"

#include <iostream> //TODO: remove this


CharacterController::CharacterController(CharSprite* sprite):
    ASpriteController(sprite), runTime(0),
    attackNum(0), attackCounter(0), knockCounter(0), hitPause(0),recoverTime(0), blockHitTime(0),
    noMoreFlip(false), setRun(false), tryingRoll(false),gotHit(false),gotKnocked(false),hitSomeone(false){
    sprite->setController(this);
}

void CharacterController::setCharacterSprite(CharSprite* sprite){
    this->sprite = sprite;
    sprite->setController(this);
}


void CharacterController::update(){
    if (hitPause>0) --hitPause;
    if (hitPause <= 1){ // means it is being updated
        // clear the hitten ones when sprite animation frame changes
        if (sprite->update()) theHittenOnes.clear();

        // test ungrabbing
        int index = sprite->getCurrentAnimIndex();
        if ((index == GameInvariants::GRABBING_FRONT || index == GameInvariants::GRABBING_BACK)
            && (presses[LEFT] && !sprite->isFacedLeft() || presses[RIGHT] && sprite->isFacedLeft())
        ){
            ++ungrabTime;
            if (ungrabTime==GameInvariants::UNGRAB_TIME){
                sprite->unGrab();
                animationBreak();
            }
        }else ungrabTime = 0;

        // hit reaction
        if (gotKnocked || gotHit){
            bool fromBack = sprite->attackedFromBack();
            if (sprite->isOnBlockingMove() && !fromBack){
                blockHitTime = GameInvariants::BLOCK_HIT_TIME;
            // hit if being attacked from back or has no grabbed one
            }else{ //if (fromBack || sprite->getGrabbed()==0){
                blockHitTime = 0;
                // be knocked down
                if (gotKnocked){
                    // TODO:
                    sprite->knockDown();
                    //knockDown();
                // be hit
                }else if (gotHit){
                    if (sprite->getTheGrabber()!=0){
                        if (sprite->isBeingBackGrabbed() && sprite->setAnimation(GameInvariants::BEING_GRABBED_HIT_BACK));
                        else sprite->setAnimation(GameInvariants::BEING_GRABBED_HIT_FRONT);
                    }else sprite->setAnimation(GameInvariants::BEING_HIT);
                    // face to hitter if that flag is defined true (and not grabbed)
                    if ( sprite->getTheGrabber()==0 && GameInvariants::FACE_WHEN_HIT)
                        sprite->faceToHitter();
                }
            }
            gotKnocked = gotHit = false;
        }

        // if blocking attack
        if (blockHitTime>1){
            --blockHitTime;
            sprite->velocity.x = GameInvariants::BLOCK_HIT_VEL;
            if (sprite->isFacedLeft()) sprite->velocity.x*=-1;
        }else if (blockHitTime == 1){
            blockHitTime = 0;
            sprite->velocity.x = 0;
        }


        // timing controllers:
        if (runTime>0) --runTime;
        if (runTime == 1) setRun = false;
        if (attackCounter>0) --attackCounter;
        if (knockCounter>1) --knockCounter;

        if (recoverTime>0){
            --recoverTime;
            if (recoverTime==1 && !sprite->isInvincibleAnimPlaying())
                sprite->setInvincible(false);
        }
    }
}



bool CharacterController::animationBreak(){
    // if grabbing, its another story...
    if (sprite->getGrabbed() != 0) return grabAnimationBreak();
    // if being grabbed, restore the grabber power
    if (sprite->getTheGrabber()!=0){
        sprite->getTheGrabber()->reloadCurrentFrame();
    }

    UInt anim = sprite->getCurrentAnimIndex();

    if (!sprite->isInAir()){
        if (sprite->isBeingThrown() || anim == GameInvariants::BEING_KNOCKED){
            // TODO: safe landing
            // TODO: quaque
            sprite->setBeingThrown(false);
            if (anim == GameInvariants::BEING_THROWN || anim == GameInvariants::BEING_KNOCKED)
                groundBounce();
            else
                groundSuffer();
            return true;
        }
        else if (anim == GameInvariants::GROUND_BOUNCING || anim == GameInvariants::LYING){
            groundSuffer();
            return true;
        }
    }

    if (   anim == GameInvariants::NORMAL
        || anim == GameInvariants::WALK
        || anim == GameInvariants::RUN
        || (sprite->getTheGrabber() != 0 && anim != GameInvariants::BEING_BACK_GRABBED_DEFENCE)// if grabbed, nothing to do
    ) return false;
    // landing
    if (anim == GameInvariants::IN_AIR || anim == GameInvariants::JUMP || anim == GameInvariants::RUN_JUMP
        || anim == GameInvariants::JUMP_DOWN_ATTACK || anim == GameInvariants::JUMP_MOVE_ATTACK
        || anim == GameInvariants::JUMP_RUN_ATTACK || anim == GameInvariants::JUMP_STATIC_ATTACK){
        if (!sprite->isInAir()){
            sprite->setAnimation(GameInvariants::LAND);
        }else // else if jump anim ends, set in air
            if (anim == GameInvariants::JUMP || anim == GameInvariants::RUN_JUMP) sprite->setAnimation(GameInvariants::IN_AIR);
        else return false;
    }
    // if prepare to jump is over, jump!
    else if (anim == GameInvariants::PREPARE_JUMP){
        if (sprite->isRunning()) sprite->setAnimation(GameInvariants::RUN_JUMP);
        else sprite->setAnimation(GameInvariants::JUMP);
    }
    // attacking
    else if (anim >= GameInvariants::ATTACK_1 && anim <= GameInvariants::ATTACK_5){
        if (anim < GameInvariants::ATTACK_5 && hitSomeone){
            attackCounter = GameInvariants::ATTACK_TIME;
            ++attackNum;
        }else attackCounter = attackNum = 0;
        hitSomeone = false;

        sprite->setAnimation(GameInvariants::NORMAL);
        testWalk();
    }
    // defending from grab
    else if ( anim == GameInvariants::BEING_BACK_GRABBED_DEFENCE){
        sprite->setAnimation(GameInvariants::NORMAL);
        sprite->getTheGrabber()->reloadCurrentFrame();
    //default:
    }else if (sprite->isInAir()){
        if (anim == GameInvariants::BEING_THROWN) return false;
        sprite->setAnimation(GameInvariants::IN_AIR);
    }else if (sprite->animationEnded()){
            // recover from knock hit?
            if (anim == GameInvariants::STANDING_UP){
                recoverTime = GameInvariants::RECOVER_TIME;
            }
            sprite->setAnimation(GameInvariants::NORMAL);
            // if directional keys pressed, walk
            testWalk();
    }else return false;
    return true;
}







bool CharacterController::grabAnimationBreak(){
    UInt anim = sprite->getCurrentAnimIndex();
    if (   anim == GameInvariants::GRABBING_FRONT
        || anim == GameInvariants::GRABBING_BACK
        || anim == GameInvariants::GRAB_WALK
        || anim == GameInvariants::GRAB_BACK_WALK
        || anim == GameInvariants::GRAB_RUN
    ) return false;
    //grabbing
    if (anim == GameInvariants::GRABBING_UP){
        if (sprite->isGrabbingOnBack()) sprite->setAnimation(GameInvariants::GRABBING_BACK);
        else sprite->setAnimation(GameInvariants::GRABBING_FRONT);
        testWalk();
    }
    // landing
    else if (anim == GameInvariants::GRAB_JUMP || anim == GameInvariants::FRONT_GRAB_JUMP_ATTACK
        || anim == GameInvariants::BACK_GRAB_JUMP_ATTACK){
        if (!sprite->isInAir()){
            if (anim == GameInvariants::GRAB_JUMP || anim == GameInvariants::FRONT_GRAB_JUMP_ATTACK && !sprite->setAnimation(GameInvariants::FRONT_GRAB_JUMP_ATTACK_LAND)
             || anim == GameInvariants::BACK_GRAB_JUMP_ATTACK && !sprite->setAnimation(GameInvariants::BACK_GRAB_JUMP_ATTACK_LAND)
            ) sprite->setAnimation(GameInvariants::LAND);
            sprite->unGrab();
            testWalk();
        }
        else return false;
    }
    // attacking
    else if (anim >= GameInvariants::FRONT_GRAB_FRONT_ATTACK_1 && anim <= GameInvariants::FRONT_GRAB_FRONT_ATTACK_5){
        if (anim < GameInvariants::FRONT_GRAB_FRONT_ATTACK_5){
            attackCounter = GameInvariants::ATTACK_TIME;
            ++attackNum;
        }else attackCounter = attackNum = 0;

        if (sprite->isGrabbingOnBack()) sprite->setAnimation(GameInvariants::GRABBING_BACK);
        else sprite->setAnimation(GameInvariants::GRABBING_FRONT);
        testWalk();
    }
    // flip
    else if (anim == GameInvariants::FLIP_OVER || anim == GameInvariants::FAILED_FLIP_OVER){
        if (!noMoreFlip){
            sprite->grab(sprite->getGrabbed(),!sprite->isGrabbingOnBack());
            if (sprite->isGrabbingOnBack()) sprite->setAnimation(GameInvariants::GRABBING_BACK);
            else sprite->setAnimation(GameInvariants::GRABBING_FRONT);
            noMoreFlip = true;
        }else{
            sprite->setAnimation(GameInvariants::NORMAL);
            sprite->unGrab();
            testWalk();
        }
    }
    //default:
    else if (sprite->animationEnded()){
        if (sprite->isGrabbingOnBack()) sprite->setAnimation(GameInvariants::GRABBING_BACK);
        else sprite->setAnimation(GameInvariants::GRABBING_FRONT);
            // if directional keys pressed, walk
            testWalk();
    }else return false;
    return true;
}







void CharacterController::testWalk(){
    // test walk occurs after a break or stop, so no run possibility
    //runTime = 0;
    sprite->run(false);
    // test if is still moving
    bool moving = true;
    tryingRoll = false;
    // left/right
    if (presses[LEFT]){ sprite->moveLeft(true); }
    else if (presses[RIGHT]){ sprite->moveRight(true); }
    else moving = false;
    // up/down
    if (presses[UP]){ sprite->moveUp(true); moving = true; }
    else if (presses[DOWN]){ sprite->moveDown(true); moving = true; }

    if (moving){
        tryingRoll = !presses[LEFT] && !presses[RIGHT];
        if (sprite->getGrabbed() != 0)
            grabWalk();
        else walk();
    }
}



// walk, run, and roll up/down
void CharacterController::walk(){
    UInt anim = sprite->getCurrentAnimIndex();
    // if on normal or waiting, put it walking
    if (anim == GameInvariants::NORMAL || anim == GameInvariants::WAITING)
        sprite->setAnimation(GameInvariants::WALK);
    // if walking and press left or right
    if (sprite->getCurrentAnimIndex() == GameInvariants::WALK){
        // flip if needed
        if (presses[LEFT] || presses[RIGHT]){
            sprite->faceLeft(presses[LEFT]);
        }

        // pressing oposite directions, stop and don't run
        if (presses[LEFT] && presses[RIGHT] || presses[UP] && presses[DOWN]){
            setRun = false;
            sprite->run(false);
            return;
        }

        // test run or roll
        if (setRun && runTime>0){
            setRun = false;
            // left or right, means to run
            if (!tryingRoll){
                sprite->setAnimation(GameInvariants::RUN);
                sprite->run(true);
            // else test roll up or down
            }else if (presses[UP])sprite->setAnimation(GameInvariants::ROLL_UP);
            else if (presses[DOWN])sprite->setAnimation(GameInvariants::ROLL_DOWN);
            //runTime = 0;
        }//else runTime = GameInvariants::RUN_TIME;
    }
}

// stops the movement if any move key is pressed/released
void CharacterController::stop(){
    UInt anim = sprite->getCurrentAnimIndex();
    // some animations ignores the stop
    if (anim == GameInvariants::PREPARE_JUMP || sprite->isInAir()) return;
    // walk or run, what to do
    if (anim == GameInvariants::WALK || anim == GameInvariants::RUN){
        // do not keep runing if faced in wrong direction
        if (anim == GameInvariants::RUN && ( sprite->isFacedLeft() && !presses[LEFT] || (!sprite->isFacedLeft() && !presses[RIGHT])) ){
            setRun = false;
            sprite->run(false);
            sprite->setAnimation(GameInvariants::NORMAL);
        }
        // walk: left or right
        else if (presses[LEFT] || presses[RIGHT] || presses[UP] || presses[DOWN]){
            if (presses[LEFT] || presses[RIGHT]){
                // see if keep waling or runing
                if (presses[LEFT] && sprite->isFacedLeft() || presses[RIGHT] && !sprite->isFacedLeft())
                    return;
                sprite->faceLeft(presses[LEFT]);
            }
            sprite->run(false);
            sprite->setAnimation(GameInvariants::WALK);
        // stoped: if not pressing any dir key at all, stop and return
        }else{
            sprite->run(false);
            sprite->setAnimation(GameInvariants::NORMAL);
            return;
        }
    }
    // if directional keys pressed, walk or run or roll
    testWalk();
}

// Jump movement
void CharacterController::jump(){
    UInt anim = sprite->getCurrentAnimIndex();
    if (   anim == GameInvariants::NORMAL
        || anim == GameInvariants::WAITING
        || anim == GameInvariants::WALK
        || anim == GameInvariants::RUN
    ) sprite->setAnimation(GameInvariants::PREPARE_JUMP);
}




void CharacterController::attack(){
    UInt anim = sprite->getCurrentAnimIndex();
    // TODO: add a state to reduce comparisons
    if (   anim == GameInvariants::NORMAL
        || anim == GameInvariants::WAITING
        || anim == GameInvariants::WALK
    ){
        // if ready to run attack, do it and return
        if (setRun && runTime>0){ runAttack(); return; }
        // if time exceeded, reset attack combos
        if (attackCounter ==0) attackNum = 0;
        // try set the next attack animation
        if (!sprite->setAnimation(GameInvariants::ATTACK_1 + attackNum)){
            // if fail, means no more animations, reset it and do the first one
            attackNum = 0;
            sprite->setAnimation(GameInvariants::ATTACK_1);
        }
    }else if (anim == GameInvariants::RUN) runAttack();
}


void CharacterController::runAttack(){
    setRun = false;
    sprite->run(false);
    sprite->setAnimation(GameInvariants::RUN_ATTACK_STAR_0); // TODO: Add stars
}


void CharacterController::jumpAttack(){
    UInt anim = sprite->getCurrentAnimIndex();
    // TODO: add a state to reduce comparisons
    if (anim == GameInvariants::PREPARE_JUMP)
        sprite->setAnimation(GameInvariants::JUMP);
    if (   anim == GameInvariants::JUMP
        || anim == GameInvariants::RUN_JUMP
        || anim == GameInvariants::IN_AIR
    ){
        if (presses[DOWN]) sprite->setAnimation(GameInvariants::JUMP_DOWN_ATTACK);
        else if (sprite->isStaticJumping()) sprite->setAnimation(GameInvariants::JUMP_STATIC_ATTACK);
        else if (!(sprite->isRunning() && sprite->setAnimation(GameInvariants::JUMP_RUN_ATTACK)))
            sprite->setAnimation(GameInvariants::JUMP_MOVE_ATTACK);
    }
}




// walk, run, and roll up/down
void CharacterController::grabWalk(){
    UInt anim = sprite->getCurrentAnimIndex();
    // if on normal or waiting, put it walking
    if (anim == GameInvariants::GRABBING_FRONT || anim == GameInvariants::GRABBING_BACK){
        if (presses[LEFT] && !sprite->isFacedLeft() || presses[RIGHT] && sprite->isFacedLeft()){
            if (!sprite->setAnimation(GameInvariants::GRAB_BACK_WALK)) return;
        } else if (!sprite->setAnimation(GameInvariants::GRAB_WALK)) return;
    }
    anim = sprite->getCurrentAnimIndex();
    // if walking and press left or right
    if ( anim == GameInvariants::GRAB_WALK || anim == GameInvariants::GRAB_BACK_WALK){

        // pressing oposite directions, stop and don't run
        if (presses[LEFT] && presses[RIGHT] || presses[UP] && presses[DOWN]){
            setRun = false;
            sprite->run(false);
            return;
        }
        // flip anim if needed
        if (anim == GameInvariants::GRAB_BACK_WALK && (presses[LEFT] && sprite->isFacedLeft() || presses[RIGHT] && !sprite->isFacedLeft()))
            sprite->setAnimation(GameInvariants::GRAB_WALK);
        else if (anim == GameInvariants::GRAB_WALK && (presses[LEFT] && !sprite->isFacedLeft() || presses[RIGHT] && sprite->isFacedLeft()))
            sprite->setAnimation(GameInvariants::GRAB_BACK_WALK);
        // test run, only if faced to the same direction
        if (setRun && runTime>0 && sprite->isFacedLeft() == presses[LEFT]){
            setRun = false;
            // left or right, means to run
            if (!tryingRoll){
                sprite->setAnimation(GameInvariants::GRAB_RUN);
                sprite->run(true);
            }
        }
    }
}



// stops the movement if any move key is pressed/released
void CharacterController::grabStop(){
    UInt anim = sprite->getCurrentAnimIndex();
    // some animations ignores the stop
    if (anim == GameInvariants::PREPARE_JUMP || sprite->isInAir()) return;
    // walk or run, what to do
    if (anim == GameInvariants::GRAB_WALK || anim == GameInvariants::GRAB_BACK_WALK || anim == GameInvariants::GRAB_RUN){
        // do not keep runing if faced in wrong direction
        if (anim == GameInvariants::GRAB_RUN && ( sprite->isFacedLeft() && !presses[LEFT] || (!sprite->isFacedLeft() && !presses[RIGHT])) ){
            setRun = false;
            sprite->run(false);
            if (sprite->isGrabbingOnBack()) sprite->setAnimation(GameInvariants::GRABBING_BACK);
            else sprite->setAnimation(GameInvariants::GRABBING_FRONT);
        }
        // walk: left or right
        else if (presses[LEFT] || presses[RIGHT] || presses[UP] || presses[DOWN]){
            // see if keep running
            if (anim == GameInvariants::GRAB_RUN && (presses[LEFT] && sprite->isFacedLeft() || presses[RIGHT] && !sprite->isFacedLeft()))
                return;
            setRun = false;
            sprite->run(false);
            // flip anim if needed
            if (!presses[LEFT] && !presses[RIGHT] || (anim == GameInvariants::GRAB_BACK_WALK && (presses[LEFT] && sprite->isFacedLeft() || presses[RIGHT] && !sprite->isFacedLeft())))
                sprite->setAnimation(GameInvariants::GRAB_WALK);
            else if (anim == GameInvariants::GRAB_WALK && (presses[LEFT] && !sprite->isFacedLeft() || presses[RIGHT] && sprite->isFacedLeft()))
                sprite->setAnimation(GameInvariants::GRAB_BACK_WALK);
        // stoped: if not pressing any dir key at all, stop and return
        }else{
            sprite->run(false);
            if (sprite->isGrabbingOnBack()) sprite->setAnimation(GameInvariants::GRABBING_BACK);
            else sprite->setAnimation(GameInvariants::GRABBING_FRONT);
            return;
        }
    }
    // if directional keys pressed, walk or run or roll
    testWalk();
}



void CharacterController::grabJump(){
    UInt anim = sprite->getCurrentAnimIndex();
    if (    anim == GameInvariants::GRABBING_BACK
         || anim == GameInvariants::GRABBING_FRONT
         || anim == GameInvariants::GRAB_WALK
         || anim == GameInvariants::GRAB_BACK_WALK
         || anim == GameInvariants::GRAB_RUN
    ){
        if (!sprite->setAnimation(GameInvariants::GRAB_JUMP))
            // TODO: may fail because of obstacles
            sprite->setAnimation(GameInvariants::FLIP_OVER);
    }
}



void CharacterController::grabAttack(){
    UInt anim = sprite->getCurrentAnimIndex();
    if ( !sprite->isGrabbingOnBack() && (
            anim == GameInvariants::GRABBING_FRONT
         || anim == GameInvariants::GRAB_WALK
         || anim == GameInvariants::GRAB_BACK_WALK
    )){
        if (presses[LEFT] || presses[RIGHT]){
            // if pressing forward
            if (sprite->isFacedLeft() && presses[LEFT] || !sprite->isFacedLeft() && presses[RIGHT]){
                // if ready to run attack, do it and return
                if (setRun && runTime>0){ grabRunAttack(); return; }
                // try set the next attack animation
                if (!sprite->setAnimation(GameInvariants::FRONT_GRAB_FRONT_ATTACK_1 + attackNum)){
                    // if fail, means no more animations, ungrab
                    sprite->unGrab();
                }
            // else, it's pressing backward
            }else sprite->setAnimation(GameInvariants::FRONT_GRAB_BACK_ATTACK);
            //no pressing at all
        }else  sprite->setAnimation(GameInvariants::FRONT_GRAB_STATIC_ATTACK);
    }else if (anim == GameInvariants::GRAB_RUN) grabRunAttack();
    else if (anim == GameInvariants::GRABBING_BACK || anim == GameInvariants::GRAB_WALK || anim == GameInvariants::GRAB_BACK_WALK){
        if (presses[LEFT] || presses[RIGHT]){
            // if pressing forward
            if ((sprite->isFacedLeft() && presses[LEFT] || !sprite->isFacedLeft() && presses[RIGHT])
                && sprite->setAnimation(GameInvariants::BACK_GRAB_FRONT_ATTACK) ) ;
            // else if pressing backward
            else if (!sprite->setAnimation(GameInvariants::BACK_GRAB_BACK_ATTACK))
                sprite->setAnimation(GameInvariants::BACK_GRAB_STATIC_ATTACK);
        }else sprite->setAnimation(GameInvariants::BACK_GRAB_STATIC_ATTACK);
    }
    else if (anim == GameInvariants::FLIP_OVER
            && sprite->getCurrentAnimFrame()>=sprite->getAnimTotalFrames()*GameInvariants::FLIP_ATTACK_MINIMUM
            && sprite->getCurrentAnimFrame()<=sprite->getAnimTotalFrames()*GameInvariants::FLIP_ATTACK_MAXIMUM
    ){
        sprite->setAnimation(GameInvariants::FLIP_OVER_ATTACK);
    }
}

void CharacterController::grabRunAttack(){
    setRun = false;
    sprite->run(false);
    sprite->setAnimation(GameInvariants::GRAB_RUN_ATTACK);
}



void CharacterController::grabJumpAttack(){
    UInt anim = sprite->getCurrentAnimIndex();
    if (anim == GameInvariants::GRAB_JUMP){
        if (sprite->isGrabbingOnBack())
            sprite->setAnimation(GameInvariants::BACK_GRAB_JUMP_ATTACK);
        else sprite->setAnimation(GameInvariants::FRONT_GRAB_JUMP_ATTACK);
    }
}


void CharacterController::grabDefence(){
    CharSprite* theGrabber = sprite->getTheGrabber();
    // not grabbed on back or already defending, ignore
    if (!theGrabber->isGrabbingOnBack() || sprite->getCurrentAnimIndex() == GameInvariants::BEING_BACK_GRABBED_DEFENCE)
        return;
    UInt anim = theGrabber->getCurrentAnimIndex();
    if (anim == GameInvariants::GRABBING_BACK
        || anim == GameInvariants::GRAB_WALK        // TODO: keep this?
        || anim == GameInvariants::GRAB_BACK_WALK   // TODO: keep this?
        || anim == GameInvariants::GRABBING_UP      // TODO: keep this?
    ) sprite->setAnimation(GameInvariants::BEING_BACK_GRABBED_DEFENCE);

}


void CharacterController::grabDefenceThrow(){
    if (sprite->getCurrentAnimIndex() == GameInvariants::BEING_BACK_GRABBED_DEFENCE
            && sprite->getCurrentAnimFrame()>=sprite->getAnimTotalFrames()*GameInvariants::FLIP_ATTACK_MINIMUM
            && sprite->getCurrentAnimFrame()<=sprite->getAnimTotalFrames()*GameInvariants::FLIP_ATTACK_MAXIMUM
    ){
        sprite->grab(sprite->getTheGrabber());
        sprite->getTheGrabber()->unGrab();
        sprite->setAnimation(GameInvariants::THROWS_WHOS_GRABBING);
    }
}



void CharacterController::special(){
    UInt anim = sprite->getCurrentAnimIndex();
    if (    !sprite->isInAir() &&(
           anim == GameInvariants::NORMAL
        || anim == GameInvariants::WAITING
        || anim == GameInvariants::WALK
        || anim == GameInvariants::RUN
        || anim == GameInvariants::BEING_HIT
        || anim == GameInvariants::BEING_GRABBED_FRONT
        || anim == GameInvariants::BEING_GRABBED_BACK
        || anim == GameInvariants::BEING_GRABBED_HIT_BACK
        || anim == GameInvariants::BEING_GRABBED_HIT_FRONT
        || anim == GameInvariants::GRABBING_FRONT
        || anim == GameInvariants::GRABBING_BACK
        || anim == GameInvariants::GRAB_WALK
        || anim == GameInvariants::GRAB_BACK_WALK
        || anim == GameInvariants::GRAB_RUN
    )){
        // if grabbing, ungrab
        if (sprite->getGrabbed()!=0) sprite->unGrab();
        // if being grabbed, ungrab
        if (sprite->getTheGrabber() != 0){
            CharSprite* grabber = sprite->getTheGrabber();
            UInt anim = grabber->getCurrentAnimIndex();
            if ( anim == GameInvariants::FLIP_OVER || anim == GameInvariants::FAILED_FLIP_OVER)
                return; // do not special while being fliped
            grabber->unGrab();
            grabber->breakAnim();
        }
        // defencive or offencive
        if (presses[LEFT] || presses[RIGHT])
            sprite->setAnimation(GameInvariants::OFENCIVE_SPECIAL);
        else sprite->setAnimation(GameInvariants::DEFENCIVE_SPECIAL);

    }
}


void CharacterController::jumpSpecial(){
    if ((sprite->getCurrentAnimIndex() == GameInvariants::JUMP || sprite->getCurrentAnimIndex() == GameInvariants::RUN_JUMP)
        && ::fabs(sprite->velocity.z) < GameInvariants::JUMP_SPECIAL_MAX_SPEED
    ) sprite->setAnimation(GameInvariants::JUMP_SPECIAL);
}



void CharacterController::backAttack(){
    UInt anim = sprite->getCurrentAnimIndex();
    // TODO: add a state to reduce comparisons
    if (   anim == GameInvariants::NORMAL
        || anim == GameInvariants::WAITING
        || anim == GameInvariants::WALK
        || anim == GameInvariants::PREPARE_JUMP
    ){
        sprite->setAnimation(GameInvariants::BACK_ATTACK);
        knockCounter = 0;
    }
}

void CharacterController::knockAttack(){
    UInt anim = sprite->getCurrentAnimIndex();
    // TODO: add a state to reduce comparisons
    if (   anim == GameInvariants::NORMAL
        || anim == GameInvariants::WAITING
        || anim == GameInvariants::WALK
    ) sprite->setAnimation(GameInvariants::KNOCK_ATTACK);
    knockCounter =0;
}

void CharacterController::super(){

}

void CharacterController::helpCall(){

}







UInt CharacterController::beingHit(const HitInfo& hitInfo){
    // grabing and being attacked from front, do not get hitten
    if (!sprite->attackedFromBack() && sprite->getGrabbed()!=0) return 0;
    // TODO: invincible sprite, ignore
    // TODO: Use sf::vectors!
    // TODO: rotation! (when thrown rotation) --> TODO: just transform to local, should work
//    std::cout << "\n\n\n\n\n\n";
    std::vector<Point16>::const_iterator it;
    for(it = hitInfo.hits->actionPoints.begin() ; it!= hitInfo.hits->actionPoints.end() ; ++it){
        float realX = hitInfo.hitter->GetPosition().x + (hitInfo.hitter->isFacedLeft() ? -it->x : it->x);
        float realY = hitInfo.hitter->GetPosition().y + it->y;
//        std::cout << "(" << it->x << ", " << it->y << ") and original: (" << hitInfo.hitter->GetPosition().x << ", " << hitInfo.hitter->GetPosition().y << ")\n";
//        std::cout << "real: (" << realX << ", " << realY << ")\n";
        // TODO: rotation
        // TODO: transform to local
        realX-=sprite->GetPosition().x;
        realX+=sprite->GetCenter().x;
        realY-=sprite->GetPosition().y;
        realY+=sprite->GetCenter().y;
        int localX = (int) realX;
        int localY = (int) realY;
//        std::cout << "local: (" << localX << ", " << localY << ")\n";
//        std::cout << "sprite pos: (" << sprite->GetPosition().x << ", " << sprite->GetPosition().y << "), size: (" << sprite->GetSize().x << ", " << sprite->GetSize().y << ")\n";
//        std::cout << "sprite center: (" << sprite->GetCenter().x << ", " << sprite->GetCenter().y << ")\n";

        // TODO: why local > 0 ? not sure, but getpixel gives error when it's zero
        if ( localX > 0 && localY > 0 && localX < sprite->GetSize().x && localY < sprite->GetSize().y
             && sprite->GetPixel(localX, localY).a==255
        ){
            //std::cout << "I'm hit\n\n";
            gotKnocked = (sprite->isInAir() && sprite->getTheGrabber()==0 || hitInfo.hits->actionType >= GameInvariants::AT_KNOCK);
            gotHit = true;
            // TODO: count energy and points
            return 1;   // TODO: return energy lost
        }
//        std::cout << "\n";
    }
    return 0;
}



void CharacterController::groundBounce(){
    float oldVelX = sprite->velocity.x*GameInvariants::ELASTICITY;
    float oldVelY = sprite->velocity.y*GameInvariants::ELASTICITY;
    if (sprite->setAnimation(GameInvariants::GROUND_BOUNCING) || sprite->setAnimation(GameInvariants::LYING))
        sprite->velocity.z = GameInvariants::GROUND_BOUNCE_VEL;
    sprite->velocity.x = oldVelX;
    sprite->velocity.y = oldVelY;
}

void CharacterController::groundSuffer(){
    sprite->resetSpeed();
    // TODO: do not stand up if dying
    // TODO: somewhere the suffering energy must be stored, and suffered here
    sprite->setAnimation(GameInvariants::STANDING_UP);
}



//void CharacterController::knockDown(){
//    sprite->setAnimation(GameInvariants::BEING_KNOCKED);
//    HitFrame f;
//    f.throwDamage = 0;
//    f.throwImpulse = new FloatVector3D(GameInvariants::KNOKED_X_VEL,0,GameInvariants::KNOKED_Z_VEL);
//    f.throwRot = 0;
//    sprite->beThrown(&f);
//}





void CharacterController::threathKey(Key k, bool press){
    // TODO: regist if it has any effect. If yes, on end regist it (quickState)
    // else, just ignore the key
    bool grabbing = sprite->getGrabbed()!=0;
    switch(k){
        // Directional Keys, for walk, run and roll
        // TODO: when it's on a grabbing, and ofencive special, etc
        // If oposite key is pressed, just ignore it
        case UP:
            // if being grabbed, ignore move
            if (sprite->getTheGrabber() != 0) break;

            sprite->moveUp(press);

            if (press){
                tryingRoll = !presses[LEFT] && !presses[RIGHT];
                if (runTime == 0 || !tryingRoll) runTime = GameInvariants::RUN_TIME;
                else setRun = true;
                grabbing ? grabWalk() : walk();
            }else grabbing ? grabStop() : stop();

            break;
        case DOWN:
            // if being grabbed, ignore move
            if (sprite->getTheGrabber() != 0) break;

            sprite->moveDown(press);

            if (press){
                tryingRoll = !presses[LEFT] && !presses[RIGHT];
                if (runTime == 0 || !tryingRoll) runTime = GameInvariants::RUN_TIME;
                else setRun = true;
                grabbing ? grabWalk() : walk();
            }else grabbing ? grabStop() : stop();

            break;
        case LEFT:
            // if being grabbed, ignore move
            if (sprite->getTheGrabber() != 0) break;
            sprite->moveLeft(press);

           if (press){
                if (runTime == 0 || tryingRoll) runTime = GameInvariants::RUN_TIME;
                else setRun = true;
                tryingRoll = false;
                grabbing ? grabWalk() : walk();
            }else grabbing ? grabStop() : stop();

            break;
        case RIGHT:
            // if being grabbed, ignore move
            if (sprite->getTheGrabber() != 0) break;

            sprite->moveRight(press);

            if (press){
                if (runTime == 0 || tryingRoll) runTime = GameInvariants::RUN_TIME;
                else setRun = true;
                tryingRoll = false;
                grabbing ? grabWalk() : walk();
            }else grabbing ? grabStop() : stop();

            break;


        case A:
            if (press)  // TODO: count to super
                if (grabbing && sprite->getGrabbed()->getCurrentAnimIndex() == GameInvariants::BEING_BACK_GRABBED_DEFENCE) break;
                else if (sprite->isInAir()) jumpSpecial();
                else special();
            break;

        case B:
            if (press){
                if (sprite->getTheGrabber() !=0){
                    grabDefenceThrow();
                    break;
                }
                else if (grabbing){
                    if (sprite->getGrabbed()->getCurrentAnimIndex() == GameInvariants::BEING_BACK_GRABBED_DEFENCE) break;
                    if (sprite->isInAir()) grabJumpAttack();
                    else grabAttack();
                    break;
                }
            }else{
                if (knockCounter == 1)
                    knockAttack();
                break; // TODO count to knockAttack
            }

            knockCounter = GameInvariants::KNOCK_TIME+1;
            if (sprite->isInAir()) jumpAttack();
            else if (presses[C]) backAttack();
            else attack();
            break;

        case C:
            if (press){
                if (grabbing){
                    if (sprite->getGrabbed()->getCurrentAnimIndex() == GameInvariants::BEING_BACK_GRABBED_DEFENCE) break;
                    grabJump();
                    break;
                }
                else if (sprite->getTheGrabber()!=0){ grabDefence(); break; }
                if (presses[B]) backAttack();
                else jump();
            }
            break;

        case D:
            if (grabbing && sprite->getGrabbed()->getCurrentAnimIndex() == GameInvariants::BEING_BACK_GRABBED_DEFENCE) break;
            else if (sprite->getTheGrabber()!=0){
                if (sprite->getCurrentAnimIndex() == GameInvariants::BEING_BACK_GRABBED_DEFENCE) grabDefenceThrow();
                else grabDefence();
                break;
            }
            if (press) backAttack();
            break;

        case E:
            if (press) knockAttack();
            break;

        case F:
            break;

        case CALL:
            break;

        default:
            break;
    }
//    if (press && (k == KEFT || k == RIGHT)){
//        if (doubleForward = last == k && (k == LEFT || k == RIGHT))
//            doubleForwardTime = GameInvariants::RUN_TIME;
//    }
}




// -------------------------
//  ---- grabbing test ----
// -------------------------
bool CharacterController::testGrab(CharSprite* other){
    // if the other is in air or invincible, ignore TODO: do not grab when invincible!
    if (other->isInAir() || other->isInvincible()
        || other->getTheGrabber() != 0 || other->getGrabbed()!=0
    ) return false;
    // if no move, no grab
    UInt anim = sprite->getCurrentAnimIndex();
    if (!(anim==GameInvariants::WALK || anim==GameInvariants::RUN)) return false;

    // get const pointers to their positions
    const FloatVector3D& thisPos = sprite->pos;
    const FloatVector3D& otherPos = other->pos;
    // save the old position of the grabbed one
    FloatVector3D oldOtherPos(otherPos);
    // compute the distance to grab
    int difX = (int)(otherPos.getX() - thisPos.getX());
    if (sprite->isFacedLeft()) difX*=-1;
    int difY = ::abs((int)(otherPos.getY() - thisPos.getY()));

    // test if the distance is enough
    if (difX>-GameInvariants::GRAB_X_SPACE/2 && difX<GameInvariants::GRAB_X_SPACE && difY < GameInvariants::GRAB_Y_SPACE){
        bool backGrab = other->isFacedLeft() == sprite->isFacedLeft();
        sprite->grab(other,backGrab);
        // grab, animation acordingly to where they're facing.
        if (!sprite->setAnimation(GameInvariants::GRABBING_UP)){
            if (backGrab) sprite->setAnimation(GameInvariants::GRABBING_BACK);
            else sprite->setAnimation(GameInvariants::GRABBING_FRONT);
            testWalk();
        }
        // arrange positions: the grabbed one stays in same position
        other->move(oldOtherPos.getX()-otherPos.getX(), oldOtherPos.getY()-otherPos.getY());
        attackNum = 0;
        noMoreFlip = false; // can flip twice

        return true;
    }
    return false;
}




void CharacterController::testHit(ActiveSprite* otherSprite){
    if (hitPause>0 && hitPause!= GameInvariants::HIT_PAUSE || theHittenOnes.find(otherSprite)!=theHittenOnes.end()) return;
    if (otherSprite->isInvincible()) return;
    // TODO: this test before
    if (!sprite->hasHitFrame()) return;

    HitInfo hf(sprite->getHits(), sprite);
    if (hf.hits->actionType == GameInvariants::AT_THROW) return; // throw, not hit
    // only if in close y
    if (::abs((int)(sprite->pos.y-otherSprite->pos.y)) > GameInvariants::GRAB_Y_SPACE) return;

    if (hf.hits->frontalKnock) hf.fromLeft = !sprite->isFacedLeft();
    else if (hf.hits->backKnock) hf.fromLeft = sprite->isFacedLeft();
    else hf.fromLeft = (
            sprite->velocity.x > 0 || sprite->airPlayerVelX > 0 && !sprite->isFacedLeft()
            || sprite->velocity.x == 0 && sprite->pos.x<otherSprite->pos.x
        );
    if (otherSprite->beingHit(hf) >0 ){
        //otherSprite->attackedFromLeft = otherSprite->pos.x>sprite->pos.x;

//        if (hf.hits->actionType >= GameInvariants::AT_KNOCK)
//            hitPause = GameInvariants::KNOCK_PAUSE;
//        else{
//            hitPause = GameInvariants::HIT_PAUSE;
//            // only add characters that have been hit, not knocked
//            theHittenOnes.insert(otherSprite);
//        }

        //hitPause = GameInvariants::HIT_PAUSE;
        // make the pause
        if (hitPause == 0){
            if (hf.hits->actionType >= GameInvariants::AT_KNOCK)
                hitPause = GameInvariants::KNOCK_PAUSE;
            else hitPause = GameInvariants::HIT_PAUSE;
        }

        // regist the hit
        if (hf.hits->actionType < GameInvariants::AT_KNOCK){
            theHittenOnes.insert(otherSprite);
            hitSomeone = true;
        }


        // TODO: maybe different when jumping?
        //if (otherSprite->isOnBlockingMove() && sprite->isInAir())
        //    this->animationBreak();
    }
    // TODO: return type, extention to add points
}


void CharacterController::testThrow(){
    CharSprite* grabbed = sprite->getGrabbed();
    if (grabbed==0) return;

    const HitFrame* hf = sprite->getHits();
    if (hf == 0) return;

    if (hf->actionType == GameInvariants::AT_THROW){
        grabbed->beThrown(hf);
        sprite->unGrab();
    }
}

