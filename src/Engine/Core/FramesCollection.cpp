#include "FramesCollection.h"
#include "../Invariants/GameInvariants.h"

/** Constructor: an empty FramesCollection */
FramesCollection::FramesCollection():ACollection<Frame>(GameInvariants::FRAMES_VERSION),cmOn(true){}

FramesCollection::FramesCollection(const std::string& fileName):ACollection<Frame>(GameInvariants::FRAMES_VERSION){
    setFileName(fileName);
}

FramesCollection::FramesCollection(FramesCollection& other, Pallet& pallet):
    ACollection<Frame>(GameInvariants::FRAMES_VERSION), cmOn(other.cmOn)
{
    elements.reserve(other.size());
    for(int i=0; i<other.size(); ++i){
        Frame f = other[i];
        elements.push_back(Frame(f,pallet));
    }

}


//-------------------------
// ------- GETTERS -------

bool FramesCollection::hasCMOn() const { return cmOn; }


//-------------------------
// ------- SETTERS -------
void FramesCollection::setCMOn(bool on){
    cmOn = on;
}



//-----------------------------
// ------- LOAD / SAVE -------


void FramesCollection::readElement(Frame& frame, DataInputStream& dis) throw(IOException){
    frame.readData(dis,cmOn);
}

void FramesCollection::readHeader(DataInputStream& dis) throw(IOException){
    cmOn = dis.readBool();
}



//void writeHeader(DataOutputStream dos){
//    for(Frame f:elements){
//        f.setCMOn(cmOn);
//    }
//    dos.writeBoolean(cmOn);
//}




//-------------------------------
// ------- ADDING IMAGES -------

//void FramesCollection::addImages(sf::Image[] imgs){
//    List<Frame> addingFrames = new ArrayList<Frame>(imgs.length);
//    for(BufferedImage img:imgs){
//        Frame f = new Frame(img);
//        if (f!=null) addingFrames.add(f);
//    }
//    elements.addAll(addingFrames);
//}
//void FramesCollection::addImage(sf::Image img){
//    Frame f = new Frame(img);
//    if (f!=null) elements.add(f);
//}
