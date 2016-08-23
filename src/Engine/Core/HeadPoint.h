#ifndef HEADPOINT_H_INCLUDED
#define HEADPOINT_H_INCLUDED

#include "Point.h"

class HeadPoint: public Point16{
    public:
        //--------------
        // -- FIELDS --
        //--------------
        UInt frame; // UByte

        //--------------------
        // -- CONSTRUCTORS --
        //--------------------
        /** default constructor */
        HeadPoint();
        /** constructor by fields */
        HeadPoint(int x, int y, UByte frame);
        /** constructor by data input stream */
        HeadPoint(DataInputStream& dis); //throws IOException
        /** copy constructor */
        HeadPoint(const HeadPoint& other);


        //---------------
        // -- METHODS --
        //---------------
        /** read headPoint */
        void readData(DataInputStream& dis);
        //void writeData(DataOutputStream& dos);



};


#endif // HEADPOINT_H_INCLUDED
