#ifndef FRAME_H_INCLUDED
#define FRAME_H_INCLUDED


#include <SFML/Graphics.hpp>


#include "../Files/AReadable.h"
#include "Point.h"
#include "Pallet.h"


/**
 * A single image frame, with hot spot information
 * @author Gil Costa
 */
class Frame: public AReadable{
    protected:
        /** the image*/
        sf::Image img;
        /** hot spot */
        Point16 cm;

    public:

        /** default constructor: everything uninitialised */
        Frame();
        virtual ~Frame();

        // /** constructor by giving the image */
        // Frame(const sf::Image& img);

         // /** constructor by fields */
         //Frame(const Point16& cm);

        /** copy constructor */
        Frame(const Frame& other);

        /** copy constructor aplying a pallet to the copied frame */
        Frame(const Frame& other, Pallet& pallet);

        Frame(DataInputStream& dis, bool cmOn);

        //-------------------------
        // ------- GETTERS -------

        const sf::Image& getImage() const;
        /** get the hot spot */
        const Point16& getCM() const;

        //-------------------------
        // ------- SETTERS -------

        /** exceptional function - replaces the current image */
        void setImg(sf::Image& img);


        void readData(DataInputStream& dis) throw(IOException);
        void readData(DataInputStream& dis, bool cmOn) throw(IOException);

        //void writeData(DataOutputStream& dos); //throws IOException


};



#endif // FRAME_H_INCLUDED
