
#include "NoCodec.h"
#include <fstream>


/**
 * encode method
 * @param data the byteArray to encode
 * @return the result of encoding the given byteArray
 */
Data NoCoDec::encode(const Data& data) throw(){
    // Does nothing
    return data;
}
/**
 * decode method
 * @param data the byteArray to decode
 * @return the result of decoding the given byteArray
 */
Data NoCoDec::decode(const Data& data) throw(){
    // does nothing
    return data;
}
