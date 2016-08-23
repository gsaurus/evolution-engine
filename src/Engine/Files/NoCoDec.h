#ifndef NOCODEC_H_INCLUDED
#define NOCODEC_H_INCLUDED

#include "ACoDec.h"


/**
 * Abstract class for codeing/decoding data
 * @author Gil Costa
 */
class NoCoDec: public ACoDec {
	public:
        /** @see ACoDec::encode */
        Data encode(const Data& data) throw();
        /** @see ACoDec::decode */
        Data decode(const Data& data) throw();

};

#endif // NOCODEC_H_INCLUDED
