#include "AFileResolver.h"
#include "NoCoDec.h"

#include "IOException.h"

//#include <iostream>


//protected:
//    /** the file name */
//    std::string fileName;
//
//    /** A codec to save and load data */
//    protected static ACoDec* codec;


// defining the static codec
ACoDec* AFileResolver::codec;


// --------------------------
//  ----- Constructors -----

/** Default constructor */
AFileResolver::AFileResolver(UByte versionID){
    loaded = false;
    this->versionID = versionID;
}

/** Constructor by fileName*/
AFileResolver::AFileResolver(const std::string& fileName, UByte versionID):fileName(fileName), loaded(false), versionID(versionID){}



// --------------------------
//  ------ Destructor ------
AFileResolver::~AFileResolver(){
    // Does nothing
}


// -----------------------------------
// ----- Static codec Modifiers -----

/** uses the given codec */
void AFileResolver::setCodec(ACoDec* codec) throw() {
    AFileResolver::codec = codec;
}
/** uses the default codec */
void AFileResolver::setDefaultCoDec() throw(){
    codec = new NoCoDec();
}
/** delete the Codec pointer*/
void AFileResolver::deleteCoDec() throw(){
    delete codec;
}




//-------------------------
// ------- GETTERS -------

bool AFileResolver::isLoaded() const throw(){ return loaded; }

/** @return the file name of this FramesCollection */
const std::string& AFileResolver::getFileName() const throw(){
    return fileName;
}

std::string AFileResolver::getTruncatedPath(std::string fileName) throw(){
    size_t afterDash = fileName.find_last_of('\\')+1;
    if (afterDash>=0 && afterDash<=fileName.length())
        return fileName.substr(afterDash);
    else return fileName;
}


//-------------------------
// ------- SETTERS -------
/** sets the fileName of the images collection */
void AFileResolver::setFileName(const std::string& fileName) throw() {
    this->fileName = fileName;
}






//-----------------------------
// ------- LOAD / SAVE -------

//void AFileResolver::writeFileProtection(DataOutputStream& dos){
//    dos.writeByte(fileName.length());
//    Data nameData(fileName.c_str(),fileName.length());
//    dos.write(nameData);
//}



void AFileResolver::readFileProtection(DataInputStream& dis) throw(IOException){
    UByte len = dis.readByte();
    std::string st;
    st.resize(len);
    for(int i=0; i<len ; i++)
        st[i] = dis.readByte();

    std::string realName = fileName.substr(fileName.find_last_of('\\')+1);

    // if the regist is different from the name, its corrupted
    if (realName.compare(st))
        throw IOException(IOException::CORRUPTED_FILE, fileName);

    // version control
    UByte v = dis.readByte();
    if (v != versionID) throw IOException(IOException::INVALID_VERSION,fileName);


//    char* str = new char[len];
//    for(int i=0; i<len ; i++)
//        str[i] = dis.readByte();
//    // if the regist is different from the name, its corrupted
//    if (strcmp(str,fileName.c_str()))
//        throw IOException(IOException::CORRUPTED_FILE, fileName);
}




//void AFileResolver::save(){
//    //throw IOException(IOException::UNDEFINED_OP);
//
//    // create a DataOutputStream to stream everything into a byte array
//    // to encode it first and finally save into file
//    DataOutputStream dos;
//
//    // write file protection
//    writeFileProtection(dos);
//
//    // write the data
//    writeData(dos);
//
//    //------------------------------------
//    // --- Encode information to file ---
//    Data toWrite = dos.getData();
//    codec->encodeToFile(toWrite, fileName);
//    toWrite.clean();
//}



void AFileResolver::load() throw(IOException){
    // decode file information
    Data data = codec->decodeFromFile(fileName);

    // create DataInputStream to read the data
    DataInputStream dis(data);

    // readFileNameProtection
    readFileProtection(dis);

    // read the data
    readData(dis);

    // close, clean, return
    data.clean();
    loaded = true;  // it's ready
}




// ---------------------------
//  ----- LOADER THREAD -----
// ---------------------------


AFileResolver::Loader::Loader(AFileResolver& resolver):resolver(resolver){}

void AFileResolver::Loader::Run(){
    resolver.load();
}


