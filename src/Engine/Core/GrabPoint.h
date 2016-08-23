#ifndef GRABPOINT_H_INCLUDED
#define GRABPOINT_H_INCLUDED

#include "Point.h"

class GrabPoint: public Point16{
    public:
        static const int INVALID_GRAB_POINT;
        //--------------
        // -- FIELDS --
        //--------------
        UInt angle; // UByte
        UInt anim;  // UByte

        //--------------------
        // -- CONSTRUCTORS --
        //--------------------
        /** default constructor */
        GrabPoint();
        /** constructor by fields */
        GrabPoint(int x, int y, UByte angle, UByte anim);
        /** constructor by data input stream */
        GrabPoint(DataInputStream& dis); //throws IOException
        /** copy constructor */
        GrabPoint(const GrabPoint& other);


        //---------------
        // -- METHODS --
        //---------------
        /** read headPoint */
        void readData(DataInputStream& dis);
        //void writeData(DataOutputStream& dos);



};


#endif // GRABPOINT_H_INCLUDED
