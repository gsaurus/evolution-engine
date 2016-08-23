#ifndef VECTOR3D_H_INCLUDED
#define VECTOR3D_H_INCLUDED

//#include #include <SFML/System.hpp>   // TODO: use sf::vector3?

#include "../Files/AReadable.h"
template <typename T>
class Vector3D: public AReadable{
    public:
        // int32 is faster than int8
        T x;  // byte or int16
        T y;  // byte or int16
        T z;  // byte or int16

        Vector3D::Vector3D():x(0),y(0),z(0){}
        Vector3D::Vector3D(T x, T y, T z):x(x),y(y),z(z){}
        Vector3D::Vector3D(const Vector3D& other):x(other.x),y(other.y),z(other.z){}
        Vector3D::Vector3D(DataInputStream& dis){
            readData(dis);
        }


        Vector3D& Vector3D::operator=(const Vector3D&other){
            x = other.x;
            y = other.y;
            z = other.z;
            return *this;
        }

        const T getX() const { return x; }
        const T getY() const { return y; }
        const T getZ() const { return z; }

        void Vector3D::readData(DataInputStream& dis) throw(IOException){
            x = dis.readByte();
            y = dis.readByte();
            z = dis.readByte();
        }

        void Vector3D::readInt16Data(DataInputStream& dis) throw(IOException){
            x = dis.readInt16();
            y = dis.readInt16();
            z = dis.readInt16();
        }

        void Vector3D::readSignedByteData(DataInputStream& dis) throw(IOException){
            x = dis.readSignedByte();
            y = dis.readSignedByte();
            z = dis.readSignedByte();
        }
        // -------------------------
        // ---- STATIC METHODS ----
        // -------------------------

};


typedef Vector3D<int> IntVector3D;
typedef Vector3D<float> FloatVector3D;





#endif // VECTOR3D_H_INCLUDED
