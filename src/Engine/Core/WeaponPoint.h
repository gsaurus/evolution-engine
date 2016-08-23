#ifndef WEAPONPOINT_H_INCLUDED
#define WEAPONPOINT_H_INCLUDED

#include "Point.h"

class WeaponPoint: public Point16{
    public:
        static const int INVALID_WEAPON_POINT;
        //--------------
        // -- FIELDS --
        //--------------
        UInt angle;      // UByte
        bool inFront;

        //--------------------
        // -- CONSTRUCTORS --
        //--------------------
        /** default constructor */
        WeaponPoint();
        /** constructor by fields */
        WeaponPoint(int x, int y, UByte angle, bool inFront);
        /** constructor by data input stream */
        WeaponPoint(DataInputStream& dis); //throws IOException
        /** copy constructor */
        WeaponPoint(const WeaponPoint& other);


        //---------------
        // -- METHODS --
        //---------------
        /** read headPoint */
        void readData(DataInputStream& dis);
        //void writeData(DataOutputStream& dos);



};


#endif // WEAPONPOINT_H_INCLUDED
