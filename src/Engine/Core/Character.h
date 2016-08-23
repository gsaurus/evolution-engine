#ifndef CHARACTER_H_INCLUDED
#define CHARACTER_H_INCLUDED

#include <vector>
#include <string>

#include "../Files/AFileResolver.h"
#include "../Utilities/HashMap.h"
#include "Animation.h"

class Character: public AFileResolver{  // TODO: inerits from more generic class for animated objects
    protected:
        void readHeader(DataInputStream& dis) throw(IOException);
        void readFoother(DataInputStream& dis) throw(IOException);
    public:
        HashMap<int,Animation*> animSet;

        std::vector<std::string> names;
        std::vector<std::string> bodies;
        std::vector<std::string> heads;
        std::vector<std::string> icons;

        float walkVel;
        float jumpVel;
        float runJumpVel;
        bool autoPallete;
        // TODO: more later


        /** default constructor */
        Character();
        /** create a character associated to the given fileName */
        Character(const std::string& fileName);
        /** destructor */
        ~Character();


        //-----------------------------
        // ------- LOAD / SAVE -------



        void readData(DataInputStream& dis) throw(IOException);
        //void writeHeader(DataOutputStream& dos) throw(IOException);

};



#endif // CHARACTER_H_INCLUDED
