#ifndef IOEXCEPTION_H_INCLUDED
#define IOEXCEPTION_H_INCLUDED

#include <string>
#include <exception>
#include "FileUtils.h"

class IOException: public std::exception{
    protected:
        UByte error;        // exception error
        std::string msg;    // aditional information, like the filename
    public:
        // -----------------------------------------
        //  ---------- ERROR DESCRIPTORS ----------
        // -----------------------------------------
        //static const UByte IO_SUCESS            = 0;
        static const UByte IO_ACCESS_ERROR      = 1;
        static const UByte DATA_OUT_OF_BOUNDS   = 2;
        static const UByte CODIFICATION_ERROR   = 3;
        static const UByte CORRUPTED_FILE       = 4;
        static const UByte INVALID_VERSION      = 5;

        static const UByte UNDEFINED_OP         = 255;


        // ------------------------------------
        //  ---------- CONSTRUCTORS ----------

        IOException(UByte error, const std::string& msg) throw():
            error(error),msg(msg)
        {}
        IOException(UByte error) throw():
            error(error),msg("")
        {}

        ~IOException() throw(){}

        // -------------------------------
        //  ---------- GETTERS ----------

        const UByte& getError() const throw(){
            return error;
        }
        const char* what() const throw(){
            return msg.c_str();
        }
};


#endif // IOEXCEPTION_H_INCLUDED
