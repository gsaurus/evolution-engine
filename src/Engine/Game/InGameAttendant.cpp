#import "InGameAttendant.h"

void InGameAttendant::addPlayer(ASpriteController* c){
    players.push_back(c);
}

void InGameAttendant::deletePlayer(int playerNo){
    players[playerNo] = 0;
}

void InGameAttendant::attendEvents(sf::Window app){
    sf::Event e;
    while (app.GetEvent(e)){
        switch (e.Type){
        // TODO (Gil#1#): external events
        //Window closed
        //case sf::Event::Closed:
        //    Running = false; break;

        case sf::Event::KeyPressed:
        case sf::Event::KeyReleased:
//            bool pressed = (e.Type == sf::Event::KeyPressed);
//            switch (e.Key.Code){
//                case
//            }
            break;
        default: ;
        }
    }

}


void InGameAttendant::sendToCharacter(int player, Key key, bool pressed){
    players[player]->takeKey(key,pressed);
}
