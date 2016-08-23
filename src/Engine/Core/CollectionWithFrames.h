#ifndef GOODIESCOLLECTION_H_INCLUDED
#define GOODIESCOLLECTION_H_INCLUDED

#include <vector>
#include <string>

#include "../Files/ACollection.h"
#include "../Invariants/GameInvariants.h"
#include "Goodie.h"
#include "Weapon.h"

template<typename T>
class CollectionWithFrames:public ACollection<T>{
    protected:
        /** list of animations files associated to it */
        std::vector<std::string> frameCols;
        /** list of animations files of the icons associated to this collection */
        std::vector<std::string> icons;

    public:
        /** default constructor, does nothing */
        CollectionWithFrames(UByte versionID):ACollection<T>(versionID){}


        /** get the name of the #i associated frames collection */
        const std::string& getCollectionName(int i) const{
            return frameCols[i];
        }

        /** get the name of the #i icon anim collection */
        const std::string& getIconnName(int i) const{
            return frameCols[i];
        }

        int size() const{
            return frameCols.size();
        }

        //-----------------------------
        // ------- LOAD / SAVE -------


        void readHeader(DataInputStream& dis) throw(IOException){
            int length = dis.readByte();
            frameCols.resize(length);
            for(int i=0; i<length; ++i){
                int strLen = dis.readByte();
                std::string str; str.resize(strLen);
                for(int j=0; j<strLen ; ++j)
                    str[j] = dis.readByte();
                frameCols.push_back(str);
            }

            // icon
            length = dis.readByte();
            icons.resize(length);
            for(int i=0; i<length; ++i){
                int strLen = dis.readByte();
                std::string str; str.resize(strLen);
                for(int j=0; j<strLen ; ++j)
                    str[j] = dis.readByte();
                icons.push_back(str);
            }
        }


        //void GoodiesCollection::writeHeader(DataOutputStream& dos){ //throws IOException
        //    dos.writeByte(frames.size());
        //    for(String fileName:frames){
        //        dos.writeByte(fileName.length());
        //        dos.writeBytes(fileName);
        //    }
        //}

};

//typedef CollectionWithFrames<Goodie> GoodiesCollection;
//typedef CollectionWithFrames<Weapon> WeaponsCollection;
class GoodiesCollection: public CollectionWithFrames<Goodie>{
    public: GoodiesCollection():CollectionWithFrames<Goodie>(GameInvariants::GOODIES_VERSION){}
};

class WeaponsCollection: public CollectionWithFrames<Weapon>{
    public: WeaponsCollection():CollectionWithFrames<Weapon>(GameInvariants::GOODIES_VERSION){}
};

#endif // GOODIESCOLLECTION_H_INCLUDED
