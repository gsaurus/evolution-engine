#ifndef POINT16_H_INCLUDED
#define POINT16_H_INCLUDED

#include "../Files/AReadable.h"

template<typename T>
class Point: public AReadable{
    public:
        // int32 is faster than int16
        T x;
        T y;

        /** default constructor */
        Point():x(0),y(0){}

        /** constructor by fields */
        Point(int x, int y):x(x),y(y){}

        /** copy constructor */
        Point(const Point<T>& other):x(other.x),y(other.y){}

        /** constructor from data input stream */
        Point(DataInputStream& dis){ readData(dis); }

        virtual void Point::readData(DataInputStream& dis)=0;

//        void Point::readData(DataInputStream& dis){
//            x = dis.readInt16();
//            y = dis.readInt16();
//        }

        // -------------------------
        // ---- STATIC METHODS ----
        // -------------------------

};
template<>
class Point<int>{
    public:
        int x; int y;
        Point():x(0),y(0){}
        Point(int x, int y):x(x),y(y){}
        Point(const Point<int>& other):x(other.x),y(other.y){}
        Point(DataInputStream& dis){ readData(dis); }

        virtual void readData(DataInputStream& dis){
            x = dis.readInt16();
            y = dis.readInt16();
        }
};
template<>
class Point<UByte>{
    public:
        UByte x; UByte y;
        Point():x(0),y(0){}
        Point(int x, int y):x(x),y(y){}
        Point(const Point<UByte>& other):x(other.x),y(other.y){}
        Point(DataInputStream& dis){ readData(dis); }

        virtual void readData(DataInputStream& dis){
            x = dis.readByte();
            y = dis.readByte();
        }
};

typedef Point<int> Point16;
typedef Point<UByte> UBPoint;


#endif // POINT16_H_INCLUDED
