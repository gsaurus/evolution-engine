#ifndef CHARACTERHEADER_H_INCLUDED
#define CHARACTERHEADER_H_INCLUDED

#include <string>
#include "CharSprite.h"
#include "../Utilities/TypeUtils.h"
#include "../Core/Character.h"
#include "../Core/FramesCollection.h"
#include "../Core/Pallet.h"

class CharSprite;

/** Represents the main info of a character, before it's initialization */
class CharacterHeader{
    public:
        std::string characterName;
        UInt body;  // UByte
        UInt head;  // UByte
        UInt pallet;// UByte

        const Character* charObj;
        const FramesCollection* bodyObj;
        const FramesCollection* headObj;
        const Pallet* palletObj;

        /** constructor by fields, pointers are set to null */
        CharacterHeader(const std::string& characterName, UInt body, UInt head, UInt pallet);
        /** load the information from the dataBase */
        void load();
        /** say if the character resources are all loaded */
        bool ready() const throw();
        /**
         * create a sprite from this header,
         * the sprite info is put into the given sprite
         */
        void createSprite(CharSprite& sprite, UInt name, UInt energy, IntVector3D startPos, UInt startAnimation = 0);

};

#endif // CHARACTERHEADER_H_INCLUDED
