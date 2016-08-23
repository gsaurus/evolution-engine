#include "Pallet.h"
#include "../Invariants/GameInvariants.h"

//protected:
//    //--------------
//    // -- FIELDS --
//    //--------------
//    HashMap<int, int> colors;


int Pallet::color2Int(const int& r, const int& g, const int& b){
    int res;
    res = r; res<<=8;
    res|= g; res<<=8;
    res|= b; // ignore alpha
    //return r<<16 | g<<8 | b;
    return res;
}

int Pallet::color2Int(const sf::Color& color){
    return color2Int(color.r,color.g,color.b);
}

int Pallet::color2Int(IntVector3D color){
    return color2Int(color.x,color.y,color.z);
}

sf::Color Pallet::int2Color(int val){
    sf::Color res;
    res.r = (val>>16);
    res.g = (val>>8)&0xff;
    res.b = val&0xff;
    return res;
}

//--------------------
// -- CONSTRUCTORS --
//--------------------
Pallet::Pallet(const std::string& Filename):AFileResolver(GameInvariants::PALLET_VERSION){
    setFileName(Filename);
}

Pallet::Pallet(DataInputStream& dis):AFileResolver(GameInvariants::PALLET_VERSION){ //throws IOException
    readData(dis);
}
Pallet::Pallet():AFileResolver(GameInvariants::PALLET_VERSION){}


//---------------
// -- METHODS --
//---------------


 void Pallet::applyToImage(const sf::Image& imgIn, sf::Image& imgOut){
    int width = imgIn.GetWidth();
    int height = imgIn.GetHeight();
    imgOut.Create(width, height, sf::Color(0,0,0,0));
    for(int x=0; x<width; ++x){
        for(int y=0; y<height; ++y){
            sf::Color c = imgIn.GetPixel(x,y);
            // ignore transparent pixels
            if (c.a == 0) continue;
            // get the integer representation,
            int original = color2Int(c);
            sf::Color newColor = c;
            if (colors.containsKey(original)){
                // get the matching color
                int matching = colors[original];
                newColor = int2Color(matching);
            }
            newColor.a = c.a;   // keep transparency
            // set the pixel in the destination image
            imgOut.SetPixel(x,y,newColor);
        }
    }
}


//void Pallet::applyToFramesCollection(const FramesCollection& iclIn, FramesCollection& iclOut){
//    iclOut.clear();
//    for(int i=0; i<iclIn.size(); ++i){
//        Frame f = iclIn[i];
//        iclOut.add(Frame(f,*this));
//    }
//}


void Pallet::readData(DataInputStream& dis) throw(IOException){
    int size = dis.readInt16();
    colors.clear();
    for(int i=0; i<size; ++i){
        IntVector3D key(dis);
        IntVector3D value(dis);
        colors[color2Int(key)] = color2Int(value);
    }
}
