#ifndef DATA_H_INCLUDED
#define DATA_H_INCLUDED

#include "TypeUtils.h"


class Data{
    protected:
        char* array;
        UInt lenght;
    public:

        Data(UInt size);
        Data(const char* data, UInt size);
        Data(const Data& other);
        virtual ~Data();

        char& operator[](UInt index);

        UInt size() const throw();
        char* getData() const throw();

        void clean() throw();

};

#endif // DATA_H_INCLUDED
