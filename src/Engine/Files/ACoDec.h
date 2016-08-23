#ifndef ACODEC_H_INCLUDED
#define ACODEC_H_INCLUDED

#include <string>
#include "IOException.h"
#include "../Utilities/Data.h"


/**
 * Abstract class for codeing/decoding data
 * @author Gil Costa
 */
class ACoDec {
	public:
        /**
         * encode method
         * @param data the data to encode (+-)
         */
        virtual Data encode(const Data& data) throw() = 0;
        /**
         * decode method
         * @param data the data to decode (+-)
         */
        virtual Data decode(const Data& data) throw() = 0;


        /**
         * encodes and saves data directly to the given file
         * @param data the data to encode and write to file
         * @param fileName the output file name
         * @return true if sucess
         */
        void encodeToFile(const Data& data, const std::string& fileName) throw(IOException);

        /**
         * reads the given file and decodes it's information
         * @param data a structure to receive the decoded data from the file
         * @param fileName the input file name
         * @return true if sucess
         */
        Data decodeFromFile(const std::string& fileName) throw(IOException);

};

#endif // ACODEC_H_INCLUDED
