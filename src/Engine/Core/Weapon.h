#ifndef WEAPON_H_INCLUDED
#define WEAPON_H_INCLUDED


#include "../Files/AReadable.h"
#include "../Utilities/TypeUtils.h"
#include "HitFrame.h"
#include "Point.h"
#include "Vector3D.h"


class Weapon: public HitFrame, public AReadable{
    public:

        bool rotation;  // when thrown

        UInt name;      // byte
        //--------------------
        // -- CONSTRUCTORS --
        //--------------------
        Weapon(DataInputStream& dis);

        Weapon();
        ~Weapon();


        //---------------
        // -- METHODS --
        //---------------


        //void readData(DataInputStream& dis); //throws IOException
        void readData(DataInputStream& dis) throw(IOException);
        //void writeData(DataOutputStream& dos); //throws IOException

};


#endif // WEAPON_H_INCLUDED
