#ifndef AREADABLE_H_INCLUDED
#define AREADABLE_H_INCLUDED

#include "DataInputStream.h"
#include "DataOutputStream.h"
#include "IOException.h"

/** Abstract class for readable objects */
class AReadable{
    public:
        virtual ~AReadable(){}
        virtual void readData(DataInputStream& dis) throw(IOException) = 0;     //throws IOException;
        //virtual void writeData(DataOutputStream& dos) = 0;   //throws IOException;
};

#endif // AREADABLE_H_INCLUDED
