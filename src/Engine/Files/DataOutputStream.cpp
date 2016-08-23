#include "DataOutputStream.h"

//protected:
//    std::vector<UByte> data;    // data is constructed


/** constructor: receives the expected stream size */
DataOutputStream::DataOutputStream(UInt size) throw(){
    data.reserve(size);
}

/** destrutor */
DataOutputStream::~DataOutputStream() throw(){
    // Does nothing
}

// ---------------------------
//  ---- READING METHODS ----

/** read 1 byte */
void DataOutputStream::writeByte(UByte val) throw(){
    data.push_back(val);
}

/** read one short (2 bytes)*/
void DataOutputStream::writeInt16(int val) throw(){
    data.push_back(val>>8);
    data.push_back(val);
}

/** read one int (4 bytes) */
void DataOutputStream::writeInt32(int val) throw(){
    data.push_back(val>>24);
    data.push_back(val>>16);
    data.push_back(val>>8);
    data.push_back(val);
}

/** reads one bool (1 byte) */
void DataOutputStream::writeBool(bool val) throw(){
    data.push_back(val);
}

/** reads a set of data (size bytes) */
void DataOutputStream::write(Data dt) throw(){
    data.reserve(data.size()+dt.size());
    for(UInt i=0;i<dt.size(); ++i)
        data.push_back(dt[i]);
}



// -----------------------
//  ---- DATA GETTER ----
/** gets the Data written */
Data DataOutputStream::getData() const throw(){
    Data res(data.size());
    for(UInt i = 0; i<res.size(); ++i){
        res[i] = data[i];
    }
    return res;
}






