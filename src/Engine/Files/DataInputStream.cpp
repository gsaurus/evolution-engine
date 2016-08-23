#include "DataInputStream.h"
#include "IOException.h"

#include <stdio.h>

//    protected:
//        Data* data;
//        unsigned int pos;



DataInputStream::DataInputStream(Data& data) throw():
    data(data)
{
    reset();
}


DataInputStream::~DataInputStream() throw(){
    // Does nothing
}


void DataInputStream::hasNext(UInt sizeNeeded) throw(IOException){
    if (pos+sizeNeeded > data.size()) throw IOException(IOException::DATA_OUT_OF_BOUNDS);
}

void DataInputStream::reset() throw(){
    pos = 0;
}




// ---------------------------
//  ---- READING METHODS ----
// ---------------------------


UByte DataInputStream::readByte()throw(IOException){
    hasNext(1);
    return data[pos++];
}

int DataInputStream::readSignedByte() throw(IOException){
    hasNext(1);
    int res = data[pos++];
    if (res<=127) return res;
    else return res-256;
}

int DataInputStream::readInt16()throw(IOException){
    hasNext(2);
    short res(data[pos++]&0xFF); res<<=8;
    res |= data[pos++]&0xFF;
    //if (res<32768) return res;
    //else return res-65536;
    return res;
    //return (data[pos++]&0xFF)<<8 | data[pos++]&0xFF;
}


int DataInputStream::readInt32()throw(IOException){
    hasNext(4);
    int res(data[pos++]&0xFF);  res<<=8;
    res |= data[pos++]&0xFF;    res<<=8;
    res |= data[pos++]&0xFF;    res<<=8;
    res |= data[pos++]&0xFF;
    return res;
    //return (data[pos++]&0xFF)<<24 | (data[pos++]&0xFF)<<16 | (data[pos++]&0xFF)<<8 | data[pos++]&0xFF;
}

bool DataInputStream::readBool()throw(IOException){
    hasNext(1);
    return data[pos++]&0x01;
}


Data DataInputStream::read(UInt size)throw(IOException){
    hasNext(size);
    Data res(&data[pos], size);
    pos+=size;
    return res;
}






