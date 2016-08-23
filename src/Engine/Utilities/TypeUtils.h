#ifndef TYPESUTIL_H_INCLUDED
#define TYPESUTIL_H_INCLUDED

#include <string>
#include <sstream>
#include <math.h>

typedef signed char Byte;
typedef unsigned char UByte;
typedef unsigned int UInt;


template <typename T>
void toString(const T& object, std::string & s){
    std::ostringstream os;
    os << object;
    s = os.str();
}


float toRadians (float d);
float toDegrees (float r);

void rotate(float& x, float& y, float angle);

double angleBetween(float x1, float y1, float x2, float y2);

#endif // TYPESUTIL_H_INCLUDED
