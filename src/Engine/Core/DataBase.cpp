//#include <iostream>

#include "DataBase.h"
#include <SFML/System.hpp>  // for threads
#include "../Invariants/GameInvariants.h"

HashMap<std::string,Character*> DataBase::chars;
HashMap<std::string,std::vector<FramesCollection*> > DataBase::icls;
HashMap<std::string,Pallet*> DataBase::palls;

std::vector<UByte> DataBase::randomTable;
UInt DataBase::randIndex;

// -------------------
//  ---- GETTERS ----
// -------------------

std::string DataBase::getPalletNameFromICLName(const std::string& fileName, UInt pall) throw(){
    std::string tmp = fileName;
    tmp = fileName.substr(0,fileName.find_last_of('.')); tmp+="_";
    std::string numStr;
    ::toString<UInt>(pall,numStr);
    tmp+= numStr;
    tmp+= '.';
    tmp += GameInvariants::PALLET_EXTENSION;
    return tmp;
}


const Character* DataBase::getCharacter(const std::string& fileName) throw(IOException){
    if (chars.containsKey(fileName))
        return chars[fileName];
    else{
        // open it, add it and return
        std::string tmp(GameInvariants::CHARACTERS_DIR); tmp += fileName;
        Character* ch = new Character(tmp);
        // add name reference
//        name = new char[fileName.size()];
//        strcpy(name,fileName.c_str());
        chars[fileName] = ch;
        // load the character directly
        ch->load();
        // load the character in other thread:
        //AFileResolver::Loader loader(*ch);
        //loader.Launch();
        // return, but not loaded yet
        return ch;
    }
}

// called by the headers, so they can apply pallets later
const FramesCollection* DataBase::getICL(const std::string& fileName) throw(IOException){
    std::vector<FramesCollection*>& vec = icls[fileName];
    if (vec.empty())
        vec.push_back(0);
    else if (vec[0] != 0) return vec[0];

    // open it, add it and return
    std::string tmp(GameInvariants::ANIMATIONS_DIR); tmp += fileName;
    FramesCollection* icl = new FramesCollection(tmp);
    // add name reference
    //name = new char[fileName.size()];
    //strcpy(name,fileName.c_str());
    vec[0] = icl;
    // load the character directly
    icl->load();
    // load the character in other thread:
    //AFileResolver::Loader loader(*ch);
    //loader.Launch();
    // return, but not loaded yet
    return icl;
}


const FramesCollection* DataBase::getICL(const std::string& fileName, UInt pall) throw(){
    if (!icls.containsKey(fileName)) return 0;
    std::vector<FramesCollection*>& vec = icls[fileName];
    // if it exists, return
    if (pall<vec.size() && vec[pall]!=0) return vec[pall];

    // else it must be applyed to the first icl
    FramesCollection* originalICL = vec[0];
    if (originalICL == 0) return 0; // original ICL missing! :(

    // else, find the pallet
    std::string st = getPalletNameFromICLName(fileName, pall);
    if (!palls.containsKey(st)) return 0; // pallet missing! :(

    // get the pallet
    Pallet* pallet = palls[st];
    // if not loaded yet, fail!
    if (!pallet->isLoaded() || !originalICL->isLoaded()) return 0;
    // fill the unused positions
    for(UInt i=vec.size(); i<=pall; ++i)
        vec.push_back(0);
    // create the icl from the original by applying the pallet
    vec[pall] = new FramesCollection(*originalICL, *pallet);
    return vec[pall];
}


const Pallet* DataBase::getPallet(const std::string& fileName) throw(IOException){
    if (palls.containsKey(fileName))
        return palls[fileName];
    else{
        // open it, add it and return
        std::string tmp(GameInvariants::PALLETS_DIR); tmp += fileName;
        Pallet* pall = new Pallet(tmp);
        // add name reference
        //name = new char[fileName.size()];
        //strcpy(name,fileName.c_str());
        palls[fileName] = pall;
        // load the character directly
        pall->load();
        // load the character in other thread:
        //AFileResolver::Loader loader(*ch);
        //loader.Launch();
        // return, but not loaded yet
        return pall;
    }
}



//const Pallet* DataBase::getPalletFromICLName(const std::string& fileName, UInt pall) throw(IOException){
//    return getPallet(getPalletNameFromICLName(fileName,pall));
//}







// ---------------------
//  ---- REMOTIONS ----
// ---------------------

//void DataBase::removeCharacter(const std::string& fileName){
//    HashMap<const std::string&,Character*>::iterator it = characters.find(fileName);
//    if (it!= characters.end()){
//        delete (it->second);
//        characters.remove(it);
//    }
//}
//void DataBase::removeICL(const std::string& fileName){
//    HashMap<const std::string&,std::vector<FramesCollection*> >::iterator it = imagesCollections.find(fileName);
//    if (it!= imagesCollections.end()){
//        std::vector<FramesCollection*>::iterator it2 = it->second.begin();
//        for(; it2 != it->second.end(); ++it2)
//            delete *it2;
//        imagesCollections.remove(it);
//    }
//}
//
//void DataBase::removePallet(const std::string& fileName){
//    HashMap<char*,Pallet*>::iterator it = pallets.find(fileName);
//    if (it!= pallets.end()){
//        std::string name(it->first);
//        delete (it->second);
//        pallets.remove(it);
//        std::string num = name.substr(name.find_last_of('_')+1,name.find_last_of('.'));
//        name = name.substr(0,name.find_last_of('_'));
//        name += '.'; name += GameInvariants::FRAMES_EXTENSION;
//        removeICL(name.c_str(),1);  // TODO convert to int
//    }
//}
//
//
//
//// remove only one pallet version (perhaps the original!)
//void DataBase::removeICL(const std::string& fileName, UInt pall){
//    HashMap<char*,std::vector<FramesCollection*> >::iterator it = imagesCollections.find(fileName);
//    if (it!= imagesCollections.end()){
//        delete (it->second[pall]);
//        it->second[pall] = 0;
//        arrangeVector(it->second);
//    }
//}



// ---------------------------------
//  ----- REMOVE EVERYTHING!! -----
// ---------------------------------

//void DataBase::clearIds(HashMap<char*,int>& ids){
//    HashMap<char*,int>::iterator it = ids.begin();
//    for( ; it!=ids.end() ; ++it)
//        delete[] it->first;
//    ids.clear();
//}


void DataBase::clearEverything(){

    // characters
    HashMap<std::string,Character*>::iterator chIt = chars.begin();
    for( ; chIt!= chars.end() ; ++chIt){
        delete chIt->second;
    }
    chars.clear();

    // icls
    HashMap<std::string,std::vector<FramesCollection*> >::iterator iclsIt = icls.begin();
    for( ; iclsIt!= icls.end() ; ++iclsIt){
        std::vector<FramesCollection*>::iterator it = iclsIt->second.begin();
        for(; it != iclsIt->second.end() ; ++it){
            if (*it != 0) delete *it;
        }
        //delete[] iclsIt->first;
    }
    icls.clear();

    // pallets
    HashMap<std::string,Pallet*>::iterator pallIt = palls.begin();
    for( ; pallIt!= palls.end() ; ++pallIt){
        delete pallIt->second;
    }
    palls.clear();

}




void DataBase::initRandomTable(){
    //sf::Randomizer.SetSeed(sf::); // TODO SetSeed, probably enter as parameter, whatch this on net!!
    randomTable.clear();
    randomTable.reserve(GameInvariants::RANDOM_TB_SIZE);
//    for(int i=0; i<GameInvariants::RANDOM_TB_SIZE; ++i){
//        randomTable.push_back(sf::Randomizer.Random()%GameInvariants::RANDOM_MAX_VAL);
//    }
}

const std::vector<UByte>& DataBase::getRandomTable(){
    return randomTable;
}


UInt DataBase::getRandomNum(){
    UInt res = randomTable[randIndex];
    if (++randIndex > randomTable.size()) randIndex = 0;
    return res;
}
int DataBase::getRandomPercentageOver(int val){ return val*getRandomNum()/GameInvariants::RANDOM_MAX_VAL; }
float DataBase::getRandomPercentageOver(float val){ return val*getRandomNum()/GameInvariants::RANDOM_MAX_VAL; }
UInt DataBase::getRandIndex(){ return randIndex; }
void DataBase::setRandIndex(UInt index){ randIndex = index; }
