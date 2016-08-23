#include "Frame.h"

//protected:
//    /** the image*/
//    sf::Image img;
//    /** hot spot */
//    Point16 cm;
//    /** is cm On? */
//    bool cmOn;

/** default constructor: everything uninitialised */
Frame::Frame(){}


Frame::~Frame(){
    // does nothing
}

// /** constructor by giving the image */
//Frame::Frame(const sf::Image& img):img(img) {
//    cmOn = true;
//}

// /** constructor by copy fields */
//Frame::Frame(const sf::Image& img, const Point16& cm):img(img), cm(cm){}

/** copy constructor */
Frame::Frame(const Frame& other):
    img(other.getImage()), cm(other.getCM())
{ }

Frame::Frame(DataInputStream& dis, bool cmOn){ //throws IOException{
    readData(dis, cmOn);
}

Frame::Frame(const Frame& other, Pallet& pallet): img(other.getImage()), cm(other.getCM()){
    pallet.applyToImage(other.getImage(),img);
}


//-------------------------
// ------- GETTERS -------

const sf::Image& Frame::getImage() const { return img; }
/** get the hot spot */
const Point16& Frame::getCM() const { return cm; }

//-------------------------
// ------- SETTERS -------

/** exceptional function - replaces the current image */
void Frame::setImg(sf::Image& img){ this->img = img; }




//----------------------------
// ------- READ/WRITE -------

void Frame::readData(DataInputStream& dis, bool cmOn) throw(IOException){
    // hot spot
    if (cmOn)
        cm.readData(dis);

    //------------------------------
    //  --- read the image data ---
    Data imgData = dis.read(dis.readInt32());
    img.LoadFromMemory(imgData.getData(),imgData.size());
    imgData.clean();
}

void Frame::readData(DataInputStream& dis) throw(IOException){
    readData(dis,false);
}



//void writeData(DataOutputStream& dos) throws IOException {
//    // hot spot
//    if (cm!=0)
//        cm.writeData(dos);
//
//    //--------------------------------
//    //  --- write the images data ---
//    // write image into byteArray, to know its size first
//    ByteArrayOutputStream imgsData = new ByteArrayOutputStream();
//
//    // write the image into the temporary imgsData stream
//    ImageIO.write(img, IMG_FORMAT, imgsData);
//
//    // write the sizes into the data output stream
//    dos.writeInt(imgsData.size());
//
//    // finally write the image
//    dos.write(imgsData.toByteArray());
//}
