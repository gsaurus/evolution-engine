#ifndef PALLET_H_INCLUDED
#define PALLET_H_INCLUDED

#include "Vector3D.h"
#include "../Files/AFileResolver.h"
#include "../Utilities/HashMap.h"

#include <SFML/Graphics.hpp>

class Pallet:public AFileResolver{
    protected:
        //--------------
        // -- FIELDS --
        //--------------
        HashMap<int, int> colors;


        static int color2Int(const int& r, const int& g, const int& b);
        static int color2Int(const sf::Color& color);
        static int color2Int(IntVector3D color);
        static sf::Color int2Color(int val);

    public:

        //--------------------
        // -- CONSTRUCTORS --
        //--------------------
        Pallet(const std::string& Filename);

        Pallet(DataInputStream& dis); //throws IOException

        Pallet();


        //---------------
        // -- METHODS --
        //---------------


        void applyToImage(const sf::Image& imgIn, sf::Image& imgOut);
        //void applyToFramesCollection(const FramesCollection& imgsIn, FramesCollection& imgOut);

        void readData(DataInputStream& dis) throw(IOException);
        //void writeData(DataOutputStream dos); //throws IOException

};


#endif // PALLET_H_INCLUDED
