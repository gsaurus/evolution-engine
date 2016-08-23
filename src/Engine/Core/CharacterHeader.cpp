//#include <iostream>

#include "CharacterHeader.h"
#include "DataBase.h"
#include "../Invariants/GameInvariants.h"


CharacterHeader::CharacterHeader(const std::string& characterName, UInt body, UInt head, UInt pallet):
    characterName(characterName), body(body), head(head), pallet(pallet)
{
    charObj = 0; bodyObj = 0; palletObj = 0;
}






void CharacterHeader::load(){
    // character
    charObj = DataBase::getCharacter(characterName);
    // body
    const std::string* tmp = &charObj->bodies[body];
    bodyObj = DataBase::getICL(*tmp);
    //head
    if (head != 0){
        tmp = &charObj->heads[head];
        headObj = DataBase::getICL(*tmp);
    }
    //pallet
    if (pallet != 0){
        std::string tmp2 = DataBase::getPalletNameFromICLName(charObj->bodies[body],pallet);
//        tmp2 = tmp2.substr(0,tmp2.find_last_of('.')); tmp2+="_";
//        std::string tmp3;
//        ::toString<UInt>(pallet,tmp3);
//        tmp2+= tmp3;
//        tmp2+= '.';
//        tmp2 += GameInvariants::PALLET_EXTENSION;
        palletObj = DataBase::getPallet(tmp2);
        bodyObj = DataBase::getICL(charObj->bodies[body],pallet);
    }
}


bool CharacterHeader::ready() const throw(){
    if (charObj == 0 || bodyObj == 0 && (head!=0 && headObj == 0) || (pallet!=0 && palletObj == 0)) return false;
    return charObj->isLoaded() && bodyObj->isLoaded() && (head==0 || headObj->isLoaded()) && (palletObj == 0 || palletObj->isLoaded());
}

//bool CharacterHeader::applyPallet(){
//    if (charObj == 0 || palletObj == 0) return false;   // fail
//    if (pallet == 0) return true;
//    const std::string& tmp = charObj->bodies[body];
//    bodyObj = DataBase::getICL(tmp.c_str(), pallet);
//    return true;
//}


void CharacterHeader::createSprite(CharSprite& sprite, UInt name, UInt energy, IntVector3D startPos, UInt startAnimation){
    sprite.initialize(charObj,this,bodyObj,headObj, name, energy, startPos, startAnimation);
}
