#ifndef INGAMEATTENDER_H_INCLUDED
#define INGAMEATTENDER_H_INCLUDED

#import "AEventAttendant.h"
#import "../Core/ASpriteController.h"
#import <vector>
#import <map>

typedef std::map<Key,UInt> KeyMap;

class InGameAttendant: public AEventAttendant{
    protected:
        // allows multiple players { maybe for online combat mode :) }
        std::vector<ASpriteController*> players;
        // key mappings
        KeyMap keyMap1; // player1
        KeyMap keyMap2; // player2
    public:
        ~InGameAttendant();
        void addPlayer(ASpriteController* c);
        void deletePlayer(int playerNo);
        void getMapping(int playerNo, KeyMap map);

        void attendEvents(sf::Window app);
        void sendToCharacter(int player, Key key, bool pressed);
};

#endif // INGAMEATTENDER_H_INCLUDED
