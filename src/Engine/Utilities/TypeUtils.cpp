#include "TypeUtils.h"

float toRadians (float d) { return d * M_PI / 180; }
float toDegrees (float r) { return r * 180/ M_PI; }

void rotate(float& x, float& y, float angle){
    float oldX = x;
    x = oldX*::cos(angle) - y*::sin(angle);
    y = oldX*::sin(angle) + y*::cos(angle);
}

double angleBetween(float x1, float y1, float x2, float y2){
    return ::atan2(y2-y1,x2-x1);
}
