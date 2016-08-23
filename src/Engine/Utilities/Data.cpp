#include "Data.h"

#include <string.h>



Data::Data(UInt size){
    lenght = size;
    array = new char[size];
}

Data::Data(const char* data, UInt size){
    // copy data
    lenght = size;
    array = new char[size];
    memcpy(array,data,size);
}

Data::Data(const Data& other){
    lenght = other.size();
    array = new char[lenght];
    memcpy(array,other.getData(),lenght);
}

Data::~Data(){
    // Does nothing
}


char& Data::operator[](UInt index){
    return array[index];
}


UInt Data::size() const  throw(){
    return lenght;
}


char* Data::getData() const  throw(){
    return array;
}


void Data::clean()  throw(){
    if (array!=0) delete[] array;
}
