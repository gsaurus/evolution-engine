#ifndef ACOLLECTION_H_INCLUDED
#define ACOLLECTION_H_INCLUDED

#include <vector>

#include "AFileResolver.h"

template<typename E>

/** class for collection of readable elements */
class ACollection: public AFileResolver{
	protected:
        /** elements */
        std::vector<E> elements;


	/** Constructor: an empty FramesCollection */
	public:
        ACollection(UByte versionID):AFileResolver(versionID){ }

        void clear()  throw(){ elements.clear(); }


        //-------------------------
        // ------- GETTERS -------
        int size() const throw(){ return elements.size(); }
        bool isEmpty() const throw() { return elements.empty(); }





        //---------------------------------
        // ------- ADDING ELEMENTS -------

//        void add(E* elements, int numElements){
//            if (elements!=0)
//                for(int i=0; i<numElements; i++)
//                    elements.push_back(elements[i]);
//        }
//
//        void add(E& element){
//            if (element!=0) elements.push_back(element);
//        }
//
//        void add(int pos, E& element){
//            if (element!=0 && pos>=0 && pos<elements.size()){
//                typename std::vector<E>::iterator it = elements.begin()+pos;
//                elements.insert(it, element);
//            }
//        }

        /** accessing an element */
       E& operator[](UInt index){
            return elements[index];
        }

        const E& get(UInt index) const {
            return elements[index];
        }



        //---------------------------------
        // ------- REMOVING ELEMENTS -------

//        void remove(int pos){
//             if (elements!=0 && pos>=0 && pos<elements.size()){
//                typename std::vector<E>::iterator it = elements.begin()+pos;
//                elements.erase(it);
//            }
//        }




        //-----------------------------
        // ------- LOAD / SAVE -------
        virtual void readElement(E& element, DataInputStream& dis) throw(IOException){
            element.readData(dis);
        }

        virtual void readHeader(DataInputStream& dis) throw(IOException) = 0;

        //virtual void writeHeader(DataOutputStream dos) = 0; //throws IOException

//        virtual void writeData(DataOutputStream& dos){ //throws IOException
//            //throw IOException(IOException::UNDEFINED_OP);
//            writeHeader(dos);
//
//            int N = size();
//            // write number of elements
//            dos.writeInt16(N);
//
//            // write all elements
//            typename std::vector<E>::iterator it = elements.begin();
//            for( ; it!=elements.end(); it++ ){
//                it->writeData(dos);
//            }
//        }



        void readData(DataInputStream& dis) throw(IOException){
            readHeader(dis);

            // read number of frames
            int N = dis.readInt16();
            elements.clear();
            elements.reserve(N);
            // write all elements
            for(int i=0 ; i<N ; ++i){
                E element;
                readElement(element,dis);
                elements.push_back(element);
            }
        }


};


#endif // ACOLLECTION_H_INCLUDED
