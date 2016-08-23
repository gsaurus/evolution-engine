#include "Goodie.h"

//    int name;   // byte
//    int type;   // byte
//    int amount; // short



//--------------------
// -- CONSTRUCTORS --
//--------------------

Goodie::Goodie(int name, int type, int amount):
    name(name), type(type), amount(amount)
{}


Goodie::Goodie(DataInputStream& dis){ //throws IOException
    readData(dis);
}

Goodie::Goodie(const Goodie& other):
    name(other.name),type(other.type), amount(other.amount)
{}


Goodie::Goodie():
    name(255), type(0), amount(0)
{}




//---------------
// -- METHODS --
//---------------


void Goodie::readData(DataInputStream& dis) throw(IOException){
    // v 1.1
    name = dis.readByte();
    type = dis.readByte();
    amount = dis.readInt16();
}



//void Goodie::writeData(DataOutputStream& dos){ //throws IOException
//    dos.
//    name.writeData(dos);
//    type.writeData(dos);
//    dos.writeShort(amount);
//}
