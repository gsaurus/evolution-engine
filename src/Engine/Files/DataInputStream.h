#ifndef DATAINPUTSTREAM_H_INCLUDED
#define DATAINPUTSTREAM_H_INCLUDED

#include "../Utilities/Data.h"
#include "IOException.h"

class DataInputStream{
    protected:
        Data& data;         // data to iterate over
        unsigned int pos;   // iterator pointer (current byte)

        /**
         * Verifies if the stream has more sizeNeeded bytes,
         * throws DATA_OUT_OF_BOUNDS exception if not
         */
        void hasNext(UInt sizeNeeded) throw(IOException);
    public:
        /** constructor: receives the data to iterate over */
        DataInputStream(Data& data) throw();
        /** destrutor */
        virtual ~DataInputStream() throw();

        /** reste the stream back to the first byte */
        void reset() throw();

        // ---------------------------
        //  ---- READING METHODS ----

        /** read 1 byte */
        UByte readByte() throw(IOException);
        /** read 1 signed byte */
        int readSignedByte() throw(IOException);
        /** read one short (2 bytes)*/
        int readInt16() throw(IOException);
        /** read one int (4 bytes) */
        int readInt32() throw(IOException);
        /** reads one bool (1 byte) */
        bool readBool() throw(IOException);
        /** reads a set of data (size bytes) */
        Data read(UInt size) throw(IOException);
};

#endif // DATAINPUTSTREAM_H_INCLUDED
