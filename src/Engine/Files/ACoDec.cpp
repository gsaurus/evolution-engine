
#include <fstream>

#include "ACodec.h"
#include "IOException.h"


/** encodes and saves data directly to the given file */
void ACoDec::encodeToFile(const Data& data, const std::string& fileName) throw(IOException){
    Data encoded = encode(data);
    std::ofstream f(fileName.c_str(),std::ios::binary);
    if (!f) throw IOException(IOException::IO_ACCESS_ERROR, fileName);

    f.write(encoded.getData(),encoded.size());
    f.close();
    encoded.clean();
}



/** reads the given file and decodes it's information */
Data ACoDec::decodeFromFile(const std::string& fileName) throw(IOException){
    std::ifstream f(fileName.c_str(),std::ios::binary | std::ios::ate);
    if (!f) throw IOException(IOException::IO_ACCESS_ERROR, fileName);

    Data data(f.tellg());
    f.seekg (0, std::ios::beg);
    f.read (data.getData(), data.size());
    f.close();

    Data res = decode(data);
    data.clean();
    return res;
}
