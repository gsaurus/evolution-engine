#ifndef AFILERESOLVER_H_INCLUDED
#define AFILERESOLVER_H_INCLUDED

#include <SFML/System.hpp>  // for threads

#include <iostream>
#include "ACoDec.h"
#include "FileUtils.h"
#include "DataInputStream.h"
#include "DataOutputStream.h"


class AFileResolver{
    protected:
        /** the file name */
        std::string fileName;
        /** info if it was already loaded */
        bool loaded;

        /** A codec to save and load data */
        static ACoDec* codec;

        UByte versionID;



        //void writeFileProtection(DataOutputStream& dos);
        void readFileProtection(DataInputStream& dis) throw(IOException);

    public:


        // loader thread
        class Loader : public sf::Thread{
            private:
                AFileResolver& resolver;
                void Run();
            public: Loader(AFileResolver& resolver);
        };

        /** Default constructor */
        AFileResolver(UByte versionID);

        /** Constructor by fileName*/
        AFileResolver(const std::string& fileName, UByte versionID);

        virtual ~AFileResolver();


        // -----------------------------------
        // ----- Static codec Modifiers -----

        /** uses the given codec */
        static void setCodec(ACoDec* codec) throw();
        /** uses the default codec */
        static void setDefaultCoDec() throw();
        /** delete the Codec pointer*/
        static void deleteCoDec() throw();



        //-------------------------
        // ------- GETTERS -------
        bool isLoaded() const throw();
        /** @return the file name of this FramesCollection */
        const std::string& getFileName() const throw();


        /** returns only the name of the file, get rid of the path */
        static std::string getTruncatedPath(std::string fileName) throw();

        //-------------------------
        // ------- SETTERS -------
        /** sets the fileName of the images collection */
        void setFileName(const std::string& fileName) throw();


        //-----------------------------
        // ------- LOAD / SAVE -------

        /** Load the structure from file, including file verification */
        virtual void load() throw(IOException);
        // /** Save the structure into file, including file verification */
        //virtual void save();


        /** read the core data information */
        virtual void readData(DataInputStream& is) throw(IOException) = 0;
        // /** write the core data information */
        //virtual void writeData(DataOutputStream& os) = 0;

};



#endif // AFILERESOLVER_H_INCLUDED
