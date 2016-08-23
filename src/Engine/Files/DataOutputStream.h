#ifndef DATAOUTPUTSTREAM_H_INCLUDED
#define DATAOUTPUTSTREAM_H_INCLUDED

#include <vector>
#include "../Utilities/Data.h"

class DataOutputStream{
    protected:
        std::vector<UByte> data;    // data is constructed during the output

    public:
        /** constructor: receives the expected stream size */
        DataOutputStream(UInt size = 0) throw();
        /** destrutor */
        virtual ~DataOutputStream() throw();

        // ---------------------------
        //  ---- READING METHODS ----

        /** read 1 byte */
        void writeByte(UByte val) throw();
        /** read one short (2 bytes)*/
        void writeInt16(int val) throw();
        /** read one int (4 bytes) */
        void writeInt32(int val) throw();
        /** reads one bool (1 byte) */
        void writeBool(bool val) throw();
        /** reads a set of data (size bytes) */
        void write(Data data) throw();

        // -----------------------
        //  ---- DATA GETTER ----
        /** gets the Data written */
        Data getData() const throw();
};

#endif // DATAOUTPUTSTREAM_H_INCLUDED
