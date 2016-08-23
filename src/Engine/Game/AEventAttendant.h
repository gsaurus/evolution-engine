#ifndef AEVENTATTENDANT_H_INCLUDED
#define AEVENTATTENDANT_H_INCLUDED

#include <SFML/Window.hpp>


class AEventAttendant{
    public:
        virtual ~AEventAttendant() = 0;
        /** Catch the window events and threath them */
        virtual void attendEvents(sf::Window app) = 0;
};

#endif // AEVENTATTENDANT_H_INCLUDED
