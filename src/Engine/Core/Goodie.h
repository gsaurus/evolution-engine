#ifndef GOODIE_H_INCLUDED
#define GOODIE_H_INCLUDED

#include "../Files/AReadable.h"
#include "../Files/FileUtils.h"

class Goodie: public AReadable{
    public:
        //--------------
        // -- FIELDS --
        //--------------
        UInt name;  // UByte
        UInt type;  // UByte
        int amount; // short


        //--------------------
        // -- CONSTRUCTORS --
        //--------------------

        /** by values */
        Goodie(int name, int type, int amount);
        /** by data input stream */
        Goodie(DataInputStream& dis); //throws IOException
        /** by copy */
        Goodie(const Goodie& other);
        /** default */
        Goodie();


        //---------------
        // -- METHODS --
        //---------------

        void readData(DataInputStream& dis) throw(IOException);
        //void writeData(DataOutputStream& dos); //throws IOException

};

#endif // GOODIE_H_INCLUDED
