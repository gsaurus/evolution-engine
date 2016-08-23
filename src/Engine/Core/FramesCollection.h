#ifndef IMAGES_COLLECTION_H_INCLUDED
#define IMAGES_COLLECTION_H_INCLUDED

#include "../Files/ACollection.h"
#include "Frame.h"
#include "Pallet.h"

class FramesCollection: public ACollection<Frame> {
    protected:
        /** if centerInformation exists */
        bool cmOn;

    public:
        /** Constructor: an empty FramesCollection */
        FramesCollection();
        /** copy constructor applying a pallet */
        FramesCollection(FramesCollection& other, Pallet& pallet);
        FramesCollection(const std::string& fileName);


        //---------------------------------
        // ------- GETTERS/SETTERS -------

        bool hasCMOn() const;

        void setCMOn(bool on);


        //-----------------------------
        // ------- LOAD / SAVE -------
        void readElement(Frame& frame, DataInputStream& dis) throw(IOException);

        void readHeader(DataInputStream& dis) throw(IOException);
        //virtual void writeHeader(DataOutputStream dos);


        //-------------------------------
        // ------- ADDING IMAGES -------

        //void addImages(sf::Image[] imgs);
        //void addImage(sf::Image img);


};

#endif // IMAGES_COLLECTION_H_INCLUDED
