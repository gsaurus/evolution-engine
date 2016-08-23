#ifndef DATABASE_H_INCLUDED
#define DATABASE_H_INCLUDED

#include <vector>

#include "../Invariants/GameInvariants.h"
#include "../Utilities/HashMap.h"
#include "../Core/Character.h"
#include "../Core/FramesCollection.h"
#include "../Core/Pallet.h"


/** A "static" class which handles the opened objects in memory */
class DataBase{
    protected:
        // TODO (Gil#1#): concorrential loads, deletes, manage data being used
        static HashMap<std::string,Character*> chars;
        static HashMap<std::string,std::vector<FramesCollection*> > icls;
        static HashMap<std::string,Pallet*> palls;

        static std::vector<UByte> randomTable;
        static UInt randIndex;

    public:
        static std::string getPalletNameFromICLName(const std::string& fileName, UInt pall) throw();

        /** Get the objects referent to the given fileNames */
        static const Character* getCharacter(const std::string& fileName) throw(IOException);
        static const FramesCollection* getICL(const std::string& fileName) throw(IOException);
        static const FramesCollection* getICL(const std::string& fileName, UInt pall) throw();
        static const Pallet* getPallet(const std::string& fileName) throw(IOException);

        //static const Pallet* getPalletFromICLName(const char* fileName, UInt pall) throw(IOException);

//        static void removeCharacter(int id);
//        static void removeICL(int id);
//        static void removePallet(int id);
//        /** remove only one pallet version (perhaps the original!) */
//        static void removeICL(int id, UInt pall);

        static void clearEverything();

        static void initRandomTable();
        static const std::vector<UByte>& getRandomTable();

        static UInt getRandomNum();
        static int getRandomPercentageOver(int val);
        static float getRandomPercentageOver(float val);
        static void setRandIndex(UInt index);
        static UInt getRandIndex();

};

#endif // DATABASE_H_INCLUDED
