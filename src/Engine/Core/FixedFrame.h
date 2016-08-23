#ifndef FIXEDFRAME_H_INCLUDED
#define FIXEDFRAME_H_INCLUDED

#include "HeadPoint.h"
#include "WeaponPoint.h"
#include "GrabPoint.h"

class FixedFrame: public AReadable{
    public:
        //--------------
        // -- FIELDS --
        //--------------

        int frameIndex; // int16
        UInt duration;  // UByte
        // Note: use x = invalid to invalidade weapon and grab options
        // null means it is disabled on the animation
        HeadPoint* head;
        WeaponPoint* weapon;
        GrabPoint* grab;

        //--------------------
        // -- CONSTRUCTORS --
        //--------------------
        /** default constructor */
        FixedFrame();
        /** destructor: delete points */
        ~FixedFrame();


        //---------------
        // -- METHODS --
        //---------------

        void readData(DataInputStream& dis) throw(IOException);
        //void writeData(DataOutputStream& dos);

};

#endif // FIXEDFRAME_H_INCLUDED
